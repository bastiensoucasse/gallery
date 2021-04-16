package pdl.backend.security.jwt;


import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import pdl.backend.mysqldb.UserRepository;
import pdl.backend.security.services.CustomUserDetails;

import java.util.Date;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    //@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret = "secret";

	//@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs = 86400000;

	


    public String generateJwtToken(Authentication authentication) {

		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

    public boolean validateJwtToken(String authToken) {
        try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();

    }

	public static boolean checkUserAuthentification(pdl.backend.mysqldb.User user){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(user + " " + authentication.isAuthenticated() + " " + user.getUsername() + " " + authentication.getName());
		return (user != null && authentication.isAuthenticated() && authentication.getName().equals(user.getUsername()));
		
	}




    
}