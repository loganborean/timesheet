package api;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.DBsheets;
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
        return null;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public void updateRow(@PathParam("id") int id, TimesheetRow row) {
        if (db.getTimesheetRow(id) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        db.updateRow(row);
    }

    @POST
    @Consumes("application/xml")
    public void createRow(TimesheetRow row) {

        if (db.getTimesheetRow(row.getId()) != null) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }

        db.insertRow(row);
    }

    @DELETE
    @Path("{id}")
    public void deleteRow(@PathParam("id") int id) {

        TimesheetRow row = db.getTimesheetRow(id);
        if (row == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        db.deleteRow(row);
    }

}

