package services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDBempl;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * A bean for dealing with admin tasks and displaying the admin page.
 */
@Named("admin")
@SessionScoped
public class AdminService implements Serializable {

    /**
     * The injected DAO for accessing the employee list "DB".
     */
    @Inject @NoDBempl private EmployeeList employeeList;

    /** The employee that is currently edible. */
    private Employee editable;

    /** A map of credentials -- username, password. */
    private Map<String, String> credentials;

    /** Constructor. */
    public AdminService() {
        editable = null;
    }
    
    public Employee getEditable() {
        return editable;
    }

    public void setEditable(Employee e) {
        editable = e;
    }

    /**
     * Returns whether editing is allowed.
     * @return if editing is allowed.
     */
    public boolean isEditingMode() {
        // false if the emp is not in the editable list
        return editable != null;
    }

    /**
     * Initializes the credentials with a list of the valid login combos.
     */
    @PostConstruct
    public void initCredentials() {
        credentials = employeeList.getLoginCombos();
    }

    /**
     * Adds a set of credentials to the credential list.
     * @param userName the username.
     * @param password the password.
     */
    public void add(String userName, String password) {
        credentials.replace(userName, password);
    }

    /**
     * Returns the credentials.
     * @return A map of the credentials.
     */
    public Map<String, String> getCredentials() {
        return credentials;
    }

    /**
     * Returns all employees.
     * @return a list of all employees.
     */
    public List<Employee> getAllEmployees() {
        return employeeList.getEmployees();
    }

    /**
     * The action method for editing and employee.
     * @param emp the employee to edit.
     * @return the page to navigate to.
     */
    public String editAction(Employee emp) {
        editable = emp;
        return "admin";
    }

    /**
     * Returns true if currently editing the passed employee.
     * @param emp the employee.
     * @return whether editing.
     */
    public boolean isEditing(Employee emp) {
        // false if the emp is not in the editable list
        if (editable != null) {
            return editable.getId() == emp.getId();
        }
        return false;
    }

    /**
     * Action for deleting an employee.
     * @param emp the employee to delete.
     * @return the page to navigate to.
     */
    public String deleteAction(Employee emp) {
        employeeList.deleteEmpoyee(emp);
        return "admin";
    }

    /**
     * Action for saving the current state of the page.
     * @return the page to navigate to.
     */
    public String saveAction() {
        if (editable != null) {
            employeeList.editEmpoyee(editable);
            editable = null;
        }
        return "admin";
    }

}
