package hu.webuni.hr.fic.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.fic.model.Company;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.model.LegalForm;
import hu.webuni.hr.fic.model.Position;
import hu.webuni.hr.fic.repository.CompanyRepository;
import hu.webuni.hr.fic.repository.EmployeeRepository;
import hu.webuni.hr.fic.repository.LeaveRepository;
import hu.webuni.hr.fic.repository.LegalFormRepository;
import hu.webuni.hr.fic.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	LegalFormRepository legalFormRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	LeaveRepository leaveRepository;

	@Autowired
	CompanyService companyService;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	PasswordEncoder passwordEncoder;


	public void clearDB() {
		leaveRepository.deleteAll();
		employeeRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
		legalFormRepository.deleteAll();
		
	}

	@Transactional
	public void insertTestData() {

		legalFormRepository.save(new LegalForm(1, "nyrt"));
//		legalFormRepository.save(new LegalForm(2, "zrt"));
//		legalFormRepository.save(new LegalForm(3, "bt"));
//		legalFormRepository.save(new LegalForm(4, "kft"));

		List<Company> companies = new ArrayList<>();
		companies.add(new Company(1L, "JavaWorks", "1111 Budapest, Java Street 1.",
				legalFormRepository.findByForm("nyrt").get(), null, null));
//		companies.add(new Company(2L, "SpringWorks", "2222 Budapest, Spring Street 1.",
//				legalFormRepository.findByForm("zrt").get(), null, null));
//		companies.add(new Company(3L, "HtmlWorks", "3333 Budapest, Html Street 1.",
//				legalFormRepository.findByForm("kft").get(), null, null));
//		companies.add(new Company(4L, "CSSWorks", "4444 Budapest, CSS Street 1.",
//				legalFormRepository.findByForm("bt").get(), null, null));
//		companies.add(new Company(5L, "NodeJSWorks", "5555 Budapest, NodeJS Street 1.",
//				legalFormRepository.findByForm("nyrt").get(), null, null));

		int i = 0;

		for (Company company : companies) {
			Company newCompany = companyService.save(company);
			List<Position> positions = new ArrayList<>();
			positions.add(new Position(1, "CEO","MSc", 1_000_000, newCompany, null));
			positions.add(new Position(2, "CTO","MSc", 500_000, newCompany, null));
			positions.add(new Position(3, "CXO","MSc", 500_000, newCompany, null));
			positions.add(new Position(4, "developer","", 300_000, newCompany, null));
			positions.add(new Position(5, "administrative","graduate", 200_000, newCompany, null));
			positions.add(new Position(5, "associate","BSc", 200_000, newCompany, null));
			positions.add(new Position(5, "trainee","", 100_000, newCompany, null));
			for (Position position : positions) {
				positionService.save(position);
			}
			
			
			List<Employee> employees = new ArrayList<>();
			employees.add(new Employee(1, "sam", passwordEncoder.encode("pass"), "Sam Mendes", positionService.findByNameAndCompany("CEO", newCompany).get(), null, 1_000_000 + i,
					LocalDateTime.parse("1980-03-01T10:00:00"), null));
			employees.add(new Employee(2, "john", passwordEncoder.encode("pass"),  "John Smith", positionService.findByNameAndCompany("CTO", newCompany).get(), null, 500_000 + i, LocalDateTime.parse("1990-03-01T10:00:00"),
					null));
			employees.add(new Employee(3, "angela", passwordEncoder.encode("pass"), "Angela Davidson", positionService.findByNameAndCompany("CXO", newCompany).get(), null, 500_000 + i,
					LocalDateTime.parse("2000-03-01T10:00:00"), null));
			employees.add(new Employee(4, "peter", passwordEncoder.encode("pass"), "Peter Knee", positionService.findByNameAndCompany("developer", newCompany).get(), null, 300_000 + i,
					LocalDateTime.parse("2010-03-01T10:00:00"), null));
			employees.add(new Employee(5, "anthony", passwordEncoder.encode("pass"), "Anthony Spacy", positionService.findByNameAndCompany("administrative", newCompany).get(), null, 200_000 + i,
					LocalDateTime.parse("2015-03-01T10:00:00"), null));
			employees.add(new Employee(6, "richard", passwordEncoder.encode("pass"), "Richard Pearce", positionService.findByNameAndCompany("associate", newCompany).get(), null, 200_000 + i,
					LocalDateTime.parse("2018-09-01T10:00:00"), null));
			employees.add(new Employee(7, "megan", passwordEncoder.encode("pass"), "Megan Baker", positionService.findByNameAndCompany("trainee", newCompany).get(), null, 100_000 + i,
					LocalDateTime.parse("2020-09-01T10:00:00"), null));
			i += 50_000;

			for (Employee employee : employees) {
				Employee newEmployee = positionService.addEmployee(employee.getPosition().getId(), employee);
				companyService.addEmployee(newCompany.getRegistrationNumber(), newEmployee.getPosition().getName(), employeeRepository.findByUsername("sam").get(0).getId(), newEmployee);
			}
			
			employeeRepository.findByUsername("sam").get(0).setSuperior(null);
			
			
		}

	}

}
