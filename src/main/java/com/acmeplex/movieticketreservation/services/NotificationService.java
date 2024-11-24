package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.*;
import com.acmeplex.movieticketreservation.observer.Observer;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {
    private List<Observer> observers = new ArrayList<>();
    EmailService emailService = new EmailService();

public void sendTicketAndReceiptOrdinaryUser(Ticket ticket, Receipt receipt, String email){
    Date currentDate = new Date();
    String message = createTicketAndReceiptMessage(ticket, receipt, null);
    Notification notification = new Notification(currentDate, message, email);
    try {emailService.sendSimpleMessage(email, "AcmePlex Ticket and Receipt Confirmation", message);
    } catch (Exception e) {
        System.out.println("Error sending email to: " + email + "\nError Message: " + e.getMessage());
    }

}

public void sendTicketAndReceiptRegisteredUser(Ticket ticket, Receipt receipt){
    Date currentDate = new Date();
    String message = createTicketAndReceiptMessage(ticket, receipt, ticket.getUser().getFirstName());
    Notification notification = new Notification(currentDate, message, ticket.getUser().getEmail());
   try {emailService.sendSimpleMessage(ticket.getUser().getEmail(), "AcmePlex Ticket and Receipt Confirmation", message);
   } catch (Exception e) {
       System.out.println("Error sending email to: " + ticket.getUser().getEmail() + "\nError Message: " + e.getMessage());
   }
    ticket.getUser().addNotification(notification);
}

public void sendTicketCancellationOrdinaryUser(AcmePlexCredit acmePlexCredit, String email){
    Date currentDate = new Date();
    String message = createTicketCancellationMessageForOrdinaryUser(acmePlexCredit);
    Notification notification = new Notification(currentDate, message, email);

    try {
        emailService.sendSimpleMessage(email, "AcmePlex Ticket Cancellation Confirmation", message);
    } catch (Exception e) {
        System.out.println("Error sending email to: " + email + "\nError Message: " + e.getMessage());
    }
}


public void sendTicketCancellationRegisteredUser(AcmePlexCredit acmePlexCredit, AcmePlexUser acmePlexUser){
    Date currentDate = new Date();
    String message = createTicketCancellationMessageForRegisteredUser(acmePlexCredit, acmePlexUser);
    Notification notification = new Notification(currentDate, message, acmePlexUser.getEmail());

    try {
        emailService.sendSimpleMessage(acmePlexUser.getEmail(), "AcmePlex Ticket Cancellation Confirmation", message);
    } catch (Exception e) {
        System.out.println("Error sending email to: " + acmePlexUser.getEmail() + "\nError Message: " + e.getMessage());
    }
   acmePlexUser.addNotification(notification);
}


public void sendAnnualFeeReminder(AcmePlexUser acmePlexUser){
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(currentDate);  // Set the calendar to the current date
    calendar.add(Calendar.DAY_OF_MONTH, 7); // Add 7 days to the current date
    Date paymentDueDate = calendar.getTime();

    String message = "Hello " + acmePlexUser.getFirstName() +
            ", \nThe due date for your annual AcmePlex fee is approaching in 7 days. " +
            "The payment card we have on file will be charged $20.00 on " + paymentDueDate + " .";
    Notification notification = new Notification(currentDate, message, acmePlexUser.getEmail());
    try {
        emailService.sendSimpleMessage(acmePlexUser.getEmail(), "AcmePlex Annual Fee Reminder", message);
    } catch (Exception e) {
        System.out.println("Error sending email to: " + acmePlexUser.getEmail() + "\nError Message: " + e.getMessage());
    }
    acmePlexUser.addNotification(notification);
    }

    public void sendAnnualFeePaymentConfirmation(AcmePlexUser acmePlexUser){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 1);
        Date nextPaymentDueDate = calendar.getTime();

        String message = "Hello " + acmePlexUser.getFirstName() +
                ", \nThis message is to provide confirmation of the payment for your annual AcmePlex fee. " +
                "The payment was processed successfully." +
                "Your next annual fee payment will be due on: " + nextPaymentDueDate +
                ". ";
        Notification notification = new Notification(currentDate, message, acmePlexUser.getEmail());
        try {
            emailService.sendSimpleMessage(acmePlexUser.getEmail(), "AcmePlex Annual Fee Confirmation", message);
        } catch (Exception e) {
            System.out.println("Error sending email to: " + acmePlexUser.getEmail() + "\nError Message: " + e.getMessage());
        }
        acmePlexUser.addNotification(notification);
    }


    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

public void sendMovieNews(String movieNews){
    // Create a message for the movie news
    String message = "AcmePlex Movie News: " + movieNews;

    // Notify all observers (registered AcmePlexUsers)
    notifyObservers(message);
}


// Helper method to create Ticket and Receipt notification message
    private String createTicketAndReceiptMessage(Ticket ticket, Receipt receipt, String firstName) {
        return "Hello " + (firstName != null ? firstName : "AcmePlex Customer") + ", \nThis message confirms your payment and ticket reservation for AcmePlex Theatres." +
                " \nThe details of your ticket and receipt are included below. Enjoy the movie!" +
                "\n\nTicket \nMovie: " + ticket.getShowtime().getMovie() +
                "\nShowtime: " + ticket.getShowtime() +
                "\nSeat: " + ticket.getSeat() +
                "\nStatus: " + ticket.getStatus() +
                "\nTicket id: " + ticket.getId() +
                "\n\nReceipt \nDate Paid: " + receipt.getPayment().getDatePaid() +
                "\nAmount: " + receipt.getPayment().getAmount() +
                "\nMethod: " + receipt.getPayment().getMethod() +
                "\nStatus: " + receipt.getPayment().getStatus();
    }

    // Helper method for creating cancellation confirmation message for ordinary users
    private String createTicketCancellationMessageForOrdinaryUser(AcmePlexCredit acmePlexCredit) {
        return "Hello AcmePlex Customer, \nThis message confirms your ticket cancellation for AcmePlex Theatres." +
                " You have been awarded an AcmePlex Credit for future purchase. The credit is valid for 1 year. " +
                "A 15% administration fee has been applied to process this credit. The details of your credit are included below. " +
                "Next time you make a movie ticket purchase, you can use the Credit Secret Code given below. " +
                "Hope to see you again soon!" +
                "\n\nAcmePlex Credit \nAmount: " + acmePlexCredit.getAmount() +
                "\nCredit Secret Code: " + acmePlexCredit.getCreditSecretCode() +
                "\nExpiration Date: " + acmePlexCredit.getExpirationDate();
    }

    // Helper method for creating cancellation confirmation message for registered users
    private String createTicketCancellationMessageForRegisteredUser(AcmePlexCredit acmePlexCredit, AcmePlexUser acmePlexUser) {
        return "Hello " + acmePlexUser.getFirstName() + ",\nThis message confirms your ticket cancellation for AcmePlex Theatres." +
                " You have been awarded an AcmePlex Credit for future purchase. The credit is valid for 1 year. " +
                "The credit has been applied to your account. Details of the credit are also available below." +
                "Next time you make a movie ticket purchase, you will be given the option to apply this credit. " +
                "Hope to see you again soon!" +
                "\n\nAcmePlex Credit \nAmount: " + acmePlexCredit.getAmount() +
                "\nCredit Secret Code: " + acmePlexCredit.getCreditSecretCode() +
                "\nExpiration Date: " + acmePlexCredit.getExpirationDate() +
                "\n\nCurrent AcmePlex Credit Balance: " + acmePlexUser.getTotalCredits();
    }


}

