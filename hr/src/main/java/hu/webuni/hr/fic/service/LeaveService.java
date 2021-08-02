package hu.webuni.hr.fic.service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.hr.fic.dto.LeaveExampleDto;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.model.Leave;
import hu.webuni.hr.fic.repository.LeaveRepository;

@Service
public class LeaveService {

	@Autowired
	LeaveRepository leaveRepository;

	@Autowired
	EmployeeService employeeService;

	public List<Leave> findAll() {
		return leaveRepository.findAll();
	}

	public Optional<Leave> findById(long id) {
		return leaveRepository.findById(id);
	}

	public Page<Leave> findLeavesByExample(LeaveExampleDto example, Pageable pageable) {
		long id = example.getId();
		LocalDateTime createDateTimeStart = example.getCreateDateTimeStart();
		LocalDateTime createDateTimeEnd = example.getCreateDateTimeEnd();
		String employeeName = example.getEmployeeName();
		String approvalName = example.getApproverName();
		Boolean approved = example.getApproved();
		LocalDateTime startOfLeave = example.getStartOfLeave();
		LocalDateTime endOfLeave = example.getEndOfLeave();

		Specification<Leave> spec = Specification.where(null);

		if (approved != null)
			spec = spec.and(LeaveSpecifications.hasApproved(approved));
		if (id > 0)
			spec = spec.and(LeaveSpecifications.hasId(id));
		if (createDateTimeStart != null && createDateTimeEnd != null)
			spec = spec.and(LeaveSpecifications.createDateIsBetween(createDateTimeStart, createDateTimeEnd));
		if (StringUtils.hasText(employeeName))
			spec = spec.and(LeaveSpecifications.hasEmployeeName(employeeName));
		if (StringUtils.hasText(approvalName))
			spec = spec.and(LeaveSpecifications.hasApprovalName(approvalName));
		if (startOfLeave != null)
			spec = spec.and(LeaveSpecifications.leaveEndIsGreaterThan(startOfLeave));
		if (endOfLeave != null)
			spec = spec.and(LeaveSpecifications.leaveStartIsLessThan(endOfLeave));
		return leaveRepository.findAll(spec, pageable);
	}

	@Transactional
	@PreAuthorize("#employeeId == authentication.principal.employeeId")
	public Leave addLeave(Leave leave, long employeeId) {
		Employee employee = employeeService.findById(employeeId).get();
		employee.addLeave(leave);
		leave.setCreateDateTime(LocalDateTime.now());
		
		return leaveRepository.save(leave);
	}
	

	@Transactional
	@PreAuthorize("#approvalId == authentication.principal.employeeId")
	public Leave approveLeave(long id, long approvalId, boolean status) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getEmployee().getSuperior().getId() != approvalId)
			throw new AccessDeniedException("");
		leave.setApprover(employeeService.findById(approvalId).get());
		leave.setApproved(status);
		leave.setApproveDateTime(LocalDateTime.now());
		return leave;
	}
	
	

	@Transactional
	@PreAuthorize("#employeeId == authentication.principal.employeeId")
	public Leave modifyLeave(long id, Leave newLeave, long employeeId) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getApproved() != null)
			throw new InvalidParameterException();
		leave.setStartOfLeave(newLeave.getStartOfLeave());
		leave.setEndOfLeave(newLeave.getEndOfLeave());
		leave.setCreateDateTime(LocalDateTime.now());
		return leave;
	}


	@Transactional
	@PreAuthorize("#employeeId == authentication.principal.employeeId")
	public void deleteLeave(long id, long employeeId) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getApproved() != null)
			throw new InvalidParameterException();
		employeeService.findById(employeeId).get().getLeaves().remove(leave);
		leaveRepository.deleteById(id);
	}
	
	

}
