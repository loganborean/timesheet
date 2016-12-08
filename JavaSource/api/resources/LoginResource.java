package api.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import annotations.DBempl;
import annotations.DBtoken;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

import java.util.Random;

import db.TokenList;

@Path("/login")
public class LoginResource {

    /** DAO for employees. */
    @Inject @DBempl private EmployeeList db;

    /** The token DAO. */
    @Inject @DBtoken private TokenList dbToken;

    /** The token length. */
    private static final int TOKEN_LENGTH = 20;

    @POST
    @Produces("application/xml")
    public Response login(@Context final UriInfo info) {
        // Authenticate the user using the credentials provided
        String username = info.getQueryParameters().getFirst("username");
        String password = info.getQueryParameters().getFirst("password");
        Credentials cred = new Credentials(username, password);

        if (!db.verifyUser(cred)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Employee emp = db.findEmployeeByUsername(username);
        // Issue a token for the user
        String token = makeToken();

        dbToken.storeToken(token, emp);

        return Response.ok(token).build();
    }

    /**
     * Makes a random 20 character token.
     * @return the token.
     */
    private String makeToken() {
        String tokenChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder token = new StringBuilder();
        Random rnd = new Random();
        while (token.length() < TOKEN_LENGTH) {
            int index = (int) (rnd.nextFloat() * tokenChars.length());
            token.append(tokenChars.charAt(index));
        }
        String toeknStr = token.toString();
        return toeknStr;
    }
}

