package dev.jcasaslopez.classroom.filter;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import dev.jcasaslopez.classroom.shared.domain.UserInfo;
import dev.jcasaslopez.classroom.shared.enums.TokenType;
import dev.jcasaslopez.classroom.shared.filter.AuthenticationFilterBase;
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

	    public ClassroomAuthenticationFilter(JwtService jwtService, @Value("${jwt.secretKey}") String secretKey) {
	        super(jwtService, secretKey);
	    }

	    @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        String path = request.getRequestURI();
	        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
	    }

	    @Override
	    protected Optional<UserInfo> validateToken(String authHeader) {
	        return jwtService.validateJwt(authHeader, base64SecretKey, TokenType.ACCESS);
	    }

}
