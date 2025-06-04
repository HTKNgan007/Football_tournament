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
        List<Matches> matches = new ArrayList<>();

        if (teams.size() < 2) return; // Không đủ đội

        Collections.shuffle(teams); // Ngẫu nhiên danh sách đội

        LocalDate startDate = tournament.getStartDate();
        LocalDate endDate = tournament.getEndDate();
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu hoặc kết thúc không hợp lệ");
        }

        // Tạo danh sách tất cả các cặp trận đấu
        List<MatchPair> matchPairs = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matchPairs.add(new MatchPair(teams.get(i), teams.get(j)));
            }
        }

        Collections.shuffle(matchPairs); // Ngẫu nhiên cặp đấu

        // Tạo lịch cho từng ngày từ start đến end
        long totalDays = startDate.datesUntil(endDate.plusDays(1)).count();
        List<LocalDate> possibleDates = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(possibleDates::add);

        // Map lưu lại danh sách các đội đã thi đấu trong từng ngày
        Map<LocalDate, Set<Long>> scheduleMap = new HashMap<>();

        for (MatchPair pair : matchPairs) {
            boolean scheduled = false;
            Collections.shuffle(possibleDates); // Ngẫu nhiên chọn ngày
            for (LocalDate date : possibleDates) {
                Set<Long> teamsPlayed = scheduleMap.getOrDefault(date, new HashSet<>());
                if (!teamsPlayed.contains(pair.team1.getId()) && !teamsPlayed.contains(pair.team2.getId())) {
                    Matches match = new Matches();
                    match.setTournament(tournament);
                    match.setTeam1(pair.team1);
                    match.setTeam2(pair.team2);
                    match.setMatchDate(date);
                    match.setLocation("Sân vận động ABC");

                    matches.add(match);

                    teamsPlayed.add(pair.team1.getId());
                    teamsPlayed.add(pair.team2.getId());
                    scheduleMap.put(date, teamsPlayed);
                    scheduled = true;
                    break;
                }
            }
            if (!scheduled) {
                throw new IllegalStateException("Không thể xếp lịch cho tất cả các trận trong khoảng thời gian đã cho.");
            }
        }

        matchesRepository.saveAll(matches);
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
