package gr.aueb.cf.schoolapp.dto.student;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudentReadOnlyDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String vat;
}
