package api.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.DBempl;
import annotations.DBsheets;
import annotations.DBtoken;
import annotations.Secured;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;
import db.TokenList;
import domain.Token;
import services.TimesheetService;

@Path("/timesheets")
@Secured
public class TimesheetResource {

    @Inject @DBsheets private TimesheetCollection db;
    @Inject @DBempl private EmployeeList dbEmp;
    @Inject @DBtoken private TokenList dbToken;

    @Inject TimesheetService sheetService;

    public TimesheetResource() { }

    @GET
    @Secured
    @Produces("application/xml")
    public List<Timesheet> getTimesheets(@Context final UriInfo info) {
        Token token = dbToken.getTokenFromStr(
                info.getQueryParameters().getFirst("t"));

       List<Timesheet> sheets = db.getTimesheetsForEmployee(token.getEmp());
       if (sheets == null) {
           throw new WebApplicationException(Response.Status.BAD_REQUEST);
       }
       return sheets;
    }

    @GET
    @Secured
    @Path("{id}")
    @Produces("application/xml")
    public Timesheet getTimesheet(@PathParam("id") final int id,
                                  @Context final UriInfo info) {
        Timesheet sheet = db.getTimesheetById(id);

        Token token = dbToken.getTokenFromStr(
                info.getQueryParameters().getFirst("t"));

        if (sheet == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        // if sheet belongs to the employee that requested it.
        if (token.getEmp().getId() == sheet.getEmployee().getId()) {

            return sheet;
        }

        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Secured
    @Path("{id}/rows")
    @Produces("application/xml")
    public TimesheetRowResource getTimesheets(@Context final UriInfo info,
                                              @PathParam("id") final int id) {
        this.manualFilter(info);
        Timesheet sheet = db.getTimesheetById(id);

        if (sheet == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Token token = dbToken.getTokenFromStr(
                info.getQueryParameters().getFirst("t"));

        Employee emp = token.getEmp();

        if (emp.getId() == sheet.getEmployee().getId()) {
            return new TimesheetRowResource(emp, sheet, db);
        }

        throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
    
    @POST
    @Secured
    public Response newTimesheet(@Context final UriInfo info) {
        
        Token token = dbToken.getTokenFromStr(
                info.getQueryParameters().getFirst("t"));

        Employee emp = token.getEmp();
        
        
        
        List<Timesheet> sheetsForUser = db.getTimesheetsForEmployee(emp);

        Date latestWeek;

        if (sheetsForUser.size() != 0) {
            latestWeek = sheetsForUser.get(0).getEndWeek();
        } else {
            latestWeek = sheetService.getOneWeekBefore( //ok
                    sheetService.getLastDayOfWeekDate()); //ok
        }

        for (Timesheet sheet : sheetsForUser) {
            // find the last week
            if (sheet.getEndWeek().after(latestWeek)) {
                latestWeek = sheet.getEndWeek();
            }
        }
        Date currentLast = sheetService.getLastDayOfWeekDate(); // ok

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(latestWeek);
        cal2.setTime(currentLast);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                          cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR); 
        if (sameDay) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity("Timesheet already exists for the current week")
                           .build();
        }

        Timesheet newSheet =
                new Timesheet(emp, sheetService.getOneWeekAfter(latestWeek),
                              new ArrayList<TimesheetRow>());

        db.addTimesheet(newSheet);

        TimesheetRow tempRow = new TimesheetRow();
        for (int i = 0; i < 5; i++) {
            tempRow = new TimesheetRow();
            tempRow.setTimesheetId(this.getLatestSheet(emp).getId());
            db.insertRow(tempRow);
        }


        return null;
    }


    public void manualFilter(UriInfo info) {
        String token = info.getQueryParameters().getFirst("t");

        if (token == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        try {
            validateToken(token);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    /**
     * Validates a token.
     * @param token the token to validate.
     * @throws Exception ex thrown.
     */
    private void validateToken(final String token) throws Exception {

        if (!dbToken.isValidToken(token)) {
            throw new Exception();
        }
    }

    public Timesheet getLatestSheet(Employee currentEmp) {
        if (currentEmp == null) {
            return null;
        }
        List<Timesheet> sheetsForUser =
                db.getTimesheetsForEmployee(currentEmp);

        if (sheetsForUser.size() == 0)
            return null;

        Timesheet latestSheet = sheetsForUser.get(0);

        for (Timesheet sheet : sheetsForUser) {
            sheet.getEndWeek().after(latestSheet.getEndWeek());
            latestSheet = sheet;
        }

        return latestSheet;

    }
}

