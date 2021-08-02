package hu.webuni.hr.fic.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.repository.EmployeeRepository;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Employee> employees = employeeRepository.findByUsername(username);
		if (employees.size() != 1) 
			throw new UsernameNotFoundException(username);
		
		List<SimpleGrantedAuthority> authorities= new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("user"));
		return new EmployeeUserDetails(username, employees.get(0).getPassword(), authorities, employees.get(0));
	}

}
