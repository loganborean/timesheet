package api;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import annotations.AdminSecured;
import annotations.DBempl;
import annotations.DBtoken;
import ca.bcit.infosys.employee.EmployeeList;
import db.TokenList;

/**
 * Filter for admin requests.
 */
@AdminSecured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AdminAuthenticationFilter implements ContainerRequestFilter {

    /** DAO for employees. */
    @Inject @DBempl private EmployeeList db;

    /** the token Db. */
    @Inject @DBtoken private TokenList dbToken;

    @Override
    public final void filter(final ContainerRequestContext ctx)
            throws IOException {
        String token = ctx.getUriInfo().getQueryParameters().getFirst("t");

        if (token == null) {
            throw new NotAuthorizedException(
                    "Authorization token must be provided");
        }

        try {
            validateToken(token);
        } catch (Exception e) {
            ctx.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    /**
     * Validates a token.
     * @param token the token to validate.
     * @throws Exception ex thrown.
     */
    private void validateToken(final String token) throws Exception {
        if (!dbToken.isValidToken(token) && !dbToken.isAdminToken(token)) {
            throw new Exception();
        }

    }

}
