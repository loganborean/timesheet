package validators;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;

import annotations.DBempl;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;
import services.TimesheetService;

/**
 * A class for validating input.
 */
@Named("validator")
@SessionScoped
public class Validator implements Serializable {

    /**
     * DAO for the employee list.
     */
    @Inject
    @DBempl
    private EmployeeList employeeList;

    /**
     * The object currently representing the timesheet.
     */
    @Inject
    private TimesheetService sheetService;

    /**
     * Validates the password.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException
     */
    public void validatePassword(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        String password = (String) value;

        if (password == null || password.length() == 0) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "nullPassword", null);
            throw new ValidatorException(message);
        }

        if (password.length() < 5 || password.length() > 20) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "passwordLength", null);
            throw new ValidatorException(message);

        }

        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetter(password.charAt(i))
                    && !Character.isDigit(password.charAt(i))
                    && password.charAt(i) != '_') {
                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "passwordCharacters", null);
                throw new ValidatorException(message);

            }
        }
    }

    /**
     * Validates the username.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException
     */
    public void validateUsername(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        String username = (String) value;

        if (username == null || username.length() == 0) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "usernameNull", null);
            throw new ValidatorException(message);
        }

        if (username.length() < 5 || username.length() > 20) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "usernameNull", null);
            throw new ValidatorException(message);
        }

        for (int i = 0; i < username.length(); i++) {
            if (!Character.isLetter(username.charAt(i))
                    && !Character.isDigit(username.charAt(i))
                    && username.charAt(i) != '_') {
                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "usernameCharacters", null);
                throw new ValidatorException(message);
            }
        }

    }

    /**
     * Validates the employee id.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException
     */
    public void validateEmpId(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        Integer empNum = (Integer) value;

        if (empNum == null) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "empIdNull", null);
            throw new ValidatorException(message);
        }

        if (empNum < 1 || empNum > 99999999) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "empIdLength", null);
            throw new ValidatorException(message);
        }

        for (Employee emp : employeeList.getEmployees()) {
            if (emp.getEmpNumber() == empNum) {
                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "empIdUnique", null);
                throw new ValidatorException(message);
            }
        }
    }

    /**
     * Validates the employee id.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException
     */
    public void validateEmpIdEdit(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        Integer empNum = (Integer) value;

        if (empNum == null) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "empIdNull", null);
            throw new ValidatorException(message);
        }

        if (empNum < 1 || empNum > 99999999) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "empIdLength", null);
            throw new ValidatorException(message);
        }

        Employee currentlyEditingEmployee =
                (Employee) componentToValidate.getAttributes()
                                              .get("currentEmp");

        for (Employee emp : employeeList.getEmployees()) {
            if (emp.getId() != currentlyEditingEmployee.getId()
                    && emp.getEmpNumber() == empNum) {
                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "empIdUnique", null);
                throw new ValidatorException(message);
            }

        }

    }


    /**
     * Validates the name.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException
     */
    public void validateName(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        String name = (String) value;

        if (name == null || name.length() == 0) {
             FacesMessage message
                = Messages.getMessage(
                   "messages.errors", "nullName", null);
             throw new ValidatorException(message);
        }

        if (name.length() < 2 || name.length() > 20) {

            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "nameLength", null);
            throw new ValidatorException(message);
        }

        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetter(name.charAt(i))
                    && name.charAt(i) != '-'
                    && name.charAt(i) != ' ') {

                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "nameCharacters", null);
                throw new ValidatorException(message);
            }
        }
    }

    /**
     * Validates the hours.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException the exception.
     */
    public void validateHours(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        if (value == null) {
            return;
        }

        BigDecimal hours = (BigDecimal) value;

        if (getNumberOfDecimalPlaces(hours) > 1) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "hoursDecimal", null);
            throw new ValidatorException(message);
        }


        UIInput proj = (UIInput) componentToValidate.findComponent("projID");
        Integer id = (Integer) proj.getLocalValue();
        UIInput work = (UIInput) componentToValidate.findComponent("workp");
        String wp = (String) work.getLocalValue();


        Timesheet currentSheet = sheetService.getCurrentSheet();

        String strDayIndex =
                (String) componentToValidate.getAttributes()
                                            .get("day_index");

        int dayIndex = Integer.parseInt(strDayIndex);

        BigDecimal totalExceptThisHour =
                sheetService.getTotalForDayExcept(dayIndex, id, wp);

        totalExceptThisHour = totalExceptThisHour.add(hours);

        if (totalExceptThisHour.compareTo(new BigDecimal("24")) > 0) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "hours24", null);
            throw new ValidatorException(message);
        }

    }

    /**
     * Returns the number of decimal places.
     * @param bigDecimal the big decimal.
     * @return the number of decimal places.
     */
    private int getNumberOfDecimalPlaces(final BigDecimal bigDecimal) {
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        return index < 0 ? 0 : string.length() - index - 1;
    }

    /**
     * Validates the notes.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException the exception.
     */
    public void validateNotes(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {
        String notes = (String) value;

        if (notes.length() > 100) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "notesLength", null);
            throw new ValidatorException(message);
        }

    }

    /**
     * Validates the work package.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException the exception.
     */
    public void validateWP(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        String wp = (String) value;

        if (wp == null || wp.length() == 0) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "wpNull", null);
            throw new ValidatorException(message);
        }

        if (wp.length() > 10) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "wpLength", null);
            throw new ValidatorException(message);
        }

        for (int i = 0; i < wp.length(); i++) {
            if (!Character.isLetter(wp.charAt(i))
                    && !Character.isDigit(wp.charAt(i))) {

                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "wpCharacters", null);
                throw new ValidatorException(message);
            }
        }

        List<TimesheetRow> rows = sheetService.getCurrentSheet().getDetails();
        UIInput proj = (UIInput) componentToValidate.findComponent("projID");
        Integer id = (Integer) proj.getLocalValue();

        if (id == null) {
            return;
        }

        int projectId = id.intValue();

        TimesheetRow currentlyEditingRow =
                (TimesheetRow) componentToValidate.getAttributes()
                                                  .get("currentRow");

        for (TimesheetRow row : rows) {
            Integer thisRowsID = row.getProjectID();

            if (thisRowsID == null) {
                continue;
            }

            if (row != currentlyEditingRow
                    && row.getProjectID().equals(projectId)
                    && row.getWorkPackage().equals(wp)) {
                FacesMessage message
                   = Messages.getMessage(
                      "messages.errors", "wpUnique", null);
                throw new ValidatorException(message);

            }
        }
    }

    /**
     * Validates the project ID.
     * @param context
     * @param componentToValidate
     * @param value the value to validate.
     * @throws ValidatorException the exception.
     */
    public void validateProjID(FacesContext context,
            UIComponent componentToValidate, Object value)
            throws ValidatorException {

        Integer empId = (Integer) value;

        if (empId == null) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "projIdNull", null);
            throw new ValidatorException(message);
        }

        if (empId < 10 || empId > 99999999) {
            FacesMessage message
               = Messages.getMessage(
                  "messages.errors", "projIdLength", null);
            throw new ValidatorException(message);
        }
    }
}
