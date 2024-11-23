package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.services.strategy.PaymentStrategy;
import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.response.PaymentResponse;
import com.acmeplex.movieticketreservation.factory.PaymentStrategyFactory;
import com.acmeplex.movieticketreservation.model.*;
import com.acmeplex.movieticketreservation.repository.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final ReceiptRepository receiptRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public PaymentService(
            PaymentStrategyFactory paymentStrategyFactory,
            PaymentRepository paymentRepository,
            TicketRepository ticketRepository,
            ReceiptRepository receiptRepository,
            SeatRepository seatRepository,
            ShowtimeRepository showtimeRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.receiptRepository = receiptRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    public PaymentResponse processUnregisteredPayment(UnregisteredUserPaymentRequest paymentRequest) {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentRequest);
        if (strategy == null) {
            return new PaymentResponse(false, "Invalid payment method provided.", null);
        }

        boolean paymentSuccess = strategy.unregisteredUserPay(paymentRequest);
        if (!paymentSuccess) {
            return new PaymentResponse(false, "Payment processing failed.", null);
        }

        // Create and save the Payment entity
        Payment payment = new Payment(
                paymentRequest.getAmount(),
                new Date(),
                "completed",
                "credit card",
                null// Ticket will be linked later)
        );
        paymentRepository.save(payment);

        // Fetch the Seat and Showtime
        Seat seat = seatRepository.findById(paymentRequest.getSeatId());
        if(seat == null){
            throw new IllegalArgumentException("Invalid seat provided.");
        }
        Showtime showtime = showtimeRepository.findById(paymentRequest.getShowtimeId());
        if(showtime == null){
            throw new IllegalArgumentException("Invalid seat provided.");
        }

        // Create and save the Ticket entity
        Ticket ticket = new Ticket("confirmed", new Date(), showtime, seat, null);
        ticket.setPayment(payment);
        ticketRepository.save(ticket);

        // Link Ticket back to Payment
        payment.setTicket(ticket);
        paymentRepository.save(payment);

        // Create and save the Receipt entity
        Receipt receipt = new Receipt(payment);
        receiptRepository.save(receipt);

        // Send Ticket and Receipt via email
        notificationService.sendTicketAndReceiptOrdinaryUser(ticket, receipt, paymentRequest.getEmail());

        // Return the PaymentResponse
        return new PaymentResponse(
                true,
                "Payment processed successfully. Ticket and receipt generated.",
                payment.getId()
        );
    }

    public PaymentResponse processRegisteredPayment(RegisteredUserPaymentRequest paymentRequest) {
        Optional<AcmePlexUser> user = userRepository.findById(paymentRequest.getUserId());

        // Validate User exists.
        if(user.isEmpty()){
            return new PaymentResponse(false, "User not found.", null);
        }

        // Validate payment strategy
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentRequest);
        if (strategy == null) {
            return new PaymentResponse(false, "Invalid payment method provided.", null);
        }

        boolean paymentSuccess = strategy.registeredUserPay(paymentRequest);
        if (!paymentSuccess) {
            return new PaymentResponse(false, "Payment processing failed.", null);
        }

        // Create and save the Payment entity
        Payment payment = new Payment(
                paymentRequest.getAmount(),
                new Date(),
                "completed",
                "credit card",
                null,// Ticket will be linked later
                user.get()
        );
        paymentRepository.save(payment);

        // Fetch the Seat and Showtime
        Seat seat = seatRepository.findById(paymentRequest.getSeatId());
        if(seat == null){
            throw new IllegalArgumentException("Invalid seat provided.");
        }
        Showtime showtime = showtimeRepository.findById(paymentRequest.getShowtimeId());
        if(showtime == null){
            throw new IllegalArgumentException("Invalid seat provided.");
        }

        // Create and save the Ticket entity
        Ticket ticket = new Ticket("confirmed", new Date(), showtime, seat, user.get());
        ticket.setPayment(payment);
        ticketRepository.save(ticket);

        // Link Ticket back to Payment
        payment.setTicket(ticket);
        paymentRepository.save(payment);

        // Create and save the Receipt entity
        Receipt receipt = new Receipt(payment);
        receiptRepository.save(receipt);

        // Send Ticket and Receipt via email
        notificationService.sendTicketAndReceiptRegisteredUser(ticket, receipt);

        // Return the PaymentResponse
        return new PaymentResponse(
                true,
                "Payment processed successfully. Ticket and receipt generated.",
                payment.getId()
        );
    }
}
