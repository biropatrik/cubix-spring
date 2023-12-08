package hu.cubix.hr.patrik.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import hu.cubix.hr.patrik.config.HrConfigProperties;
import hu.cubix.hr.patrik.config.HrConfigProperties.JwtData;
import hu.cubix.hr.patrik.model.Employee;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class JwtService {

    private static String USERNAME = "username";
    private static String ID = "id";
    private static String FULLNAME = "fullname";
    private static String AUTH = "auth";
    private static String MANAGED_EMPLOYEES = "managedEmployees";
    private static String MANAGER = "manager";

    @Autowired
    private HrConfigProperties hrConfig;

    private String issuer; //NPE-t okozna: = hrConfig.getJwtData().getIssuer();
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        JwtData jwtData = hrConfig.getJwtData();
        issuer = jwtData.getIssuer();
        try {
            Method algMethod = Algorithm.class.getMethod(hrConfig.getJwtData().getAlg(), String.class);
            algorithm = (Algorithm) algMethod.invoke(null, jwtData.getSecret());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String createJwt(UserDetails userDetails) {
        Employee employee = ((HrUser) userDetails).getEmployee();
        Employee manager = employee.getManager();
        List<Employee> managedEmployees = employee.getManagedEmployees();

        Builder jwtBuilder = JWT.create()
                .withSubject(userDetails.getUsername())
                .withArrayClaim(AUTH, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .withClaim(FULLNAME, employee.getName())
                .withClaim(ID, employee.getId());

        if (manager != null) {
            jwtBuilder.withClaim(MANAGER, createMapFromEmployee(manager));
        }

        if (managedEmployees != null && !managedEmployees.isEmpty()) {
            jwtBuilder.withClaim(MANAGED_EMPLOYEES, managedEmployees.stream()
                    .map(this::createMapFromEmployee)
                    .toList());
        }

        return jwtBuilder
                .withExpiresAt(new Date(System.currentTimeMillis() + hrConfig.getJwtData().getDuration().toMillis()))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    private Map<String, Object> createMapFromEmployee(Employee employee) {
        return Map.of(
                ID, employee.getId(),
                USERNAME, employee.getUsername()
        );
    }

    public UserDetails parseJwt(String jwtToken) {

        DecodedJWT decodedJwt = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);

        Employee employee = new Employee();
        employee.setId(decodedJwt.getClaim(ID).asLong());
        employee.setName(decodedJwt.getClaim(FULLNAME).asString());
        employee.setUsername(decodedJwt.getSubject());

        Claim managerClaim = decodedJwt.getClaim(MANAGER);
        if (managerClaim != null) {
            Map<String, Object> managerData = managerClaim.asMap();
            employee.setManager(parseEmployeeFromMap(managerData));
        }

        Claim managedEmployeesClaim = decodedJwt.getClaim(MANAGED_EMPLOYEES);
        if (managedEmployeesClaim != null) {
            employee.setManagedEmployees(new ArrayList<>());
            List<HashMap> managedEmployees = managedEmployeesClaim.asList(HashMap.class);
            if (managedEmployees != null) {
                for (var employeeMap : managedEmployees) {
                    Employee managedEmployee = parseEmployeeFromMap(employeeMap);
                    if (managedEmployee != null) {
                        employee.getManagedEmployees().add(employee);
                    }
                }
            }
        }

        return new HrUser(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim("auth").asList(String.class).stream()
                .map(SimpleGrantedAuthority::new).toList(), employee);
    }

    private Employee parseEmployeeFromMap(Map<String, Object> employeeMap) {
        if (employeeMap != null) {
            Employee employee = new Employee();
            employee.setId(((Integer) employeeMap.get(ID)).longValue());
            employee.setUsername((String) employeeMap.get(USERNAME));
            return employee;
        }
        return null;
    }
}
