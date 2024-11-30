package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.ThirdPartyPaymentRequest;
import com.acmeplex.movieticketreservation.dto.response.ThirdPartyPaymentResponse;
import io.lettuce.core.dynamic.annotation.Value;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/send-payment")
public class SendPaymentToThirdParty {

    private final RestTemplate restTemplate;

    private String thirdPartyPaymentUrl;

    private String thirdPartyApiKey;

    public SendPaymentToThirdParty(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<ThirdPartyPaymentResponse> processThirdPartyPayment(@RequestBody @Valid ThirdPartyPaymentRequest request) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(thirdPartyApiKey);

            HttpEntity<ThirdPartyPaymentRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ThirdPartyPaymentResponse> response = restTemplate.exchange(
                    thirdPartyPaymentUrl,
                    HttpMethod.POST,
                    entity,
                    ThirdPartyPaymentResponse.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ThirdPartyPaymentResponse(
                            "failure",
                            "An error occured: " + e.getMessage()
                    )
            );
        }
    }
}
