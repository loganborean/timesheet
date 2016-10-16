package services;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import annotations.NoDB;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
//import pojos.EmployeeListNoDBimpl;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@Named("user") 
@SessionScoped
public class UserService implements Serializable {
	
	private String username;
	private String password;
	private Employee currentEmployee;
	
	@Inject @NoDB private EmployeeList employeeList;
	
	public UserService() { }

	public boolean isLoggedIn() {
		return currentEmployee != null;
	}

	public String loginAction() {
		
		Credentials cred = makeCredentials();
		if (employeeList.verifyUser(cred)) {
			Employee employee = findEmployeeByUsername(cred.getUserName());
			if (isAdmin(employee)) {
				//current user is admin
			}
			setCurrentEmployee(employee);
			return "admin.xhtml?faces-redirect=true";
		}
		return "login";

	}
	
	public void createUserAction() {
		
	}
	public String logoutAction() {
		username = null;
		password = null;
		currentEmployee = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(false);
		httpSession.invalidate();
		return "login.xhtml?faces-redirect=true";
	}
	
	
	public void setCurrentEmployee(Employee emp) {
		currentEmployee = emp;
	}
	
	private boolean isAdmin(Employee emp) {
		return emp.getEmpNumber() == employeeList.getAdministrator().getEmpNumber();
		
	}
	
	private Employee findEmployeeByUsername(String username) {
		for (Employee emp : employeeList.getEmployees()) {
			if (emp.getUserName().equals(username)) {
				return emp;
			}
		}
		System.out.println("not found");
		return null;
	}

	public Credentials makeCredentials() {
		Credentials cred = new Credentials();
		cred.setUserName(getUsername());
		cred.setPassword(getPassword());
		return cred;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	

}
