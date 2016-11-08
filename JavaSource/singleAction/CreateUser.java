package singleAction;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDBempl;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * A class to represent creation of an employee.
 */
@Named("createUser")
@RequestScoped
public class CreateUser implements Serializable {

    /**
     * The DAO for employee list.
     */
    @Inject
    @NoDBempl
    private EmployeeList employeeList;

    /** The name of the employee. */
    private String name;
    /** The employee ID of the employee. */
    private Integer empId;
    /** The username of the employee. */
    private String username;
    /** The password of the employee. */
    private String password;

    /** constructor. **/
    public CreateUser() { }

    /**
     * Creates a user.
     * @return the page to navigate to.
     */
    public String createUserAction() {
//        Employee newEmp = new Employee(name, empId, username);
        Employee newEmp = new Employee();
        newEmp.setName(name);
        newEmp.setUserName(username);
        newEmp.setPassword(password);

        Credentials cred = new Credentials();
        cred.setUserName(username);
        cred.setPassword(password);

        employeeList.setLoginCombos(cred);
        employeeList.addEmployee(newEmp);

        return "admin";
    }

    /** returns the employee name. */
    public String getName() {
        return name;
    }

    /** sets the employee name. */
    public void setName(String name) {
        this.name = name;
    }

    /** returns the employee id. */
    public Integer getEmpId() {
        return empId;
    }

    /** sets the employee id. */
    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    /** returns the employee username */
    public String getUsername() {
        return username;
    }

    /** sets the employee username */
    public void setUsername(String username) {
        this.username = username;
    }

    /** returns the employee password */
    public String getPassword() {
        return password;
    }

    /** sets the employee password */
    public void setPassword(String password) {
        this.password = password;
    }

}
