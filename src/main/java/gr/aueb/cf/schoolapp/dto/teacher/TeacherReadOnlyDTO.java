package gr.aueb.cf.schoolapp.dto.teacher;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeacherReadOnlyDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String vat;
}
