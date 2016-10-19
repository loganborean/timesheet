package services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDBsheets;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;
import dao.TimesheetCollectionNoDBimpl;

@Named("timesheet")
@SessionScoped
public class TimesheetService implements Serializable {

	@Inject @NoDBsheets TimesheetCollectionNoDBimpl sheetCollection;
	@Inject UserService user;

	private Timesheet currentSheet;
	
	private List<TimesheetRow> editable;
	
	public TimesheetService() {
		editable = new ArrayList<TimesheetRow>();
	}

	public boolean isEditing(TimesheetRow row) {
		//false if the emp is not in the editable list
		return editable.indexOf(row) != -1;
	}

	public String editAction(TimesheetRow row) {
		editable.add(row);
		return "timesheet";
	}
	
	public void clearEditable() {
		editable.clear();
	}

	public String deleteAction(TimesheetRow row) {
		currentSheet.deleteRow(row);
		return "timesheet";
	}
	
	public String saveAction() {
		this.clearEditable();
		return "timesheet";
	}

	public String createTimesheetAction() {
		Timesheet newSheet = new Timesheet();
		newSheet.addRow();
		newSheet.addRow();
		newSheet.addRow();
		newSheet.addRow();
		newSheet.addRow();
		sheetCollection.addTimesheet(newSheet);
		currentSheet = newSheet;
		
		return "timesheet?faces-redirect=true";
	}

	@PostConstruct
	private void init() {
		List<TimesheetRow> rowss = new ArrayList<TimesheetRow>();

		BigDecimal[] d = {new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("1.0")
						, new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("1.0")};

		rowss.add(new TimesheetRow(1,"wp1", d, "comments1"));
		rowss.add(new TimesheetRow(2,"wp3", d, "comments2"));
		rowss.add(new TimesheetRow(3,"wp4", d, "comments3"));

		Timesheet sheet = new Timesheet(user.getCurrentEmployee()
				, new Date(Calendar.FRIDAY), rowss); 
		sheetCollection.addTimesheet(sheet);

		List<Timesheet> sheets = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());
		currentSheet = sheets.get(0);
	}
	
	public Timesheet getCurrentSheet() {
		return currentSheet;
	}
	
	public void setCurrentTimesheet(Timesheet sheet) {
		this.currentSheet = sheet;
	}
	
	public void addTimesheetRow(TimesheetRow row) {
		currentSheet.addSheetRow(row);
	}
	

	
}
