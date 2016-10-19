package services;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDBsheets;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;
import dao.TimesheetCollectionNoDBimpl;
import sun.util.calendar.LocalGregorianCalendar.Date;

@Named("createSheetRow")
@RequestScoped
public class CreateTimesheetRow extends TimesheetRow {
	
	@Inject @NoDBsheets TimesheetCollectionNoDBimpl sheetCollection;
	
	@Inject TimesheetService timesheetService;
	
	public CreateTimesheetRow() { }
	
	public String createRowAction() {
//		TimesheetRow row = new TimesheetRow(getProjectID(), getWorkPackage(), getHoursForWeek(), getNotes());
		timesheetService.addTimesheetRow(this);
		
		return "timesheet?faces-redirect=true";
		
	}
	
}
