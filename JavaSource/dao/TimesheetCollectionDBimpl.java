package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;

import annotations.DBsheets;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;
import db.DatabaseUtils;

/**
 * Implementation of ca.bcit.infosys.employee.TimesheetCollection acting as a
 * data access object for a simulated database. The "database is simply the
 * instance variables.
 */
@SessionScoped
@DBsheets
public class TimesheetCollectionDBimpl implements TimesheetCollection, Serializable {
    
    /** Timesheet row index for Saturday. */
    public static final int SAT = 0;
    /** Timesheet row index for Sunday. */
    public static final int SUN = 1;
    /** Timesheet row index for Monday. */
    public static final int MON = 2;
    /** Timesheet row index for Tuesday. */
    public static final int TUE = 3;
    /** Timesheet row index for Wednesday. */
    public static final int WED = 4;
    /** Timesheet row index for Thursday. */
    public static final int THU = 5;
    /** Timesheet row index for Friday. */
    public static final int FRI = 6;


    /* **********************PUBLIC******************************* */

    /** Constructor */
    public TimesheetCollectionDBimpl() { }

    /** @return the timesheets. */
    public List<Timesheet> getTimesheets() {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM timesheet";

        Statement stmt = DatabaseUtils.makeStatement(con);
        ResultSet result = DatabaseUtils.execute(stmt, sql);
        List<Timesheet> timesheetList = this.getTimesheetsFromResultSet(result);
        DatabaseUtils.close(con);

        return timesheetList;
    }



    /**
     * Returns timesheets for a specified employee.
     * @param e the employee.
     * @return the timesheet for the employee.
     * @throws SQLException 
     */
    public List<Timesheet> getTimesheetsForEmployee(final Employee e)  {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM timesheet";
        sql += " WHERE empId = ?";


        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, e.getId());
        ResultSet result = DatabaseUtils.executePreparedStatement(stmt);
        List<Timesheet> timesheetList = this.getTimesheetsFromResultSet(result);
        DatabaseUtils.close(con);

        return timesheetList;
    }

    public Timesheet getTimesheetById(int id) {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM timesheet";
        sql += " WHERE id = ?";
        sql += " LIMIT 1";


        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, id);
        ResultSet result = DatabaseUtils.executePreparedStatement(stmt);
        List<Timesheet> timesheetList = this.getTimesheetsFromResultSet(result);
        return timesheetList.get(0);
        
    }

    /**
     * Adds a timesheet to the DB.
     * @param sheet teh sheet to be added.
     */
    public void addTimesheet(final Timesheet sheet) {
        insertTimesheet(sheet);

        for (TimesheetRow row : sheet.getDetails()) {
            insertTimesheetRow(row);
        }
    }
    
    public void insertRow(final TimesheetRow row) {
        insertTimesheetRow(row);
    }
    
    /**
     * Deltes a row.
     * @param row the row to delete.
     */
    public void deleteRow(final TimesheetRow row) {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");
        String sql = "";
        sql += "DELETE FROM timesheet_row "
            + " WHERE id = ?";
        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        int i = 0;
        DatabaseUtils.setInt(stmt, ++i, row.getId());
        DatabaseUtils.executeUpdate(stmt);
    }

    /**
     * Updates a row.
     * @param row the row to update.
     */
    public void updateRow(final TimesheetRow row) {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "UPDATE timesheet_row "
            + " SET projectId = ?, "
            + " work_package = ?, "
            + " notes = ?, "
            + " hoursMon = ?, "
            + " hoursTues = ?, "
            + " hoursWed = ?, "
            + " hoursThur = ?, "
            + " hoursFri = ?, "
            + " hoursSat = ?, "
            + " hoursSun = ? "
            + " WHERE id = ?";


        BigDecimal[] weekHours = row.getHoursForWeek();

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        int i = 0;

        DatabaseUtils.setInt(stmt, ++i, row.getProjectID());
        DatabaseUtils.setString(stmt, ++i, row.getWorkPackage());
        DatabaseUtils.setString(stmt, ++i, row.getNotes());
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[MON]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[TUE]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[WED]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[THU]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[FRI]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[SAT]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[SUN]);
        DatabaseUtils.setInt(stmt, ++i, row.getId());
        DatabaseUtils.executeUpdate(stmt);
        DatabaseUtils.close(con);
    }

    /* **********************PRIVATE******************************* */

    /**
     * Inserts a timesheet row.
     * @param row the row to insert.
     */
    private void insertTimesheetRow(final TimesheetRow row) {
       Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");



        String sql = "";
        sql += "INSERT INTO timesheet_row"
             + " (timesheetId, projectId, work_package, notes,"
             + " hoursMon, hoursTues, hoursWed, hoursThur, hoursFri,"
             + " hoursSat, hoursSun)";
        sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        BigDecimal[] weekHours = row.getHoursForWeek();

        int i = 0;
        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, ++i, row.getTimesheetId());
        DatabaseUtils.setInt(stmt, ++i, row.getProjectID());
        DatabaseUtils.setString(stmt, ++i, row.getWorkPackage());
        DatabaseUtils.setString(stmt, ++i, row.getNotes());
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[MON]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[TUE]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[WED]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[THU]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[FRI]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[SAT]);
        DatabaseUtils.setBigDecimal(stmt, ++i, weekHours[SUN]);
        DatabaseUtils.executeUpdate(stmt);

    }

    /**
     * Inserts a timesheet into the db.
     * @param sheet the timesheet to insert.
     */
    private void insertTimesheet(final Timesheet sheet) {
       Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "INSERT INTO timesheet";
        sql += " (empId, dateEnd)";
        sql += " VALUES (?, ?)";

        int i = 0;
        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, ++i, sheet.getEmployee().getId());
        DatabaseUtils.setDate(stmt, ++i, new Date(sheet.getEndWeek().getTime()));
        DatabaseUtils.executeUpdate(stmt);
    }

    /**
     * Returns a single employee from a result set.
     * @param result the result set.
     * @return the employee.
     */
    private Employee getSingleEmployeeFromResultSet(final ResultSet result) {
        Employee emp = null;
        try {

            if (result.next()) {
                emp = new Employee(result.getInt("id"),
                                   result.getInt("empNum"),
                                   result.getString("name"),
                                   result.getString("username"),
                                   result.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emp;
    }
    /**
     * Returns an employee based on their id.
     * @param id their id.
     * @return the employee.
     */
    private Employee getEmployee(final int id) {

       Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM employee";
        sql += " WHERE id = ?";
        sql += " LIMIT 1";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, id);
        ResultSet result = DatabaseUtils.executePreparedStatement(stmt);
        Employee emp = getSingleEmployeeFromResultSet(result);
        DatabaseUtils.close(con);
        
        return emp;
    }

    /**
     * Returns a list of timesheet rows from a result set.
     * @param result the result set.
     * @return A list of TimesheetRows.
     */
    private List<TimesheetRow> getRowsFromResultSet(final ResultSet result) {
        List<TimesheetRow> rows = new ArrayList<TimesheetRow>();

        try {
            while (result.next()) {

                BigDecimal[] tempHoursArray = new BigDecimal[7];
                tempHoursArray[SAT] = result.getBigDecimal("hoursSat");
                tempHoursArray[SUN] = result.getBigDecimal("hoursSun");
                tempHoursArray[MON] = result.getBigDecimal("hoursMon");
                tempHoursArray[TUE] = result.getBigDecimal("hoursTues");
                tempHoursArray[WED] = result.getBigDecimal("hoursWed");
                tempHoursArray[THU] = result.getBigDecimal("hoursThur");
                tempHoursArray[FRI] = result.getBigDecimal("hoursFri");

                Integer projId = result.getInt("projectId");
                if (result.wasNull()) {
                    projId = null;
                }

                TimesheetRow tempRow =
                        new TimesheetRow(result.getInt("id"),
                                         result.getInt("timesheetId"),
                                         projId,
                                         result.getString("work_package"),
                                         tempHoursArray,
                                         result.getString("notes"));

                rows.add(tempRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * Returns a list of timesheet rows for a timesheet.
     * @param id the id of the timesheet.
     * @return A list of timesheet rows.
     */
    private List<TimesheetRow> getRowsForTimesheetFromId(final int id) {
       Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet"
                                 + "?autoReconnect=true&useSSL=false",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM timesheet_row";
        sql += " WHERE timesheetId = ?";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, id);
        ResultSet result = DatabaseUtils.executePreparedStatement(stmt);
        List<TimesheetRow> rows = getRowsFromResultSet(result);
        DatabaseUtils.close(con);
        return rows;
    }

    /**
     * Returns a list of timesheets.
     * @param result the result set.
     * @return a list.
     */
    private List<Timesheet> getTimesheetsFromResultSet(final ResultSet result) {
        List<Timesheet> timesheetList = new ArrayList<Timesheet>();

        try {
            while (result.next()) {
                Timesheet tempSheet =
                        new Timesheet(result.getInt("id"),
                              getEmployee(result.getInt("empId")),
                              result.getDate("dateEnd"),
                              getRowsForTimesheetFromId(result.getInt("id")));

                timesheetList.add(tempSheet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timesheetList;
    }
}
