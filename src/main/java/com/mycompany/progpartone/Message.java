/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progpartone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;


/**
 *
 * @author Musawenkosi Mncube ST10507845
 */
public class Message {
    
     public static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    public static final String RECIPIENT_FAILURE = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    public static final String MESSAGE_SUCCESS = "Message ready to send.";
    
    //keeps track of all messages sent while the program is running
   private static Message[] sentMessages = new Message[10];
   private static int sentCount = 0;
   
   //Start of PART 3
    private static Message[] disregardedMessages = new Message[10];
    private static int disregardedCount = 0;

    private static Message[] storedMessages = new Message[10];   // filled by reading messages.json
    private static int storedCount = 0;

    private static String[] messageHashes = new String[10];      // every message hash
    private static int hashCount = 0;

    private static String[] messageIDs = new String[10];         // every message ID
    private static int idCount = 0;

    private static String currentSender = "";                    // the logged-in user
    //End of PART 3 section
    
    private static String storageFile = "messages.json";
    
    private String messageID;
    private String sender; //part 3 who sent the message
    private String recipientNumber;
    private String message;
    private String messageHash;
    private int messageNumber; // the number of the message used in the hash
    
    public Message(String recipientNumber, String message){
        this.sender = currentSender;
        this.recipientNumber = recipientNumber;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageNumber = sentCount; //starts at 0
    }
    
    public Message(String messageID, int messageNumber, String recipientNumber, String message) {
        this.sender = currentSender;
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipientNumber = recipientNumber;
        this.message = message;
    }
    
    private String generateMessageID(){
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < 10; i++){
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
    
    public static void setStorageFile(String fileName){
        storageFile = fileName;
    }
     private static Message[] growIfFull(Message[] array, int count) {
        if (count < array.length) {
            return array;
        }
        Message[] bigger = new Message[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            bigger[i] = array[i];
        }
        return bigger;
    }
    //Boolean: checkMessageID()to ensure the message is not more than 10 characters.
    public boolean checkMessageID(){
        return messageID != null && messageID.length() <= 10;
    }
    
    //Message ID generated: <Message ID>
    public String getMessageIDStatus(){
        return "Message ID generated: " + messageID;
    }
    //String: checkRecipientCell() - the number must start with an international
    //code ("+" and 1-3 digits) followed by no more than 10 digits, so
    //+27718693002 is accepted and 08575975889 (no international code) is rejected.
    public static String checkRecipientCell(String recipientNumber) {
        if (recipientNumber != null && recipientNumber.matches("^\\+\\d{1,3}\\d{1,10}$")) {
            return RECIPIENT_SUCCESS;
        } else {
            return RECIPIENT_FAILURE;
        }
    }

    public static String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return MESSAGE_SUCCESS;
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    // String: createMessageHash() - first two digits of the message ID, the message
    // number, and the first and last words of the message, all in caps.
    public String createMessageHash() {
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0].toUpperCase().replaceAll("[^A-Z0-9]", "");
        String lastWord = words[words.length - 1].toUpperCase().replaceAll("[^A-Z0-9]", "");
        String idPrefix = messageID.substring(0, 2);

        messageHash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }

    // String: SentMessage() - lets the user choose to send, disregard or store the message
    public String SentMessage(int choice) {
        if (messageHash == null) {
            createMessageHash(); // the hash can never be "null" on screen or in the JSON
        }
        switch (choice) {
            case 1: // Send Message
                sentMessages = growIfFull(sentMessages, sentCount);
                sentMessages[sentCount] = this;
                sentCount++;
                recordHashAndID();      // PART 3: fill the Message Hash and Message ID arrays
                return "Message successfully sent.";
            case 2: // Disregard Message
                // PART 3: fill the Disregarded Messages array
                disregardedMessages = growIfFull(disregardedMessages, disregardedCount);
                disregardedMessages[disregardedCount] = this;
                disregardedCount++;
                recordHashAndID();
                return "Press 0 to delete the message.";
            case 3: // Store Message to send later
                String saved = storeMessage();
                if (saved.startsWith("Error")) {
                    return saved;
                }
                recordHashAndID();
                loadStoredMessages();   // PART 3: read the JSON file back into the Stored Messages array
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // String: printMessages() - returns all messages sent while the program is running
    public static String printMessages() {
        if (sentCount == 0) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentCount; i++) {
            Message m = sentMessages[i];
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
        return sentCount;
    }

    public String storeMessage() {
        if (messageHash == null) {
            createMessageHash();
        }
        String entry = toJsonEntry();
        try {
            Path path = Paths.get(storageFile);
            String existing = "";
            if (Files.exists(path)) {
                existing = Files.readString(path, StandardCharsets.UTF_8).trim();
            }
            int closingBracket = existing.lastIndexOf(']');
            String updated;
            if (existing.isEmpty() || closingBracket < 0 || existing.replaceAll("\\s", "").equals("[]")) {
                updated = "[\n" + entry + "\n]\n";
            } else {
                String before = existing.substring(0, closingBracket).stripTrailing();
                updated = before + ",\n" + entry + "\n]\n";
            }
            Files.writeString(path, updated, StandardCharsets.UTF_8);
            return "Message stored to " + storageFile + ".";
        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

    // one message written as a JSON object
    private String toJsonEntry() {
        return "  {\n"
                + "    \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"messageNumber\": " + messageNumber + ",\n"
                + "    \"sender\": \"" + escapeJson(sender) + "\",\n"
                + "    \"recipientNumber\": \"" + escapeJson(recipientNumber) + "\",\n"
                + "    \"message\": \"" + escapeJson(message) + "\"\n"
                + "  }";
    }

    // makes text safe to sit inside a JSON string (quotes, backslashes, new lines...)
    private static String escapeJson(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public String getMessageID() { return messageID; }
    public String getSender() { return sender; }
    public String getRecipientNumber() { return recipientNumber; }
    public String getMessage() { return message; }
    public String getMessageHash() {
        if (messageHash == null) {
            createMessageHash();
        }
        return messageHash;
    }
    public int getMessageNumber() { return messageNumber; }


    //#####################################################################
    //#####################################################################
    //##                                                                 ##
    //##                      PART 3 STARTS HERE                         ##
    //##       (arrays, Stored Messages menu, searches, delete, report)  ##
    //##                                                                 ##
    //#####################################################################
    //#####################################################################

    // the logged-in user, written into every new message as its sender
    public static void setSender(String senderName) {
        currentSender = senderName;
    }

    // empties every array - used by the unit tests so each test starts clean
    public static void resetAll() {
        sentMessages = new Message[10];
        sentCount = 0;
        disregardedMessages = new Message[10];
        disregardedCount = 0;
        storedMessages = new Message[10];
        storedCount = 0;
        messageHashes = new String[10];
        hashCount = 0;
        messageIDs = new String[10];
        idCount = 0;
    }

    // adds this message's hash and ID to the two arrays (once per message ID)
    private void recordHashAndID() {
        if (indexOf(messageIDs, idCount, messageID) >= 0) {
            return;
        }
        messageHashes = growIfFull(messageHashes, hashCount);
        messageHashes[hashCount] = messageHash;
        hashCount++;
        messageIDs = growIfFull(messageIDs, idCount);
        messageIDs[idCount] = messageID;
        idCount++;
    }

    private static int indexOf(String[] array, int count, String value) {
        for (int i = 0; i < count; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private static void removeStringAt(String[] array, int count, int index) {
        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }
        array[count - 1] = null;
    }

    private static void removeMessageAt(Message[] array, int count, int index) {
        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }
        array[count - 1] = null;
    }

    private static Message findByID(Message[] array, int count, String id) {
        for (int i = 0; i < count; i++) {
            if (array[i].messageID.equals(id)) {
                return array[i];
            }
        }
        return null;
    }

    private static int indexOfHash(Message[] array, int count, String hash) {
        for (int i = 0; i < count; i++) {
            if (array[i].getMessageHash().equals(hash)) {
                return i;
            }
        }
        return -1;
    }

    private static String[] textsOf(Message[] array, int count) {
        String[] texts = new String[count];
        for (int i = 0; i < count; i++) {
            texts[i] = array[i].message;
        }
        return texts;
    }

    private static String[] trim(String[] array, int count) {
        String[] exact = new String[count];
        for (int i = 0; i < count; i++) {
            exact[i] = array[i];
        }
        return exact;
    }

    // ---- the arrays, as exact-size String arrays ----
    public static String[] getSentMessageTexts() { return textsOf(sentMessages, sentCount); }
    public static String[] getDisregardedMessageTexts() { return textsOf(disregardedMessages, disregardedCount); }
    public static String[] getStoredMessageTexts() { return textsOf(storedMessages, storedCount); }
    public static String[] getMessageHashes() { return trim(messageHashes, hashCount); }
    public static String[] getMessageIDs() { return trim(messageIDs, idCount); }

    /*
     * loadStoredMessages() - reads messages.json back into the Stored Messages array.
     *
     * References / where this code came from:
     *  - The reader below follows the JSON grammar on json.org (RFC 8259): an array of
     *    objects whose values are strings or numbers. I wrote a small reader instead of
     *    adding a library (e.g. Gson) so the project has no extra dependencies.
     *  - Files.readString: Oracle Java SE API docs (java.nio.file.Files).
     *  - This code was written with assistance from Claude (Anthropic), 2026, then
     *    reviewed and tested by me. Declare this as required by the IIE AI policy.
     *
     * Returns how many stored messages were read, or -1 if the file could not be read.
     */
    public static int loadStoredMessages() {
        storedMessages = new Message[10];
        storedCount = 0;
        Path path = Paths.get(storageFile);
        if (!Files.exists(path)) {
            return 0;
        }
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            Message[] loaded = new JsonReader(json).readMessageArray();
            for (int i = 0; i < loaded.length; i++) {
                storedMessages = growIfFull(storedMessages, storedCount);
                storedMessages[storedCount] = loaded[i];
                storedCount++;
                loaded[i].recordHashAndID();   // also covers messages stored in an earlier run
            }
            return storedCount;
        } catch (IOException | IllegalArgumentException e) {
            storedMessages = new Message[10];
            storedCount = 0;
            return -1;
        }
    }

    // rewrites messages.json from the Stored Messages array (used after a delete)
    private static void writeStoredFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (storedCount == 0) {
            sb.append("[]\n");
        } else {
            sb.append("[\n");
            for (int i = 0; i < storedCount; i++) {
                sb.append(storedMessages[i].toJsonEntry());
                if (i < storedCount - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("]\n");
        }
        Files.writeString(Paths.get(storageFile), sb.toString(), StandardCharsets.UTF_8);
    }

    // 2a. Display the sender and recipient of all stored messages
    public static String displayStoredSenderRecipient() {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
            sb.append("Sender: ").append(storedMessages[i].sender)
              .append(" | Recipient: ").append(storedMessages[i].recipientNumber).append("\n");
        }
        return sb.toString();
    }

    // 2b. Display the longest stored message
    public static String getLongestStoredMessage() {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        String longest = storedMessages[0].message;
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].message.length() > longest.length()) {
                longest = storedMessages[i].message;
            }
        }
        return longest;
    }

    // 2c. Search for a message ID (looks through the sent, disregarded and stored messages)
    public static Message findMessageByID(String id) {
        if (indexOf(messageIDs, idCount, id) < 0) {
            return null;
        }
        Message found = findByID(sentMessages, sentCount, id);
        if (found == null) {
            found = findByID(disregardedMessages, disregardedCount, id);
        }
        if (found == null) {
            found = findByID(storedMessages, storedCount, id);
        }
        return found;
    }

    // returns just the message text for an ID (the menu also shows the recipient)
    public static String searchMessageID(String id) {
        Message found = findMessageByID(id);
        if (found == null) {
            return "Message ID not found.";
        }
        return found.message;
    }

    // 2d. All the messages sent or stored for a particular recipient
    public static String[] searchByRecipient(String recipientNumber) {
        String[] matches = new String[sentCount + storedCount];
        int n = 0;
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].recipientNumber.equals(recipientNumber)) {
                matches[n] = sentMessages[i].message;
                n++;
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].recipientNumber.equals(recipientNumber)) {
                matches[n] = storedMessages[i].message;
                n++;
            }
        }
        return trim(matches, n);
    }

    // 2e. Delete a message using its message hash
    public static String deleteMessageByHash(String hash) {
        if (indexOf(messageHashes, hashCount, hash) < 0) {
            return "Message hash not found.";
        }
        Message deleted = null;
        boolean wasStored = false;

        int i = indexOfHash(storedMessages, storedCount, hash);
        if (i >= 0) {
            deleted = storedMessages[i];
            removeMessageAt(storedMessages, storedCount, i);
            storedCount--;
            wasStored = true;
        } else {
            i = indexOfHash(sentMessages, sentCount, hash);
            if (i >= 0) {
                deleted = sentMessages[i];
                removeMessageAt(sentMessages, sentCount, i);
                sentCount--;
            } else {
                i = indexOfHash(disregardedMessages, disregardedCount, hash);
                if (i >= 0) {
                    deleted = disregardedMessages[i];
                    removeMessageAt(disregardedMessages, disregardedCount, i);
                    disregardedCount--;
                }
            }
        }
        if (deleted == null) {
            return "Message hash not found.";
        }

        // take its hash and ID out of the two arrays as well
        int idIndex = indexOf(messageIDs, idCount, deleted.messageID);
        if (idIndex >= 0) {
            removeStringAt(messageIDs, idCount, idIndex);
            idCount--;
            removeStringAt(messageHashes, hashCount, idIndex);
            hashCount--;
        }

        // keep messages.json in step with the array
        if (wasStored) {
            try {
                writeStoredFile();
            } catch (IOException e) {
                return "Message deleted, but messages.json could not be updated: " + e.getMessage();
            }
        }
        return "Message: \"" + deleted.message + "\" successfully deleted.";
    }

    // 2f. Report with the full details of all the stored messages
    public static String displayStoredReport() {
        return buildReport("Stored Messages Report", storedMessages, storedCount);
    }

    // report of all the sent messages (Message Hash, Recipient, Message and more)
    public static String displayReport() {
        return buildReport("Sent Messages Report", sentMessages, sentCount);
    }

    private static String buildReport(String title, Message[] list, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== ").append(title).append(" =====\n");
        if (count == 0) {
            sb.append("No messages.\n");
            return sb.toString();
        }
        for (int i = 0; i < count; i++) {
            Message m = list[i];
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Sender: ").append(m.sender).append("\n");
            sb.append("Recipient: ").append(m.recipientNumber).append("\n");
            sb.append("Message: ").append(m.message).append("\n");
            sb.append("------------------------------\n");
        }
        return sb.toString();
    }

    // Small JSON reader for messages.json: [ { "key": "text" or number, ... }, ... ]
    private static class JsonReader {
        private final String text;
        private int pos = 0;

        JsonReader(String text) {
            this.text = text;
        }

        Message[] readMessageArray() {
            Message[] result = new Message[10];
            int count = 0;
            skipWhitespace();
            expect('[');
            skipWhitespace();
            if (peek() == ']') {
                pos++;
                return new Message[0];
            }
            while (true) {
                result = growIfFull(result, count);
                result[count] = readMessageObject();
                count++;
                skipWhitespace();
                char c = next();
                if (c == ']') {
                    break;
                }
                if (c != ',') {
                    throw new IllegalArgumentException("Expected ',' or ']' at position " + (pos - 1));
                }
            }
            Message[] exact = new Message[count];
            for (int i = 0; i < count; i++) {
                exact[i] = result[i];
            }
            return exact;
        }

        private Message readMessageObject() {
            skipWhitespace();
            expect('{');
            String id = "";
            String hash = "";
            String sender = "";
            String recipient = "";
            String msg = "";
            int number = 0;
            skipWhitespace();
            if (peek() == '}') {
                pos++;
            } else {
                while (true) {
                    skipWhitespace();
                    String key = readString();
                    skipWhitespace();
                    expect(':');
                    skipWhitespace();
                    if (peek() == '"') {
                        String value = readString();
                        if (key.equals("messageID")) {
                            id = value;
                        } else if (key.equals("messageHash")) {
                            hash = value;
                        } else if (key.equals("sender")) {
                            sender = value;
                        } else if (key.equals("recipientNumber")) {
                            recipient = value;
                        } else if (key.equals("message")) {
                            msg = value;
                        }
                    } else {
                        String numberText = readNumber();
                        if (key.equals("messageNumber")) {
                            number = Integer.parseInt(numberText);
                        }
                    }
                    skipWhitespace();
                    char c = next();
                    if (c == '}') {
                        break;
                    }
                    if (c != ',') {
                        throw new IllegalArgumentException("Expected ',' or '}' at position " + (pos - 1));
                    }
                }
            }
            Message m = new Message(id, number, recipient, msg);
            m.sender = sender;
            if (!hash.isEmpty()) {
                m.messageHash = hash;
            }
            return m;
        }

        private String readString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                char c = next();
                if (c == '"') {
                    return sb.toString();
                }
                if (c != '\\') {
                    sb.append(c);
                    continue;
                }
                char escaped = next();
                switch (escaped) {
                    case '"':  sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/':  sb.append('/'); break;
                    case 'b':  sb.append('\b'); break;
                    case 'f':  sb.append('\f'); break;
                    case 'n':  sb.append('\n'); break;
                    case 'r':  sb.append('\r'); break;
                    case 't':  sb.append('\t'); break;
                    case 'u':
                        if (pos + 4 > text.length()) {
                            throw new IllegalArgumentException("Bad \\u escape");
                        }
                        sb.append((char) Integer.parseInt(text.substring(pos, pos + 4), 16));
                        pos += 4;
                        break;
                    default:
                        throw new IllegalArgumentException("Bad escape \\" + escaped);
                }
            }
        }

   private String readNumber() {
            int start = pos;
            while (pos < text.length() && "-+.eE0123456789".indexOf(text.charAt(pos)) >= 0) {
                pos++;
            }
            if (start == pos) {
                throw new IllegalArgumentException("Expected a value at position " + pos);
            }
            return text.substring(start, pos);
        }

        private void skipWhitespace() {
            while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) {
                pos++;
            }
        }

        private char peek() {
            if (pos >= text.length()) {
                throw new IllegalArgumentException("Unexpected end of file");
            }
            return text.charAt(pos);
        }

        private char next() {
            char c = peek();
            pos++;
            return c;
        }

        private void expect(char wanted) {
            char c = next();
            if (c != wanted) {
                throw new IllegalArgumentException("Expected '" + wanted + "' at position " + (pos - 1));
            }
        }
    }

    //End of PART 3
}
