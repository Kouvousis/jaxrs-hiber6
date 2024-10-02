package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherInsertDTO {

    @NotNull(message = "Firstname must exist")
    @Size(min = 2, max = 255, message = "Firstname must be between 2-255 characters.")
    private String firstname;

    @NotNull(message = "Lastname must exist")
    @Size(min = 2, max = 255, message = "Lastname must be between 2-255 characters.")
    private String lastname;

    @NotNull(message = "VAT must exists")
    @Size(min = 9, message = "VAT must include at least 9 characters.")
    private String vat;
}
