package hu.webuni.hr.fic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.fic.dto.LoginDto;

@RestController
public class JwtLoginController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;

	@PostMapping("/api/login")
	public String login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		return jwtService.createJwtToken((EmployeeUserDetails)authentication.getPrincipal());
		
	}
}
