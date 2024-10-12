package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.IStudentDAO;
import gr.aueb.cf.schoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.StudentMapper;
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
public class StudentServiceImpl implements IStudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final IStudentDAO studentDAO;

    @Override
    public StudentReadOnlyDTO insertStudent(StudentInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Student student = StudentMapper.mapToStudent(insertDTO);

            if (studentDAO.getByVat(insertDTO.getVat()).isPresent()) {
                throw new EntityAlreadyExistsException("Student", "Student with vat: " + insertDTO.getVat() + " already exists");
            }

            StudentReadOnlyDTO readOnlyDTO = studentDAO.insert(student)
                    .map(StudentMapper::mapToStudentReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Student", "Student with vat: "
                            + insertDTO.getVat() + " not inserted"));

            JPAHelper.commitTransaction();
            LOGGER.info("Student with id {}, firstname {}, lastname{}, vat{} inserted",
                    student.getId(),
                    student.getFirstname(),
                    student.getLastname(),
                    student.getVat());

            return readOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Student not inserted firstname: {}, lastname: {}, vat: {}",
                    insertDTO.getFirstname(),
                    insertDTO.getLastname(),
                    insertDTO.getVat());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public StudentReadOnlyDTO updateStudent(StudentUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Student student = StudentMapper.mapToStudent(updateDTO);

            studentDAO.getByVat(updateDTO.getVat()).orElseThrow(() -> new EntityNotFoundException("Student", "Student with vat: "
                    + updateDTO.getVat() + " not found"));
            studentDAO.getById(updateDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Student", "Student with id: "
                    + updateDTO.getId() + " not found"));

            StudentReadOnlyDTO readOnlyDTO = studentDAO.update(student)
                    .map(StudentMapper::mapToStudentReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Student", "Error during update"));

            JPAHelper.commitTransaction();
            LOGGER.info("Student with id {}, firstname {}, lastname{}, vat{} updated",
                    student.getId(),
                    student.getFirstname(),
                    student.getLastname(),
                    student.getVat());

            return readOnlyDTO;
        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Student with id {}, firstname {}, lastname{}, vat{} not updated",
                    updateDTO.getId(),
                    updateDTO.getFirstname(),
                    updateDTO.getLastname(),
                    updateDTO.getVat());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public void deleteStudent(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            studentDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Student", "Student with id: " + id + " not found"));
            studentDAO.delete(id);
            JPAHelper.commitTransaction();
            LOGGER.info("Student with id {}, deleted", id);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Student with id {}, was not deleted", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public StudentReadOnlyDTO getStudentById(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            StudentReadOnlyDTO readOnlyDTO = studentDAO.getById(id)
                    .map(StudentMapper::mapToStudentReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Student", "Student with id: " + id + " not found"));
            JPAHelper.commitTransaction();
            return readOnlyDTO;
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Student with id {}, was not found", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<StudentReadOnlyDTO> getAllStudents() {
        try {
            JPAHelper.beginTransaction();
            List<StudentReadOnlyDTO> readOnlyDTOS = studentDAO.getAll()
                    .stream()
                    .map(StudentMapper::mapToStudentReadOnlyDTO)
                    .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<StudentReadOnlyDTO> getStudentByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<StudentReadOnlyDTO> readOnlyDTOS = studentDAO.getByCriteria(criteria)
                    .stream()
                    .map(StudentMapper::mapToStudentReadOnlyDTO)
                    .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
