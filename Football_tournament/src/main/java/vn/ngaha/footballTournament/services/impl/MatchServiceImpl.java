package vn.ngaha.footballTournament.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        // Nếu đã có lịch thi đấu, không tạo lại
        List<Matches> existingMatches = matchesRepository.findByTournament(tournament);
        if (!existingMatches.isEmpty()) {
            return;
        }

        List<Teams> teams = teamsRepository.findByTournament(tournament);
        List<Matches> matches = new ArrayList<>();

        // Hoán vị danh sách đội ngẫu nhiên
        Collections.shuffle(teams);

        int numTeams = teams.size();
        int matchCount = 0;

        // Tạo ngày bắt đầu giải đấu (giả sử là hôm nay)
        LocalDate startDate = tournament.getStartDate(); // cần có cột startDate trong Tournament
        if (startDate == null) {
            startDate = LocalDate.now(); // fallback nếu chưa có
        }

        // Lập lịch đấu vòng tròn: mỗi cặp đấu 1 lần
        for (int i = 0; i < numTeams; i++) {
            for (int j = i + 1; j < numTeams; j++) {
                Matches match = new Matches();
                match.setTournament(tournament);
                match.setTeam1(teams.get(i));
                match.setTeam2(teams.get(j));

                // Phân bổ ngày thi đấu: mỗi trận cách nhau 1 ngày
                LocalDate matchDate = startDate.plusDays(matchCount);
                match.setMatchDate(matchDate);

                match.setLocation("Sân vận động ABC");
                matches.add(match);
                matchCount++;
            }
        }

        matchesRepository.saveAll(matches);
    }
}
