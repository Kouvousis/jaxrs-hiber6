package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Student;

import java.util.Optional;

public interface IStudentDAO extends IGenericDAO<Student> {
    Optional<Student> getByVat(String vat);
}
