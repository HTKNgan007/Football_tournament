package vn.ngaha.footballTournament.services;

import java.util.List;

import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;

public interface TeamService {

	List<Teams> getTeamsByTournament(Tournaments tournament);
    void saveTeam(Teams team);
}
