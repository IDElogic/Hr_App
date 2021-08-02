package hu.webuni.hr.fic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.fic.dto.AvgSalaryDto;
import hu.webuni.hr.fic.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@EntityGraph("companyWithEmployeesAndEmployeePositions")
	@Query("SELECT DISTINCT c FROM Company c INNER JOIN c.employees e WHERE e.salary > :salary")
	List<Company> findWhereEmployeeSalaryIsGreaterThan(int salary);
	
	@EntityGraph("companyWithEmployeesAndEmployeePositions")
//	@Query("SELECT c FROM Company c INNER JOIN c.employees e GROUP BY c.registrationNumber HAVING COUNT(e) > :employeeNumber")
	@Query("SELECT c FROM Company c WHERE SIZE(c.employees) > :employeeNumber")
	List<Company> findWhereEmployeeNumberIsAbove(int employeeNumber);
	
	@Query("SELECT new hu.webuni.hr.mzsombor.dto.AvgSalaryDto(e.position.name, avg(e.salary)) FROM Company c INNER JOIN c.employees e WHERE c.registrationNumber = :id GROUP BY e.position.name ORDER BY avg(e.salary) DESC")
	List<AvgSalaryDto> listAverageSalaryiesGroupedByTitlesAtACompany(Long id);
	
	Optional<Company> findByName(String name);
	
	
	@EntityGraph("companyWithEmployeesAndEmployeePositions")
	@Query("SELECT c FROM Company c")
	List<Company> findAllWithEmployees();
	
	@EntityGraph("companyWithEmployeesAndEmployeePositions")
	@Query("SELECT c FROM Company c WHERE c.registrationNumber = :id")
	Optional<Company> findByWithEmployees(long id);
	
}
