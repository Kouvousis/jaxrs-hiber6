package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Student;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;
@ApplicationScoped
public class StudentDAOImpl extends AbstractDAO<Student> implements IStudentDAO {
    public StudentDAOImpl() {
        setPersistenceClass(Student.class);
    }


    @Override
    public Optional<Student> getByVat(String vat) {
        EntityManager em = getEntityManager();
        String sql = "SELECT s FROM Student s WHERE s.vat = :vat";

        try {
            Student student = em.createQuery(sql, Student.class)
                    .setParameter("vat", vat)
                    .getSingleResult();
            return Optional.of(student);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
