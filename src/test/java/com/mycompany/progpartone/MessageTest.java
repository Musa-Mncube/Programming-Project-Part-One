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

/**
 * Unit tests for the Message class, using the Part 2 test data.
 *
 * @author Student
 */
public class MessageTest {

    private static final String TEST_FILE = "target/test-messages.json";

    // Test Data for Task 1 and Message 2
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";

    public MessageTest() {
    }

    // keep the tests away from the real messages.json
    @BeforeEach
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("target"));
        Files.deleteIfExists(Paths.get(TEST_FILE));
        Message.setStorageFile(TEST_FILE);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
        Message.setStorageFile("messages.json");
    }

    /**
     * The message should not be more than 250 characters - success.
     */
    @Test
    public void testCheckMessageLengthSuccess() {
        System.out.println("checkMessageLength");
        String expResult = "Message ready to send.";
        String result = Message.checkMessageLength(MESSAGE_1);
        assertEquals(expResult, result);
    }

    /**
     * The message should not be more than 250 characters - failure.
     */
    @Test
    public void testCheckMessageLengthFailure() {
        System.out.println("checkMessageLength");
        String longMessage = "a".repeat(260); // 10 characters over the limit
        String expResult = "Message exceeds 250 characters by 10; please reduce the size.";
        String result = Message.checkMessageLength(longMessage);
        assertEquals(expResult, result);
    }

    /**
     * The recipient number is correctly formatted - success (+27718693002).
     */
    @Test
    public void testCheckRecipientCellSuccess() {
        System.out.println("checkRecipientCell");
        String expResult = "Cell phone number successfully captured.";
        String result = Message.checkRecipientCell(RECIPIENT_1);
        assertEquals(expResult, result);
    }

    /**
     * The recipient number is correctly formatted - failure (08575975889 has no international code).
     */
    @Test
    public void testCheckRecipientCellFailure() {
        System.out.println("checkRecipientCell");
        String expResult = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        String result = Message.checkRecipientCell(RECIPIENT_2);
        assertEquals(expResult, result);
    }

    /**
     * Message ID is created - ten digits and passes checkMessageID().
     */
    @Test
    public void testMessageIDCreated() {
        System.out.println("checkMessageID");
        Message instance = new Message(RECIPIENT_1, MESSAGE_1);
        assertTrue(instance.checkMessageID());
        assertEquals(10, instance.getMessageID().length());
        assertTrue(instance.getMessageID().matches("\\d{10}"));
        assertEquals("Message ID generated: " + instance.getMessageID(), instance.getMessageIDStatus());
    }

    @Test
    public void testCheckMessageIDTooLong() {
        System.out.println("checkMessageID");
        Message instance = new Message("12345678901", 0, RECIPIENT_1, MESSAGE_1); // 11 characters
        assertFalse(instance.checkMessageID());
    }

    /**
     * Message hash is correct - Test Case 1 must give 00:0:HITONIGHT.
     */
    @Test
    public void testCreateMessageHashTestCase1() {
        System.out.println("createMessageHash");
        Message instance = new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1);
        String expResult = "00:0:HITONIGHT";
        String result = instance.createMessageHash();
        assertEquals(expResult, result);
    }

    /**
     * Message hash is correct - the remaining hashes are tested in a loop.
     */
    @Test
    public void testCreateMessageHashLoop() {
        System.out.println("createMessageHash loop");
        String[] ids = {"0012345678", "0198765432"};
        int[] numbers = {0, 1};
        String[] recipients = {RECIPIENT_1, RECIPIENT_2};
        String[] messages = {MESSAGE_1, MESSAGE_2};
        String[] expected = {"00:0:HITONIGHT", "01:1:HIPAYMENT"};

        for (int i = 0; i < messages.length; i++) {
            Message instance = new Message(ids[i], numbers[i], recipients[i], messages[i]);
            String result = instance.createMessageHash();
            assertEquals(expected[i], result);
        }
    }

    /**
     * MessageSent - user selected 'Send Message'.
     */
    @Test
    public void testSentMessageSend() {
        System.out.println("SentMessage send");
        Message instance = new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1);
        String expResult = "Message successfully sent.";
        String result = instance.SentMessage(1);
        assertEquals(expResult, result);
    }

    /**
     * MessageSent - user selected 'Disregard Message'.
     */
    @Test
    public void testSentMessageDisregard() {
        System.out.println("SentMessage disregard");
        Message instance = new Message("0198765432", 1, RECIPIENT_2, MESSAGE_2);
        String expResult = "Press 0 to delete the message.";
        String result = instance.SentMessage(2);
        assertEquals(expResult, result);
    }

    /**
     * MessageSent - user selected 'Store Message'.
     */
    @Test
    public void testSentMessageStore() {
        System.out.println("SentMessage store");
        Message instance = new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1);
        String expResult = "Message successfully stored.";
        String result = instance.SentMessage(3);
        assertEquals(expResult, result);
    }

    /**
     * returnTotalMessages goes up by one for every message sent, but not for disregarded ones.
     */
    @Test
    public void testReturnTotalMessages() {
        System.out.println("returnTotalMessages");
        int before = Message.returnTotalMessages();
        new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1).SentMessage(1);
        assertEquals(before + 1, Message.returnTotalMessages());
        new Message("0198765432", 1, RECIPIENT_2, MESSAGE_2).SentMessage(2);
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    /**
     * printMessages shows Message ID, Message Hash, Recipient, Message - in that order.
     */
    @Test
    public void testPrintMessagesOrder() {
        System.out.println("printMessages");
        Message instance = new Message("0055555555", 0, RECIPIENT_1, MESSAGE_1);
        instance.SentMessage(1);
        String result = Message.printMessages();
        int idPos = result.indexOf("Message ID: 0055555555");
        int hashPos = result.indexOf("Message Hash: 00:0:HITONIGHT", idPos);
        int recipientPos = result.indexOf("Recipient: " + RECIPIENT_1, hashPos);
        int messagePos = result.indexOf("Message: " + MESSAGE_1, recipientPos);
        assertTrue(idPos >= 0);
        assertTrue(hashPos > idPos);
        assertTrue(recipientPos > hashPos);
        assertTrue(messagePos > recipientPos);
    }

    /**
     * Stored messages are saved as one proper JSON array.
     */
    @Test
    public void testStoreMessageWritesJsonArray() throws IOException {
        System.out.println("storeMessage");
        new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1).SentMessage(3);
        new Message("0198765432", 1, RECIPIENT_2, "He said \"pay me\"").SentMessage(3);

        String json = Files.readString(Paths.get(TEST_FILE)).trim();
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
        assertEquals(1, json.split("\\],").length); // only one closing bracket group
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\"messageID\": \"0198765432\""));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(json.contains("He said \\\"pay me\\\""));
        assertTrue(json.contains("},\n  {")); // the two objects are separated by a comma
    }
}
