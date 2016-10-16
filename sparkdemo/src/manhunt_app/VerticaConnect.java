package manhunt_app;

import java.sql.*;
import java.util.Properties;
public class VerticaConnect {
	
	private Connection conn;
	
	public Connection start() {
        Properties myProp = new Properties();
        myProp.put("user", "team3");
        myProp.put("password", "");
        myProp.put("database", "test");
        myProp.put("host", "ec2-54-227-78-132.compute-1.amazonaws.com");
        myProp.put("schema", "team3_schema");
        myProp.put("loginTimeout", "35");
        myProp.put("binaryBatchInsert", "true");
        try {
            conn = DriverManager.getConnection(
            		"jdbc:vertica://ec2-54-227-78-132.compute-1.amazonaws.com/test", myProp);
            System.out.println("Connected!");
            return conn;
        } catch (SQLTransientConnectionException connException) {
            // There was a potentially temporary network error
            // Could automatically retry a number of times here, but
            // instead just report error and exit.
            System.out.print("Network connection issue: ");
            System.out.print(connException.getMessage());
            System.out.println(" Try again later!");
            return null;
        } catch (SQLInvalidAuthorizationSpecException authException) {
            // Either the username or password was wrong
            System.out.print("Could not log into database: ");
            System.out.print(authException.getMessage());
            System.out.println(" Check the login credentials and try again.");
            return null;
        } catch (SQLException e) {
            // Catch-all for other exceptions
            e.printStackTrace();
        }
		return null;
    }
	
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}