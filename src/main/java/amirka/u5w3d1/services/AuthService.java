package amirka.u5w3d1.services;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.UnauthorizedEx;
import amirka.u5w3d1.payloads.LoginDTO;
import amirka.u5w3d1.security.JWTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final EmployeeService employeeService;
    private final JWTools jwTools;
    private PasswordEncoder bCrypt;


    public AuthService(PasswordEncoder bCrypt, JWTools jwTools, EmployeeService employeeService) {
        this.bCrypt = bCrypt;
        this.jwTools = jwTools;
        this.employeeService = employeeService;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        // Searches for the employee using the email provided during login.
        Employee found = this.employeeService.findByEmail(body.email());

        // Checks if the provided password matches the stored password.
        if (bCrypt.matches(body.password(), found.getPassword())) {

            // Generates and returns a JWT token if the credentials are valid.
            return this.jwTools.generateToken(found);

        } else {

            // Throws an exception if the email or password is incorrect.
            throw new UnauthorizedEx("Invalid password or email.");
        }
    }
}
