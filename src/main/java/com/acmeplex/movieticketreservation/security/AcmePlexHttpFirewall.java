package com.acmeplex.movieticketreservation.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

public class AcmePlexHttpFirewall implements HttpFirewall {

    private final StrictHttpFirewall strictHttpFirewall;

    public AcmePlexHttpFirewall() {
        strictHttpFirewall = new StrictHttpFirewall();
        // Customize the strictHttpFirewall as needed
        strictHttpFirewall.setAllowUrlEncodedSlash(true); // Allow encoded slashes
        strictHttpFirewall.setAllowUrlEncodedPercent(true); // Allow encoded percent signs
    }

    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException {
        // Implement custom validation logic here, if needed
        return strictHttpFirewall.getFirewalledRequest(request);
    }

    @Override
    public HttpServletResponse getFirewalledResponse(HttpServletResponse response) {
        return response;
    }
}