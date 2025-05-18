package vn.ngaha.footballTournament.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Match;
import vn.ngaha.footballTournament.models.Tournament;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{
	List<Match> findByTournament(Tournament tournament);

}
