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
import vn.ngaha.footballTournament.repositories.TeamRepository;
import vn.ngaha.footballTournament.services.StandingService;

@Service
public class StandingServiceImpl implements StandingService{
	@Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public List<Matches> getMatchesByTournament(Tournaments tournament) {
        return matchRepository.findByTournament(tournament);
    }

    @Override
    public List<Standings> calculateStandings(Tournaments tournament, List<Matches> matches) {
        // Lấy tất cả các đội thuộc giải
        List<Teams> allTeams = teamRepository.findByTournament(tournament);

        // Khởi tạo standings cho tất cả đội (kể cả chưa thi đấu)
        Map<Long, Standings> standingsMap = new HashMap<>();
        for (Teams team : allTeams) {
            Standings s = new Standings();
            s.setTeam(team);
            s.setTournament(tournament);
            s.setPlayed(0);
            s.setWon(0);
            s.setDrawn(0);
            s.setLost(0);
            s.setGoalsFor(0);
            s.setGoalsAgainst(0);
            s.setGoalDifference(0);
            s.setPoints(0);
            standingsMap.put(team.getId(), s);
        }

        // Cập nhật standings từ các trận đã đấu
        for (Matches match : matches) {
            if (match.getTeam1() == null || match.getTeam2() == null || match.getResult() == null) {
                continue;
            }

            Teams team1 = match.getTeam1();
            Teams team2 = match.getTeam2();
            int score1 = match.getResult().getTeam1Score();
            int score2 = match.getResult().getTeam2Score();

            Standings s1 = standingsMap.get(team1.getId());
            Standings s2 = standingsMap.get(team2.getId());

            s1.setPlayed(s1.getPlayed() + 1);
            s2.setPlayed(s2.getPlayed() + 1);

            s1.setGoalsFor(s1.getGoalsFor() + score1);
            s1.setGoalsAgainst(s1.getGoalsAgainst() + score2);

            s2.setGoalsFor(s2.getGoalsFor() + score2);
            s2.setGoalsAgainst(s2.getGoalsAgainst() + score1);

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

            // Cập nhật điểm và hiệu số cho mỗi đội sau trận
            s1.setPoints(s1.getWon() * 3 + s1.getDrawn());
            s2.setPoints(s2.getWon() * 3 + s2.getDrawn());

            s1.setGoalDifference(s1.getGoalsFor() - s1.getGoalsAgainst());
            s2.setGoalDifference(s2.getGoalsFor() - s2.getGoalsAgainst());
        }

        List<Standings> standings = new ArrayList<>(standingsMap.values());

        // Sắp xếp bảng xếp hạng
//        standings.sort(
//            Comparator.comparingInt(Standings::getPoints).reversed()
//                      .thenComparingInt(Standings::getGoalDifference).reversed()
//                      .thenComparingInt(Standings::getGoalsFor).reversed()
//        );
        standings.sort(
        	    Comparator.comparingInt(Standings::getPoints).reversed() // Điểm giảm dần
        	              .thenComparing((s1, s2) -> Integer.compare(s2.getGoalDifference(), s1.getGoalDifference())) // Hiệu số giảm dần
        	              .thenComparing((s1, s2) -> Integer.compare(s2.getGoalsFor(), s1.getGoalsFor())) // Bàn thắng giảm dần
        	              .thenComparing(s -> s.getTeam().getName()) // Tên đội tăng dần
        	);




        return standings;
    }
}
