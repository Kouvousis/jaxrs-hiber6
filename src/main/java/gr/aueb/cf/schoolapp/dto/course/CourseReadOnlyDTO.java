package gr.aueb.cf.schoolapp.dto.course;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseReadOnlyDTO {
    private Long id;
    private String courseName;
}
