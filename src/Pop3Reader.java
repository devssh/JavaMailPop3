import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.search.FlagTerm;

public class Pop3Reader {

    public static void check(String host, String storeType, String user,
                             String password) {
        try {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, password);

            Folder[] folders = store.getDefaultFolder().list();

            for (Folder f : folders) {
                System.out.println("Folder: " + f.getName() + ", Messages: " + f.getMessageCount());
            }

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
//            Message[] messages = emailFolder.getMessages();
            Message[] messages = emailFolder.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));

            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + message.getMessageNumber());
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
//                System.out.println("Recipients: " + message.getAllRecipients());
                System.out.println("Sent on: " + message.getSentDate());
//                System.out.println("Text: " + message.getContent().toString());

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String host = "pop.gmail.com";// change accordingly
            String mailStoreType = "pop3";
            Scanner sc = new Scanner(new File("password.dat"));
            String username = sc.nextLine();// change accordingly
            String password = sc.nextLine();// change accordingly

            check(host, mailStoreType, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}