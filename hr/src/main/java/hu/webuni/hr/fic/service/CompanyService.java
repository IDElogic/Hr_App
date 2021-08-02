package hu.webuni.hr.fic.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.fic.dto.AvgSalaryDto;
import hu.webuni.hr.fic.model.Company;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.model.Position;
import hu.webuni.hr.fic.repository.CompanyRepository;
import hu.webuni.hr.fic.repository.EmployeeRepository;

@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	LegalFormService legalFormService;

	@Autowired
	PositionService positionService;

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public List<Company> findAllWithEmployees() {
		return companyRepository.findAllWithEmployees();
	}

	public Optional<Company> findById(long id) {
		return companyRepository.findById(id);
	}

	public Optional<Company> findByIdFull(long id) {
		return companyRepository.findByWithEmployees(id);
	}

	public Optional<Company> findByName(String name) {
		return companyRepository.findByName(name);
	}

	@Transactional
	public Company addEmployee(Long id, String title, Long superiorId, Employee employee) {
		Company company = companyRepository.findByWithEmployees(id).get();
		Position position = positionService.findByNameAndCompany(title, company).get();
		if (superiorId != null) {
			Employee superior = employeeRepository.findById(superiorId).get();
			employee.setSuperior(superior);
		} else {
			employee.setSuperior(null);
		}
		position.addEmployee(employee);
		company.addEmployee(employee);
		employeeRepository.save(employee);
		return companyRepository.findByWithEmployees(id).get();
	}

	@Transactional
	public Company replaceEmployees(List<Employee> employees, long registrationNumber) {
		deleteEmployeesOfACompany(registrationNumber);
		for (Employee employee : employees) {
			String title = employee.getPosition().getName();
			employee.setPosition(null);
			Long superiorId = employee.getSuperior().getId();
			addEmployee(registrationNumber, title, superiorId, employee);
		}
		return findById(registrationNumber).get();
	}

	@Transactional
	public Company deleteEmployeesOfACompany(long id) {
		Company company = companyRepository.findById(id).get();
		company.getEmployees().stream().forEach(e -> e.setCompany(null));
		company.getEmployees().clear();
		return company;
	}

	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findById(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		//employeeRepository.save(employee);
		return company;
	}

	@Transactional
	public Company save(Company company) {
		return companyRepository.save(company);
	}

	@Transactional
	public Company update(Company company) {
		if (!companyRepository.existsById(company.getRegistrationNumber()))
			return null;
		return companyRepository.save(company);
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
	}

	public List<Company> getAllCompanies(String full) {
		if (full == null || full.equals("false")) {
			return findAll();
		} else {
			return findAllWithEmployees();
		}
	}

	public List<Company> findWhereEmployeeSalaryIsGreaterThan(int aboveSalary) {
		return companyRepository.findWhereEmployeeSalaryIsGreaterThan(aboveSalary);
	}

	public List<Company> findWhereEmployeeNumberIsAbove(int aboveEmployeeNumber) {
		return companyRepository.findWhereEmployeeNumberIsAbove(aboveEmployeeNumber);
	}

	public List<AvgSalaryDto> listAverageSalaryiesGroupedByTitlesAtACompany(long id) {
		findById(id).orElseThrow(() -> new NoSuchElementException());
		return companyRepository.listAverageSalaryiesGroupedByTitlesAtACompany(id);
	}

	@Transactional
	public Company addNewCompany(Company company, String legalForm) {
		company.setLegalForm(legalFormService.findByForm(legalForm).orElseThrow(() -> new NoSuchElementException()));
		company.setRegistrationNumber(null);
		return companyRepository.save(company);
	}

	@Transactional
	public Company updateCompany(Company company, String legalForm) {
		company.setLegalForm(legalFormService.findByForm(legalForm).orElseThrow(() -> new NoSuchElementException()));
		Company updatedCompany = update(company);
		if (updatedCompany == null)
			throw new NoSuchElementException();
		return updatedCompany;
	}

}
