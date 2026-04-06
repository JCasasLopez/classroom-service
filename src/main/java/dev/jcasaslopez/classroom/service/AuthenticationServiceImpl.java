package dev.jcasaslopez.classroom.service;

import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import dev.jcasaslopez.classroom.enums.RoleName;
import dev.jcasaslopez.classroom.enums.TokenType;
import dev.jcasaslopez.classroom.exception.FailedAuthenticatedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

public class AuthenticationServiceImpl implements AuthenticationService {


	private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	private final SecretKey key;
	@Value("${jwt.secretKey}") String base64SecretKey;

	public AuthenticationServiceImpl(SecretKey key, String base64SecretKey) {
		this.key = key;
		
		// Convert the Base64 encoded string back into a raw byte array.
		byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
		
		// Create a secure HMAC signing key from the bytes and validate its length.
		key = Keys.hmacShaKeyFor(keyBytes);
		logger.info("Secret key read from application.properties, decoded and converted into SecretKey format");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean validateToken(String header) {
		try {
			String token = extractToken(header);
			Claims claims = parseToken(token, key);
			String purpose = claims.get("purpose", String.class);
			List<String> roles = claims.get("roles", List.class);
			purposeIsValid(purpose);
			rolesAreValid(roles);
			logger.info("Token validated");
			return true;
		} catch (FailedAuthenticatedException ex) {
			logger.warn("Validation failed: {}", ex.getMessage());
			return false;
		}
	}

	private String extractToken(String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new FailedAuthenticatedException("Token is missing or does not start with 'Bearer '");
		}
		logger.debug("Token extracted");
		return authHeader.substring(7);
	}

	private Claims parseToken(String token, SecretKey key) {
		try {
			Claims claims = Jwts.parser()
					
					// Sets the key that will be used to verify the signature.
					.verifyWith(key)
					
					// Builds the JWT parser with the specified configuration.
					.build()
					
					// This is where all verifications happen.
					.parseSignedClaims(token)
					.getPayload();
			logger.debug("Token payload extracted");
			return claims;
		} catch (ExpiredJwtException | MalformedJwtException | io.jsonwebtoken.security.SecurityException ex) {
			throw new FailedAuthenticatedException("Expired or malformed token");
		}
	}

	private void purposeIsValid(String purpose) {
		if(!TokenType.VERIFICATION.name().equals(purpose)) {
			throw new FailedAuthenticatedException("Token is not a verification token");
		}
		logger.debug("Verification token validated");
	}

	private void rolesAreValid(List<String> roles) {
		for(String rol:roles) {
			if(rol.equals(RoleName.ROLE_ADMIN.name()) || rol.equals(RoleName.ROLE_SUPERADMIN.name())) {
				logger.debug("Admin credentials validated");
				return;
			}
		}
		throw new FailedAuthenticatedException("User does not have the admin credentials to access this service");
	}
	
}