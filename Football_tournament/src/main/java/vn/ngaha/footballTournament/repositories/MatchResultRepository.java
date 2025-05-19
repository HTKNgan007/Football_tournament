package vn.ngaha.footballTournament.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.MatchResults;
import vn.ngaha.footballTournament.models.Matches;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResults, Long>{
	Optional<MatchResults> findByMatch(Matches match);
}
