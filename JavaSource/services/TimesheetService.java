package services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	
	public UserService getUser() {
		return user;
	}

	public String nextSheetAction() {
		List<Timesheet> sheetsForUser = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());
		List<Timesheet> sheetsAfterCurrent = new ArrayList<Timesheet>();
		
		Date currentSheetWeek = currentSheet.getEndWeek();
				
		for(Timesheet sheet : sheetsForUser) {
			//find the last week
			if (sheet.getEndWeek().after(currentSheetWeek)) {
				sheetsAfterCurrent.add(sheet);
			}
		}

		if (sheetsAfterCurrent.size() <= 0)
			return null;

		Date smallestWeek = sheetsAfterCurrent.get(0).getEndWeek();
		Timesheet nextSheet = sheetsAfterCurrent.get(0);

		for(Timesheet sheet : sheetsAfterCurrent) {
			//find the last week
			if (sheet.getEndWeek().before(smallestWeek)) {
				smallestWeek = sheet.getEndWeek();
				nextSheet = sheet;
			}
		}
		currentSheet = nextSheet;

		return "timesheet?faces-redirect=true";
	}
	
	public boolean hasNextSheet() {
		List<Timesheet> sheetsForUser = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());
		Date currentSheetWeek = currentSheet.getEndWeek();
				
		for(Timesheet sheet : sheetsForUser) {
			//find the last week
			if (sheet.getEndWeek().after(currentSheetWeek)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean hasPreviousSheet() {
		List<Timesheet> sheetsForUser = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());
		Date currentSheetWeek = currentSheet.getEndWeek();
				
		for(Timesheet sheet : sheetsForUser) {
			//find the last week
			if (sheet.getEndWeek().before(currentSheetWeek)) {
				return true;
			}
		}
		return false;
		
	}

	public String previousSheetAction() {
		List<Timesheet> sheetsForUser = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());
		List<Timesheet> sheetsBeforeCurrent = new ArrayList<Timesheet>();
		
		Date currentSheetWeek = currentSheet.getEndWeek();
				
		for(Timesheet sheet : sheetsForUser) {
			//find the last week
			if (sheet.getEndWeek().before(currentSheetWeek)) {
				sheetsBeforeCurrent.add(sheet);
			}
		}

		if (sheetsBeforeCurrent.size() <= 0)
			return null;

		Date largestWeek = sheetsBeforeCurrent.get(0).getEndWeek();
		Timesheet nextSheet = sheetsBeforeCurrent.get(0);

		for(Timesheet sheet : sheetsBeforeCurrent) {
			//find the last week
			if (sheet.getEndWeek().after(largestWeek)) {
				largestWeek = sheet.getEndWeek();
				nextSheet = sheet;
			}
		}
		currentSheet = nextSheet;

		return "timesheet?faces-redirect=true";
	}
	
	public TimesheetService() {
		editable = new ArrayList<TimesheetRow>();
	}

	public boolean isEditing(TimesheetRow row) {
		//false if the emp is not in the editable list
		return editable.indexOf(row) != -1;
	}
	
	public boolean canEdit() {
		//only if its the current week
		return (currentSheet.getWeekNumber() == getCurrentWeekNum());
	}

	public int getCurrentWeekNum() {
        Calendar c = new GregorianCalendar();
        Date now = new Date();
        c.setTime(now);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
		
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
		List<TimesheetRow> rows = new ArrayList<TimesheetRow>();

		// need 5 empty rows
		for(int i = 0; i < 5; i++) {
			rows.add(new TimesheetRow());
		}
		
		List<Timesheet> sheetsForUser = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());

		Date latestWeek = sheetsForUser.get(0).getEndWeek();
				
		for(Timesheet sheet : sheetsForUser) {
			//find the last week
			if (sheet.getEndWeek().after(latestWeek)) {
				latestWeek = sheet.getEndWeek();
			}
		}

		Timesheet newSheet = new Timesheet(user.getCurrentEmployee(), 
				getOneWeekAfter(latestWeek), rows);
		sheetCollection.addTimesheet(newSheet);
		currentSheet = newSheet;
		
		return "timesheet?faces-redirect=true";
	}

	@PostConstruct
	private void init() {
		List<TimesheetRow> rowss = new ArrayList<TimesheetRow>();

		BigDecimal[] t1 = {new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("1.0")
						, new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("1.0")};
		BigDecimal[] t2 = {new BigDecimal("2.0"), new BigDecimal("2.0"), new BigDecimal("2.0"), new BigDecimal("2.0")
						, new BigDecimal("2.0"), new BigDecimal("2.0"), new BigDecimal("2.0")};
		BigDecimal[] t3 = {new BigDecimal("3.0"), new BigDecimal("3.0"), new BigDecimal("3.0"), new BigDecimal("3.0")
						, new BigDecimal("3.0"), new BigDecimal("3.0"), new BigDecimal("3.0")};

		rowss.add(new TimesheetRow(1,"wp1", t1, "comments1"));
		rowss.add(new TimesheetRow(2,"wp2", t2, "comments2"));
		rowss.add(new TimesheetRow(3,"wp3", t3, "comments3"));

		Timesheet sheet = new Timesheet(user.getCurrentEmployee(), 
									    getLastDayOfWeekDate(), rowss); 
		sheetCollection.addTimesheet(sheet);

		List<Timesheet> sheets = sheetCollection.getTimesheetsForEmployee(user.getCurrentEmployee());
		currentSheet = sheets.get(0);
	}
	
	private Date getOneWeekAfter(Date week) {
        Date newDate = new Date(week.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, 7);
        newDate.setTime(calendar.getTime().getTime());

        return newDate;
		
	}

    public static Date getLastDayOfWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,
                cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        Date now = new Date();
        cal.setTime(now);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        return new Date(now.getTime() - 24 * 60 * 60 * 1000 * (week - 6));
    }
    
    public static int getWeekNumber() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,
                cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        Date now = new Date();
        cal.setTime(now);
        return (cal.get(Calendar.DAY_OF_WEEK) -1);
    	
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
