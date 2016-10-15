package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named("admin") 
@SessionScoped
public class AdminService implements Serializable {

	@Inject @NoDB private EmployeeList employeeList;
	private List<Employee> editable;
	
	public AdminService() {
		editable = new ArrayList<Employee>();
	}
	
	public List<Employee> getAllEmployees() {
		return employeeList.getEmployees();
	}

	public String editAction(Employee e) {
		editable.add(e);
		return "timesheet";
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
		return "timesheet";
	}
	
	public String saveAction() {
		this.clearEditable();
		return "timesheet";
	}
	
	
}
