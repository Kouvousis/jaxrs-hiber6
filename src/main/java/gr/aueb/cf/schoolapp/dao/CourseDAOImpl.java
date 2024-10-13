package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Course;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

@ApplicationScoped
public class CourseDAOImpl extends AbstractDAO<Course> implements ICourseDAO {
    public CourseDAOImpl() {
        setPersistenceClass(Course.class);
    }

    @Override
    public Optional<Course> getByName(String courseName) {
        EntityManager em = getEntityManager();
        String sql = "select c from Course c where c.courseName = :courseName";

        try {
            Course course = em.createQuery(sql, Course.class)
                    .setParameter("courseName", courseName)
                    .getSingleResult();
            return Optional.of(course);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
