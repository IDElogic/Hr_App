package hu.webuni.hr.fic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import hu.webuni.hr.fic.dto.PositionDto;
import hu.webuni.hr.fic.mapper.PositionMapper;
import hu.webuni.hr.fic.model.Position;
import hu.webuni.hr.fic.service.PositionService;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

	@Autowired
	PositionService positionService;

	@Autowired
	PositionMapper positionMapper;

	@GetMapping
	public List<PositionDto> getPositions() {
		return positionMapper.positionsToDtos(positionService.findAll());
	}

	@GetMapping(params = "name")
	public List<PositionDto> getPositionsByName(String name) {
		return positionMapper.positionsToDtos(positionService.findByName(name));
	}

	@PutMapping("/raiseMinSalary")
	public List<PositionDto> raiseMinSalary(@RequestParam int minSalary, @RequestParam String positionName,
			@RequestParam(required = false) Long registrationNumber) {
		List<PositionDto> positionDtos;
		if (registrationNumber == null) {
			positionDtos = positionMapper.positionsToDtos(
					positionService.setMinSalaryAndRaiseSalaryToMinByPositionName(minSalary, positionName));
		} else {
			positionDtos = new ArrayList<>();
			positionDtos.add(positionMapper
					.positionToDto(positionService.setMinSalaryAndRaiseSalaryToMinByPositionNameAndCompaniId(minSalary,
							positionName, registrationNumber)));
		}
		return positionDtos;
	}

	@PostMapping
	public PositionDto addPosition(@RequestBody PositionDto positionDto) {
		Position position = positionMapper.dtoToPosition(positionDto);
		try {
			return positionMapper.positionToDto(positionService.addPosition(position, positionDto.getCompany()));
		} catch (EntityExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public PositionDto changePosition(@RequestBody PositionDto positionDto, @PathVariable long id) {
		positionDto.setId(id);
		Position position = positionMapper.dtoToPosition(positionDto);
		try {
			return positionMapper.positionToDto(positionService.updatePosition(position, positionDto.getCompany()));
		} catch (EntityExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public void deletePosition(@PathVariable long id) {
		try {
			positionService.delete(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

	}
}
