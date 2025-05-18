package vn.ngaha.footballTournament.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Team;
import vn.ngaha.footballTournament.models.Tournament;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{
	List<Team> findByTournament(Tournament tournament);

}
