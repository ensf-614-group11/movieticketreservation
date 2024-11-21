package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.controller.PaymentStrategy;
import com.acmeplex.movieticketreservation.dto.request.PaymentRequest;
import com.acmeplex.movieticketreservation.dto.response.PaymentResponse;
import com.acmeplex.movieticketreservation.factory.PaymentStrategyFactory;
import com.acmeplex.movieticketreservation.model.*;
import com.acmeplex.movieticketreservation.repository.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final ReceiptRepository receiptRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    public PaymentService(
            PaymentStrategyFactory paymentStrategyFactory,
            PaymentRepository paymentRepository,
            TicketRepository ticketRepository,
            ReceiptRepository receiptRepository,
            SeatRepository seatRepository,
            ShowtimeRepository showtimeRepository,
            UserRepository userRepository
    ) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.receiptRepository = receiptRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentRequest);
        if (strategy == null) {
            return new PaymentResponse(false, "Invalid payment method provided.", null);
        }

        boolean paymentSuccess = strategy.pay(paymentRequest);
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

        // Fetch the Seat and Showtime (modify as per your logic)
        Seat seat = seatRepository.findById(paymentRequest.getSeatId());
        Showtime showtime = showtimeRepository.findById(paymentRequest.getShowtimeId());

        // Create and save the Ticket entity
        Ticket ticket = new Ticket("confirmed", new Date(), showtime, seat, payment.getAcmePlexUser());
        ticket.setPayment(payment);
        ticketRepository.save(ticket);

        // Send Ticket ADD CODE HERE

        // Link Ticket back to Payment
        payment.setTicket(ticket);
        paymentRepository.save(payment);

        // Create and save the Receipt entity
        Receipt receipt = new Receipt(payment);
        receiptRepository.save(receipt);

        // Receipt ADD CODE HERE

        // Return the PaymentResponse
        return new PaymentResponse(
                true,
                "Payment processed successfully. Ticket and receipt generated.",
                payment.getId()
        );
    }
}
