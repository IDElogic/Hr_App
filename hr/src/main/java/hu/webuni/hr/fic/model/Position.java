package hu.webuni.hr.fic.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
public class Position {

	@Id
	@GeneratedValue
	private long id;
	@NotEmpty
	private String name;
	private String minDegree;
	@Min(value = 1)
	private int minSalary;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Company company;
	
	@OneToMany(mappedBy="position")
	private List<Employee> employees;
	
	public Position() {
		
	}


	public Position(long id, @NotEmpty String name, String minDegree, @Min(1) int minSalary, Company company,
			List<Employee> employees) {
		this.id = id;
		this.name = name;
		this.minDegree = minDegree;
		this.minSalary = minSalary;
		this.company = company;
		this.employees = employees;
	}





	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMinDegree() {
		return minDegree;
	}


	public void setMinDegree(String minDegree) {
		this.minDegree = minDegree;
	}


	public int getMinSalary() {
		return minSalary;
	}


	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}


	public List<Employee> getEmployees() {
		return employees;
	}


	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	public void addEmployee(Employee employee) {
		if (this.employees == null)
			this.employees = new ArrayList<>();
		this.employees.add(employee);
		employee.setPosition(this);
	}
	
	
	
	
}
