package amirka.u5w3d1.controllers;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.ValidationEx;
import amirka.u5w3d1.payloads.EmployeeDTO;
import amirka.u5w3d1.payloads.EmployeeResponseDTO;
import amirka.u5w3d1.payloads.LoginDTO;
import amirka.u5w3d1.payloads.LoginRespDTO;
import amirka.u5w3d1.services.AuthService;
import amirka.u5w3d1.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final EmployeeService employeeService;

    public AuthController(AuthService authService, EmployeeService employeeService) {
        this.authService = authService;
        this.employeeService = employeeService;
    }

    @PostMapping("/login") // Handles POST requests to /auth/login.
    public LoginRespDTO login(@RequestBody LoginDTO body) {

        // Checks the user's credentials and returns a JWT token if they are valid.
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register") // Handles POST requests to /auth/register.
    @ResponseStatus(HttpStatus.CREATED) // Returns HTTP 201 Created when registration succeeds.
    public EmployeeResponseDTO saveUser(@RequestBody @Validated EmployeeDTO body,
                                        BindingResult validationResult) {

        // Checks whether any validation errors occurred.
        if (validationResult.hasErrors()) {

            // Collects all validation error messages into a list.
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            // Throws a custom exception containing all validation errors.
            throw new ValidationEx(errorsList);
        }

        // Saves the new employee in the database.
        Employee saved = this.employeeService.save(body);

        // Returns the ID of the newly created employee.
        return new EmployeeResponseDTO(saved.getId());
    }
}
