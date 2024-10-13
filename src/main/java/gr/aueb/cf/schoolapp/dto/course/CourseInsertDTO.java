package gr.aueb.cf.schoolapp.dto.course;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseInsertDTO {

    @NotNull(message = "Course name must exist")
    @Size(min = 2, max = 255, message = "Course name must be between 2-255 characters.")
    private String courseName;
}
