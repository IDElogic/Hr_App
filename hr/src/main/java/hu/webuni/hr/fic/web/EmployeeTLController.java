package hu.webuni.hr.fic.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hu.webuni.hr.fic.dto.EmployeeDto;

@Controller
public class EmployeeTLController {

	private Map<Long, EmployeeDto> allEmployees = new HashMap<>();
	{
//		allEmployees.put(1L,
//				new EmployeeDto(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00"), ""));
//		allEmployees.put(2L,
//				new EmployeeDto(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00"), ""));
//		allEmployees.put(3L,
//				new EmployeeDto(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00"), ""));
//		allEmployees.put(4L,
//				new EmployeeDto(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00"), ""));
//		allEmployees.put(5L, new EmployeeDto(5, "Anthony Spacy", "adminstrative", 200_000,
//				LocalDateTime.parse("2015-03-01T10:00:00"), ""));
//		allEmployees.put(6L,
//				new EmployeeDto(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00"), ""));
//		allEmployees.put(7L,
//				new EmployeeDto(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00"), ""));
	}

	// Az összes alkalmazott kilistázása
	@GetMapping("/employees")
	public String listEmployees(Map<String, Object> model) {
		model.put("employees", allEmployees.values());
		model.put("newEmployee", new EmployeeDto());
		return "employees";
	}

	// Adott alkalmazott szerkesztése
	@GetMapping("/editemployee/{id}")
	public String showEmployee(Map<String, Object> model, @PathVariable long id) {
		model.put("modifiedEmployee", allEmployees.get(id));
		return "editemployee";
	}
	
	// Adott alkalmazott törlése
	@GetMapping("/deleteemployee/{id}")
	public String deleteEmployee(@PathVariable long id) {
		allEmployees.remove(id);
		return "redirect:/employees";
	}

	// Alkalmazott hozzáadása
	@PostMapping("/employees")
	public String addEmployee(EmployeeDto employee) {
		allEmployees.put(employee.getId(),employee);
		return "redirect:/employees";
	}
	
	// Alkalmazott módosítása
	@PostMapping("/editemployee")
	public String modifyEmployee(EmployeeDto employee) {
		allEmployees.put(employee.getId(),employee);
		return "redirect:/employees";
	}

}
