package vn.ngaha.footballTournament.services;

import java.util.List;

import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Standings;
import vn.ngaha.footballTournament.models.Tournaments;

public interface StandingService {

	List<Matches> getMatchesByTournament(Tournaments tournament);
    List<Standings> calculateStandings(Tournaments tournament, List<Matches> matches);

}
