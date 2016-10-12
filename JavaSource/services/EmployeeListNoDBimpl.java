package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

public class EmployeeListNoDBimpl implements EmployeeList {

	/**
	 * username, password
	 */
	Map<String, String> loginCombos;
	
	List<Employee> employees;
	Employee administrator;
	Employee currentEmployee;
	
	public EmployeeListNoDBimpl() {
		loginCombos = new HashMap<String, String>();
		employees = new ArrayList<Employee>();
		administrator = new Employee("admin", 1111, "logan");
		
						//username, paassword
		loginCombos.put("logan", "aaa");
		employees.add(administrator);
	}

	@Override
	public List<Employee> getEmployees() {
		return this.employees;
	}

	@Override
	public Employee getEmployee(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getLoginCombos() {
		// TODO Auto-generated method stub
		return loginCombos;
	}

	@Override
	public Employee getCurrentEmployee() {
		return null;
	}

	@Override
	public Employee getAdministrator() {
		return administrator;
	}

	@Override
	public boolean verifyUser(Credentials credential) {
		Map<String, String> validLogins = getLoginCombos();
		for (Entry<String, String> entry : validLogins.entrySet()) {
			if ( (entry.getKey()).equals(credential.getUserName()) &&
				 (entry.getValue()).equals(credential.getPassword()) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String logout(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEmpoyee(Employee userToDelete) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEmployee(Employee newEmployee) {
		// TODO Auto-generated method stub
		
	}
	

}
