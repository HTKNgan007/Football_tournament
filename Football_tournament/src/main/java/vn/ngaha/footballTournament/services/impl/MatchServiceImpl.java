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

        List<Teams> teams = teamsRepository.findByTournament(tournament);
        if (teams.size() < 2) return;

        // Lấy danh sách trận đã có
        List<Matches> existingMatches = matchesRepository.findByTournament(tournament);

        // Tạo tập hợp chứa các cặp đội đã thi đấu
        Set<String> existingPairs = new HashSet<>();
        for (Matches match : existingMatches) {
            Long id1 = match.getTeam1().getId();
            Long id2 = match.getTeam2().getId();
            existingPairs.add(pairKey(id1, id2));
        }

        // Tạo các cặp chưa đấu
        List<MatchPair> matchPairs = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Long id1 = teams.get(i).getId();
                Long id2 = teams.get(j).getId();
                if (!existingPairs.contains(pairKey(id1, id2))) {
                    matchPairs.add(new MatchPair(teams.get(i), teams.get(j)));
                }
            }
        }

        // Không còn cặp mới -> return
        if (matchPairs.isEmpty()) return;

        Collections.shuffle(matchPairs);

        LocalDate startDate = tournament.getStartDate();
        LocalDate endDate = tournament.getEndDate();
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu hoặc kết thúc không hợp lệ");
        }

        List<LocalDate> possibleDates = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(possibleDates::add);

        Map<Long, List<LocalDate>> matchDatesPerTeam = new HashMap<>();
        for (Matches match : existingMatches) {
            matchDatesPerTeam.computeIfAbsent(match.getTeam1().getId(), k -> new ArrayList<>()).add(match.getMatchDate());
            matchDatesPerTeam.computeIfAbsent(match.getTeam2().getId(), k -> new ArrayList<>()).add(match.getMatchDate());
        }

        List<Matches> newMatches = new ArrayList<>();

        for (MatchPair pair : matchPairs) {
            boolean scheduled = false;

            for (LocalDate date : possibleDates) {
                boolean team1Free = isTeamFree(pair.team1.getId(), date, matchDatesPerTeam);
                boolean team2Free = isTeamFree(pair.team2.getId(), date, matchDatesPerTeam);
                if (!team1Free || !team2Free) continue;

                boolean team1Rested = isRested(pair.team1.getId(), date, matchDatesPerTeam);
                boolean team2Rested = isRested(pair.team2.getId(), date, matchDatesPerTeam);

                if ((team1Rested && team2Rested) || isLastOption(possibleDates, date)) {
                    Matches match = new Matches();
                    match.setTournament(tournament);
                    match.setTeam1(pair.team1);
                    match.setTeam2(pair.team2);
                    match.setMatchDate(date);
                    match.setLocation("Sân vận động ABC");

                    newMatches.add(match);
                    matchDatesPerTeam.computeIfAbsent(pair.team1.getId(), k -> new ArrayList<>()).add(date);
                    matchDatesPerTeam.computeIfAbsent(pair.team2.getId(), k -> new ArrayList<>()).add(date);
                    scheduled = true;
                    break;
                }
            }

            if (!scheduled) {
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

                    newMatches.add(match);
                    matchDatesPerTeam.computeIfAbsent(pair.team1.getId(), k -> new ArrayList<>()).add(date);
                    matchDatesPerTeam.computeIfAbsent(pair.team2.getId(), k -> new ArrayList<>()).add(date);
                    break;
                }
            }
        }

        matchesRepository.saveAll(newMatches);
    }

    private String pairKey(Long id1, Long id2) {
        return id1 < id2 ? id1 + "-" + id2 : id2 + "-" + id1;
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

    private static class MatchPair {
        Teams team1;
        Teams team2;

        MatchPair(Teams team1, Teams team2) {
            this.team1 = team1;
            this.team2 = team2;
        }
    }

}
