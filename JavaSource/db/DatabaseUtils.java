package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * A class for static database utility methods.
 */
public final class DatabaseUtils {

    /** Private constructor. */
    private DatabaseUtils() { }

    /**
     * Creates a connection.
     * @param driver driver to load
     * @param url the host
     * @param username the username
     * @param password the password
     * @return the connection
     * @throws ClassNotFoundException if the driver class not found
     * @throws SQLException if there was an error establishing conn
     */
    public static Connection
    createConnection(final String driver, final String url,
                     final String username, final String password) {

        loadClass(driver);
        Connection con = null;

        try {
            if ((username == null) || (password == null)
                || (username.trim().length() == 0)
                || (password.trim().length() == 0)) {

                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url, username, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }
    
    public static Connection getConnection(final DataSource ds) {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
        
    }

    /**
     * Loads a driver.
     * @param driver the driver.
     */
    private static void loadClass(final String driver) {
        try {
            if (driver != null) {
                Class.forName(driver);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes a connection.
     * @param connection the connection to close.
     */
    public static void close(final Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param connnection the connection
     * @param sql the sql statment.
     * @return the prepared statment.
     */
    public static PreparedStatement
    prepareStatement(final Connection connnection,
                     final String sql) {
        PreparedStatement stmt = null;
        try {
            stmt = connnection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stmt;
    }

    /**
     * Executes sql on a statement.
     * @param stmt the statement.
     * @param sql the sql to execute.
     * @return the results returned.
     */
    public static ResultSet execute(final Statement stmt, final String sql) {
        ResultSet result = null;
        try {
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Executes sql on a prepared statement.
     * @param stmt the statement.
     * @return the results returned.
     */
    public static ResultSet
    executePreparedStatement(final PreparedStatement stmt) {
        ResultSet result = null;
        try {
            result = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Creates a statement.
     * @param connnection the connection
     * @return the statment.
     */
    public static Statement makeStatement(final Connection connnection) {
        Statement stmt = null;
        try {
            stmt = connnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stmt;
    }


    /**
     * @param stmt the prepared statment.
     * @param index the index to add it to.
     * @param num the number to add the the statment
     */
    public static void setInt(final PreparedStatement stmt,
                              final int index, final Integer num) {
        try {
            if (num == null) {
                stmt.setNull(index, Types.INTEGER);
            } else {
                stmt.setInt(index, num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param stmt the prepared statment.
     * @param index the index to add it to.
     * @param date the date to add the the statment.
     */
    public static void setDate(final PreparedStatement stmt,
                               final int index, final Date date) {
        try {
            if (date == null) {
                stmt.setNull(index, Types.DATE);
            } else {
                stmt.setDate(index, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param stmt the prepared statment.
     * @param index the index to add it to.
     * @param str the string to add the the statment
     */
    public static void setString(final PreparedStatement stmt,
                              final int index, final String str) {
        try {
            if (str == null) {
                stmt.setNull(index, Types.VARCHAR);
            } else {
                stmt.setString(index, str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an update on a statement.
     * @param stmt the statement
     */
    public static void executeUpdate(final PreparedStatement stmt) {
        try {
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a big decimal.
     * @param stmt the statement.
     * @param index the index.
     * @param bigDecimal the big decimal.
     */
    public static void setBigDecimal(final PreparedStatement stmt,
                    final int index, final BigDecimal bigDecimal) {
        try {
            if (bigDecimal == null) {
                stmt.setNull(index, Types.DECIMAL);
            } else {
                stmt.setBigDecimal(index, bigDecimal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

