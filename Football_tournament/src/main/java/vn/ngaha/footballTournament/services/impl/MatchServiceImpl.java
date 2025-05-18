package vn.ngaha.footballTournament.services.impl;

import java.util.ArrayList;
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

        List<Teams> teams = teamsRepository.findByTournament(tournament);
        List<Matches> matches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Matches match = new Matches();
                match.setTournament(tournament);
                match.setTeam1(teams.get(i));
                match.setTeam2(teams.get(j));
                match.setMatchDate(null); // Có thể thêm ngày thi đấu sau
                match.setLocation("Sân vận động ABC"); // Tạm thời

                matches.add(match);
            }
        }

        matchesRepository.saveAll(matches);
    }
}
