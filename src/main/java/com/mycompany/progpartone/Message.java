/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progpartone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Student
 */
class Message {
    
    //keeps track of all messages sent while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();
    private static int totalMessages = 0;
    
    private String messageID;
    private String recipientNumber;
    private String message;
    private String messageHash;
    private int messageNumber; // the number of the messages used in the hash
    
    public Message(String reipientNumber, String message){
        this.recipientNumber = recipientNumber;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageNumber = totalMessagesSent; //starts at 0
    }
    
    private String generateMessageID(){
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < 10; i++){
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
    //Boolean: checkMessageID()to ensure the message is not more than 10 characters.
    public boolean checkMessageID(){
        return messageID != null && messageID.length() <= 10;
    }
    //String: checkRecipientCell()- to ensure that the recipient's cell number is not more than 10 characters
   
     public static String checkRecipientCell(String recipientNumber) {
        boolean validLength = recipientNumber.length() <= 10;
        boolean hasCode = recipientNumber.startsWith("+") || 
recipientNumber.matches("^0\\d*");
        if (validLength && hasCode) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    public static String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message sent.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size";
        }
    // String: createMessageHash() - first two digits of the message ID, the message
    // number, and the first and last words of the message, all in caps.
    public String createMessageHash() {
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0].toUpperCase().replaceAll("[^A-Z0-9]", "");
        String lastWord = words[words.length - 1].toUpperCase().replaceAll("[^A-Z09]", "");
        String idPrefix = messageID.substring(0, 2);
        messageHash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }
    // String: SentMessage() - lets the user choose to send, disregard or store the message
    public String SentMessage(int choice) {
        switch (choice) {
            case 1: // Send Message
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2: // Disregard Message
                return "Press 0 to delete the message.";
            case 3: // Store Message to send later
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }
    // String: printMessages() - returns all messages sent while the program is running
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipientNumber).append("\n");
            sb.append("Message: ").append(m.message).append("\n");
            sb.append("------------------------------\n");
        }
        return sb.toString();
    }
    // Int: returnTotalMessages() - returns the total number of messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    public String storeMessage() {
        try (java.io.FileWriter fw = new java.io.FileWriter("messages.json", true)) {
            String json = "{"
                    + "\"messageID\":\"" + messageID + "\","
                    + "\"messageNumber\":" + messageNumber + ","
                    + "\"recipientNumber\":\"" + recipientNumber + "\","
                    + "\"message\":\"" + message.replace("\"", "\\\"") + "\","
                    + "\"hash\":\"" + (messageHash != null ? messageHash : "") + "\""
                    + "}" + System.lineSeparator();
            fw.write(json);
            return "Message stored to messages.json.";
        } catch (java.io.IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }
    public String getMessageID() { return messageID; }
    public String getRecipientNumber() { return recipientNumber; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public int getMessageNumber() { return messageNumber; }
}
    }
