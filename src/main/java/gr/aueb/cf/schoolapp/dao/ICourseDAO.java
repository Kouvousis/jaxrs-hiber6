package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Course;

import java.util.Optional;

public interface ICourseDAO extends IGenericDAO<Course>{
    Optional<Course> getByName(String courseName);
}
