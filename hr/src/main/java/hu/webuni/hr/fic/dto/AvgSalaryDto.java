package hu.webuni.hr.fic.dto;

public class AvgSalaryDto {
	private String title;
	private double avgSalary;

	public AvgSalaryDto(String title, double avgSalary) {
		this.title = title;
		this.avgSalary = avgSalary;
	}

	public AvgSalaryDto() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getAvgSalary() {
		return avgSalary;
	}

	public void setAvgSalary(double avgSalary) {
		this.avgSalary = avgSalary;
	}

}
