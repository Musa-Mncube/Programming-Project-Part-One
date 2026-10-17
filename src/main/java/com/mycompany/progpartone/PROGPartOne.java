/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.progpartone;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class PROGPartOne {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        UserLogins obj = new UserLogins("","","");
        
            System.out.println("======Registration======");
            
         System.out.println("Hi there, what is your name?");
        String name = input.nextLine();
        obj.name = name;
        
        System.out.println("============================");
        System.out.println("Welcome " + name + ", please enter your surname.");
         String surname = input.nextLine();
         obj.surname = surname;
         
        System.out.println("Create your username.");
        String userName = input.nextLine();
        
        System.out.println("============================");
        System.out.println("Enter your phone number.");
        String phoneNumber = input.nextLine();
        
        obj.checkPhoneNumber(phoneNumber);
        
        System.out.println("============================");
        System.out.println("Please create your password.");
        String Password = input.next();
        obj.checkPassword(Password);
        System.out.println("============================");
        
        String results = obj.registerUser(userName, phoneNumber, Password);
        System.out.println(results);
        
        System.out.println("===== Login ======");
        
        input.nextLine();
        boolean loginSuccessful = false;
        while (!loginSuccessful){
            System.out.println("Please enter your username to login.");
            String loginUsername = input.nextLine(); //come back and check this later
            System.out.println("Enter your password to login.");
            String loginPassword = input.nextLine();
            
            loginSuccessful = obj.UserLogin(userName, phoneNumber, Password);
            System.out.println(obj.returnUserLoginStatus(loginSuccessful, loginSuccessful));
        }
        
        //----------------------------------------Part 2: Sending Messages------------------------
        
        
        
        
        
    }
}

