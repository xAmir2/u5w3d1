package amirka.u5w3d1.services;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.UnauthorizedEx;
import amirka.u5w3d1.payloads.LoginDTO;
import amirka.u5w3d1.security.JWTools;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final EmployeeService employeeService;
    private final JWTools jwTools;

    public AuthService(EmployeeService employeeService, JWTools jwTools) {
        this.employeeService = employeeService;
        this.jwTools = jwTools;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        // Searches for the employee using the email provided during login.
        Employee found = this.employeeService.findByEmail(body.email());

        // Checks if the provided password matches the stored password.
        if (found.getPassword()
                .equals(body.password())) {

            // Generates and returns a JWT token if the credentials are valid.
            return this.jwTools.generateToken(found);

        } else {

            // Throws an exception if the email or password is incorrect.
            throw new UnauthorizedEx("Invalid password or email.");
        }
    }
}
