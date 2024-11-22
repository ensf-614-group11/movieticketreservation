package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.PaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.response.PaymentResponse;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class MakePayment {

    private final PaymentService paymentService;
    private final AuthUserPrincipalService authUserPrincipalService;

    public MakePayment(PaymentService paymentService, AuthUserPrincipalService authUserPrincipalService) {
        this.paymentService = paymentService;
        this.authUserPrincipalService = authUserPrincipalService;
    }

    @PostMapping("/unregistered")
    public ResponseEntity<PaymentResponse> processUnRegisteredPayment(@RequestBody @Valid UnregisteredUserPaymentRequest request) {
        PaymentResponse response = paymentService.processUnregisteredPayment(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/registered")
    public ResponseEntity<PaymentResponse> processRegisteredPayment(@RequestBody @Valid RegisteredUserPaymentRequest request) {
        PaymentResponse response = paymentService.processRegisteredPayment(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
}

