package vn.ngaha.footballTournament.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngaha.footballTournament.models.Standing;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long>{

}
