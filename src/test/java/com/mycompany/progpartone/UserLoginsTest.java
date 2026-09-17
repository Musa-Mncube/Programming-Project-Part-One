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
    public void testCheckuserName() {
        System.out.println("CheckuserName");
        String userName = "kyl_1";
        UserLogins instance = null;
        boolean expResult = true;
        boolean result = instance.CheckuserName(userName);
        assertEquals(expResult, result);
        
       
    }

    /**
     * Test of checkPhoneNumber method, of class UserLogins.
     */
    @Test
    public void testCheckPhoneNumber() {
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
    public void testCheckPassword() {
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
    public void testRegisterUser() {
        System.out.println("registerUser");
        String userName = "";
        String phoneNumber = "";
        String Password = "";
        UserLogins instance = null;
        String expResult = "";
        String result = instance.registerUser(userName, phoneNumber, Password);
        assertEquals(expResult, result);
    }
    
}
