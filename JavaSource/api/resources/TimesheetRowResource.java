package api.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.DBsheets;
import annotations.Secured;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

public class TimesheetRowResource {

    @Inject @DBsheets private TimesheetCollection db;

    private Employee currentEmp;
    
    private Timesheet timesheet;
    
    public TimesheetRowResource() { }

    public TimesheetRowResource(Employee emp, Timesheet sheet, TimesheetCollection db) {
        this.currentEmp = emp; 
        this.timesheet = sheet;
        this.db = db;
    }

    @GET
    @Produces("application/xml")
    public List<TimesheetRow> getRows() {
        return timesheet.getDetails();
    }


    @GET
    @Path("{idRow}")
    @Produces("application/xml")
    public TimesheetRow getRow(@PathParam("idRow") int idRow) {
        List<TimesheetRow> rows = timesheet.getDetails();
        for (TimesheetRow row : rows) {
            if (row.getId() == idRow) {
                return row;
            }
        }
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public Response updateRow(@PathParam("id") int id, TimesheetRow row) {
        row.setId(id);
        if (db.getTimesheetRow(id) == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity("timesheet row does not exist")
                           .build();
        }
        row.setTimesheetId(timesheet.getId());

        db.updateRow(row);
        return Response.ok().build();
    }

    @POST
    @Consumes("application/xml")
    public Response createRow(TimesheetRow row) {

        row.setTimesheetId(timesheet.getId());
        db.insertRow(row);
        return Response.created(
                URI
                .create("/timesheets/" + timesheet.getId()
                        + "/rows/" + currentEmp.getId()))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteRow(@PathParam("id") int id) {

        TimesheetRow row = db.getTimesheetRow(id);
        if (row == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity("No row with that id")
                           .build();
        }

        if (row.getTimesheetId() != timesheet.getId()) {

            return Response.status(Response.Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XHTML_XML)
                           .entity("You do not have access "
                                   + "to that timesheet row")
                           .build();
        }

        db.deleteRow(row);
        return Response.ok().build();
    }

}

