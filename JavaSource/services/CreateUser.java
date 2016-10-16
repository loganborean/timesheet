package services;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDB;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named("createUser")
@RequestScoped
public class CreateUser implements Serializable {
	
	@Inject @NoDB EmployeeList employeeList;

	private String name;
	private Integer empId;
	private String username;
	private String password;
	
	public CreateUser() { }
	
	public String createUserAction() {
		Employee newEmp = new Employee(name, empId, username);
		Credentials cred = new Credentials();
		cred.setUserName(username);
		cred.setPassword(password);
		employeeList.setLoginCombos(cred);
		employeeList.addEmployee(newEmp);
		
		return "timesheet";
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
