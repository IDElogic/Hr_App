package hu.webuni.hr.fic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.fic.model.Company;
import hu.webuni.hr.fic.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long>{
	
	List<Position> findByName(String name);

	Optional<Position> findByNameAndCompany(String name, Company company);
	
	

}
