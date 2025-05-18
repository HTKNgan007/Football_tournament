package vn.ngaha.footballTournament.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.MatchResults;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResults, Long>{

}
