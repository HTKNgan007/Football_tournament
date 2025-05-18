package vn.ngaha.footballTournament.services;

import java.util.List;

import vn.ngaha.footballTournament.models.Tournaments;

public interface TournamentService {

	List<Tournaments> getAllTournaments();
	void saveTournament(Tournaments tournament);
	Tournaments getTournamentById(Long id);

}
