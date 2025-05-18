package vn.ngaha.footballTournament.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;

@Repository
public interface TeamRepository extends JpaRepository<Teams, Long>{
	List<Teams> findByTournament(Tournaments tournament);

}
