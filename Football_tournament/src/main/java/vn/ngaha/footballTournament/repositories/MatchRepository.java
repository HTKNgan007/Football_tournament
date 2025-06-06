package vn.ngaha.footballTournament.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;

@Repository
public interface MatchRepository extends JpaRepository<Matches, Long>{
	List<Matches> findByTournament(Tournaments tournament);
	List<Matches> findByTeam1OrTeam2(Teams team1, Teams team2);


}
