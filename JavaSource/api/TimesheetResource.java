package api;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

@Path("/timesheets")
public class TimesheetResource {

    @Inject @DBsheets private TimesheetCollection db;
    @Inject @DBempl private EmployeeList dbEmp;

    @Inject private ApiAuthenticator authenticator;

    public TimesheetResource() { }

    @GET
    @Produces("application/xml")
    public List<Timesheet> getTimesheets(@Context final UriInfo info) {
        authenticator.validateCredentials(info);
        Employee emp = dbEmp.findEmployeeByUsername(info.getQueryParameters()
                                                        .getFirst("username"));

       List<Timesheet> sheets = db.getTimesheetsForEmployee(emp);
       return sheets;
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Timesheet getTimesheet(@PathParam("id") final int id,
                                  @Context final UriInfo info) {
        authenticator.validateCredentials(info);
        Timesheet sheet = db.getTimesheetById(id);

        Employee emp = dbEmp.findEmployeeByUsername(info.getQueryParameters()
                                                        .getFirst("username"));
        //if sheet belongs to
        if (emp.getId() == sheet.getEmployee().getId()) {
            return sheet;
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @Path("{id}/rows")
    @Produces("application/xml")
    public TimesheetRowResource getTimesheets(@Context final UriInfo info,
                                              @PathParam("id") final int id) {
        authenticator.validateCredentials(info);

        Timesheet sheet = db.getTimesheetById(id);

        Employee emp = dbEmp.findEmployeeByUsername(info.getQueryParameters()
                                                        .getFirst("username"));

        if (emp.getId() == sheet.getEmployee().getId()) {
            return new TimesheetRowResource(emp, sheet, db);
        }

        throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
}

