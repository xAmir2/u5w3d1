package amirka.u5w3d1.security;

import amirka.u5w3d1.exceptions.UnauthorizedEx;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTools jwTools;

    public TokenFilter(JWTools jwTools) {
        this.jwTools = jwTools;
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

        // If the token is valid, continues the request to the next filter/controller.
        filter.doFilter(req, resp);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {

        // Skips this filter for the login endpoint because users don't have a token yet.
        return new AntPathMatcher().match("/auth/login", req.getServletPath());
    }
}
