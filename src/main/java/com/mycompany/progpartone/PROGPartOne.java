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
        
        System.out.println("============================");
        System.out.println("Welcome " + name + ", please enter your surname.");
         String surname = input.nextLine();
         
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
        
        
        
        
    }
}

