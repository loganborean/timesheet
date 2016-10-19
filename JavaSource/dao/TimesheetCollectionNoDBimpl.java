package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import annotations.NoDBsheets;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

@ApplicationScoped
@NoDBsheets
public class TimesheetCollectionNoDBimpl implements TimesheetCollection, Serializable{
	
	private List<Timesheet> timesheets;
	

	public TimesheetCollectionNoDBimpl() {
		timesheets = new ArrayList<Timesheet>();
	}

	@Override
	public List<Timesheet> getTimesheets() {
		return timesheets;
	}

	@Override
	public List<Timesheet> getTimesheetsForEmployee(Employee e) {
		List<Timesheet> sheetsForEmployee = new ArrayList<Timesheet>();

		for(Timesheet sheet : timesheets) {
			if (sheet.getEmployee() == e) {
				sheetsForEmployee.add(sheet);
			}
		}
		return sheetsForEmployee;
	}

	@Override
	public Timesheet getCurrentTimesheet(Employee e) {
		return null;
	}

	@Override
	public void addTimesheet(Timesheet sheet) {
		timesheets.add(sheet);

	}

}
