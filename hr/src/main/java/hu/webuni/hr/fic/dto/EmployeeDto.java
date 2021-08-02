package hu.webuni.hr.fic.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

public class EmployeeDto {
	private long id;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String name;
	@NotEmpty
	private String title;
	
	private Long superiorId;
	
	@Min(value = 1)
	private int salary;
	@Past
	private LocalDateTime entryDate;
	private String companyName;

	public EmployeeDto() {

	}

	public EmployeeDto(long id, String username, String password, String name, String title, Long superiorId, int salary, LocalDateTime entryDate, String companyName) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.title = title;
		this.superiorId = superiorId;
		this.salary = salary;
		this.entryDate = entryDate;
		this.companyName = companyName;
	}
	
	public EmployeeDto(long id, String name, String title, int salary, LocalDateTime entryDate, String companyName) {
		this.id = id;
		this.username = null;
		this.password = null;
		this.name = name;
		this.title = title;
		this.superiorId = null;
		this.salary = salary;
		this.entryDate = entryDate;
		this.companyName = companyName;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDateTime entryDate) {
		this.entryDate = entryDate;
	}
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getSuperiorId() {
		return superiorId;
	}

	public void setSuperiorId(Long superiorId) {
		this.superiorId = superiorId;
	}

	@Override
	public String toString() {
		return "EmployeeDto [id=" + id + ", name=" + name + ", title=" + title + ", salary=" + salary + ", entryDate="
				+ entryDate + ", companyName=" + companyName + "]";
	}

	

}
