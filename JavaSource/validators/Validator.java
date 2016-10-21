package validators;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import annotations.NoDB;
import ca.bcit.infosys.employee.EmployeeList;

@Named("validator")
@SessionScoped
public class Validator implements Serializable {
	
	@Inject @NoDB private EmployeeList employeeList;

	public void validatePassword(FacesContext context, UIComponent componentToValidate, 
												       Object value) throws ValidatorException {
		
		String password = (String) value;

		if (password == null || password.length() == 0) {
			throw new ValidatorException(
				new FacesMessage("You must enter a password"));
		}

		if (password.length() < 5 || password.length() > 20) {
			throw new ValidatorException(
				new FacesMessage("password must be between 5 and 20 characters"));
		}

		for(int i = 0; i < password.length(); i++) {
			if (!Character.isLetter(password.charAt(i)) && 
				!Character.isDigit(password.charAt(i)) && 
				password.charAt(i) != '_') {
				throw new ValidatorException(
					new FacesMessage("Only alphabetic, numeric and \'_\' characters are allowed in password"));
			}
		}
	}
		

	public void validateUsername(FacesContext context, UIComponent componentToValidate, 
												       Object value) throws ValidatorException {
		
		String username = (String) value;

		if (username == null || username.length() == 0) {
			throw new ValidatorException(
				new FacesMessage("You must enter a username"));
		}

		if (username.length() < 5 || username.length() > 20) {
			throw new ValidatorException(
				new FacesMessage("username must be between 5 and 20 characters"));
		}

		for(int i = 0; i < username.length(); i++) {
			if (!Character.isLetter(username.charAt(i)) && 
				!Character.isDigit(username.charAt(i)) && 
				username.charAt(i) != '_') {
				throw new ValidatorException(
					new FacesMessage("Only alphabetic, numeric and \'_\' characters are allowed in username"));
			}
		}
	}

	public void validateEmpId(FacesContext context, UIComponent componentToValidate, 
												       Object value) throws ValidatorException {
		

		Integer empId = (Integer) value;

		if (empId == null) {
			throw new ValidatorException(
				new FacesMessage("You must enter an employee id"));
		}

		if (empId < 10 || empId > 99999999) {
			throw new ValidatorException(
				new FacesMessage("Employee ID must be between 2 and 8 characters"));
		}

	}

	public void validateName(FacesContext context, UIComponent componentToValidate, 
												       Object value) throws ValidatorException {
		
		String name = (String) value;

		if (name == null || name.length() == 0) {
			throw new ValidatorException(
				new FacesMessage("You must enter a name"));
		}

		if (name.length() < 2 || name.length() > 20) {
			throw new ValidatorException(
				new FacesMessage("name must be between 2 and 20 characters"));
		}

		for(int i = 0; i < name.length(); i++) {
			if (!Character.isLetter(name.charAt(i)) && 
				name.charAt(i) != '-') {
				throw new ValidatorException(
					new FacesMessage("Only alphabetic, numeric and \'-\' characters are allowed in username"));
			}
		}
	}

	public void validateHours(FacesContext context, UIComponent componentToValidate, 
												       Object value) throws ValidatorException {
		if (value == null) return;
		
		BigDecimal hours = (BigDecimal) value;

		if (hours.compareTo(BigDecimal.ZERO) < 0 || hours.compareTo(new BigDecimal("24")) > 0) {
			throw new ValidatorException(
				new FacesMessage("Hours must be between 0 and 24"));
		}

	}


}
