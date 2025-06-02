package vn.ngaha.footballTournament.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Tournaments;

@Repository
public interface TournamentRepository extends JpaRepository<Tournaments, Long>{

	List<Tournaments> findByNameContainingIgnoreCase(String keyword);


}
