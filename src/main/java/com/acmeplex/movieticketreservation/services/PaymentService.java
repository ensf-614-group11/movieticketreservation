package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.BillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.services.exceptions.PaymentFailedException;
import com.acmeplex.movieticketreservation.services.strategy.PaymentStrategy;
import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.response.PaymentResponse;
import com.acmeplex.movieticketreservation.factory.PaymentStrategyFactory;
import com.acmeplex.movieticketreservation.model.*;
import com.acmeplex.movieticketreservation.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final SeatAvailabilityRepository seatAvailabilityRepository;

    public PaymentService(
            PaymentStrategyFactory paymentStrategyFactory,
            PaymentRepository paymentRepository,
            TicketRepository ticketRepository,
            ReceiptRepository receiptRepository,
            SeatRepository seatRepository,
            ShowtimeRepository showtimeRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            SeatAvailabilityRepository seatAvailabilityRepository
    ) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.receiptRepository = receiptRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.seatAvailabilityRepository = seatAvailabilityRepository;
    }

    /**
     * Saves a payment to the repository.
     *
     * @param payment the Payment entity to save
     * @return the saved Payment entity
     */
    public Payment savePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null.");
        }
        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment;
    }

    public PaymentResponse processUnregisteredPayment(UnregisteredUserPaymentRequest paymentRequest) {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentRequest);
        if (strategy == null) {
            return new PaymentResponse(false, "Invalid payment method provided.", null);
        }

        // Create and save the Payment entity
        Payment payment = new Payment(
                paymentRequest.getAmount(),
                new Date(),
                "pending",
                "credit card",
                null// Ticket will be linked later
        );
        paymentRepository.save(payment);

        boolean paymentSuccess = strategy.unregisteredUserPay(paymentRequest);
        if (!paymentSuccess) {
            // Set payment status to failed on a failed response.
            payment.setStatus("failed");
            paymentRepository.save(payment);
            return new PaymentResponse(false, "Payment processing failed.", null);
        }

        // Set payment status to completed on a successful response.
        payment.setStatus("completed");
        paymentRepository.save(payment);


        // Fetch the Seat and Showtime
        Seat seat = seatRepository.findById(paymentRequest.getSeatId());
        if(seat == null){
            return new PaymentResponse(false, "Payment processing failed. Invalid seat.", null);
        }

        Showtime showtime = showtimeRepository.findById(paymentRequest.getShowtimeId());
        if(showtime == null){
            return new PaymentResponse(false, "Payment processing failed. Invalid showtime.", null);
        }

        // Mark Seat Availability as Not Available
        Optional<SeatAvailability> seatAvailabilityOptional = seatAvailabilityRepository.findBySeatIdAndShowtimeId(paymentRequest.getSeatId(), paymentRequest.getShowtimeId());
        if(seatAvailabilityOptional.isEmpty()){
            throw new IllegalArgumentException("Invalid seat provided.");
        }
        SeatAvailability seatAvailability = seatAvailabilityOptional.get();

        // Create and save the Ticket entity
        Ticket ticket = new Ticket("confirmed", new Date(), showtime, seat, null);
        ticket.setPayment(payment);
        ticketRepository.save(ticket);

        // Link Ticket back to Payment
        payment.setTicket(ticket);
        paymentRepository.save(payment);

        // Create and save the Receipt entity
        Receipt receipt = new Receipt(payment);

        // Send Ticket and Receipt via email
        notificationService.sendTicketAndReceiptOrdinaryUser(ticket, receipt, paymentRequest.getEmail());

        // Set seat availability to unavailable
        seatAvailability.setAvailable(false);
        seatAvailabilityRepository.save(seatAvailability);

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

        // Mark Seat Availability as Not Available
        Optional<SeatAvailability> seatAvailabilityOptional = seatAvailabilityRepository.findBySeatIdAndShowtimeId(paymentRequest.getSeatId(), paymentRequest.getShowtimeId());
        if(seatAvailabilityOptional.isEmpty()){
            throw new IllegalArgumentException("Invalid seat provided.");
        }
        SeatAvailability seatAvailability = seatAvailabilityOptional.get();

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

        seatAvailability.setAvailable(false);
        seatAvailabilityRepository.save(seatAvailability);

        // Return the PaymentResponse
        return new PaymentResponse(
                true,
                "Payment processed successfully. Ticket and receipt generated.",
                payment.getId()
        );
    }

    public Payment processAcmePlexMembershipPayment(PaymentTypeRequest paymentTypeRequest,
                                                    BillingAddressRequest billingAddressRequest) {
        // Step 1: Retrieve the payment strategy using PaymentStrategyFactory
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentTypeRequest, billingAddressRequest);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment method provided.");
        }

        // Step 2: Call the strategy's newAcmePlexMembershipPay method
        boolean paymentSuccess = strategy.newAcmePlexMembershipPay(paymentTypeRequest, billingAddressRequest);
        if (!paymentSuccess) {
            throw new PaymentFailedException("Payment processing failed for card: " + paymentTypeRequest.getCardNumber());
        }

        // Step 3: Create and save the Payment entity (associate later with user)
        Payment payment = new Payment(
                Constants.Pricing.MEMBERSHIP_PRICE, // Use constant price
                new Date(),
                "completed",
                paymentTypeRequest.getType(),
                null, // Not associated with a ticket
                null  // User will be associated after registration
        );
        paymentRepository.save(payment);

        // Step 4: Create and save the Receipt entity
        Receipt receipt = new Receipt(payment);
        receiptRepository.save(receipt);

        return payment;
    }

    public Payment processAcmePlexMembershipRenewal(String renewalToken) {
        // Step 2: Call the strategy's newAcmePlexMembershipPay method
        boolean paymentSuccess = true;
        if (!paymentSuccess) {
            throw new PaymentFailedException("Renewal payment failed");
        }

        // Step 3: Create and save the Payment entity (associate later with user)
        Payment payment = new Payment(
                Constants.Pricing.MEMBERSHIP_PRICE, // Use constant price
                new Date(),
                "completed",
                "RENEWAL",
                null, // Not associated with a ticket
                null  // User will be associated after registration
        );
        paymentRepository.save(payment);

        // Step 4: Create and save the Receipt entity
        Receipt receipt = new Receipt(payment);
        receiptRepository.save(receipt);

        return payment;
    }
}
