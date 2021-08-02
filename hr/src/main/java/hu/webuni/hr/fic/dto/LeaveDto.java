package hu.webuni.hr.fic.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class LeaveDto {
	private long id;
	private LocalDateTime createDateTime;
	@NotNull
	private Long employeeId;
	private Long approverId;
	private Boolean approved;
	private LocalDateTime approveDateTime;
	@NotNull
	private LocalDateTime startOfLeave;
	@NotNull
	private LocalDateTime endOfLeave;
	
	public LeaveDto() {
	}

	public LeaveDto(long id, LocalDateTime createDateTime, @NotNull Long employeeId, Long approverId, Boolean approved,
			LocalDateTime approveDateTime, @NotNull LocalDateTime startOfLeave, @NotNull LocalDateTime endOfLeave) {
		this.id = id;
		this.createDateTime = createDateTime;
		this.employeeId = employeeId;
		this.approverId = approverId;
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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
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
