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

    /** DAO for the saved timesheets. */
    @Inject
    @NoDBsheets
    private TimesheetCollectionNoDBimpl sheetCollection;

    /** The bean handling the current user. */
    @Inject
    private UserService user;

    /** The current sheet being displayed. */
    private Timesheet currentSheet;

    /** The row being edited. */
    private TimesheetRow editable;

    /**
     * Returns the user service.
     * @return the user service.
     */
    public UserService getUser() {
        return user;
    }

    /**
     * constructor.
     */
    public TimesheetService() {
        setEditable(null);
    }

    /**
     * This is used to create sample data for each employee it is the timesheet
     * for the PREVIOUS week, allowing you to create a new timesheet when first
     * using the application.
     */
    @PostConstruct
    private void init() {
        List<TimesheetRow> rowss = new ArrayList<TimesheetRow>();
        List<Timesheet> sheets =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());

        if (sheets.size() == 0) {
            BigDecimal[] t1 = {new BigDecimal("1.0"), new BigDecimal("1.0"),
                    new BigDecimal("1.0"), new BigDecimal("1.0"),
                    new BigDecimal("1.0"), new BigDecimal("1.0"),
                    new BigDecimal("1.0") };
            BigDecimal[] t2 = {new BigDecimal("2.0"), new BigDecimal("2.0"),
                    new BigDecimal("2.0"), new BigDecimal("2.0"),
                    new BigDecimal("2.0"), new BigDecimal("2.0"),
                    new BigDecimal("2.0") };
            BigDecimal[] t3 = {new BigDecimal("3.0"), new BigDecimal("3.0"),
                    new BigDecimal("3.0"), new BigDecimal("3.0"),
                    new BigDecimal("3.0"), new BigDecimal("3.0"),
                    new BigDecimal("3.0") };
            BigDecimal[] t4 = {new BigDecimal("4.0"), new BigDecimal("4.0"),
                    new BigDecimal("4.0"), new BigDecimal("4.0"),
                    new BigDecimal("4.0"), new BigDecimal("4.0"),
                    new BigDecimal("4.0") };
            BigDecimal[] t5 = {new BigDecimal("5.0"), new BigDecimal("5.0"),
                    new BigDecimal("5.0"), new BigDecimal("5.0"),
                    new BigDecimal("5.0"), new BigDecimal("5.0"),
                    new BigDecimal("5.0") };

            rowss.add(new TimesheetRow(1, "wp1", t1, "comments1"));
            rowss.add(new TimesheetRow(2, "wp2", t2, "comments2"));
            rowss.add(new TimesheetRow(3, "wp3", t3, "comments3"));
            rowss.add(new TimesheetRow(4, "wp4", t4, "comments4"));
            rowss.add(new TimesheetRow(5, "wp5", t5, "comments5"));

            Timesheet sheet =
                    new Timesheet(user.getCurrentEmployee(),
                            getOneWeekBefore(getLastDayOfWeekDate()), rowss);
            sheetCollection.addTimesheet(sheet);

            currentSheet =
                    sheetCollection.getTimesheetsForEmployee(
                            user.getCurrentEmployee()).get(0);

        } else {
            // make currentSheet the current week
            currentSheet = getLatestSheet();
        }

    }

    /**
     * Sets the current sheet to the next latest sheet.
     * @return the next page to navigate to.
     */
    public String nextSheetAction() {

        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());

        List<Timesheet> sheetsAfterCurrent = new ArrayList<Timesheet>();

        Date currentSheetWeek = currentSheet.getEndWeek();

        for (Timesheet sheet : sheetsForUser) {
            // find the last week
            if (sheet.getEndWeek().after(currentSheetWeek)) {
                sheetsAfterCurrent.add(sheet);
            }
        }

        if (sheetsAfterCurrent.size() <= 0) {
            return null;
        }

        Date smallestWeek = sheetsAfterCurrent.get(0).getEndWeek();
        Timesheet nextSheet = sheetsAfterCurrent.get(0);

        for (Timesheet sheet : sheetsAfterCurrent) {
            // find the last week
            if (sheet.getEndWeek().before(smallestWeek)) {
                smallestWeek = sheet.getEndWeek();
                nextSheet = sheet;
            }
        }
        currentSheet = nextSheet;

        return "timesheet?faces-redirect=true";
    }
    
    public String createRowAction() {
        currentSheet.addRow();
        return "timesheet?faces-redirect=true";
    }

    /**
     * Returns whether there is another sheet at a later date than the current.
     * @return whether the sheet exitsts.
     */
    public boolean hasNextSheet() {
        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());

        Date currentSheetWeek = currentSheet.getEndWeek();

        for (Timesheet sheet : sheetsForUser) {
            // find the last week
            if (sheet.getEndWeek().after(currentSheetWeek)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether there is another sheet at an earlier
     * date than the current.
     * @return whether the sheet exists.
     */
    public boolean hasPreviousSheet() {
        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());
        Date currentSheetWeek = currentSheet.getEndWeek();

        for (Timesheet sheet : sheetsForUser) {
            // find the last week
            if (sheet.getEndWeek().before(currentSheetWeek)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether a timesheet exists for the current week.
     * @return whether the timesheet exists.
     */
    public boolean hasCurrentTimesheet() {
        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());

        for (Timesheet sheet : sheetsForUser) {
            if (sheet.getWeekNumber() == getCurrentWeekNum()) {
                return true;
            }
        }
        return false;

    }

    /**
     * Changes the current sheet to the previous one.
     * @return the page the navigate to.
     */
    public String previousSheetAction() {
        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());
        List<Timesheet> sheetsBeforeCurrent = new ArrayList<Timesheet>();

        Date currentSheetWeek = currentSheet.getEndWeek();

        for (Timesheet sheet : sheetsForUser) {
            // find the last week
            if (sheet.getEndWeek().before(currentSheetWeek)) {
                sheetsBeforeCurrent.add(sheet);
            }
        }

        if (sheetsBeforeCurrent.size() <= 0) {
            return null;
        }

        Date largestWeek = sheetsBeforeCurrent.get(0).getEndWeek();
        Timesheet nextSheet = sheetsBeforeCurrent.get(0);

        for (Timesheet sheet : sheetsBeforeCurrent) {
            // find the last week
            if (sheet.getEndWeek().after(largestWeek)) {
                largestWeek = sheet.getEndWeek();
                nextSheet = sheet;
            }
        }
        currentSheet = nextSheet;

        return "timesheet?faces-redirect=true";
    }


    /**
     * Returns whether the passed-in row is being edited.
     * @param row the row to check
     * @return whether it is being edited
     */
    public boolean isEditing(TimesheetRow row) {
        // false if the emp is not in the editable list
        return getEditable() == row;
    }

    /**
     * REturns whether something is being edited.
     * @return bool.
     */
    public boolean isEditingMode() {
        // false if the emp is not in the editable list
        return getEditable() != null;
    }

    /**
     * Returns weather editing is possible due to only being
     * able to edit the current weeks.
     * @return weather you can edit.
     */
    public boolean canEdit() {
        // only if its the current week
        return (currentSheet.getWeekNumber() == getCurrentWeekNum());
    }

    /**
     * Returns the current week number.
     * @return the index of the current week.
     */
    public int getCurrentWeekNum() {
        Calendar c = new GregorianCalendar();
        Date now = new Date();
        c.setTime(now);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Edit a row.
     * @param row to be edited.
     * @return the page to navigate to.
     */
    public String editAction(TimesheetRow row) {
        setEditable(row);
        return "timesheet";
    }


    /**
     * Deletes a row.
     * @param row row to delete.
     * @return page to navigate to.
     */
    public String deleteAction(TimesheetRow row) {
        currentSheet.deleteRow(row);
        return "timesheet";
    }

    /**
     * Saves current state.
     * @return page to navigate to.
     */
    public String saveAction() {
        //store editable in db
        
        
        
        setEditable(null);
        return "timesheet";
    }

    /**
     * Creates a timesheet.
     * @return the page to navigate to.
     */
    public String createTimesheetAction() {
        List<TimesheetRow> rows = new ArrayList<TimesheetRow>();

        // need 5 empty rows
        for (int i = 0; i < 5; i++) {
            rows.add(new TimesheetRow());
        }

        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());

        Date latestWeek = sheetsForUser.get(0).getEndWeek();

        for (Timesheet sheet : sheetsForUser) {
            // find the last week
            if (sheet.getEndWeek().after(latestWeek)) {
                latestWeek = sheet.getEndWeek();
            }
        }

        Timesheet newSheet =
                new Timesheet(user.getCurrentEmployee(), 
                              getOneWeekAfter(latestWeek), rows);

        sheetCollection.addTimesheet(newSheet);
        currentSheet = newSheet;

        return "timesheet?faces-redirect=true";
    }


    /**
     * Returns the latest timsheet for the current employee.
     * @return
     */
    private Timesheet getLatestSheet() {
        List<Timesheet> sheetsForUser =
                sheetCollection.getTimesheetsForEmployee(
                                user.getCurrentEmployee());

        Timesheet latestSheet = sheetsForUser.get(0);

        for (Timesheet sheet : sheetsForUser) {
            sheet.getEndWeek().after(latestSheet.getEndWeek());
            latestSheet = sheet;
        }

        return latestSheet;

    }

    /**
     * Return the date one week after teh passed in date.
     * @param week the passed in date.
     * @return the date a week after.
     */
    private Date getOneWeekAfter(Date week) {
        Date newDate = new Date(week.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, 7);
        newDate.setTime(calendar.getTime().getTime());

        return newDate;
    }

    /**
     * Return the date one week before the passed in date.
     * @param week the passed in date.
     * @return the date a week before.
     */
    private Date getOneWeekBefore(Date week) {
        Date newDate = new Date(week.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, -7);
        newDate.setTime(calendar.getTime().getTime());

        return newDate;
    }

    /**
     * Returns the sum of the days hours in a timesheet.
     * @param day_index the index of the day.
     * @return the sum.
     */
    public BigDecimal getTotalForDay(int day_index) {

        BigDecimal total = BigDecimal.ZERO;
        for (TimesheetRow row : currentSheet.getDetails()) {
            BigDecimal hour = row.getHour(day_index);
            if (hour != null) {
                total = total.add(hour);
            }

        }

        return total;
    }

    /**
     * Returns the sum of the days hours in a timesheet
     * except for the specified row.
     * @param day_index the index of the day.
     * @return the sum.
     */
    public BigDecimal getTotalForDayExcept(int day_index,
                                Integer projectID, String wp) {

        BigDecimal total = BigDecimal.ZERO;
        int index = 0;
        for (TimesheetRow row : currentSheet.getDetails()) {

            if (row.getProjectID() == null
                    || row.getWorkPackage() == null
                    || projectID == null
                    || wp == null) {
                continue;
            }

            if (row.getProjectID() != projectID
                && !wp.equals(row.getWorkPackage())) {

                BigDecimal hour = row.getHour(day_index);
                if (hour != null) {
                    total = total.add(hour);
                }
            }
            index++;
        }

        return total;
    }

    /**
     * Returns the friday for the week.
     * @return the friday date.
     */
    public Date getLastDayOfWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,
                cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        Date now = new Date();
        cal.setTime(now);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        return new Date(now.getTime() - 24 * 60 * 60 * 1000 * (week - 6));
    }

    /**
     * Returns the current week number based on todays time.
     * @return the week number.
     */
    public int getWeekNumber() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,
                cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        Date now = new Date();
        cal.setTime(now);
        return (cal.get(Calendar.DAY_OF_WEEK) - 1);

    }

    /**
     * Returns the current timesheet.
     * @return the timesheet.
     */
    public Timesheet getCurrentSheet() {
        return currentSheet;
    }

    /**
     * Sets the current timesheet.
     * @param sheet the sheet.
     */
    public void setCurrentTimesheet(Timesheet sheet) {
        this.currentSheet = sheet;
    }

    /**
     * Adds a row to the current timesheet.
     * @param row teh row to add.
     */
    public void addTimesheetRow(TimesheetRow row) {
        currentSheet.addSheetRow(row);
    }

    public TimesheetRow getEditable() {
        return editable;
    }

    public void setEditable(TimesheetRow editable) {
        this.editable = editable;
    }

}
