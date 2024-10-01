package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherInsertDTO {

    @NotNull
    @Size(min = 2, max = 255, message = "Firstname must be between 2-255 characters.")
    private String firstname;

    @NotNull
    @Size(min = 2, max = 255, message = "Lastname must be between 2-255 characters.")
    private String lastname;
}
