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

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.sql.DataSource;

import annotations.DBempl;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import db.DatabaseUtils;

/**
 * Implementation of ca.bcit.infosys.employee.EmployeeList acting as a data
 * access object for a simulated database. The "database" is simply the instance
 * variables.
 */
@DBempl
@SessionScoped
public class EmployeeListDBimpl implements EmployeeList, Serializable {
    @Resource(mappedName = "java:jboss/datasources/timesheet-jdbc")
    private DataSource ds;

    /**
     * Sets the login combos.
     */
    public void resetPassword(Employee emp, Credentials cred) {
        Connection con = DatabaseUtils.getConnection(ds);
        String sql = "";
        sql += "UPDATE employee";
        sql += " SET password = ?";
        sql += " WHERE id = ?";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        int i = 0;
        DatabaseUtils.setString(stmt, ++i, cred.getPassword());
        DatabaseUtils.setInt(stmt, ++i, emp.getId());
        DatabaseUtils.executeUpdate(stmt);
        DatabaseUtils.close(con);
    }

    /**
     * Gets a list of employees.
     * @return a list of employees.
     */
    public List<Employee> getEmployees() {
        Connection con = DatabaseUtils.getConnection(ds);
        String sql = "";
        sql += "SELECT * FROM employee";

        Statement stmt = DatabaseUtils.makeStatement(con);
        ResultSet result = DatabaseUtils.execute(stmt, sql);
        List<Employee> empList = this.getEmployeeListFromResult(result);
        DatabaseUtils.close(con);

        return empList;
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

       Connection con = DatabaseUtils.getConnection(ds);

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
       Connection con = DatabaseUtils.getConnection(ds);

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

    }

    @Override
    public void editEmpoyee(final Employee emp) {
       Connection con = DatabaseUtils.getConnection(ds);

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

    /**
     * Returns the administrator.
     * @return the administrator.
     */
    public Employee getAdministrator() {
       Connection con = DatabaseUtils.getConnection(ds);

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
     * Returns the valid login combos.
     * @return the map of the login combos.
     */
    public Map<String, String> getLoginCombos() {
       Connection con = DatabaseUtils.getConnection(ds);

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

    @Override
    public Employee getEmployeeById(int id) {
       Connection con = DatabaseUtils.getConnection(ds);

        String sql = "";
        sql += "SELECT * FROM employee";
        sql += " WHERE id = ?";

        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, 1, id);
        ResultSet result = DatabaseUtils.executePreparedStatement(stmt);
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

}
