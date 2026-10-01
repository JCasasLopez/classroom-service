package dev.jcasaslopez.classroom.filter;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.shared.dto.AuthResponse;
import dev.jcasaslopez.classroom.shared.enums.RoleName;
import dev.jcasaslopez.classroom.shared.enums.TokenPurpose;
import dev.jcasaslopez.classroom.shared.filter.AuthenticationFilterBase;
import dev.jcasaslopez.classroom.shared.handler.StandardResponseHandler;
import dev.jcasaslopez.classroom.shared.security.JwtService;
import dev.jcasaslopez.classroom.shared.utility.PublicSwaggerPaths;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ClassroomAuthenticationFilter extends AuthenticationFilterBase {
	
	private static final Set<String> EXCLUDED_PATHS = Set.of(
	        ClassroomEndpoints.GENERATE_TOKEN,
	        PublicSwaggerPaths.SWAGGER_UI, PublicSwaggerPaths.API_DOCS
	    );

	    public ClassroomAuthenticationFilter(JwtService jwtService, @Value("${jwt.secretKey}") String secretKey, 
	    		StandardResponseHandler standardResponseHandler) {
	        super(jwtService, secretKey, standardResponseHandler);
	    }

	    @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        String path = request.getRequestURI();
	        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
	    }

	    @Override
	    protected AuthResponse validateToken(String authHeader) {
	        return jwtService.validateJwt(authHeader, base64SecretKey, TokenPurpose.ACCESS, List.of(RoleName.ROLE_ADMIN));
	    }

}
