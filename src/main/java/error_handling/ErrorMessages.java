package error_handling;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class ErrorMessages {

	// Method to get the error message by error code
    public static String getSQLErrorMessage(String err_code) {
    	
    	String r_path = "errors/errors.properties";
    	
    	Properties errorProperties = new Properties();

        try (InputStream input = ErrorMessages.class.getClassLoader().getResourceAsStream(r_path)) {
        	if (input == null) {
        		return "Sorry, unable to find " + r_path;
        	}
        	errorProperties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    	
        // Convert the integer error code to a string and look it up in the properties
        String message = errorProperties.getProperty(err_code);
        
        // Return the message if found, otherwise return a default message
        return message != null ? message : "Unknown error code: " + err_code;
    }
	
}
