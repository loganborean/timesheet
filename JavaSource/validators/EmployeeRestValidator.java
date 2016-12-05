package validators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Properties;

import javax.inject.Inject;

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
     * Validates each component of the employee.
     * @param emp the employee to validate.
     * @throws Exception the ex thrown incase invalid.
     */
    public final void validate(final Employee emp) throws Exception {
        validateEmpNum(emp.getEmpNumber());
        validateName(emp.getName());
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
