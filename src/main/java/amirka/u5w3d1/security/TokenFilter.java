package amirka.u5w3d1.security;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.UnauthorizedEx;
import amirka.u5w3d1.services.EmployeeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTools jwTools;
    private final EmployeeService employeeService;

    public TokenFilter(JWTools jwTools, EmployeeService employeeService) {
        this.jwTools = jwTools;
        this.employeeService = employeeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    FilterChain filter) throws ServletException, IOException {

        // Gets the Authorization header from the HTTP request.
        String authHeader = req.getHeader("Authorization");

        // Checks if the header exists and follows the "Bearer <token>" format.
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedEx("Inser token in authorization bearer!");

        // Removes "Bearer " prefix and extracts only the JWT token.
        String accessToken = authHeader.replace("Bearer ", "");

        // Verifies that the JWT is valid (correct signature, not expired, not modified).
        this.jwTools.tokenVerification(accessToken);

        UUID employeeId = jwTools.extractIdFromToken(accessToken);
        Employee currentEmployee = this.employeeService.findById(employeeId);

        //associo un utente al security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentEmployee, null,
                currentEmployee.getAuthorities());
        //security context
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        // If the token is valid, continues the request to the next filter/controller.
        filter.doFilter(req, resp);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {

        // Skips this filter for the login endpoint because users don't have a token yet.
        return new AntPathMatcher().match("/auth/**", req.getServletPath());
    }
}
