/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.progpartone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class UserLoginsTest {
    
    public UserLoginsTest() {
    }
    
    
    /**
     * Test of CheckuserName method, of class UserLogins.
     */
    @Test
    public void testCheckuserNameValid() {
        System.out.println("CheckuserName");
        String userName = "kyl_1";
        UserLogins instance = new UserLogins("","","");
        boolean expResult = true;
        boolean result = instance.CheckuserName(userName);
        assertEquals(expResult, result);
  
    }
    
    @Test
    public void testCheckuserNameInValid() {
        System.out.println("CheckuserName");
        String userName = "kyle!!!!!";
        UserLogins instance =new UserLogins("","","");
        boolean expResult = false;
        boolean result = instance.CheckuserName(userName);
        assertEquals(expResult, result);
  
    }

    /**
     * Test of checkPhoneNumber method, of class UserLogins.
     */
    @Test
    public void testCheckPhoneNumberValid() {
        System.out.println("checkPhoneNumber");
        String phoneNumber = "";
        UserLogins instance = null;
        boolean expResult = false;
        boolean result = instance.checkPhoneNumber(phoneNumber);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testCheckPhoneNumberInvalid() {
        System.out.println("checkPhoneNumber");
        String phoneNumber = "";
        UserLogins instance = null;
        boolean expResult = false;
        boolean result = instance.checkPhoneNumber(phoneNumber);
        assertEquals(expResult, result);
        
    }
    

    /**
     * Test of checkPassword method, of class UserLogins.
     */
    @Test
    public void testCheckPasswordValid() {
        System.out.println("checkPassword");
        String Password = "";
        UserLogins instance = null;
        boolean expResult = false;
        boolean result = instance.checkPassword(Password);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testCheckPasswordInvalid() {
        System.out.println("checkPassword");
        String Password = "";
        UserLogins instance = null;
        boolean expResult = false;
        boolean result = instance.checkPassword(Password);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of registerUser method, of class UserLogins.
     */
    @Test
    public void testRegisterUserValid() {
        System.out.println("registerUser");
        String userName = "";
        String phoneNumber = "";
        String Password = "";
        UserLogins instance = null;
        String expResult = "";
        String result = instance.registerUser(userName, phoneNumber, Password);
        assertEquals(expResult, result);
        ;
    }
    
    @Test
    public void testRegisterUserInvalid() {
        System.out.println("registerUser");
        String userName = "";
        String phoneNumber = "";
        String Password = "";
        UserLogins instance = null;
        String expResult = "";
        String result = instance.registerUser(userName, phoneNumber, Password);
        assertEquals(expResult, result);
        ;
    }

    /**
     * Test of UserLogin method, of class UserLogins.
     */
    @Test
    public void testUserLoginValid() {
        System.out.println("UserLogin");
        String userName = "";
        String phoneNumber = "";
        String Password = "";
        UserLogins instance = null;
        boolean expResult = false;
        boolean result = instance.UserLogin(userName, phoneNumber, Password);
        assertEquals(expResult, result);
        
    }
    
      @Test
    public void testUserLoginInvalid() {
        System.out.println("UserLogin");
        String userName = "";
        String phoneNumber = "";
        String Password = "";
        UserLogins instance = null;
        boolean expResult = false;
        boolean result = instance.UserLogin(userName, phoneNumber, Password);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of returnUserLoginStatus method, of class UserLogins.
     */
    @Test
    public void testReturnUserLoginStatusValid() {
        System.out.println("returnUserLoginStatus");
        Boolean LoginSuccessful = null;
        boolean LoginUnsuccessful = false;
        UserLogins instance = null;
        String expResult = "";
        String result = instance.returnUserLoginStatus(LoginSuccessful, LoginUnsuccessful);
        assertEquals(expResult, result);
     
    }
    
      @Test
    public void testReturnUserLoginStatusInvalid() {
        System.out.println("returnUserLoginStatus");
        Boolean LoginSuccessful = null;
        boolean LoginUnsuccessful = false;
        UserLogins instance = null;
        String expResult = "";
        String result = instance.returnUserLoginStatus(LoginSuccessful, LoginUnsuccessful);
        assertEquals(expResult, result);
     
    }
    
}
