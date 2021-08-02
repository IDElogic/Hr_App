package hu.webuni.hr.fic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Leave {
	@Id
	@GeneratedValue
	private long id;
	private LocalDateTime createDateTime;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "approver_id")
	private Employee approver;
	private Boolean approved;
	private LocalDateTime approveDateTime;

	private LocalDateTime startOfLeave;
	private LocalDateTime endOfLeave;

	public Leave() {
	}

	public Leave(long id, LocalDateTime createDateTime, Employee employee, Employee approver, Boolean approved,
			LocalDateTime approveDateTime, LocalDateTime startOfLeave, LocalDateTime endOfLeave) {
		this.id = id;
		this.createDateTime = createDateTime;
		this.employee = employee;
		this.approver = approver;
		this.approved = approved;
		this.approveDateTime = approveDateTime;
		this.startOfLeave = startOfLeave;
		this.endOfLeave = endOfLeave;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public LocalDateTime getApproveDateTime() {
		return approveDateTime;
	}

	public void setApproveDateTime(LocalDateTime approveDateTime) {
		this.approveDateTime = approveDateTime;
	}

	public LocalDateTime getStartOfLeave() {
		return startOfLeave;
	}

	public void setStartOfLeave(LocalDateTime startOfLeave) {
		this.startOfLeave = startOfLeave;
	}

	public LocalDateTime getEndOfLeave() {
		return endOfLeave;
	}

	public void setEndOfLeave(LocalDateTime endOfLeave) {
		this.endOfLeave = endOfLeave;
	}

}
