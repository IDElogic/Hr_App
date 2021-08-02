package hu.webuni.hr.fic.dto;

import java.util.List;

public class PositionDto {
	
	private long id;
	private String name;
	private String minDegree;
	private int minSalary;
	private String company;
	private List<String> employees;
	
	public PositionDto() {
		
	}

	public PositionDto(long id, String name, String minDegree, int minSalary, String company,
			List<String> employees) {
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<String> getEmployees() {
		return employees;
	}

	public void setEmployees(List<String> employees) {
		this.employees = employees;
	}
	
	
	

	
}
