package validators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import annotations.DBempl;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * Validates employees for rest functions.
 *
 */
public class EmployeeRestValidator implements Serializable {

    /** DAO for employees. */
    @Inject @DBempl private EmployeeList employeeList;

    /** Finds messages from a properties file. */
    private MessageFinder messageFinder =
            new MessageFinder("/messages/errors.properties");

    /** 0 arg ctor. */
    public EmployeeRestValidator() { }

    /**
     * Checks if an employee exists.
     * @param id the id of the employee.
     */
    public void checkEmployeeExists(final int id) {
        Employee emp = employeeList.getEmployeeById(id);
        if (emp == null) {
            throw new IllegalArgumentException("Employee doesn't exist");
        }
    }

    /**
     * Validates each component of the employee.
     * @param emp the employee to validate.
     * @throws Exception the ex thrown incase invalid.
     */
    public final void validate(final Employee emp) throws Exception {
        validateEmpNum(emp.getEmpNumber());
        validateName(emp.getName());
        validatePassword(emp.getPassword());
        validateUsername(emp.getUserName());
    }

     /** Validates each component of the employee.
     * @param emp the employee to validate.
     * @throws Exception the ex thrown incase invalid.
     */
    public final void validate(final Employee emp, int id, int empNum) throws Exception {
        validateEmpNumEdit(emp.getEmpNumber(), id, empNum);
        validateName(emp.getName());
        validatePassword(emp.getPassword());
        validateUsername(emp.getUserName());
    }


    /**
     * Validates the employee number.
     * @param empNum the employee number.
     * @throws Exception the exeption thrown.
     */
    public void validateEmpNum(Integer empNum) throws Exception {
        if (empNum == null) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("empIdNull"));
        }

        if (empNum < 1 || empNum > 99999999) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("empIdLength"));

        }

        for (Employee emp : employeeList.getEmployees()) {
            if (emp.getEmpNumber() == empNum) {
                throw new IllegalArgumentException(
                        messageFinder.getMessage("empIdUnique"));
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
    public void validateEmpNumEdit(Integer empNum, int editingId,
                                  int editingEmpNum) throws Exception {

        if (empNum == null) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("empIdNull"));
        }

        if (empNum < 1 || empNum > 99999999) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("empIdLength"));

        }


        for (Employee emp : employeeList.getEmployees()) {
            if (emp.getId() != editingId 
                    && emp.getEmpNumber() == empNum) {
                throw new IllegalArgumentException(
                        messageFinder.getMessage("empIdUnique"));
            }

        }
        System.out.println("hello");

    }

    /**
     * Validates the name of the employee.
     * @param name the name.
     * @throws Exception the ex thrown.
     */
    private void validateName(final String name) throws Exception {

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("nullName"));
        }

        if (name.length() < 2 || name.length() > 20) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("nameLength"));
        }

        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetter(name.charAt(i))
                    && name.charAt(i) != '-'
                    && name.charAt(i) != ' ') {

            throw new IllegalArgumentException(
                    messageFinder.getMessage("nameCharacters"));
            }
        }
    }

    /**
     * Validates the password.
     * @param password the password
     * @throws Exception the exception.
     */
    public void validatePassword(final String password) throws Exception {


        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("nullPassword"));
        }

        if (password.length() < 5 || password.length() > 20) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("passwordLength"));
        }

        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetter(password.charAt(i))
                    && !Character.isDigit(password.charAt(i))
                    && password.charAt(i) != '_') {
                throw new IllegalArgumentException(
                        messageFinder.getMessage("passwordCharacters"));

            }
        }
    }

    /**
     * Validates the username.
     * @param username the username to validate.
     * @throws Exception the ex.
     */
    public void validateUsername(final String username) throws Exception {

        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("usernameNull"));
        }

        if (username.length() < 5 || username.length() > 20) {
            throw new IllegalArgumentException(
                    messageFinder.getMessage("usernameNull"));
        }

        for (int i = 0; i < username.length(); i++) {
            if (!Character.isLetter(username.charAt(i))
                    && !Character.isDigit(username.charAt(i))
                    && username.charAt(i) != '_') {
                throw new IllegalArgumentException(
                        messageFinder.getMessage("usernameCharacters"));
            }
        }

    }
    /**
     * A class for finding messages within a properties file.
     */
    public class MessageFinder {

        /**
         * The file name.
         */
        private String fileName;

        /**
         * Ctor.
         * @param fileName the file name.
         */
        MessageFinder(final String fileName) {
            this.fileName = fileName;
        }

        /**
         * Returns the message from the file.
         * @param key the key.
         * @return the message.
         */
        public final String getMessage(final String key) {
            Properties prop = new Properties();

            try {
                prop.load(loadFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prop.getProperty(key);
        }

        /**
         * Loads the file.
         * @return the input stream.
         */
        private InputStream loadFile() {
            InputStream input = null;
            
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("messages/errors.properties");
            return is;
        }
    }

}
