package hu.webuni.hr.fic.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;



@NamedEntityGraph(
		name = "companyWithEmployeesAndEmployeePositions",
		attributeNodes = {
				@NamedAttributeNode("legalForm"),
				@NamedAttributeNode( value = "employees", subgraph = "employeesGraph")
		},
		subgraphs = {
				@NamedSubgraph(
						name = "employeesGraph",
						attributeNodes = {
								@NamedAttributeNode("position")
						})
		})
@Entity
public class Company {
	@Id
	@GeneratedValue
	private Long registrationNumber;
	private String name;
	private String address;

	@ManyToOne
	private LegalForm legalForm;

	@OneToMany(mappedBy = "company")
	private List<Position> positions;

	@OneToMany(mappedBy = "company")
	private List<Employee> employees;

	public Company() {

	}

	public Company(Long registrationNumber, String name, String address, LegalForm legalForm, List<Position> positions,
			List<Employee> employees) {
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.legalForm = legalForm;
		this.positions = positions;
		this.employees = employees;
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(Long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public LegalForm getLegalForm() {
		return legalForm;
	}

	public void setLegalForm(LegalForm legalForm) {
		this.legalForm = legalForm;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public void addEmployee(Employee employee) {
		if (this.employees == null)
			this.employees = new ArrayList<>();
		this.employees.add(employee);
		employee.setCompany(this);
	}
	
	public void addPosition(Position position) {
		if (this.positions == null)
			this.positions = new ArrayList<>();
		this.positions.add(position);
		position.setCompany(this);
	}

}
