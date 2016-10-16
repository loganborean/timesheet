package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDB;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named("admin") 
@SessionScoped
public class AdminService implements Serializable {

	@Inject @NoDB private EmployeeList employeeList;
	private List<Employee> editable;
	private Map<String, String> credentials;
	
	public AdminService() {
		editable = new ArrayList<Employee>();
		System.out.println("hello");
	}
	
	@PostConstruct
	public void initCredentials() {
		credentials = employeeList.getLoginCombos();
	}
	
	public void add(String userName, String password) {
		credentials.replace(userName, password);
	}
	
	public Map<String, String> getCredentials() {
		return credentials;
	}
	
	public List<Employee> getAllEmployees() {
		return employeeList.getEmployees();
	}

	public String editAction(Employee emp) {
		editable.add(emp);
		return "admin";
	}

	public boolean isEditing(Employee emp) {
		//false if the emp is not in the editable list
		return editable.indexOf(emp) != -1;
	}

	public void clearEditable() {
		editable.clear();
	}
	
	public String deleteAction(Employee emp) {
		employeeList.deleteEmpoyee(emp); //side effect -- deletes login combo
		return "admin";
	}
	
	public String saveAction() {
		this.clearEditable();
		return "admin";
	}
	
	
}
