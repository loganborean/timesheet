package api;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.DBempl;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

public class ApiAuthenticator {

    /** DAO for employees. */
    @Inject @DBempl private EmployeeList db;

    /**
     * Returns an employee by finding them by their username.
     * @param username the username to find by.
     * @return the employee.
     */
    public Employee findEmployeeByUsername(final String username) {
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
    public void checkEmployeeDoesntExist(final int id) {
        Employee emp = db.getEmployeeById(id);
        if (emp == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    /**
     * Validates query parameters.
     * @param info the query parameters.
     */
    public void validateAdminCredentials(final UriInfo info) {
        if (!isValidCredentials(info)
                || !isAdmin(findEmployeeByUsername(info.getQueryParameters()
                                                       .getFirst("username"))
                                                       .getId())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    /**
     * Validates query parameters.
     * @param info the query parameters.
     */
    public void validateCredentials(final UriInfo info) {
        if (!isValidCredentials(info)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

}
