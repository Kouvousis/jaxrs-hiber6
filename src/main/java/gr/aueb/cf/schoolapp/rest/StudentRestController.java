package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.student.StudentFiltersDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.student.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.StudentMapper;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("/students")
public class StudentRestController {

    private final IStudentService studentService;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentInsertDTO insertDTO, @Context UriInfo uriInfo)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Student", String.join(", ", errors));
        }
        StudentReadOnlyDTO readOnlyDTO = studentService.insertStudent(insertDTO);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(readOnlyDTO.getId().toString()).build())
                .entity(readOnlyDTO)
                .build();
    }

    @PUT
    @Path("/{studentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("studentId") Long studentId, StudentUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException {
        updateDTO.setId(studentId);
        List<String> errors = ValidatorUtil.validateDTO(updateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Student", String.join(", ", errors));
        }

        StudentReadOnlyDTO readOnlyDTO = studentService.updateStudent(updateDTO);
        return Response.status(Response.Status.OK).entity(readOnlyDTO).build();
    }

    @DELETE
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("studentId") Long studentId)
            throws EntityNotFoundException {
        StudentReadOnlyDTO dto = studentService.getStudentById(studentId);
        studentService.deleteStudent(studentId);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @GET
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("studentId") Long id)
            throws EntityNotFoundException {
        StudentReadOnlyDTO dto = studentService.getStudentById(id);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFiltered(@QueryParam("firstname") String firstname,
                                @QueryParam("lastname") String lastname,
                                @QueryParam("vat") String vat) {
        StudentFiltersDTO filtersDTO = new StudentFiltersDTO(firstname, lastname, vat);
        Map<String, Object> criteria;
        criteria = StudentMapper.mapToCriteriaStudent(filtersDTO);
        List<StudentReadOnlyDTO> readOnlyDTOS = studentService.getStudentByCriteria(criteria);
        return Response.status(Response.Status.OK).entity(readOnlyDTOS).build();
    }
}
