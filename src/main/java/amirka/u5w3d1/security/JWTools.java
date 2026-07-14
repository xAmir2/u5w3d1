package amirka.u5w3d1.security;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.UnauthorizedEx;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTools {
    private final String secret;

    public JWTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    // We use string the return (token) will be a string
    public String generateToken(Employee employee) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) //Date of issue, must be in milliseconds
                .expiration(new Date(
                        System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //Date of expiry, must be in milliseconds too
                .subject(
                        String.valueOf(employee.getId())) //Token owner, we will use the ID and never use sensitive data
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) //Token's signature with HMAC SHA to 
                .compact();
    }

    public void tokenVerification(String token) {

        try {

            // 1. Create a JWT parser.
            // The parser is responsible for reading and validating a JWT token.
            Jwts.parser()

                    // 2. Specify the secret key that was originally used
                    // to sign the token during its creation.
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))

                    // 3. Build the configured parser.
                    .build()

                    // 4. Parse and validate the token.
                    .parse(token);

            // If execution reaches this point, the token is considered valid.

        } catch (Exception ex) {

            // Any exception means the token is not valid.

            throw new UnauthorizedEx(
                    "There were issues with the toke, login again!"
            );
        }
    }

    public UUID extractIdFromToken(String token) {
        return UUID.fromString(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}
