package hu.webuni.hr.fic.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.fic.dto.CompanyDto;
import hu.webuni.hr.fic.dto.EmployeeDto;
import hu.webuni.hr.fic.dto.LeaveDto;
import hu.webuni.hr.fic.dto.LeaveExampleDto;
import hu.webuni.hr.fic.dto.LegalFormDto;
import hu.webuni.hr.fic.dto.LoginDto;
import hu.webuni.hr.fic.dto.PositionDto;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.repository.CompanyRepository;
import hu.webuni.hr.fic.repository.EmployeeRepository;
import hu.webuni.hr.fic.repository.LeaveRepository;
import hu.webuni.hr.fic.repository.LegalFormRepository;
import hu.webuni.hr.fic.repository.PositionRepository;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LeaveControllerIT {
	
	
	private static final String BASE_URI = "/api";
	private static final String LEAVES_BASE_URI = "/api/leaves";
	private static final String LOGIN_URI = "/api/login";
	
	private static final String BOSS = "boss";
	private static final String ASS = "ass";
	private static final String PASS = "pass";
	

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
	
	@Autowired
	LeaveRepository leaveRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
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
		EmployeeDto newEmployee1 = new EmployeeDto(1, BOSS, passwordEncoder.encode(PASS), "Vezér Igazgató", "CEO", null, 1_000_000, LocalDateTime.now(), "JavaWorks");
		EmployeeDto newEmployee2 = new EmployeeDto(2, ASS, passwordEncoder.encode(PASS), "Asziszens Andi", "assistant", null, 200_000, LocalDateTime.now(), "JavaWorks");		
		long registrationNumber = companyRepository.findByName("JavaWorks").get().getRegistrationNumber();
		addEmployee(newEmployee1, registrationNumber);
		newEmployee2.setSuperiorId(employeeRepository.findByUsername(BOSS).get(0).getId());
		addEmployee(newEmployee2, registrationNumber);
		
	}

	private void clearDB() {
		leaveRepository.deleteAll();
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
	
	
	@Test
	void testThatANewLeaveCanBeAdded() throws Exception {
		List<Employee> employees = employeeRepository.findByNameStartingWithIgnoreCase("Vezér Igazgató");
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(BOSS, PASS);
		addLeaveOk(newLeave, jwtToken);
		List<LeaveDto> leaves = getAllLeaves(jwtToken);
		assertThat(leaves.size()).isEqualTo(1);
	}
	
	@Test
	void testThatANewLeaveCannotBeAddedToANonExistingEmployee() throws Exception {
		LeaveDto newLeave = new LeaveDto(1L, null, 10_000L, null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(BOSS, PASS);
		addLeave403(newLeave, jwtToken);
		List<LeaveDto> leaves = getAllLeaves(jwtToken);
		assertThat(leaves.size()).isEqualTo(0);
	}
	
	@Test
	void testThatANewLeaveCannotBeAddedByAnUnauthorizedEmployee() throws Exception {
		List<Employee> employees = employeeRepository.findByNameStartingWithIgnoreCase("Vezér Igazgató");
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		loginWithJwtNotOk("vezerigezgeto_invalid", PASS);
		addLeave403(newLeave, "");
		String jwtToken = loginWithJwtOk(BOSS, PASS);
		List<LeaveDto> leaves = getAllLeaves(jwtToken);
		assertThat(leaves.size()).isEqualTo(0);
	}
	
	@Test
	void testThatANewLeaveCannotBeAddedToAnotherEmployee() throws Exception {
		List<Employee> employees = employeeRepository.findByNameStartingWithIgnoreCase("Vezér Igazgató");
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		addLeave403(newLeave, jwtToken);
		String jwtToken2 = loginWithJwtOk(BOSS, PASS);
		List<LeaveDto> leaves = getAllLeaves(jwtToken2);
		assertThat(leaves.size()).isEqualTo(0);
	}
	
	
	
	@Test
	void testThatALeaveCanBeApproved() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		long approverId = employees.get(0).getId();
		
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		approveLeaveOk(leaveId, approverId, true, jwtTokenBoss);
		
		LeaveDto leave = getALeaveByIdOk(leaveId, jwtToken);
		assertThat(leave.getApproved()).isEqualTo(true);
		assertThat(leave.getApproverId()).isEqualTo(approverId);
		assertThat(leave.getApproveDateTime()).isNotNull();
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeApproved() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		long approverId = employees.get(0).getId();
		
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		approveLeave404(10_000L, approverId, true, jwtTokenBoss);
		
		LeaveDto leave = getALeaveByIdOk(leaveId, jwtToken);
		assertThat(leave.getApproved()).isNull();
		assertThat(leave.getApproverId()).isNull();
		assertThat(leave.getApproveDateTime()).isNull();
	}
	
	@Test
	void testThatALeaveCannotBeApprovedByAnInvalidEmployee() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		approveLeave403(leaveId, 10_000L, true, jwtTokenBoss);
		
		LeaveDto leave = getALeaveByIdOk(leaveId, jwtToken);
		assertThat(leave.getApproved()).isNull();
		assertThat(leave.getApproverId()).isNull();
		assertThat(leave.getApproveDateTime()).isNull();
	}
	
	@Test
	void testThatANotApprovedLeaveCanBeModified() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		LeaveDto modifiedLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2031, 5, 9, 10, 0), LocalDateTime.of(2031, 5, 10, 10, 0));
		modifyLeaveOk(leaveId, modifiedLeave, jwtToken);
		
		LeaveDto leaveAfterModification = getALeaveByIdOk(leaveId, jwtToken);
		assertThat(leaveAfterModification.getStartOfLeave()).isEqualTo(modifiedLeave.getStartOfLeave());
		assertThat(leaveAfterModification.getEndOfLeave()).isEqualTo(modifiedLeave.getEndOfLeave());
		assertThat(leaveAfterModification.getEmployeeId()).isEqualTo(modifiedLeave.getEmployeeId());
	}
	
	@Test
	void testThatAnApprovedLeaveCannotBeModified() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		
		
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		long approverId = employees.get(0).getId();
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		approveLeaveOk(leaveId, approverId, true, jwtTokenBoss);
		
		LeaveDto modifiedLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2031, 5, 9, 10, 0), LocalDateTime.of(2031, 5, 10, 10, 0));
		
		modifyLeave405(leaveId, modifiedLeave, jwtToken);
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeModified() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		LeaveDto modifiedLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2031, 5, 9, 10, 0), LocalDateTime.of(2031, 5, 10, 10, 0));
		
		modifyLeave404(10_000L, modifiedLeave, jwtToken);
	}
	
	@Test
	void testThatANotApprovedLeaveCanBeDeleted() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		deleteLeaveOk(leaveId, jwtToken);
		
		List<LeaveDto> leavesAfterModification = getAllLeaves(jwtToken);
		assertThat(leavesAfterModification.isEmpty()).isEqualTo(true);
	}
	
	@Test
	void testThatAnApprovedLeaveCannotBeDeleted() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		long approverId = employees.get(0).getId();
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		approveLeaveOk(leaveId, approverId, true, jwtTokenBoss);
				
		deleteLeave405(leaveId, jwtToken);
		
		List<LeaveDto> leavesAfterModification = getAllLeaves(jwtToken);
		assertThat(leavesAfterModification.size()).isEqualTo(1);
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeDeleted() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		deleteLeave404(10_000L, jwtToken);
		
		List<LeaveDto> leavesAfterModification = getAllLeaves(jwtToken);
		assertThat(leavesAfterModification.size()).isEqualTo(1);
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeFound() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		String jwtToken = loginWithJwtOk(ASS, PASS);
		long leaveId = addLeaveOk(newLeave, jwtToken).getId();
		
		getALeaveById404(10_000L, jwtToken);
	}
	
	@Test
	void testThatAnEmptyExapmleGivesAllLeaves() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		String jwtToken = loginWithJwtOk(ASS, PASS);
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		LeaveDto newLeave1 = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave1Id = addLeaveOk(newLeave1, jwtTokenBoss).getId();
		LeaveDto newLeave2 = new LeaveDto(2L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave2Id = addLeaveOk(newLeave2, jwtTokenBoss).getId();
		LeaveDto newLeave3 = new LeaveDto(3L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave3Id = addLeaveOk(newLeave3, jwtToken).getId();
		LeaveDto newLeave4 = new LeaveDto(4L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave4Id = addLeaveOk(newLeave4, jwtToken).getId();
	
		LeaveExampleDto example = new LeaveExampleDto(0, null, null, null, null, null, null, null);
	
		List<LeaveDto> result = getLeavesByExampleOk(example, 0, 100, "", jwtToken);
		
		assertThat(result.size()).isEqualTo(4);
	}
	
	@Test
	void testThatAnEmptyExapmleGivesAllLeavesWithPaging() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		String jwtToken = loginWithJwtOk(ASS, PASS);
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		LeaveDto newLeave1 = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave1Id = addLeaveOk(newLeave1, jwtTokenBoss).getId();
		LeaveDto newLeave2 = new LeaveDto(2L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave2Id = addLeaveOk(newLeave2, jwtTokenBoss).getId();
		LeaveDto newLeave3 = new LeaveDto(3L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave3Id = addLeaveOk(newLeave3, jwtToken).getId();
		LeaveDto newLeave4 = new LeaveDto(4L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave4Id = addLeaveOk(newLeave4, jwtToken).getId();
	
		LeaveExampleDto example = new LeaveExampleDto(0, null, null, null, null, null, null, null);
	
		List<LeaveDto> result = getLeavesByExampleOk(example, 1, 3, "", jwtToken);
		
		assertThat(result.size()).isEqualTo(1);
	}
	
	@Test
	void testThatWeCanSearchByOverlappingTime() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		String jwtToken = loginWithJwtOk(ASS, PASS);
		String jwtTokenBoss = loginWithJwtOk(BOSS, PASS);
		LeaveDto newLeave1 = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave1Id = addLeaveOk(newLeave1, jwtTokenBoss).getId();
		LeaveDto newLeave2 = new LeaveDto(2L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave2Id = addLeaveOk(newLeave2, jwtTokenBoss).getId();
		LeaveDto newLeave3 = new LeaveDto(3L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave3Id = addLeaveOk(newLeave3, jwtToken).getId();
		LeaveDto newLeave4 = new LeaveDto(4L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave4Id = addLeaveOk(newLeave4, jwtToken).getId();
	
		LeaveExampleDto example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2020,1,1,10,0,0), LocalDateTime.of(2025,1,1,10,0,0));
		List<LeaveDto> result = getLeavesByExampleOk(example, 0, 100, "", jwtToken);
		assertThat(result.size()).isEqualTo(4);
		
		example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2023,1,1,10,0,0), LocalDateTime.of(2025,1,1,10,0,0));
		result = getLeavesByExampleOk(example, 0, 100, "", jwtToken);
		assertThat(result.size()).isEqualTo(2);
		
		example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2024,1,1,10,0,0), LocalDateTime.of(2025,1,1,10,0,0));
		result = getLeavesByExampleOk(example, 0, 100, "", jwtToken);
		assertThat(result.size()).isEqualTo(0);
		
		example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2010,1,1,10,0,0), LocalDateTime.of(2012,1,1,10,0,0));
		result = getLeavesByExampleOk(example, 0, 100, "", jwtToken);
		assertThat(result.size()).isEqualTo(0);	
	}
	
	
	private String loginWithJwtOk(String username, String password) {
		LoginDto loginDto = new LoginDto(username, password);
		return webTestClient
				.post()
				.uri(LOGIN_URI)
				.bodyValue(loginDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();	
	}
	
	private void loginWithJwtNotOk(String username, String password) {
		LoginDto loginDto = new LoginDto(username, password);
		webTestClient
				.post()
				.uri(LOGIN_URI)
				.bodyValue(loginDto)
				.exchange()
				.expectStatus()
				.isForbidden();
	}
	
	private List<LeaveDto> getAllLeaves(String jwtToken) {
		return webTestClient
				.get()
				.uri(LEAVES_BASE_URI)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private LeaveDto getALeaveByIdOk(long id, String jwtToken) {
		return webTestClient
				.get()
				.uri(LEAVES_BASE_URI + "/" + id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void getALeaveById404(long id, String jwtToken) {
		webTestClient
				.get()
				.uri(LEAVES_BASE_URI + "/" + id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isNotFound();	
	}

	private List<LeaveDto> getLeavesByExampleOk(LeaveExampleDto example, Integer pageNo, Integer pageSize, String sortBy, String jwtToken) {
		return webTestClient
				.post()
				.uri(LEAVES_BASE_URI + "/search/?pageNo=" + pageNo + "&pageSize=" + pageSize + "&sortBy=" + sortBy)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(example)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private LeaveDto addLeaveOk(LeaveDto leave, String jwtToken) {
		return webTestClient
				.post()
				.uri(LEAVES_BASE_URI)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void addLeave403(LeaveDto leave, String jwtToken) {
		webTestClient
				.post()
				.uri(LEAVES_BASE_URI)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	private LeaveDto modifyLeaveOk(long id, LeaveDto leave, String jwtToken) {
		return webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" +id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void modifyLeave404(long id, LeaveDto leave, String jwtToken) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" +id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
	private void modifyLeave405(long id, LeaveDto leave, String jwtToken) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" +id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	private void deleteLeaveOk(long id, String jwtToken) {
		webTestClient
				.delete()
				.uri(LEAVES_BASE_URI + "/" +id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	private void deleteLeave404(long id, String jwtToken) {
		webTestClient
				.delete()
				.uri(LEAVES_BASE_URI + "/" +id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
	private void deleteLeave405(long id, String jwtToken) {
		webTestClient
				.delete()
				.uri(LEAVES_BASE_URI + "/" +id)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	private LeaveDto approveLeaveOk(long id, long approvalId, boolean status, String jwtToken) {
		return webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" + id + "/approval?status=" + status + "&approvalId=" + approvalId)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void approveLeave403(long id, long approvalId, boolean status, String jwtToken) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" + id + "/approval?status=" + status + "&approvalId=" + approvalId)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.FORBIDDEN);
	}

	private void approveLeave404(long id, long approvalId, boolean status, String jwtToken) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" + id + "/approval?status=" + status + "&approvalId=" + approvalId)
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
}
