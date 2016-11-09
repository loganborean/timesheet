package services;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import annotations.NoDBempl;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
//import pojos.EmployeeListNoDBimpl;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * A bean for dealing with user/employee tasks and displaying the login page.
 */
@Named("user")
@SessionScoped
public class UserService implements Serializable {

    /**
     * The current employee.
     */
    private Employee currentEmployee;

    /**
     * The DAO for the employee list.
     */
    @Inject
    @NoDBempl
    private EmployeeList employeeList;

    /** ctor */
    public UserService() { }

    /**
     * Returns if there is someone logged in.
     * @return whether someone is logged in.
     */
    public boolean isLoggedIn() {
        return currentEmployee != null;
    }

    /**
     * Returns the current employee.
     * @return the current employee.
     */
    public Employee getCurrentEmployee() {
        return this.currentEmployee;
    }

    /**
     * Sets the current employee.
     * @param e the employee.
     */
    public void setCurrentEmployee(Employee e) {
        this.currentEmployee = e;
    }

    /**
     * Login to the system.
     * @return the page to be navigated to.
     */
    public String loginAction(Credentials cred) {
        if (employeeList.verifyUser(cred)) {

            Employee employee = findEmployeeByUsername(cred.getUserName());
            setCurrentEmployee(employee);
            if (employeeList.getAdministrator().getId()
                    == employee.getId()) {
                return "admin.xhtml?faces-redirect=true";
            }

            return "timesheet";
        }
        //adding an error message.
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Invalid username/password"));

        return "login";
    }

    /**
     * Logs out of the system.
     * @return the page to be navigated to.
     */
    public String logoutAction() {
//        username = null;
//        password = null;
        currentEmployee = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession =
                (HttpSession) facesContext.getExternalContext().
                                           getSession(false);
        httpSession.invalidate();
        return "login.xhtml?faces-redirect=true";
    }

    /**
     * Returns wheather the current employee is the admin.
     * @return wheather the employee is the admin.
     */
    public boolean isAdmin() {
        return currentEmployee.getEmpNumber()
                == employeeList.getAdministrator().getEmpNumber();
    }

    /**
     * Finds and employee by username.
     * @param username of the employee.
     * @return the employee.
     */
    private Employee findEmployeeByUsername(String username) {
        for (Employee emp : employeeList.getEmployees()) {
            if (emp.getUserName().equals(username)) {
                return emp;
            }
        }
        System.out.println("not found");
        return null;
    }


}
