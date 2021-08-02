package hu.webuni.hr.fic.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.model.Position;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	@EntityGraph("employeeWithLeaves")
	Optional<Employee> findById(Long id);
	
	List<Employee> findBySalaryGreaterThan(int salary);

	List<Employee> findByPosition(Position position);

	List<Employee> findByNameStartingWithIgnoreCase(String name);

	List<Employee> findByEntryDateBetween(LocalDateTime start, LocalDateTime end);

	Page<Employee> findBySalaryGreaterThan(int salary, Pageable pageable);

	List<Employee> findByUsername(String username);

	List<Employee> findBySuperiorId(Long id);
}
