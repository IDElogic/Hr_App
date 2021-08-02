package hu.webuni.hr.fic.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.fic.dto.EmployeeDto;
import hu.webuni.hr.fic.mapper.EmployeeMapper;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper employeeMapper;

	// Az összes alkalmazott kilistázása
	@GetMapping
	public List<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
	}

	// Egy alkalmazott kilistázása az ID-ja alapján
	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable long id) {
		Employee employee = employeeService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return employeeMapper.employeeToDto(employee);

	}

	// Egy új alkalmazott hozzáadása
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		Employee employee = employeeService.addEmployee(employeeMapper.dtoToEmployee(employeeDto),employeeDto.getCompanyName(), employeeDto.getTitle());
		return employeeMapper.employeeToDto(employee);
	}

	// Létező alkalmazott módosítása
	@PutMapping("/{id}")
	public EmployeeDto modifyEmployee(@PathVariable long id,
			@RequestBody @Valid EmployeeDto employeeDto) {
		employeeDto.setId(id);
		Employee employee = employeeService.updateEmployee(employeeMapper.dtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}

	// Alkamlmazott törlése
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		try {
			employeeService.delete(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	// Bizonyos fizetés fölötti alkalmazottak kilistázása
	@GetMapping(params = "aboveSalary")
	public List<EmployeeDto> getAboveASalary(@RequestParam int aboveSalary) {
		return employeeMapper.employeesToDtos(employeeService.findAboveASalary(aboveSalary));
	}

	// Bizonyos fizetés fölötti alkalmazottak kilistázása lapozhatóan
	@GetMapping(params = { "aboveSalaryPageable", "pageSize", "pageNumber" })
	public List<EmployeeDto> getAboveASalaryPageable(@RequestParam int aboveSalaryPageable, @RequestParam int pageSize,
			@RequestParam int pageNumber) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Employee> employeePage = employeeService.findAboveASalary(aboveSalaryPageable, page);
		return employeePage.map(employeeMapper::employeeToDto).toList();
	}

	// Beküldött alkalmazott fizetésemelésének mértékének meghatározása.
	@PostMapping("/getraisepercentage")
	public int getSalaryRaisePrecentage(@RequestBody EmployeeDto employeeDto) {
		return employeeService.getPayRaisePercent(employeeMapper.dtoToEmployee(employeeDto));
	}

	// Bizonyos titulusú alkalmazottak kilistázása
	@GetMapping(params = "title")
	public List<EmployeeDto> getByTitle(@RequestParam String title) {
		List<Employee> employees;
		try {
			employees = employeeService.findByPosition(title);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return employeeMapper.employeesToDtos(employees);
	}

	// Bizonyos névvel kezdődő alkalmazottak kilistázása
	@GetMapping(params = "name")
	public List<EmployeeDto> getByName(@RequestParam String name) {
		return employeeMapper.employeesToDtos(employeeService.findByName(name));
	}

	// Bizonyos belépési dátumok között belépett alkalmazottak kilistázása
	@GetMapping(params = { "startDate", "endDate" })
	public List<EmployeeDto> getByEntryDate(@RequestParam LocalDateTime startDate,
			@RequestParam LocalDateTime endDate) {
		return employeeMapper.employeesToDtos(employeeService.findByEntryDate(startDate, endDate));
	}
	
	//Alkalmazottak keresése minta alapján
	@GetMapping("/search")
	public List<EmployeeDto> findByExample(@RequestBody EmployeeDto example) {
		return employeeMapper.employeesToDtos(employeeService.findEmployeesByExample(employeeMapper.dtoToEmployee(example)));
	}

}
