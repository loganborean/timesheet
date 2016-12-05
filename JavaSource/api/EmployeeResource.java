package api;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.DBempl;
import annotations.DBsheets;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import dao.EmployeeListDBimpl;
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

    /** Validator for employees. */
    @Inject private EmployeeRestValidator employeeValidator;

    /* **********************PUBLIC*********************** */

    /** Ctor. */
    public EmployeeResource() { }

    /**
     * Creates an employee.
     * @param info the query parameters.
     * @param emp the employee.
     * @return the response.
     * @throws Exception 
     */
    @POST
    @Consumes("application/xml")
    public Response createEmployee(@Context final UriInfo info,
                                            final Employee emp)
                                                    throws Exception {
        this.validateCredentials(info);
        employeeValidator.validate(emp);
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
    @Path("{id}")
    @Produces("application/xml")
    public Employee getEmployee(@PathParam("id") final int id,
                                @Context final UriInfo info) {
        this.validateCredentials(info);
        Employee supplier = db.getEmployeeById(id);
        return supplier;
    }

    /**
     * Returns a list of employees from a GET request.
     * @param info the query parameters.
     * @return the list of employees.
     */
    @GET
    @Produces("application/xml")
    public List<Employee> getEmployees(@Context final UriInfo info) {
        this.validateCredentials(info);
        List<Employee> emps = db.getEmployees();
        return emps;
    }

    /**
     * Edits an employee.
     * @param id the id of the employee to edit.
     * @param emp the employee info to modify with.
     */
    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public void modify(@Context final UriInfo info,
                       @PathParam("id") final Integer id,
                                        final Employee emp) {
        this.validateCredentials(info);
        this.checkEmployeeDoesntExist(id);
        db.editEmpoyee(emp);
    }

    /**
     * Deletes an employee.
     * @param id the id of the employee to delete.
     */
    @DELETE
    @Path("{id}")
    public void deleteEmployee(@PathParam("id") final int id) {
        Employee emp = db.getEmployeeById(id);
        if (emp == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        db.deleteEmpoyee(emp);
    }

    // delete this
//    @Path("{id}/timesheets")
//    @Produces("application/xml")
//    public TimesheetResource getTimesheets(@PathParam("id") final int id) {
//        return new TimesheetResource(db.getEmployeeById(id), dbsheet);
//    }


    /* **********************PRIVATE*********************** */

    /**
     * Returns an employee by finding them by their username.
     * @param username the username to find by.
     * @return the employee.
     */
    private Employee findEmployeeByUsername(final String username) {
        for (Employee emp : db.getEmployees()) {
            if (emp.getUserName().equals(username)) {
                return emp;
            }
        }
        throw new WebApplicationException(Response.Status.FORBIDDEN);
    }

    /**
     * Checks if credentials are valid from query params.
     * @param info the query parameters.
     * @return whether the users credendials is valid.
     */
    private boolean isValidCredentials(final UriInfo info) {
        String username = info.getQueryParameters().getFirst("username");
        String password = info.getQueryParameters().getFirst("password");
        return db.verifyUser(new Credentials(username, password));
    }

    /**
     * Returns whether the guy is admin.
     * @param id the id to check.
     * @return whether the id is the admins.
     */
    private boolean isAdmin(final int id) {
        return db.getAdministrator().getId() == id;
    }

    /**
     * Checks for a conflicting employee.
     * @param id the id to check
     */
    private void checkConflict(final int id) {
        Employee tempEmp = db.getEmployeeById(id);
        if (tempEmp != null) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
    }

    /**
     * Checks if an employee exists.
     * @param id the id of the employee.
     */
    private void checkEmployeeDoesntExist(final int id) {
        Employee emp = db.getEmployeeById(id);
        if (emp == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    /**
     * Validates query parameters.
     * @param info the query parameters.
     */
    private void validateCredentials(final UriInfo info) {
        if (!isValidCredentials(info)
                || !isAdmin(findEmployeeByUsername(info.getQueryParameters()
                                                       .getFirst("username"))
                                                       .getId())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
