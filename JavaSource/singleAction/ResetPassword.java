package singleAction;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDBempl;
import ca.bcit.infosys.employee.Credentials;
import dao.EmployeeListNoDBimpl;
import services.UserService;

/**
 * A bean representing the resetting of a password.
 */
@Named("resetPassword")
@RequestScoped
public class ResetPassword {

    /**
     * The DAO for the employee list.
     */
    @Inject
    @NoDBempl
    private EmployeeListNoDBimpl employeeList;

    @Inject private UserService user;

    /** the employee username. **/
    private String username;
    /** the employee old pass. **/
    private String oldPassword;
    /** the employee new pass. **/
    private String newPassword;
    /** the employee new pass conform. **/
    private String newPasswordConfirm;

    /**
     * Resets the users password.
     * @return the page to navigate to.
     */
    public String resetPasswordAction() {
        // validate
        Credentials cred = new Credentials();
        cred.setUserName(username);
        cred.setPassword(oldPassword);

        if (!employeeList.verifyUser(cred)) {
            FacesContext.getCurrentInstance().
            addMessage(null, new FacesMessage("username pass do not match"));
            return "resetPassword";
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            FacesContext.getCurrentInstance().
            addMessage(null, new FacesMessage("Your new passwords dont match"));
            return "resetPassword";
        }

        Credentials newCred = new Credentials();
        newCred.setUserName(username);
        newCred.setPassword(newPassword);

        employeeList.resetPassword(user.getCurrentEmployee(), newCred);

        return "timesheet";
    }

    /** Getter. */
    public String getOldPassword() {
        return oldPassword;
    }

    /** Setter. */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /** Getter. */
    public String getNewPassword() {
        return newPassword;
    }

    /** Setter. */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /** Getter. */
    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    /** Setter. */
    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    /** Getter. */
    public String getUsername() {
        return username;
    }

    /** Setter. */
    public void setUsername(String username) {
        this.username = username;
    }

}
