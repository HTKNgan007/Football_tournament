package vn.ngaha.footballTournament.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.MatchResult;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long>{

}
