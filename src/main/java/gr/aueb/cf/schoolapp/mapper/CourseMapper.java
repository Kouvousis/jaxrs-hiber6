package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.dto.course.CourseFiltersDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.model.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourseMapper {

    private CourseMapper() {
    }

    public static Course mapToCourse(CourseInsertDTO insertDTO) {
        return new Course(null, insertDTO.getCourseName());
    }

    public static Course mapToCourse(CourseUpdateDTO updateDTO) {
        return new Course(updateDTO.getId(), updateDTO.getCourseName());
    }

    public static CourseReadOnlyDTO mapToCourseReadOnlyDTO(Course course) {
        return new CourseReadOnlyDTO(course.getId(), course.getCourseName());
    }

    public static List<CourseReadOnlyDTO> mapToCourseReadOnlyDTOs(List<Course> courses) {
        return courses.stream().map(CourseMapper::mapToCourseReadOnlyDTO).collect(Collectors.toList());
    }

    public static Map<String, Object> mapToCriteriaCourse(CourseFiltersDTO filtersDTO) {
        Map<String, Object> filters = new HashMap<>();
        if (!(filtersDTO.getCourseName() == null || filtersDTO.getCourseName().isEmpty())) {
            filters.put("courseName", filtersDTO.getCourseName());
        }

        return filters;
    }
}
