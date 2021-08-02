package hu.webuni.hr.fic.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.fic.model.Company;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.model.Position;
import hu.webuni.hr.fic.repository.EmployeeRepository;

public abstract class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	PositionService positionService;

	@Autowired
	CompanyService companyService;

	public abstract int getPayRaisePercent(Employee employee);

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> findById(long id) {
		return employeeRepository.findById(id);
	}

	public List<Employee> findAboveASalary(int aboveSalary) {
		return employeeRepository.findBySalaryGreaterThan(aboveSalary);
	}

	public Page<Employee> findAboveASalary(int aboveSalary, Pageable pageable) {
		return employeeRepository.findBySalaryGreaterThan(aboveSalary, pageable);
	}

	public List<Employee> findByPosition(String title) {
		List<Position> positions = positionService.findByName(title);
		List<Employee> employees = new ArrayList<>();
		for (Position position : positions) {
			employees.addAll(employeeRepository.findByPosition(position));
		}
		return employees;

	}

	public List<Employee> findByName(String name) {
		return employeeRepository.findByNameStartingWithIgnoreCase(name);
	}

	public List<Employee> findByEntryDate(LocalDateTime startDate, LocalDateTime endDate) {
		return employeeRepository.findByEntryDateBetween(startDate, endDate);
	}
	
	public List<Employee> findBySuperiorId(Long id) {
		return employeeRepository.findBySuperiorId(id);
	}

	public List<Employee> findEmployeesByExample(Employee example) {
		long id = example.getId();
		String name = example.getName();
		String title = example.getPosition().getName();
		int salary = example.getSalary();
		LocalDateTime entryDate = example.getEntryDate();
		String companyName = example.getCompany().getName();

		Specification<Employee> spec = Specification.where(null);

		if (id > 0)
			spec = spec.and(EmployeeSpecifications.hasId(id));

		if (StringUtils.hasText(name))
			spec = spec.and(EmployeeSpecifications.hasName(name));

		if (StringUtils.hasText(title))
			spec = spec.and(EmployeeSpecifications.hasTitle(title));

		if (salary > 0)
			spec = spec.and(EmployeeSpecifications.hasSalary(salary));

		if (entryDate != null)
			spec = spec.and(EmployeeSpecifications.hasEntryDate(entryDate));

		if (StringUtils.hasText(companyName))
			spec = spec.and(EmployeeSpecifications.hasCompany(companyName));

		return employeeRepository.findAll(spec, Sort.by("id"));
	}

	@Transactional
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Transactional
	public Employee addEmployee(Employee employee, String companyName, String positionName) {
		Company company = companyService.findByName(companyName).get();
		company.addEmployee(employee);
		employee.setCompany(company);
		Position position = positionService.findByNameAndCompany(positionName, company).get();
		position.addEmployee(employee);
		//employee.setPosition(position);
		return employeeRepository.save(employee);
	}

	@Transactional
	public Employee updateEmployee(Employee employee) {
		if (!employeeRepository.existsById(employee.getId()))
			throw new NoSuchElementException();
		return employeeRepository.save(employee);
	}

	@Transactional
	public void delete(long id) {
		employeeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		employeeRepository.deleteById(id);
	}

	@Transactional
	public void deleteAll() {
		employeeRepository.deleteAll();
	}

	

}
