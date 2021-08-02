package hu.webuni.hr.fic.web;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.fic.dto.LeaveDto;
import hu.webuni.hr.fic.dto.LeaveExampleDto;
import hu.webuni.hr.fic.mapper.EmployeeMapper;
import hu.webuni.hr.fic.mapper.LeaveMapper;
import hu.webuni.hr.fic.model.Leave;
import hu.webuni.hr.fic.service.EmployeeService;
import hu.webuni.hr.fic.service.LeaveService;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

	@Autowired
	LeaveService leaveService;

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	LeaveMapper leaveMapper;
	
	@Autowired 
	EmployeeService employeeService;

	@GetMapping
	public List<LeaveDto> getAll() {
		return leaveMapper.leavesToDtos(leaveService.findAll());
	}

	@GetMapping("/{id}")
	public LeaveDto getById(@PathVariable long id) {
		Leave leave = leaveService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return leaveMapper.leaveToDto(leave);
	}

	@PostMapping(value = "/search")
	public List<LeaveDto> findByExample(@RequestBody LeaveExampleDto example, 
			@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Leave> page = leaveService.findLeavesByExample(example, pageable);
		return leaveMapper.leavesToDtos(page.getContent());
	}

	@PostMapping
	public LeaveDto addLeave(@RequestBody @Valid LeaveDto newLeave) {
		Leave leave;
		try {
			leave = leaveService.addLeave(leaveMapper.dtoToLeave(newLeave), newLeave.getEmployeeId());
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return leaveMapper.leaveToDto(leave);
	}

	@PutMapping("/{id}")
	public LeaveDto modifyLeave(@PathVariable long id, @RequestBody @Valid LeaveDto modifiedLeave) {
		//modifiedLeave.setEmployeeId(id);
		Leave leave;
		try {
			leave = leaveService.modifyLeave(
					id, 
					leaveMapper.dtoToLeave(modifiedLeave), 
					modifiedLeave.getEmployeeId());
		} catch (InvalidParameterException e) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return leaveMapper.leaveToDto(leave);
	}

	@DeleteMapping("/{id}")
	public void deleteLeave(@PathVariable long id) {
		try {
			leaveService.deleteLeave(id, leaveService.findById(id).get().getEmployee().getId());
		} catch (InvalidParameterException e) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{id}/approval", params = { "status", "approvalId" })
	public LeaveDto approveLeave(@PathVariable long id, @RequestParam long approvalId, @RequestParam boolean status) {
		Leave leave;
		try {
			leave = leaveService.approveLeave(id, approvalId, status);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (AccessDeniedException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return leaveMapper.leaveToDto(leave);
	}

}
