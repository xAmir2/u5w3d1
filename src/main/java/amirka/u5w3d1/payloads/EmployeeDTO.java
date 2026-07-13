package amirka.u5w3d1.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EmployeeDTO(

        @NotEmpty(message = "Username is required, even if you don't have one in mind just google something")
        @Size(max = 30, message = "Username can't be longer than 30 characters")
        String username,

        @NotEmpty(message = "Name is required, seriously? We need a name.")
        @Size(max = 20, message = "Name can't be longer than 20 characters")
        String name,

        @NotEmpty(message = "Surname is required, not again.. we need to know who our employees are.")
        @Size(max = 20, message = "Surname can't be longer than 20 characters")
        String surname,

        @NotEmpty(message = "Email is required, how will we spam you with ads otherwise..")
        @Email(message = "Enter a valid email")
        String email,

        @NotEmpty(message = "Password is required, otherwise how can you login?")
        String password
) {
}
