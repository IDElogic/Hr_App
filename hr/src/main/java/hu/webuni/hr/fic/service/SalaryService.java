package hu.webuni.hr.fic.service;

import org.springframework.stereotype.Service;

import hu.webuni.hr.fic.model.Employee;

@Service
public class SalaryService {

	private EmployeeService employeeService;

	public SalaryService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setRaise(Employee employee) {
		employee.setSalary((int) (employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) * 0.01));
	}
	
}
