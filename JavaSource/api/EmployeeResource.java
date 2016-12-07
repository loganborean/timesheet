package api;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.AdminSecured;
import annotations.DBempl;
import annotations.DBsheets;
import annotations.DBtoken;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import dao.EmployeeListDBimpl;
import db.TokenList;
import services.TimesheetService;
import validators.EmployeeRestValidator;


/**
 * Employee resource.
 */
@Path("/employees")
@RequestScoped
public class EmployeeResource {

    /** DAO for employees. */
    @Inject @DBempl private EmployeeList db;

    /** DAO for Timesheets. */
    @Inject @DBsheets private TimesheetCollection dbsheet;

    /** DAO for Timesheets. */
    @Inject @DBtoken private TokenList dbToken;

    /** Validator for employees. */
    @Inject private EmployeeRestValidator employeeValidator;


    /** Ctor. */
    public EmployeeResource() { }

    /**
     * Creates an employee.
     * @param info the query parameters.
     * @param emp the employee.
     * @return the response.
     * @throws Exception the ex.
     */
    @POST
    @AdminSecured
    @Consumes("application/xml")
    public Response createEmployee(@Context final UriInfo info,
                                            final Employee emp)
                                                    throws Exception {
//        authenticator.validateAdminCredentials(info);
        try {
            employeeValidator.validate(emp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity(e.getMessage())
                           .build();
        }

        db.addEmployee(emp);
        return Response.created(URI.create("/employees/" + emp.getId()))
                                   .build();
    }

    /**
     * Returns an employee from a GET request.
     * @param id the id of the employee to return.
     * @param info the query parameters.
     * @return the employee.
     */
    @GET
    @AdminSecured
    @Path("{id}")
    @Produces("application/xml")
    public Employee getEmployee(@PathParam("id") final int id,
                                @Context final UriInfo info) {
        Employee emp = db.getEmployeeById(id);
        if (emp == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return emp;
    }

    /**
     * Returns a list of employees from a GET request.
     * @param info the query parameters.
     * @return the list of employees.
     * @throws IOException 
     */
    @GET
    @AdminSecured
    @Produces("application/xml")
    public List<Employee> getEmployees(@Context final UriInfo info) {
        List<Employee> emps = db.getEmployees();
        if (emps == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return emps;
    }

    /**
     * Edits an employee.
     * @param id the id of the employee to edit.
     * @param emp the employee info to modify with.
     */
    @PUT
    @AdminSecured
    @Path("{id}")
    @Consumes("application/xml")
    public Response modify(@Context final UriInfo info,
                       @PathParam("id") final Integer id,
                                        final Employee emp) {
        try {
            employeeValidator.checkEmployeeExists(id);
            employeeValidator.validate(emp, id,
                    db.getEmployeeById(id).getEmpNumber());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity(e.getMessage())
                           .build();
        }
        db.editEmpoyee(emp);
        return Response.ok().build();
    }

    /**
     * Deletes an employee.
     * @param id the id of the employee to delete.
     */
    @DELETE
    @AdminSecured
    @Path("{id}")
    public Response deleteEmployee(@PathParam("id") final int id) {
        Employee emp = db.getEmployeeById(id);
        if (emp == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity("Employee does not exist")
                           .build();
        }
        db.deleteEmpoyee(emp);
        return Response.ok().build();
    }

    // delete this
//    @Path("{id}/timesheets")
//    @Produces("application/xml")
//    public TimesheetResource getTimesheets(@PathParam("id") final int id) {
//        return new TimesheetResource(db.getEmployeeById(id), dbsheet);
//    }


    /* **********************PRIVATE*********************** */

}
