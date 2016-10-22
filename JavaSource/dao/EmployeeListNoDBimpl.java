package dao;

import java.io.Serializable;
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
     * Returns all employees.
     * @Return the list of employees.
     */
    public List<Employee> getEmployees() {
        return this.employees;
    }


    /**
     * Returns the valid login combos.
     * @return the map of the login combos.
     */
    public Map<String, String> getLoginCombos() {
        return loginCombos;
    }


    /**
     * Returns the administrator.
     * @return the administrator.
     */
    public Employee getAdministrator() {
        return administrator;
    }

    /**
     * Verify a credential.
     * @param credential the credential to be verified.
     * @return boolean whether user is validated.
     */
    public boolean verifyUser(final Credentials credential) {
        String dbPass = loginCombos.get(credential.getUserName());
        if (dbPass == null) {
            return false;
        }
        return dbPass.equals(credential.getPassword());
    }

    /**
     * Deletes an employee.
     * @param userToDelete the user to delete.
     */
    public void deleteEmpoyee(final Employee userToDelete) {
        // delete employee
        Iterator<Employee> iter = employees.iterator();
        while (iter.hasNext()) {
            Employee emp = iter.next();
            if (userToDelete == emp) {
                iter.remove();
            }
        }

        // delete their login credentials
        loginCombos.remove(userToDelete.getUserName());
    }

    /**
     * @param newEmployee the employee to add.
     */
    public void addEmployee(final Employee newEmployee) {
        employees.add(newEmployee);
    }


}
