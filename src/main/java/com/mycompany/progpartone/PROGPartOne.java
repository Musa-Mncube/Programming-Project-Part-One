/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.progpartone;
import java.util.Scanner;
/**
 *
 * @author Musawenkosi Mncube ST10507845
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
         
         String userName;
         String phoneNumber;
         String Password;
         String results;
         do {
             System.out.println("Create your username.");
             userName = input.nextLine();
             
             System.out.println("==============================");
             System.out.println("Enter your phone number.");
             phoneNumber = input.nextLine();
             
             obj.checkPhoneNumber(phoneNumber);
             
             System.out.println("=================================");
             System.out.println("Please create your password.");
             Password = input.nextLine();
             obj.checkPassword(Password);
             System.out.println("==================================");
             
             results = obj.registerUser(userName, phoneNumber, Password);
             System.out.println(results);
         } while (!results.equals("Registration successful."));
         
         System.out.println("==== Login ====");
         
         boolean loginSuccessful = false;
         while (!loginSuccessful){
             System.out.println("Please enter your username to login.");
             String loginUsername = input.nextLine();
             System.out.println("Enter your password to login.");
             String loginPassword = input.nextLine();
             
             loginSuccessful = obj.UserLogin(loginUsername, phoneNumber, loginPassword);
             System.out.println(obj.returnUserLoginStatus(loginSuccessful, !loginSuccessful));
             
         }
        
        //----------------------------------------Part 2: Sending Messages------------------------
        System.out.println("Welcome to QuickChat.");
        
        System.out.println("How many messages would you like to send?");
        int numMessages = readInt(input);
        int numMessagesEntered = 0;
        
        boolean running = true;
        while (running) {
            System.out.println("Please select an option:");
            System.out.println("Option 1: Send Messages");
            System.out.println("Option 2: Show Recently Sent Messages");
            System.out.println("Option 3: Quit");
            System.out.println("Enter choice: ");
            
            int choice = readInt(input);
            
            switch (choice){
                case 1: 
                    if (numMessagesEntered >= numMessages) {
                        System.out.println("You have already entered your set number of messages( "+numMessages+ ")");
                    }else{
                        numMessagesEntered++;
                        sendOneMessage(input, numMessagesEntered, numMessages);
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
        
        System.out.println("Total Messages Sent: " + Message.returnTotalMessages());
        System.out.println("\n" + Message.printMessages());
        
        input.close();
    }
    
    private static void sendOneMessage(Scanner input, int messageNum, int numMessages){
        System.out.println("\n--- Message " + messageNum + "of" + numMessages + "---");
        
        String recipientNumber;
        String recipientResult;
        do{
            System.out.println("Enter recipient's cell number: ");
            recipientNumber = input.nextLine().trim();
            recipientResult = Message.checkRecipientCell(recipientNumber);
            System.out.println(recipientResult);
        }while (!recipientResult.equals("Cell phone number successfully captured"));
        
        String message;
        String lengthResult;
        do{
            System.out.println("Enter your message: ");
            message = input.nextLine();
            lengthResult = Message.checkMessageLength(message);
            System.out.println(lengthResult);
        }while (!lengthResult.equals("Message sent"));
        
        Message newMessage = new Message(recipientNumber, message);
        
        if (!newMessage.checkMessageID()){
            System.out.println("Message ID generation failed, skipping this message.");
            return;
        }
        
        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Enter choice: ");
        int action = readInt(input);
        System.out.println(newMessage.SentMessage(action));
        if (action == 1) {
            System.out.println("\nMessage ID: " + newMessage.getMessageID());
            System.out.println("Message Hash: " + newMessage.getMessageHash());
            System.out.println("Recipient: " + newMessage.getRecipientNumber());
            System.out.println("Message: " + newMessage.getMessage());
        }
    }

    private static int readInt(Scanner input) {
      try{
          return Integer.parseInt(input.nextLine().trim());
      }catch (NumberFormatException e) {
          return -1;
    }
}
}

