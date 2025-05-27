package vn.ngaha.footballTournament.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Standings;
import vn.ngaha.footballTournament.models.Tournaments;

@Repository
public interface StandingRepository extends JpaRepository<Standings, Long>{

	List<Standings> findByTournament(Tournaments tournament);
    void deleteByTournament(Tournaments tournament);
}
