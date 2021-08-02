package hu.webuni.hr.fic.mapper;

import java.util.List;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.fic.dto.LeaveDto;
import hu.webuni.hr.fic.model.Leave;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

	
	List<LeaveDto> leavesToDtos(List<Leave> leaves);	
	
	@Mapping(source = "employee.id", target = "employeeId")
	@Mapping(source = "approver.id", target = "approverId")	
	LeaveDto leaveToDto(Leave leave);

	@Mapping(target = "employee", ignore = true)
	@Mapping(target = "approver", ignore = true)
	Leave dtoToLeave(@Valid LeaveDto leaveDto);

	List<Leave> dtosToLeaves(List<LeaveDto> leaveDtos);
	

}
