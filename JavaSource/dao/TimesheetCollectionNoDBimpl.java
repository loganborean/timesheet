package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import annotations.NoDBsheets;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

/**
 * Implementation of ca.bcit.infosys.employee.TimesheetCollection acting as a
 * data access object for a simulated database. The "database is simply the
 * instance variables.
 */
@ApplicationScoped
@NoDBsheets
public class TimesheetCollectionNoDBimpl implements TimesheetCollection, Serializable {

    /** The list of timesheets. */
    private List<Timesheet> timesheets;

    /** Constructor */
    public TimesheetCollectionNoDBimpl() {
        timesheets = new ArrayList<Timesheet>();
    }

    /** @return the timesheets. */
    public List<Timesheet> getTimesheets() {
        return timesheets;
    }

    /**
     * Returns timesheets for a specified employee.
     * @param e the employee.
     * @return the timesheet for the employee.
     */
    public List<Timesheet> getTimesheetsForEmployee(Employee e) {
        List<Timesheet> sheetsForEmployee = new ArrayList<Timesheet>();

        for (Timesheet sheet : timesheets) {
            if (sheet.getEmployee() == e) {
                sheetsForEmployee.add(sheet);
            }
        }
        return sheetsForEmployee;
    }


    /**
     * Adds a timesheet to the DB.
     * @param sheet teh sheet to be added.
     */
    public void addTimesheet(Timesheet sheet) {
        timesheets.add(sheet);

    }

}
