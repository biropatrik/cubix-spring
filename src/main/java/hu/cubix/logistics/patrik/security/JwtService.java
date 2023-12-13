package hu.cubix.logistics.patrik.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import hu.cubix.logistics.patrik.config.LogisticsConfigurationProperties;
import hu.cubix.logistics.patrik.config.LogisticsConfigurationProperties.JwtData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class JwtService {
    private static String AUTH = "auth";

    @Autowired
    private LogisticsConfigurationProperties config;

    private String issuer;
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        JwtData jwtData = config.getJwtData();
        issuer = jwtData.getIssuer();
        try {
            Method algMethod = Algorithm.class.getMethod(config.getJwtData().getAlg(), String.class);
            algorithm = (Algorithm) algMethod.invoke(null, jwtData.getSecret());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String createJwt(UserDetails userDetails) {

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withArrayClaim(AUTH, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + config.getJwtData().getDuration().toMillis()))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public UserDetails parseJwt(String jwtToken) {

        DecodedJWT decodedJwt = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);

        return new User(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim(AUTH).asList(String.class)
                .stream().map(SimpleGrantedAuthority::new).toList());
    }
}
