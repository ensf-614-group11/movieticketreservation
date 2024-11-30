package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.response.CreditValidationResponse;
import com.acmeplex.movieticketreservation.dto.request.CreditValidationRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import com.acmeplex.movieticketreservation.repository.AcmePlexCreditRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/validate-credit")
public class AcmePlexCreditValidation {

    AcmePlexCreditRepository creditRepository;

    public AcmePlexCreditValidation(AcmePlexCreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @PostMapping
    public ResponseEntity<CreditValidationResponse> validateCredit(@RequestBody @Valid CreditValidationRequest creditValidationRequest){
        boolean isValid = false;
        Optional<AcmePlexCredit> credit = creditRepository.findByCreditSecretCode(creditValidationRequest.getCreditSecretCode());
        if(credit.isEmpty()){
            return ResponseEntity.ok(new CreditValidationResponse(false, creditValidationRequest.getAmount()));
        }
        if(!credit.get().isActive()){
            return ResponseEntity.ok(new CreditValidationResponse(false, creditValidationRequest.getAmount()));
        }
        credit.get().setActive(false);
        float newAmount = creditValidationRequest.getAmount() - credit.get().getAmount();
        return ResponseEntity.ok(new CreditValidationResponse(true, newAmount));
    }

}
