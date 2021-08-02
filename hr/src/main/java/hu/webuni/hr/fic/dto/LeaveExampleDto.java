package hu.webuni.hr.fic.dto;

import java.time.LocalDateTime;

public class LeaveExampleDto {
	private long id;
	private LocalDateTime createDateTimeStart;
	private LocalDateTime createDateTimeEnd;	
	private String employeeName;
	private String approverName;
	private Boolean approved;
	private LocalDateTime startOfLeave;
	private LocalDateTime endOfLeave;
	
	public LeaveExampleDto() {
	}

	public LeaveExampleDto(long id, LocalDateTime createDateTimeStart, LocalDateTime createDateTimeEnd,
			String employeeName, String approverName, Boolean approved, LocalDateTime startOfLeave,
			LocalDateTime endOfLeave) {
		this.id = id;
		this.createDateTimeStart = createDateTimeStart;
		this.createDateTimeEnd = createDateTimeEnd;
		this.employeeName = employeeName;
		this.approverName = approverName;
		this.approved = approved;
		this.startOfLeave = startOfLeave;
		this.endOfLeave = endOfLeave;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getCreateDateTimeStart() {
		return createDateTimeStart;
	}

	public void setCreateDateTimeStart(LocalDateTime createDateTimeStart) {
		this.createDateTimeStart = createDateTimeStart;
	}

	public LocalDateTime getCreateDateTimeEnd() {
		return createDateTimeEnd;
	}

	public void setCreateDateTimeEnd(LocalDateTime createDateTimeEnd) {
		this.createDateTimeEnd = createDateTimeEnd;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
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
