package hu.webuni.hr.fic.service;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.fic.model.Employee_;
import hu.webuni.hr.fic.model.Leave;
import hu.webuni.hr.fic.model.Leave_;

public class LeaveSpecifications {

	public static Specification<Leave> hasApproved(Boolean approved) {
		return (root, cq, cb) -> cb.equal(root.get(Leave_.approved), approved);
	}

	public static Specification<Leave> hasId(long id) {
		return (root, cq, cb) -> cb.equal(root.get(Leave_.id), id);
	}

	public static Specification<Leave> createDateIsBetween(LocalDateTime createDateTimeStart,
			LocalDateTime createDateTimeEnd) {
		return (root, cq, cb) -> cb.between(root.get(Leave_.createDateTime), createDateTimeStart, createDateTimeEnd);
	}

	public static Specification<Leave> hasEmployeeName(String employeeName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Leave_.employee).get(Employee_.name)),
				(employeeName + "%").toLowerCase());
	}

	public static Specification<Leave> hasApprovalName(String approvalName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Leave_.approver).get(Employee_.name)),
				(approvalName + "%").toLowerCase());
	}

	public static Specification<Leave> leaveStartIsLessThan(LocalDateTime startOfLeave) {
		return (root, cq, cb) -> cb.lessThan(root.get(Leave_.startOfLeave), startOfLeave);
	}
	
	public static Specification<Leave> leaveEndIsGreaterThan(LocalDateTime endOfLeave) {
		return (root, cq, cb) -> cb.greaterThan(root.get(Leave_.endOfLeave), endOfLeave);
	}

}
