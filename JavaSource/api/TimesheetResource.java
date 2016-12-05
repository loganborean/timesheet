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
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

@Path("/timesheets")
public class TimesheetResource {

//    @Inject @DBsheets private TimesheetCollection db;
//
//    public TimesheetResource() { 
//    }
//
//    @GET
//    @Produces("application/xml")
//    public List<Timesheet> getTimesheets() {
//       List<Timesheet> sheets = db.getTimesheetsForEmployee(currentEmp);
//       return sheets;
//    }
//
//    @GET
//    @Path("{id}")
//    @Produces("application/xml")
//    public Timesheet getTimesheet(@PathParam("id") final int id,
//                                  @Context final UriInfo info) {
//        this.validateCredentials(info);
//       Timesheet sheet= db.getTimesheetById(id);
//       //if sheet belongs to
//       return sheet;
//    }
//
//    @Path("{id}/rows")
//    @Produces("application/xml")
//    public TimesheetRowResource getTimesheets(@PathParam("id") int id) {
//        return new TimesheetRowResource(currentEmp, db.getTimesheetById(id), db);
//    }
//
//    @GET
//    @Path("{id}")
//    @Produces("application/xml")
//    public Employee getEmployee(@PathParam("id") final int id,
//                                @Context final UriInfo info) {
//        this.validateCredentials(info);
//        Employee supplier = db.getEmployeeById(id);
//        return supplier;
//    }

}

