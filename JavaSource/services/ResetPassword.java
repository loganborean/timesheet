package services;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDB;
import ca.bcit.infosys.employee.Credentials;
import dao.EmployeeListNoDBimpl;

@Named("resetPassword")
@RequestScoped
public class ResetPassword {

	@Inject @NoDB EmployeeListNoDBimpl employeeList;

	private String username;
	private String oldPassword;
	private String newPassword;
	private String newPasswordConfirm;
	
	
	public String resetPasswordAction() {
		//validate
		Credentials cred = new Credentials();
		cred.setUserName(username);
		cred.setPassword(oldPassword);
		
		if (!employeeList.verifyUser(cred)) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("username pass do not match"));
            return "resetPassword";
		}
		
		if (! newPassword.equals(newPasswordConfirm)) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your new passwords dont match"));
            return "resetPassword";
		}

		Credentials newCred = new Credentials();
		newCred.setUserName(username);
		newCred.setPassword(newPassword);
		
		employeeList.setLoginCombos(newCred);
		
		return "timesheet";
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	

}
