package hu.webuni.hr.fic.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.fic.dto.EmployeeDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

	private static final String BASE_URI = "/api/employees";

	@Autowired
	WebTestClient webTestClient;

	@Test
	void testThatCreatedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto newEmployee = new EmployeeDto(10, "Margetán Zsombor", "developer", 500_000,
				LocalDateTime.parse("2020-09-01T10:00:00"), "");
		createEmployee(newEmployee);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);

		assertThat(employeesAfter.get(employeesAfter.size() - 1)).usingRecursiveComparison().isEqualTo(newEmployee);
	}

	@Test
	void testThatInvalidSalaryCreatedEmployeeIsNotListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto newEmployee = new EmployeeDto(10, "Margetán Zsombor", "developer", 0,
				LocalDateTime.parse("2020-09-01T10:00:00"), "");
		createEmployee(newEmployee, HttpStatus.BAD_REQUEST);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter).usingFieldByFieldElementComparator().containsExactlyElementsOf(employeesBefore);
	}

	@Test
	void testThatModifiedEmployeeIsListed() throws Exception {
		EmployeeDto originalEmployee = new EmployeeDto(10, "Margetán Zsombor", "developer", 500_000,
				LocalDateTime.parse("2020-09-01T10:00:00"), "");
		createEmployee(originalEmployee);

		EmployeeDto modifiedEmployee = new EmployeeDto(10, "Margetán Zsombor", "developer", 600_000,
				LocalDateTime.parse("2020-09-01T10:00:00"), "");
		modifyEmployee(modifiedEmployee);

		EmployeeDto employeeAfterModification = getEmployee(modifiedEmployee.getId());

		assertThat(employeeAfterModification).usingRecursiveComparison().isEqualTo(modifiedEmployee);
	}

	@Test
	void testThatInvalidSalaryModifiedEmployeeIsNotListed() throws Exception {
		EmployeeDto originalEmployee = new EmployeeDto(10, "Margetán Zsombor", "developer", 500_000,
				LocalDateTime.parse("2020-09-01T10:00:00"), "");
		createEmployee(originalEmployee);

		EmployeeDto modifiedEmployee = new EmployeeDto(10, "Margetán Zsombor", "developer", 0,
				LocalDateTime.parse("2020-09-01T10:00:00"), "");

		modifyEmployee(modifiedEmployee, HttpStatus.BAD_REQUEST);

		EmployeeDto employeeAfterModification = getEmployee(modifiedEmployee.getId());

		assertThat(employeeAfterModification).usingRecursiveComparison().isEqualTo(originalEmployee);
	}

	private void createEmployee(EmployeeDto newEmployee) {
		createEmployee(newEmployee, HttpStatus.OK);
	}

	private void createEmployee(EmployeeDto newEmployee, HttpStatus status) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			.isEqualTo(status);
	}

	private void modifyEmployee(EmployeeDto modifiedEmployee) {
		modifyEmployee(modifiedEmployee, HttpStatus.OK);
	}

	private void modifyEmployee(EmployeeDto modifiedEmployee, HttpStatus status) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + modifiedEmployee.getId())
			.bodyValue(modifiedEmployee)
			.exchange()
			.expectStatus()
			.isEqualTo(status);
	}

	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> responseList = webTestClient.get().uri(BASE_URI).exchange().expectStatus().isOk()
				.expectBodyList(EmployeeDto.class).returnResult().getResponseBody();
		Collections.sort(responseList, (e1, e2) -> Long.compare(e1.getId(), e2.getId()));
		return responseList;
	}

	private EmployeeDto getEmployee(long id) {
		EmployeeDto employee = webTestClient.get().uri(BASE_URI + "/" + id).exchange().expectStatus().isOk()
				.expectBody(EmployeeDto.class).returnResult().getResponseBody();
		return employee;
	}

}
