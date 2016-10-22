package singleAction;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.timesheet.TimesheetRow;
import services.TimesheetService;

/**
 * A class representing a request to create a timesheet row.
 */
@Named("createSheetRow")
@RequestScoped
public class CreateTimesheetRow extends TimesheetRow {

    @Inject
    TimesheetService timesheetService;

    /**
     * Constructor.
     */
    public CreateTimesheetRow() { }

    /**
     * Creates a row in the current timesheet.
     * @return the page to navigate to.
     */
    public String createRowAction() {
        timesheetService.addTimesheetRow(this);
        return "timesheet?faces-redirect=true";
    }

}
