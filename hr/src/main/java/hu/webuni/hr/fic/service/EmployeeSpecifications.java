package hu.webuni.hr.fic.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.fic.model.Company_;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.model.Employee_;
import hu.webuni.hr.fic.model.Position_;

public class EmployeeSpecifications {

	public static Specification<Employee> hasId(long id) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.id), id);
		
	}

	public static Specification<Employee> hasName(String name) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.name)), (name + "%").toLowerCase());	
	}

	public static Specification<Employee> hasTitle(String title) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.position).get(Position_.name), title);
	}

	public static Specification<Employee> hasSalary(int salary) {
		return (root, cq, cb) -> cb.between(root.get(Employee_.salary), (int)(salary * 0.95) , (int)(salary* 1.05));
	}

	public static Specification<Employee> hasEntryDate(LocalDateTime entryDate) {
		LocalDateTime startOfDay = LocalDateTime.of(entryDate.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Employee_.entryDate), startOfDay, startOfDay.plusDays(1));
	}

	public static Specification<Employee> hasCompany(String companyName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.company).get(Company_.name)), (companyName + "%").toLowerCase());
	}

}
