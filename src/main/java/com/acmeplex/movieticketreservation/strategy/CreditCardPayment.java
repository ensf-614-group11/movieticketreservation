package com.acmeplex.movieticketreservation.strategy;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.controller.SendPaymentToThirdParty;
import com.acmeplex.movieticketreservation.dto.request.*;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.CreditCard;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.CreditCardRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("creditCardPayment")
public class CreditCardPayment implements PaymentStrategy {

    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final RetrieveCreditCardPaymentType retrieveCreditCardPaymentType;

    private final SendPaymentToThirdParty sendPaymentToThirdParty;

    public CreditCardPayment(UserRepository userRepository,
                             CreditCardRepository creditCardRepository,
                             PaymentTypeService paymentTypeService,
                             RetrieveCreditCardPaymentType retrieveCreditCardPaymentType,
                             SendPaymentToThirdParty sendPaymentToThirdParty
    ) {
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
        this.retrieveCreditCardPaymentType = retrieveCreditCardPaymentType;
        this.sendPaymentToThirdParty = sendPaymentToThirdParty;
    }

    @Override
    public boolean unregisteredUserPay(UnregisteredUserPaymentRequest paymentRequest){
        String cardNumber = paymentRequest.getCreditCardNumber();
        String cardHolderName = paymentRequest.getFirstName() + " " + paymentRequest.getLastName();
        float amount = paymentRequest.getAmount();
        String billingAddress = paymentRequest.getStreet() +
                " " +
                paymentRequest.getCity() +
                " " +
                paymentRequest.getProvince() +
                " " +
                paymentRequest.getCountry() +
                " " +
                paymentRequest.getPostalCode();
        String cvv = paymentRequest.getCvv();
        String countryCode = paymentRequest.getCountryCode();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName +
                "\nBilling Address: " + billingAddress +
                "\nCVV: " + cvv +
                "\nCountry Code: " + countryCode);

        /* Sending payment information to 3rd party API.
        This has been commented out because we are not actually calling anything.
        But the code is here to show how we would do it. */

        /*
        ThirdPartyPaymentRequest request = new ThirdPartyPaymentRequest();
        request.setFirstName(paymentRequest.getFirstName());
        request.setLastName(paymentRequest.getLastName());
        request.setCreditCardNumber(cardNumber);
        request.setStreet(paymentRequest.getStreet());
        request.setCity(paymentRequest.getCity());
        request.setProvince(paymentRequest.getProvince());
        request.setCountry(paymentRequest.getCountry());
        request.setPostalCode(paymentRequest.getPostalCode());
        request.setCountryCode(paymentRequest.getCountryCode());
        request.setExpiryDate(paymentRequest.getExpiryDate());
        request.setCvv(paymentRequest.getCvv());

        ResponseEntity<ThirdPartyPaymentResponse> response = sendPaymentToThirdParty.processThirdPartyPayment(request);
         */

        return true;
    }

    public boolean registeredUserPay(RegisteredUserPaymentRequest paymentRequest){
        AcmePlexUser user = userRepository.findById(paymentRequest.getUserId()).get();

        List<PaymentType> paymentTypes = user.getPaymentTypes();

        CreditCard creditCard = null;
        for(PaymentType paymentType : paymentTypes){
            if(paymentType.getType().equals("CREDIT_CARD")){
                creditCard = retrieveCreditCardPaymentType.retrieve(user.getId());
            }
        }

        String cardNumber = creditCard.getCardNumber();
        String cardHolderName = user.getFirstName() + " " + user.getLastName();
        float amount = paymentRequest.getAmount();
        Address billingAddress = user.getSavedBillingAddress();
        if(billingAddress == null){
            throw new IllegalArgumentException("No billing address found for user.");
        }

        String cvv = paymentRequest.getCvv();
        String countryCode = user.getCountryCode().name();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName +
                "\nBilling Address: " + billingAddress +
                "\nCVV: " + cvv +
                "\nCountry Code: " + countryCode);

        /* Sending payment information to 3rd party API.
        This has been commented out because we are not actually calling an API to the payment provider.
        But the code is here to show how we would do it. */

        /*
        ThirdPartyPaymentRequest request = new ThirdPartyPaymentRequest();
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setCreditCardNumber(cardNumber);
        request.setStreet(billingAddress.getStreet());
        request.setCity(billingAddress.getCity());
        request.setProvince(billingAddress.getProvince());
        request.setCountry(billingAddress.getCountry());
        request.setPostalCode(billingAddress.getPostalCode());
        request.setCountryCode(user.getCountryCode().name());
        request.setExpiryDate(creditCard.getExpiryDate());
        request.setCvv(paymentRequest.getCvv());

        ResponseEntity<ThirdPartyPaymentResponse> response = sendPaymentToThirdParty.processThirdPartyPayment(request);
        */

        return true;
    }

    public boolean registeredUserPay(Long userId, float amount){
        AcmePlexUser user = userRepository.findById(userId).get();

        List<PaymentType> paymentTypes = user.getPaymentTypes();

        CreditCard creditCard = null;
        for(PaymentType paymentType : paymentTypes){
            if(paymentType.getType().equals("CREDIT_CARD")){
                creditCard = retrieveCreditCardPaymentType.retrieve(user.getId());
            }
        }

        Address billingAddress = user.getSavedBillingAddress();
        if(billingAddress == null){
            throw new IllegalArgumentException("No billing address found for user.");
        }

        String cardNumber = creditCard.getCardNumber();
        String cardHolderName = user.getFirstName() + " " + user.getLastName();
        String countryCode = user.getCountryCode().name();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName +
                "\nBilling Address: " + billingAddress +
                "\nCountry Code: " + countryCode);

        /* Sending payment information to 3rd party API.
        This has been commented out because we are not actually calling an API to the payment provider.
        But the code is here to show how we would do it. */

        /*
        ThirdPartyPaymentRequest request = new ThirdPartyPaymentRequest();
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setCreditCardNumber(cardNumber);
        request.setStreet(billingAddress.getStreet());
        request.setCity(billingAddress.getCity());
        request.setProvince(billingAddress.getProvince());
        request.setCountry(billingAddress.getCountry());
        request.setPostalCode(billingAddress.getPostalCode());
        request.setCountryCode(user.getCountryCode().name());
        request.setExpiryDate(creditCard.getExpiryDate());
        request.setCvv(paymentRequest.getCvv());

        ResponseEntity<ThirdPartyPaymentResponse> response = sendPaymentToThirdParty.processThirdPartyPayment(request);
        */

        return true;
    }

    @Override
    public boolean newAcmePlexMembershipPay(
            PaymentTypeRequest paymentTypeRequest,
            BillingAddressRequest billingAddressRequest) {

        // Validate input details from CreatePaymentTypeRequest
        String cardNumber = paymentTypeRequest.getCardNumber();

        float amount = Constants.Pricing.MEMBERSHIP_PRICE;

        // Simulate payment processing
        System.out.println("Processing annual membership payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nBilling Address: " +
                "\nStreet: " + billingAddressRequest.getStreet() +
                "\nCity: " + billingAddressRequest.getCity() +
                "\nRegion: " + billingAddressRequest.getRegion() +
                "\nPostal Code: " + billingAddressRequest.getPostalCode() +
                "\nCountry: " + billingAddressRequest.getCountry());

        // Assume payment is successful and return true
        return true;
    }
}

