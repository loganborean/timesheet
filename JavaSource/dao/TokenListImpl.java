package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import annotations.DBempl;
import annotations.DBtoken;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import db.DatabaseUtils;
import db.TokenList;
import domain.Token;

@DBtoken
@SessionScoped
public class TokenListImpl implements TokenList, Serializable {

    /** datasource. */
    @Resource(mappedName = "java:jboss/datasources/timesheet-jdbc")
    private DataSource ds;
    /** DAO for employees. */
    @Inject @DBempl private EmployeeList empDb;

    @Override
    public final void storeToken(final String token, final Employee emp) {
        Connection con = DatabaseUtils.getConnection(ds);

        String sql = "";
        sql += "INSERT INTO token";
        sql += " (empId, token, expires_at)";
        sql += " values(?, ?, ?)";
        sql += " ON DUPLICATE KEY UPDATE";
        sql += "  token = ?,";
        sql += "  expires_at = ?";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        Date oneHourAfterNow = cal.getTime();
        Timestamp stamp = new Timestamp(oneHourAfterNow.getTime());

        int i = 0;
        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setInt(stmt, ++i, emp.getId());
        DatabaseUtils.setString(stmt, ++i, token);
        DatabaseUtils.setTimestamp(stmt, ++i, stamp);
        DatabaseUtils.setString(stmt, ++i, token);
        DatabaseUtils.setTimestamp(stmt, ++i, stamp);
        DatabaseUtils.executeUpdate(stmt);
        DatabaseUtils.close(con);
    }

    @Override
    public final boolean isValidToken(final String tokenStr) {
        Token token = getToken(tokenStr);

        if (token == null) {
            return false;
        }

        long nowTime = new Timestamp(System.currentTimeMillis()).getTime();
        long expiresTime = token.getExpires_at().getTime();

        if (expiresTime < nowTime) {
            return false;
        }

        return true;
    }

    @Override
    public final Token getTokenFromStr(final String tokenStr) {
        return getToken(tokenStr);
    }

    @Override
    public final boolean isAdminToken(final String tokenStr) {
        Token token = getToken(tokenStr);
        if (empDb.getAdministrator().getId()
                != token.getEmp().getId()) {
            return false;
        }
        return true;
    }

    private final Token getToken(String tokenStr) {
        Connection con = DatabaseUtils.getConnection(ds);

        String sql = "";
        sql += "SELECT * FROM token";
        sql += " WHERE token = ?";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        Date oneHourAfterNow = cal.getTime();
        Timestamp stamp = new Timestamp(oneHourAfterNow.getTime());

        int i = 0;
        PreparedStatement stmt = DatabaseUtils.prepareStatement(con, sql);
        DatabaseUtils.setString(stmt, ++i, tokenStr);
        ResultSet result = DatabaseUtils.executePreparedStatement(stmt);
        Token token = getTokenFromResultSet(result);
        DatabaseUtils.close(con);

        return token;
    }

    private final Token getTokenFromResultSet(ResultSet result) {
        Token token = null;

        try {
            if (result.next()) {
                token = new Token(result.getInt("id"),
                                  empDb.getEmployeeById(result.getInt("empId")),
                                  result.getString("token"),
                                  result.getTimestamp("expires_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return token;
    }


}
