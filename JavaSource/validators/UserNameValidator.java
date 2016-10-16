package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("userName")
public class UserNameValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) 
			throws ValidatorException {
		String userName = (String) arg2;
		if (userName.length() <  3 || userName.length() > 15) {
			throw new ValidatorException(
				new FacesMessage("Username must be between 3-15 characters"));
		}
		
	}

}