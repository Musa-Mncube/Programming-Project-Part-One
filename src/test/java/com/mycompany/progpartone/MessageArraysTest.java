/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.progpartone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//PART 3 STARTS HERE 
//(unit tests for the arrays, using the Part 3 test data)

/**
 * Part 3 unit tests. Every test starts by filling the arrays with Test Data
 * Messages 1-5 through the same SentMessage() method the app uses.
 *
 * @author Musawenkosi Mncube ST10507845
 */
public class MessageArraysTest {

    private static final String TEST_FILE = "target/test-part3.json";

    private static final String MESSAGE_2 = "Where are you? You are late! I have asked you to be on time.";

    public MessageArraysTest() {
    }

    @BeforeEach
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("target"));
        Files.deleteIfExists(Paths.get(TEST_FILE));
        Message.setStorageFile(TEST_FILE);
        Message.resetAll();
        Message.setSender("kyl_1");

        // Test Data Message 1 - Sent
        new Message("0112345678", 0, "+27834557896", "Did you get the cake?").SentMessage(1);
        // Test Data Message 2 - Stored
        new Message("0287654321", 1, "+27838884567", MESSAGE_2).SentMessage(3);
        // Test Data Message 3 - Disregard
        new Message("0311223344", 2, "+27834484567", "Yohoooo, I am at your gate.").SentMessage(2);
        // Test Data Message 4 - Sent (0838884567 is used as its message ID and its number)
        new Message("0838884567", 3, "0838884567", "It is dinner time!").SentMessage(1);
        // Test Data Message 5 - Stored
        new Message("0555667788", 4, "+27838884567", "Ok, I am leaving without you.").SentMessage(3);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
        Message.setStorageFile("messages.json");
        Message.resetAll();
        Message.setSender("");
    }

    /**
     * Sent Messages array correctly populated (Test Data Messages 1-4).
     */
    @Test
    public void testSentMessagesArrayPopulated() {
        System.out.println("Sent Messages array");
        String[] sent = Message.getSentMessageTexts();
        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0]);
        assertEquals("It is dinner time!", sent[1]);
    }

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        System.out.println("Disregarded Messages array");
        String[] disregarded = Message.getDisregardedMessageTexts();
        assertEquals(1, disregarded.length);
        assertEquals("Yohoooo, I am at your gate.", disregarded[0]);
    }

    /**
     * Stored Messages array is filled by reading the JSON file.
     */
    @Test
    public void testStoredMessagesArrayReadFromJson() {
        System.out.println("Stored Messages array");
        int loaded = Message.loadStoredMessages();
        assertEquals(2, loaded);
        String[] stored = Message.getStoredMessageTexts();
        assertEquals(2, stored.length);
        assertEquals(MESSAGE_2, stored[0]);
        assertEquals("Ok, I am leaving without you.", stored[1]);
    }

    @Test
    public void testMessageHashAndIDArraysPopulated() {
        System.out.println("Message Hash and Message ID arrays");
        String[] hashes = Message.getMessageHashes();
        String[] ids = Message.getMessageIDs();
        assertEquals(5, hashes.length);
        assertEquals(5, ids.length);
        assertEquals("01:0:DIDCAKE", hashes[0]);
        assertEquals("02:1:WHERETIME", hashes[1]);
        assertEquals("08:3:ITTIME", hashes[3]);
        assertEquals("0838884567", ids[3]);
    }

    /**
     * Display the longest message.
     */
    @Test
    public void testDisplayLongestMessage() {
        System.out.println("getLongestStoredMessage");
        assertEquals(MESSAGE_2, Message.getLongestStoredMessage());
    }

    /**
     * Search for a message ID (Test Data Message 4).
     */
    @Test
    public void testSearchMessageID() {
        System.out.println("searchMessageID");
        assertEquals("It is dinner time!", Message.searchMessageID("0838884567"));
    }

    @Test
    public void testSearchMessageIDNotFound() {
        System.out.println("searchMessageID");
        assertEquals("Message ID not found.", Message.searchMessageID("9999999999"));
    }

    /**
     * Search all the messages sent or stored for a particular recipient.
     */
    @Test
    public void testSearchByRecipient() {
        System.out.println("searchByRecipient");
        String[] result = Message.searchByRecipient("+27838884567");
        assertEquals(2, result.length);
        assertEquals(MESSAGE_2, result[0]);
        assertEquals("Ok, I am leaving without you.", result[1]);
    }

    /**
     * Delete a message using a message hash (Test Data Message 2).
     */
    @Test
    public void testDeleteMessageByHash() throws IOException {
        System.out.println("deleteMessageByHash");
        String expResult = "Message: \"" + MESSAGE_2 + "\" successfully deleted.";
        String result = Message.deleteMessageByHash("02:1:WHERETIME");
        assertEquals(expResult, result);

        // gone from the Stored Messages array, the hash/ID arrays and the JSON file
        String[] stored = Message.getStoredMessageTexts();
        assertEquals(1, stored.length);
        assertEquals("Ok, I am leaving without you.", stored[0]);
        assertEquals(4, Message.getMessageHashes().length);
        assertEquals(4, Message.getMessageIDs().length);
        assertFalse(Files.readString(Paths.get(TEST_FILE)).contains("Where are you?"));
        assertEquals(1, Message.loadStoredMessages());
    }

    @Test
    public void testDeleteMessageByHashNotFound() {
        System.out.println("deleteMessageByHash");
        assertEquals("Message hash not found.", Message.deleteMessageByHash("99:9:NOPE"));
    }

    /**
     * Display Report - shows the sent messages with Message Hash, Recipient, Message.
     */
    @Test
    public void testDisplayReport() {
        System.out.println("displayReport");
        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash: 01:0:DIDCAKE"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message Hash: 08:3:ITTIME"));
        assertTrue(report.contains("Recipient: 0838884567"));
        assertTrue(report.contains("Message: It is dinner time!"));
        assertFalse(report.contains("Where are you?"));
    }

    @Test
    public void testDisplayStoredReport() {
        System.out.println("displayStoredReport");
        String report = Message.displayStoredReport();
        assertTrue(report.contains("Message ID: 0287654321"));
        assertTrue(report.contains("Message Hash: 02:1:WHERETIME"));
        assertTrue(report.contains("Sender: kyl_1"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: " + MESSAGE_2));
        assertTrue(report.contains("Message Hash: 05:4:OKYOU"));
        assertFalse(report.contains("Did you get the cake?"));
    }

    @Test
    public void testDisplayStoredSenderRecipient() {
        System.out.println("displayStoredSenderRecipient");
        String result = Message.displayStoredSenderRecipient();
        assertEquals("Sender: kyl_1 | Recipient: +27838884567\nSender: kyl_1 | Recipient: +27838884567\n", result);
    }
}

//End of PART 3
