package vn.ngaha.footballTournament.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.TournamentRepository;
import vn.ngaha.footballTournament.services.TournamentService;

@Service
public class TournamentServiceImpl implements TournamentService{

	@Autowired
    private TournamentRepository tournamentRepository;

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

}
