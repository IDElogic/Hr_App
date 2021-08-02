package hu.webuni.hr.fic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.fic.service.EmployeeService;
import hu.webuni.hr.fic.service.SmartEmployeeService;

@Configuration
@Profile("smart")
public class SmartEmployeeConfiguration {

	@Bean
	public EmployeeService employeeService() {
		return new SmartEmployeeService();
	}
}
