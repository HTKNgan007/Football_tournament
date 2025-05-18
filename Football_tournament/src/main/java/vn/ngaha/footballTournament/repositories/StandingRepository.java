package vn.ngaha.footballTournament.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Standings;

@Repository
public interface StandingRepository extends JpaRepository<Standings, Long>{

}
