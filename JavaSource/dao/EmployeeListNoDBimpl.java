package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;

import annotations.NoDBempl;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import db.DatabaseUtils;

/**
 * Implementation of ca.bcit.infosys.employee.EmployeeList acting as a data
 * access object for a simulated database. The "database" is simply the instance
 * variables.
 */
@NoDBempl
@ApplicationScoped
public class EmployeeListNoDBimpl implements EmployeeList, Serializable {

    /** A map of all the valid login combos. */
    private Map<String, String> loginCombos;
    /** A list of employees. */
    private List<Employee> employees;

    /** The administrator of the application. */
    private Employee administrator;

    /**
     * Constructor. Initializes dummy values for an admin.
     */
    public EmployeeListNoDBimpl() {
        loginCombos = new HashMap<String, String>();
        employees = new ArrayList<Employee>();

        /* initialize test values for administrator */
        administrator = new Employee("logan", 1111, "admin");
        loginCombos.put("admin", "aaaaa");
        employees.add(administrator);
    }

    /**
     * Sets the login combos.
     */
    public void setLoginCombos(Credentials cred) {
        loginCombos.put(cred.getUserName(), cred.getPassword());
    }

    /**
     * Gets a list of employees.
     * @return a list of employees.
     */
    public List<Employee> getEmployees() {

        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM employee";

        Statement stmt = DatabaseUtils.makeStatement(con);
        ResultSet result = DatabaseUtils.execute(stmt, sql);
        List<Employee> empList = this.getEmployeeListFromResult(result);
        DatabaseUtils.close(con);

        return empList;
    }

    /**
     * Gets a list of employees from a result set.
     * @param result the result set.
     * @return the list of employees.
     */
    private List<Employee> getEmployeeListFromResult(final ResultSet result) {
        List<Employee> employeeList = new ArrayList<Employee>();

        try {
            while (result.next()) {
                Employee tempEmp = new Employee(result.getInt("id"),
                                                result.getInt("empNum"),
                                                result.getString("name"),
                                                result.getString("username"),
                                                result.getString("password"));
                employeeList.add(tempEmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }


    /**
     * Returns the valid login combos.
     * @return the map of the login combos.
     */
    public Map<String, String> getLoginCombos() {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT username, password FROM employee";

        Statement stmt = DatabaseUtils.makeStatement(con);
        ResultSet result = DatabaseUtils.execute(stmt, sql);
        Map<String, String> combos = getMapOfLoginsFromResultSet(result);

        DatabaseUtils.close(con);

        return combos;
    }

    /**
     * A map of valid login combos.
     * @param result the result set.
     * @return the map.
     */
    private Map<String, String>
    getMapOfLoginsFromResultSet(final ResultSet result) {

        Map<String, String> combos = new HashMap<String, String>();

        try {
            while (result.next()) {
                combos.put(result.getString("username"),
                           result.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return combos;
    }

    /**
     * Returns the administrator.
     * @return the administrator.
     */
    public Employee getAdministrator() {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "SELECT * FROM employee";
        sql += " WHERE isAdmin = true";

        Statement stmt = DatabaseUtils.makeStatement(con);
        ResultSet result = DatabaseUtils.execute(stmt, sql);
        Employee emp = getSingleEmployeeFromResultSet(result);
        DatabaseUtils.close(con);

        return emp;
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
     * Verify a credential.
     * @param credential the credential to be verified.
     * @return boolean whether user is validated.
     */
    public boolean verifyUser(final Credentials credential) {
        String password = getLoginCombos().get(credential.getUserName());

        if (password == null) {
            return false;
        }

        return password.equals(credential.getPassword());
    }

    /**
     * Deletes an employee.
     * @param userToDelete the user to delete.
     */
    public void deleteEmpoyee(final Employee userToDelete) {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "DELETE FROM employee";
        sql += " WHERE id = ?";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, userToDelete.getId());
        DatabaseUtils.executeUpdate(stmt);
        DatabaseUtils.close(con);

    }

    /**
     * @param newEmployee the employee to add.
     */
    public void addEmployee(final Employee newEmployee) {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "INSERT INTO employee";
        sql += " (empNum, name, username, password)";
        sql += " values(?, ?, ?, ?)";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, newEmployee.getEmpNumber());
        DatabaseUtils.setString(stmt, 2, newEmployee.getName());
        DatabaseUtils.setString(stmt, 3, newEmployee.getUserName());
        DatabaseUtils.setString(stmt, 4, newEmployee.getPassword());
        DatabaseUtils.executeUpdate(stmt);
        DatabaseUtils.close(con);

//        employees.add(newEmployee);
    }

    @Override
    public void editEmpoyee(final Employee emp) {
        Connection con = DatabaseUtils.
                createConnection("com.mysql.jdbc.Driver",
                                 "jdbc:mysql://localhost/timesheet",
                                 "timesheet_user", "Secret123?");

        String sql = "";
        sql += "UPDATE employee";
        sql += " SET name = ?,";
        sql += " username = ?,";
        sql += " password = ?,";
        sql += " empNum = ?";
        sql += " WHERE id = ?";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setString(stmt, 1, emp.getName());
        DatabaseUtils.setString(stmt, 2, emp.getUserName());
        DatabaseUtils.setString(stmt, 3, emp.getPassword());
        DatabaseUtils.setInt(stmt, 4, emp.getEmpNumber());
        DatabaseUtils.setInt(stmt, 5, emp.getId());
        DatabaseUtils.executeUpdate(stmt);
        DatabaseUtils.close(con);
    }


}
