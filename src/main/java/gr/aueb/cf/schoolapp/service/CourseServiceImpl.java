package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.ICourseDAO;
import gr.aueb.cf.schoolapp.dto.course.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.CourseMapper;
import gr.aueb.cf.schoolapp.mapper.StudentMapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CourseServiceImpl implements ICourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final ICourseDAO courseDAO;

    @Override
    public CourseReadOnlyDTO insertCourse(CourseInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
       try {
           JPAHelper.beginTransaction();
           Course course = CourseMapper.mapToCourse(insertDTO);

           if (courseDAO.getByName(insertDTO.getCourseName()).isPresent()) {
               throw new EntityAlreadyExistsException("Course", "Course with name: "
                       + insertDTO.getCourseName()
                       + " already exists");
           }

           CourseReadOnlyDTO readOnlyDTO = courseDAO.insert(course)
                   .map(CourseMapper::mapToCourseReadOnlyDTO)
                   .orElseThrow(() -> new EntityInvalidArgumentException("Course", "Course with name :"
                           + insertDTO.getCourseName() + "  not inserted"));

           JPAHelper.commitTransaction();
           LOGGER.info("Course with id: {}, name: {} inserted ", course.getId() , course.getCourseName());
           return readOnlyDTO;
       } catch (EntityAlreadyExistsException | EntityInvalidArgumentException e) {
           JPAHelper.rollbackTransaction();
           LOGGER.error("Error. Course with name: {} not inserted", insertDTO.getCourseName());
           throw e;
       } finally {
           JPAHelper.closeEntityManager();
       }
    }

    @Override
    public CourseReadOnlyDTO updateCourse(CourseUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Course course = CourseMapper.mapToCourse(updateDTO);

            courseDAO.getById(updateDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Course", "Course with id: "
                    + updateDTO.getId() + " not found"));

            CourseReadOnlyDTO readOnlyDTO = courseDAO.update(course)
                    .map(CourseMapper::mapToCourseReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Course", "Error during update"));

            JPAHelper.commitTransaction();
            LOGGER.info("Course with id {}, name {} updated",
                    course.getId(),
                    course.getCourseName());
            return readOnlyDTO;
        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Course with name: {} not updated", updateDTO.getCourseName());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public void deleteCourse(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            courseDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Course", "Course with id: " + id + " not found"));
            courseDAO.delete(id);
            JPAHelper.commitTransaction();
            LOGGER.info("Course with id {} deleted", id);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Course with id: {} not deleted", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public CourseReadOnlyDTO getCourseById(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            CourseReadOnlyDTO readOnlyDTO = courseDAO.getById(id)
                    .map(CourseMapper::mapToCourseReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Course", "Course with id: " + id + " not found"));
            JPAHelper.commitTransaction();
            return readOnlyDTO;
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Course with id: {} not found", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<CourseReadOnlyDTO> getAllCourses() {
        try {
            JPAHelper.beginTransaction();
            List<CourseReadOnlyDTO> readOnlyDTOS = courseDAO.getAll()
                    .stream()
                    .map(CourseMapper::mapToCourseReadOnlyDTO)
                    .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<CourseReadOnlyDTO> getCoursesByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<CourseReadOnlyDTO> readOnlyDTOS = courseDAO.getByCriteria(criteria)
                    .stream()
                    .map(CourseMapper::mapToCourseReadOnlyDTO)
                    .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
