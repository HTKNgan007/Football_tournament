package vn.ngaha.footballTournament.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.MatchRepository;
import vn.ngaha.footballTournament.repositories.TeamRepository;
import vn.ngaha.footballTournament.repositories.TournamentRepository;
import vn.ngaha.footballTournament.services.MatchService;


@Service
public class MatchServiceImpl implements MatchService{

	@Autowired
    private MatchRepository matchesRepository;

    @Autowired
    private TeamRepository teamsRepository;

    @Autowired
    private TournamentRepository tournamentsRepository;

    @Override
    public void generateSchedule(Long tournamentId) {
        Tournaments tournament = tournamentsRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Giải đấu không tồn tại"));

        List<Matches> existingMatches = matchesRepository.findByTournament(tournament);
        if (!existingMatches.isEmpty()) return;

        List<Teams> teams = teamsRepository.findByTournament(tournament);
        if (teams.size() < 2) return;

        List<MatchPair> matchPairs = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matchPairs.add(new MatchPair(teams.get(i), teams.get(j)));
            }
        }

        // Trộn cặp để lịch đa dạng hơn
        Collections.shuffle(matchPairs);

        LocalDate startDate = tournament.getStartDate();
        LocalDate endDate = tournament.getEndDate();
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu hoặc kết thúc không hợp lệ");
        }

        List<LocalDate> possibleDates = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(possibleDates::add);

        Map<Long, List<LocalDate>> matchDatesPerTeam = new HashMap<>();
        List<Matches> matches = new ArrayList<>();

        for (MatchPair pair : matchPairs) {
            boolean scheduled = false;

            for (LocalDate date : possibleDates) {
                // Kiểm tra đội đã đá hôm đó chưa
                boolean team1Free = isTeamFree(pair.team1.getId(), date, matchDatesPerTeam);
                boolean team2Free = isTeamFree(pair.team2.getId(), date, matchDatesPerTeam);

                if (!team1Free || !team2Free) continue;

                // Kiểm tra nghỉ ít nhất 1 ngày
                boolean team1Rested = isRested(pair.team1.getId(), date, matchDatesPerTeam);
                boolean team2Rested = isRested(pair.team2.getId(), date, matchDatesPerTeam);

                // Nếu có nghỉ đủ hoặc không còn lựa chọn khác thì chấp nhận
                if ((team1Rested && team2Rested) || isLastOption(possibleDates, date)) {
                    Matches match = new Matches();
                    match.setTournament(tournament);
                    match.setTeam1(pair.team1);
                    match.setTeam2(pair.team2);
                    match.setMatchDate(date);
                    match.setLocation("Sân vận động ABC");

                    matches.add(match);

                    matchDatesPerTeam.computeIfAbsent(pair.team1.getId(), k -> new ArrayList<>()).add(date);
                    matchDatesPerTeam.computeIfAbsent(pair.team2.getId(), k -> new ArrayList<>()).add(date);

                    scheduled = true;
                    break;
                }
            }

            if (!scheduled) {
                // Nếu vẫn không xếp được, cho phép vi phạm nghỉ (trận cuối)
                for (LocalDate date : possibleDates) {
                    boolean team1Free = isTeamFree(pair.team1.getId(), date, matchDatesPerTeam);
                    boolean team2Free = isTeamFree(pair.team2.getId(), date, matchDatesPerTeam);
                    if (!team1Free || !team2Free) continue;

                    Matches match = new Matches();
                    match.setTournament(tournament);
                    match.setTeam1(pair.team1);
                    match.setTeam2(pair.team2);
                    match.setMatchDate(date);
                    match.setLocation("Sân vận động ABC");

                    matches.add(match);

                    matchDatesPerTeam.computeIfAbsent(pair.team1.getId(), k -> new ArrayList<>()).add(date);
                    matchDatesPerTeam.computeIfAbsent(pair.team2.getId(), k -> new ArrayList<>()).add(date);
                    break;
                }
            }
        }

        matchesRepository.saveAll(matches);
    }

    private boolean isTeamFree(Long teamId, LocalDate date, Map<Long, List<LocalDate>> map) {
        List<LocalDate> dates = map.get(teamId);
        if (dates == null) return true;
        return dates.stream().noneMatch(d -> d.equals(date));
    }

    private boolean isRested(Long teamId, LocalDate date, Map<Long, List<LocalDate>> map) {
        List<LocalDate> dates = map.get(teamId);
        if (dates == null) return true;
        return dates.stream().noneMatch(d -> Math.abs(d.toEpochDay() - date.toEpochDay()) < 2);
    }

    private boolean isLastOption(List<LocalDate> dates, LocalDate current) {
        return current.equals(dates.get(dates.size() - 1));
    }


    // Lớp phụ trợ dùng trong hàm
    private static class MatchPair {
        Teams team1;
        Teams team2;

        MatchPair(Teams team1, Teams team2) {
            this.team1 = team1;
            this.team2 = team2;
        }
    }

}
