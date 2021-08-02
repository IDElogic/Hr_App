package hu.webuni.hr.fic.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import hu.webuni.hr.fic.model.Employee;

public class EmployeeUserDetails extends User {

	private static final long serialVersionUID = 1L;
	private Employee employee;
	private Long employeeId;
	
	public EmployeeUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Employee employee) {
		super(username, password, authorities);
		this.employee = employee;
		this.employeeId = employee.getId();
	}
	
	public Employee getEmployee() {
		return employee;
	}

	public Long getEmployeeId() {
		return employeeId;
	}
	
	

}
