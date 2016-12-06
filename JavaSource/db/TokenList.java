package db;

import ca.bcit.infosys.employee.Employee;
import domain.Token;

public interface TokenList {
    
    /**
     * Stores a token in the database.
     * @param token
     * @param e
     */
    void storeToken(String token, Employee e);

    /**
     * Returns whether the token is currently valid.
     * @param token the token to validate.
     * @return true/false.
     */
    boolean isValidToken(String token);


    Token getTokenFromStr(String tokenStr);

    boolean isAdminToken(String tokenStr);

}
