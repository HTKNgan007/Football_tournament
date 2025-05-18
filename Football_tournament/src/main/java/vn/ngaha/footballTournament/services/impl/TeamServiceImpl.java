package vn.ngaha.footballTournament.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.TeamRepository;
import vn.ngaha.footballTournament.services.TeamService;

@Service
public class TeamServiceImpl implements TeamService{

	@Autowired
    private TeamRepository teamRepository;

    @Override
    public List<Teams> getTeamsByTournament(Tournaments tournament) {
        return teamRepository.findByTournament(tournament);
    }

    @Override
    public void saveTeam(Teams team) {
        teamRepository.save(team);
    }
}
