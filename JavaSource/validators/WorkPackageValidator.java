package validators;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;


import annotations.NoDB;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.TimesheetRow;
import services.TimesheetService;

@FacesValidator("wpvalid")
public class WorkPackageValidator implements Validator {

	@Inject @NoDB private EmployeeList employeeList;

	@Inject TimesheetService sheetService;

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) 
			throws ValidatorException {

		String wp = (String) arg2;

		if (wp == null || wp.length() == 0) {
			throw new ValidatorException(
				new FacesMessage("You must enter a Work Package"));
		}

		if (wp.length() > 10) {
			throw new ValidatorException(
				new FacesMessage("name must be less than 10 characters"));
		}

		for(int i = 0; i < wp.length(); i++) {
			if (!Character.isLetter(wp.charAt(i)) && 
				!Character.isDigit(wp.charAt(i))) { 
				throw new ValidatorException(
					new FacesMessage("Only letters and digits in WP"));
			}
		}

//		List<TimesheetRow> rows = sheetService.getCurrentSheet().getDetails();

		UIInput projIdComponent = (UIInput) arg1.getAttributes().get("project");
        Integer id =  (Integer) projIdComponent.getSubmittedValue();	
        System.out.println("~~~~~~~~~~~~~~~~" + id);


//		for(TimesheetRow row : rows) {
//			Integer thisRowsID = row.getProjectID();
//
//			if (thisRowsID == null) continue;
//
//			if (row.getProjectID().equals(id) && row.getWorkPackage().equals(wp)) {
//				throw new ValidatorException(
//					new FacesMessage("Work package must be unique for each project"));
//				
//			}
//		}
		
	}

}

