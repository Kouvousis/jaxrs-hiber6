package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.dto.teacher.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.teacher.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.teacher.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.mapper.TeacherMapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject) )
public class TeacherServiceImpl implements ITeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    //@Inject
    private final ITeacherDAO teacherDAO;

    @Override
    public TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Teacher teacher = TeacherMapper.mapToTeacher(insertDTO);

            // Insert is NOT idempotent
            if (teacherDAO.getByVat(insertDTO.getVat()).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher", "Teacher with vat: " + insertDTO.getVat() + " already exists");
            }

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.insert(teacher)
                            .map(TeacherMapper::mapToTeacherReadOnlyDTO)
                            .orElseThrow(() -> new EntityInvalidArgumentException("Teacher", "Teacher with vat: " +
                                    insertDTO.getVat() + " not inserted"));

            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {},firstname {}, lastname {}, vat{} inserted.",
                    teacher.getId(),
                    teacher.getFirstname(),
                    teacher.getLastname(),
                    teacher.getVat());

            return readOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Teacher not inserted: firstname: {}, lastname: {} vat: {}",
                    insertDTO.getFirstname(),
                    insertDTO.getLastname(),
                    insertDTO.getVat());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Teacher teacher = TeacherMapper.mapToTeacher(updateDTO);

//            if (teacherDAO.getById(updateDTO.getId()).isEmpty()) {
//                throw new EntityNotFoundException("Teacher", "Teacher with id: " + updateDTO.getId() + " not found");
//            }
            teacherDAO.getByVat(updateDTO.getVat()).orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with vat: "
                    + updateDTO.getVat() + " not found"));
            teacherDAO.getById(updateDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with vat: "
                    + updateDTO.getVat() + " not found"));

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.update(teacher)
                    .map(TeacherMapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Teacher", "Error during update"));

            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {}, vat {}, lastname {}, firstname {} updated.",
                    teacher.getId(),
                    teacher.getVat(),
                    teacher.getLastname(),
                    teacher.getFirstname());

            return readOnlyDTO;
        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Teacher with id: {}, vat {}, firstname: {}, lastname: {} not updated",
                    updateDTO.getId(),
                    updateDTO.getVat(),
                    updateDTO.getFirstname(),
                    updateDTO.getLastname());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public void deleteTeacher(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            teacherDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with id: " + id + " not found"));
            teacherDAO.delete(id);
            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id: {} was deleted.", id);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Teacher with id: {} was not deleted.", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public TeacherReadOnlyDTO getTeacherById(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.getById(id)
                            .map(TeacherMapper::mapToTeacherReadOnlyDTO)
                            .orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with id: " + id + " not found"));
            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {} was found.", id);
            return readOnlyDTO;
        } catch (EntityNotFoundException e) {
            //JPAHelper.rollbackTransaction();
            LOGGER.error("Warning. Teacher with id: {} was not found.", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<TeacherReadOnlyDTO> getAllTeachers() {
        try {
            JPAHelper.beginTransaction();
            List<TeacherReadOnlyDTO> readOnlyDTOS = teacherDAO.getAll()
                    .stream()
                    .map(TeacherMapper::mapToTeacherReadOnlyDTO)
                    .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<TeacherReadOnlyDTO> getTeachersByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<TeacherReadOnlyDTO> readOnlyDTOS = teacherDAO.getByCriteria(criteria)
                        .stream()
                        .map(TeacherMapper::mapToTeacherReadOnlyDTO)
                        .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
