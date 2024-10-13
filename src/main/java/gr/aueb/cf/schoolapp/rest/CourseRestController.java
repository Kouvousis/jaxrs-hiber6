package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.course.CourseFiltersDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.course.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.CourseMapper;
import gr.aueb.cf.schoolapp.service.ICourseService;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("/courses")
public class CourseRestController {

    private final ICourseService courseService;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourse(CourseInsertDTO insertDTO, @Context UriInfo uriInfo)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Course", String.join(", ", errors));
        }
        CourseReadOnlyDTO readOnlyDTO = courseService.insertCourse(insertDTO);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(readOnlyDTO.getId().toString()).build())
                .entity(readOnlyDTO)
                .build();
    }

    @PUT
    @Path("/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("courseId") Long courseId, CourseUpdateDTO updateDTO,
                                 @Context SecurityContext securityContext)
            throws EntityNotFoundException, EntityInvalidArgumentException  {
        updateDTO.setId(courseId);
        if (!securityContext.isUserInRole("TEACHER")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<String> errors = ValidatorUtil.validateDTO(updateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Course", String.join(", ", errors));
        }

        CourseReadOnlyDTO readOnlyDTO = courseService.updateCourse(updateDTO);
        return Response.status(Response.Status.OK).entity(readOnlyDTO).build();
    }

    @DELETE
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCourse(@PathParam("courseId") Long courseId, @Context SecurityContext securityContext)
            throws EntityNotFoundException  {
        if (!securityContext.isUserInRole("TEACHER")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        CourseReadOnlyDTO readOnlyDTO = courseService.getCourseById(courseId);
        courseService.deleteCourse(courseId);
        return Response.status(Response.Status.OK).entity(readOnlyDTO).build();
    }

    @GET
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourse(@PathParam("courseId") Long id)
            throws EntityNotFoundException {
        CourseReadOnlyDTO readOnlyDTO = courseService.getCourseById(id);
        return Response.status(Response.Status.OK).entity(readOnlyDTO).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseByName(@QueryParam("courseName") String courseName) {
        CourseFiltersDTO filtersDTO = new CourseFiltersDTO(courseName);
        Map<String, Object> criteria;
        criteria = CourseMapper.mapToCriteriaCourse(filtersDTO);
        List<CourseReadOnlyDTO> readOnlyDTOS = courseService.getCoursesByCriteria(criteria);
        return Response.status(Response.Status.OK).entity(readOnlyDTOS).build();
    }
}
