package amirka.u5w2d4.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorDTO(

        @NotBlank(message = "First name is required and cannot be blank")
        @Size(min = 2, max = 40, message = "First name must be between 2 and 40 characters")
        String name,

        @NotBlank(message = "Last name is required and cannot be blank")
        @Size(min = 2, max = 40, message = "Last name must be between 2 and 40 characters")
        String surname,

        @NotBlank(message = "Email is required and cannot be blank")
        @Email(message = "Email must be in a valid format")
        String email,

        @Past(message = "Date of birth must be in the past")
        LocalDate birthDate

) {
}
