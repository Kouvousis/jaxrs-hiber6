package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentUpdateDTO;

import java.util.List;
import java.util.Map;

public interface IStudentService {
    StudentReadOnlyDTO insertStudent(StudentInsertDTO insertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException;
    StudentReadOnlyDTO updateStudent(StudentUpdateDTO updateDTO) throws EntityNotFoundException, EntityInvalidArgumentException;
    void deleteStudent(Object id) throws EntityNotFoundException;
    StudentReadOnlyDTO getStudentById(Object id) throws EntityNotFoundException;
    List<StudentReadOnlyDTO> getAllStudents();
    List<StudentReadOnlyDTO> getStudentByCriteria(Map<String, Object> criteria);
}
