package hu.webuni.hr.fic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.fic.config.HrConfigProperties;
import hu.webuni.hr.fic.model.Employee;

@Service
public class DefaultEmployeeService extends EmployeeService {

	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(Employee employee) {
		return config.getRaise().getDef().getPercent();
	}

}
