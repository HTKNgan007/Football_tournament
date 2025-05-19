package vn.ngaha.footballTournament.services.impl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.models.Standings;
import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.repositories.MatchRepository;

@Service
public class StandingService {
	@Autowired
    private MatchRepository matchesRepository;

    public List<Matches> getMatchesByTournament(Tournaments tournament) {
        return matchesRepository.findByTournament(tournament);
    }

    public List<Standings> calculateStandings(List<Matches> matches) {
        Map<Teams, Standings> standingsMap = new HashMap<>();

        for (Matches match : matches) {
            // Bỏ qua nếu thiếu thông tin
            if (match.getTeam1() == null || match.getTeam2() == null || match.getResult() == null) {
                continue;
            }

            Teams team1 = match.getTeam1();
            Teams team2 = match.getTeam2();
            int score1 = match.getResult().getTeam1Score();
            int score2 = match.getResult().getTeam2Score();

            // Tạo hoặc lấy standings cho 2 đội
            Standings s1 = standingsMap.computeIfAbsent(team1, t -> {
                Standings s = new Standings();
                s.setTeam(t);
                s.setTournament(match.getTournament());
                return s;
            });

            Standings s2 = standingsMap.computeIfAbsent(team2, t -> {
                Standings s = new Standings();
                s.setTeam(t);
                s.setTournament(match.getTournament());
                return s;
            });

            // Cập nhật số trận đã chơi
            s1.setPlayed(s1.getPlayed() + 1);
            s2.setPlayed(s2.getPlayed() + 1);

            // Cập nhật bàn thắng và bàn thua
            s1.setGoalsFor(s1.getGoalsFor() + score1);
            s1.setGoalsAgainst(s1.getGoalsAgainst() + score2);

            s2.setGoalsFor(s2.getGoalsFor() + score2);
            s2.setGoalsAgainst(s2.getGoalsAgainst() + score1);

            // Xác định kết quả trận
            if (score1 > score2) {
                s1.setWon(s1.getWon() + 1);
                s2.setLost(s2.getLost() + 1);
            } else if (score1 < score2) {
                s2.setWon(s2.getWon() + 1);
                s1.setLost(s1.getLost() + 1);
            } else {
                s1.setDrawn(s1.getDrawn() + 1);
                s2.setDrawn(s2.getDrawn() + 1);
            }
            
        }
        List<Standings> standings = new ArrayList<>(standingsMap.values());
        standings.sort(
            Comparator.comparingInt(Standings::getPoints).reversed()
                      .thenComparingInt(Standings::getGoalDifference).reversed()
                      .thenComparingInt(Standings::getGoalsFor).reversed()
        );

        return standings;
    }


}
