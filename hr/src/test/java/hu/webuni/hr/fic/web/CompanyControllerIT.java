package hu.webuni.hr.fic.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.fic.dto.CompanyDto;
import hu.webuni.hr.fic.dto.EmployeeDto;
import hu.webuni.hr.fic.dto.LegalFormDto;
import hu.webuni.hr.fic.dto.PositionDto;
import hu.webuni.hr.fic.repository.CompanyRepository;
import hu.webuni.hr.fic.repository.EmployeeRepository;
import hu.webuni.hr.fic.repository.LegalFormRepository;
import hu.webuni.hr.fic.repository.PositionRepository;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class CompanyControllerIT {

private static final String BASE_URI="/api";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	LegalFormRepository legalFormRepository;
	
	@Autowired
	PositionRepository positionRepository;

	
	@Test
	void test1ThatANewEmployeeCanBeAdded() throws Exception {
		//prepareDB();
		EmployeeDto newEmployee = new EmployeeDto(1, "Vezér Igazgató", "CEO", 1_000_000, LocalDateTime.now(), "JavaWorks");
		long registrationNumber = companyRepository.findByName("JavaWorks").get().getRegistrationNumber();
		addEmployee(newEmployee, registrationNumber);
		CompanyDto companyAfter = getASpecificCopmanyFull(registrationNumber);
		assertThat(companyAfter.getEmployees().size()).isEqualTo(1);
		assertThat(companyAfter.getEmployees().get(0).getTitle()).isEqualTo("CEO");	
	}
	
	@Test
	void test2ThatEmployeesCanBeSwapped() throws Exception {
		long registrationNumber = companyRepository.findByName("JavaWorks").get().getRegistrationNumber();
		EmployeeDto swappableEmployee = new EmployeeDto(1, "Vezér Igazgató", "CEO", 1_000_000, LocalDateTime.now(), "JavaWorks");
		addEmployee(swappableEmployee, registrationNumber);
		List<EmployeeDto> employees = new ArrayList<>();
		employees.add(new EmployeeDto(2, "Aszisztens Andi", "assistant", 200_000, LocalDateTime.now(), "JavaWorks"));
		employees.add(new EmployeeDto(3, "Főnök Frici", "CEO", 2_000_000, LocalDateTime.now(), "JavaWorks"));
		swapEmployees(employees, registrationNumber);		
		CompanyDto companyAfter = getASpecificCopmanyFull(registrationNumber);
		assertThat(companyAfter.getEmployees().size()).isEqualTo(2);
		assertThat(companyAfter.getEmployees().get(0).getTitle()).isEqualTo("assistant");	
		assertThat(companyAfter.getEmployees().get(1).getTitle()).isEqualTo("CEO");	
	}
	
	@Test
	void test3ThatAnEmployeeCouldBeDeleted() throws Exception {
		long registrationNumber = companyRepository.findByName("JavaWorks").get().getRegistrationNumber();
		List<EmployeeDto> employees = new ArrayList<>();
		employees.add(new EmployeeDto(2, "Aszisztens Andi", "assistant", 200_000, LocalDateTime.now(), "JavaWorks"));
		employees.add(new EmployeeDto(3, "Főnök Frici", "CEO", 2_000_000, LocalDateTime.now(), "JavaWorks"));
		swapEmployees(employees, registrationNumber);		
		long id = employeeRepository.findByNameStartingWithIgnoreCase("Aszisztens Andi").get(0).getId();
		deleteEmployee(registrationNumber,id);
		CompanyDto companyAfter = getASpecificCopmanyFull(registrationNumber);
		assertThat(companyAfter.getEmployees().size()).isEqualTo(1);
		assertThat(companyAfter.getEmployees().get(0).getTitle()).isEqualTo("CEO");	
	}
	
	

	@BeforeEach
	public void prepareDB() {
		clearDB();
		initDB();
	}
	
	private void initDB() {
		createLegalForm(new LegalFormDto(1, "zrt"));
		createCompany(new CompanyDto(1, "JavaWorks", "1111 Budapest, Java street 1.", "zrt", null));
		createPosition(new PositionDto(1, "CEO", "MSc", 1_000_000, "JavaWorks", null));
		createPosition(new PositionDto(2, "assistant", "graduate", 100_000, "JavaWorks", null));
		
	}

	private void clearDB() {
		employeeRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
		legalFormRepository.deleteAll();
	}
	
	
	
	
	private void createLegalForm(LegalFormDto newLegalForm) {
		webTestClient
			.post()
			.uri(BASE_URI + "/legalforms")
			.bodyValue(newLegalForm)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	
	
	private void createCompany(CompanyDto newCompany) {
		webTestClient
			.post()
			.uri(BASE_URI + "/companies")
			.bodyValue(newCompany)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void createPosition(PositionDto newPosition) {
		webTestClient
			.post()
			.uri(BASE_URI + "/positions")
			.bodyValue(newPosition)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void addEmployee(EmployeeDto newEmployee, long registrationNumber) {
		webTestClient
			.post()
			.uri(BASE_URI + "/companies/" + registrationNumber + "/employee")
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void swapEmployees(List<EmployeeDto> employees, long registrationNumber) {
		webTestClient
			.put()
			.uri(BASE_URI + "/companies/" + registrationNumber + "/employee")
			.bodyValue(employees)
			.exchange()
			.expectStatus()
			.isOk();	
	}
	
	private void deleteEmployee(long registrationNumber, long id) {
		webTestClient
			.delete()
			.uri(BASE_URI + "/companies/" + registrationNumber + "/employee/" + id)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private CompanyDto getASpecificCopmanyFull(long registrationNumber) {
		return webTestClient
				.get()
				.uri(BASE_URI + "/companies/" + registrationNumber + "/?full=true")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(CompanyDto.class)
				.returnResult()
				.getResponseBody();
	}
	
}
