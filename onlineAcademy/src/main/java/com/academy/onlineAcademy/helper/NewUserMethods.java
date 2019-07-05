package com.academy.onlineAcademy.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.exceptions.NewUserException;
import com.academy.onlineAcademy.exceptions.NewUserException.NewUserErrorType;
import com.academy.onlineAcademy.model.Type;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.view.AdminAddUserView;
import com.academy.onlineAcademy.view.SignUpView;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class NewUserMethods {
	
	private static Logger logger = Logger.getLogger(NewUserMethods.class.getName());
	
	static PersonController obj = new PersonController();
	static String enteredFullName;
	static String enteredUsername;
	static String enteredEmail;
	static String enteredPassword;
	static String enteredConfirmPassword;
	
	public static void addUser(Binder<Person> binder, Type userType, String fullName, String username, String email, String password, String confirmPassword) {
		try {
			setFieldsValues(fullName, username, email, password, confirmPassword);
			checkValidation(userType, binder);
			existingUsername();
			existingEmail();
			matchingPasswords();
			addUserToDatabase();
		}
		catch(NewUserException ex) {
			if (ex.getNewUserErrorType() == NewUserErrorType.FAILED_VALIDATION) {
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Not all fields are filled in as expected!");
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.NO_USER_TYPE) {
				Notification notif = new Notification("Warning", "ERROR!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The type " + userType + " is not recognized.");
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.EXISTING_USERNAME) {
				Notification notif = new Notification("Warning", "The username already exists! Please use another one or log in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The username " + username + " already exists!");
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "The email already exists! Please log in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The email " + email + " already exists!");
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.PASSWORDS_NOT_MATCHING) {
				Notification notif = new Notification("Warning", "The fields for password and confrim password do not match!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The password and confrim password values do not match!");
			}
			else if(ex.getNewUserErrorType() == NewUserErrorType.DATABASE_FAIL) {
				Notification notif = new Notification("Warning", "Saving to database failed!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Failed to save the new user to the database!");
			}
		}
		
	}
	
	private static void setFieldsValues(String fullName, String username, String email, String password, String confirmPassword) {
		
			enteredFullName = fullName;
			enteredUsername = username;
			enteredEmail = email;
			enteredPassword = password;
			enteredConfirmPassword = confirmPassword;
		
	}
	
	private static void checkValidation(Type userType, Binder<Person> binder) throws NewUserException{
		if(userType == Type.ADMIN) {
			if (binder.validate().isOk() == true) {
				
			}
			else { 
				throw new NewUserException(NewUserErrorType.FAILED_VALIDATION);
			}
		}
		else if (userType == Type.USER) {
			if (binder.validate().isOk() == true) {
				
			}
			else { 
				throw new NewUserException(NewUserErrorType.FAILED_VALIDATION);
			}
		}
		else {
			throw new NewUserException(NewUserErrorType.NO_USER_TYPE);
		}
	}
	
	private static void existingUsername() throws NewUserException {
		try {
	    	obj.getPersonByUsername(enteredUsername.toUpperCase()).getUsername();
	    	throw new NewUserException(NewUserErrorType.EXISTING_USERNAME);
	    	
	     }
		catch (PersistenceException e) {
		 }
	}
	
	private static void existingEmail() throws NewUserException {
		try {
	    	obj.getPersonByEmail(enteredEmail.toUpperCase()).getEmail();
	    	throw new NewUserException(NewUserErrorType.EXISTING_EMAIL);	    	
	     }
		 catch (PersistenceException ex) {
		 }
		
	}
	
	private static void matchingPasswords() throws NewUserException {
		if (enteredPassword.equals(enteredConfirmPassword)) {
			
		}
		else {
			throw new NewUserException(NewUserErrorType.PASSWORDS_NOT_MATCHING);
		}	
	}
	
	private static void addUserToDatabase() throws NewUserException {
		try {
			obj.addPerson(enteredFullName, enteredUsername.toUpperCase(), enteredEmail.toUpperCase(), enteredPassword, null, Type.USER, null, null);
			
			Notification notif = new Notification("Confirmation", "The user has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.INFO, "User " + enteredUsername + " has been created.");
		}
		catch(PersistenceException ex) {
			throw new NewUserException(NewUserErrorType.DATABASE_FAIL);
		}
	}
	
}
