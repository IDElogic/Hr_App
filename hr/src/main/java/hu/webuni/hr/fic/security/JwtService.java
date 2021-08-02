package hu.webuni.hr.fic.security;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.hr.fic.config.HrConfigProperties;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.service.EmployeeService;

@Service
public class JwtService {
	
	@Autowired
	HrConfigProperties config;
	
	@Autowired
	EmployeeService employeeService;
	
	private static final String AUTH = "auth";
	private static final String EMPLOYEEID = "employeeid";
	private static final String EMPLOYEENAME = "employeename";
	private static final String SUPERIORID = "superiorid";
	private static final String SUPERIORNAME = "superiorname";
	private static final String SUBORDINATEID = "subordinateid";
	private static final String SUBORDINATENAME = "subordinatename";
			
	
	private int timeout;
	private String issuer;
	private Method algMethod;
	private String secret;

	
	@PostConstruct
	public void init() {
		timeout = config.getJwt().getTimeout();
		secret = config.getJwt().getSecret();
		String algorithmName = config.getJwt().getAlgorithm();
			try {
				algMethod = Class.forName("com.auth0.jwt.algorithms.Algorithm").getMethod(algorithmName, String.class);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		issuer = config.getJwt().getIssuer();
	}
	
	
	public String createJwtToken(EmployeeUserDetails principal) {
		String jwtToken = "";	
		List<Employee> subordinates = employeeService.findBySuperiorId(principal.getEmployee().getId());		
		Employee superior = principal.getEmployee().getSuperior();
		
		try {
			jwtToken = JWT.create()
				.withSubject(principal.getUsername())
				.withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
				.withClaim(EMPLOYEENAME, principal.getEmployee().getName())
				.withClaim(EMPLOYEEID, principal.getEmployeeId())
				.withClaim(SUPERIORNAME, superior != null ? superior.getName() : null)
				.withClaim(SUPERIORID, superior != null ? superior.getId() : null)
				.withArrayClaim(SUBORDINATENAME, subordinates.stream().map(Employee::getName).toArray(String[]::new))
				.withArrayClaim(SUBORDINATEID, subordinates.stream().map(Employee::getId).toArray(Long[]::new))
				.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(timeout)))
				.withIssuer(issuer)
				.sign((Algorithm) algMethod.invoke(Class.forName("com.auth0.jwt.algorithms.Algorithm") , secret));
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return jwtToken;
		
	}

	public EmployeeUserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = null;
		try {
			decodedJwt = JWT.require((Algorithm) algMethod.invoke(Class.forName("com.auth0.jwt.algorithms.Algorithm") , secret))
				.withIssuer(issuer)
				.build()
				.verify(jwtToken);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return new EmployeeUserDetails(decodedJwt.getSubject(), "dummy", 
				decodedJwt.getClaim(AUTH).asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
				employeeService.findById(decodedJwt.getClaim(EMPLOYEEID).asLong()).orElseThrow(() -> new NoSuchElementException())
				);
		
	}

}
