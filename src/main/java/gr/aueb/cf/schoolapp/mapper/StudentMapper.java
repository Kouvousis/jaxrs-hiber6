package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.core.enums.RoleType;
import gr.aueb.cf.schoolapp.dto.student.StudentFiltersDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.dto.user.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.user.UserReadOnlyDTO;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.security.SecUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentMapper {

    private StudentMapper() {}

    public static Student mapToStudent(StudentInsertDTO dto) {
        return new Student(null, dto.getVat(), dto.getFirstname(), dto.getLastname());
    }

    public static Student mapToStudent(StudentUpdateDTO dto) {
        return new Student(dto.getId(), dto.getVat(), dto.getFirstname(), dto.getLastname());
    }

    public static StudentReadOnlyDTO mapToStudentReadOnlyDTO(Student student) {
        return new StudentReadOnlyDTO(student.getId(), student.getFirstname(), student.getLastname(), student.getVat());
    }

    public static List<StudentReadOnlyDTO> mapToStudentReadOnlyDTOs(List<Student> students) {
        return  students.stream().map(StudentMapper::mapToStudentReadOnlyDTO).collect(Collectors.toList());
    }

    public static Map<String, Object> mapToCriteriaStudent(StudentFiltersDTO filtersDTO) {
        Map<String, Object> filters = new HashMap<>();
        if (!(filtersDTO.getFirstname() == null) && !(filtersDTO.getFirstname().isEmpty())) {
            filters.put("firstname", filtersDTO.getFirstname());
        }

        if (!(filtersDTO.getLastname() == null) && !(filtersDTO.getLastname().isEmpty())) {
            filters.put("lastname", filtersDTO.getLastname());
        }

        if (!(filtersDTO.getVat() == null) && !(filtersDTO.getVat().isEmpty())) {
            filters.put("vat", filtersDTO.getVat());
        }
        return filters;
    }

    public static User mapToUser(UserInsertDTO dto) {
        return new User(null, dto.getUsername(), SecUtil.hashPassword(dto.getPassword()),
                RoleType.valueOf(dto.getRole()));
    }

    public static UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRoleType().name());
    }
}
