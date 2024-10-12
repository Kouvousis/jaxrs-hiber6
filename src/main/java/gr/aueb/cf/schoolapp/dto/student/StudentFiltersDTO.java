package gr.aueb.cf.schoolapp.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentFiltersDTO {

    private String firstname;
    private String lastname;
    private String vat;
}
