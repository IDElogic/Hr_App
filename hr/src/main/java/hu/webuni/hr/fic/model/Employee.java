package hu.webuni.hr.fic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

@NamedEntityGraph(
		name="employeeWithLeaves", 
		attributeNodes= {
			@NamedAttributeNode("leaves")
		})

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private long id;

	private String username;
	private String password;
	
	@NotEmpty
	private String name;
	
	
	@ManyToOne
	@JoinColumn(name="position_id")
	private Position position;
	
	@ManyToOne
	@JoinColumn(name="superior_id")
	private Employee superior;
	
	@Min(value = 1)
	private int salary;
	@Past
	private LocalDateTime entryDate;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Company company;
	
	@OneToMany(mappedBy = "employee")
	private List<Leave> leaves;

	public Employee() {

	}

	public Employee(long id, String username, String password, String name, Position position, Employee superior, int salary, LocalDateTime entryDate, Company company) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.position = position;
		this.superior = superior;
		this.salary = salary;
		this.entryDate = entryDate;
		this.company = company;
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setEntryDate(LocalDateTime entryDate) {
		this.entryDate = entryDate;
	}
		

	public List<Leave> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<Leave> leaves) {
		this.leaves = leaves;
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

	public Employee getSuperior() {
		return superior;
	}

	public void setSuperior(Employee superior) {
		this.superior = superior;
	}

	public void addLeave(Leave leave) {
		if (this.leaves == null)
			this.leaves = new ArrayList<>();
		this.leaves.add(leave);
		leave.setEmployee(this);
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", position=" + position + ", salary=" + salary + ", entryDate="
				+ entryDate + "]";
	}

}
