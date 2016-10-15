package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@NoDB
@ApplicationScoped
public class EmployeeListNoDBimpl implements EmployeeList, Serializable {

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
    public void setLoginCombos(Credentials cred) {
		loginCombos.put(cred.getUserName(), cred.getPassword());
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
		return (loginCombos.get(credential.getUserName())).equals(credential.getPassword());
	}

	@Override
	public String logout(Employee employee) {
		return null;
	}

	@Override // has side effect -- delete login combos
	public void deleteEmpoyee(Employee userToDelete) {
		//delete employee
		Iterator<Employee> iter = employees.iterator();
		while (iter.hasNext()) {
		    Employee emp = iter.next();

		    if (userToDelete == emp)
		        iter.remove();
		}

		//delete their login credentials 
		loginCombos.remove(userToDelete.getUserName());
	}

	@Override
	public void addEmployee(Employee newEmployee) {
		employees.add(newEmployee);
	}
	

}
