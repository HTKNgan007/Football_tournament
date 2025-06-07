package vn.ngaha.footballTournament.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.MatchRepository;
import vn.ngaha.footballTournament.repositories.TeamRepository;
import vn.ngaha.footballTournament.repositories.TournamentRepository;
import vn.ngaha.footballTournament.services.TournamentService;

@Service
public class TournamentServiceImpl implements TournamentService{

	@Autowired
    private TournamentRepository tournamentRepository;
	@Autowired
    private TeamRepository teamRepository;
	@Autowired
    private MatchRepository matchRepository; 

    @Override
    public List<Tournaments> getAllTournaments() {
        return tournamentRepository.findAll();
    }
    @Override
    public void saveTournament(Tournaments tournament) {
        tournamentRepository.save(tournament);
    }
    
    @Override
    public Tournaments getTournamentById(Long id) {
        return tournamentRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public void removeTeam(Long tournamentId, Long teamId) {
        Teams team = teamRepository.findById(teamId).orElseThrow();

        // 1. Xoá các trận đấu liên quan đến đội này
        List<Matches> matches = matchRepository.findByTeam1OrTeam2(team, team);
        matchRepository.deleteAll(matches);

        // 2. Ngắt quan hệ đội với giải đấu
        team.setTournament(null);
        teamRepository.save(team);
    }

}
