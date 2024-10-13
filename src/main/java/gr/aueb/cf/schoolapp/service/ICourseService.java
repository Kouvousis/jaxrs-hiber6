package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.course.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseUpdateDTO;

import java.util.List;
import java.util.Map;

public interface ICourseService {
    CourseReadOnlyDTO insertCourse(CourseInsertDTO insertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException;
    CourseReadOnlyDTO updateCourse(CourseUpdateDTO updateDTO) throws EntityNotFoundException, EntityInvalidArgumentException;
    void deleteCourse(Object id) throws EntityNotFoundException;
    CourseReadOnlyDTO getCourseById(Object id) throws EntityNotFoundException;
    List<CourseReadOnlyDTO> getAllCourses();
    List<CourseReadOnlyDTO> getCoursesByCriteria(Map<String, Object> criteria);
}
