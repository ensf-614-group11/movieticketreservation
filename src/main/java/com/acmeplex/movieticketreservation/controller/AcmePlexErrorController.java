package com.acmeplex.movieticketreservation.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AcmePlexErrorController implements ErrorController {

    @GetMapping("/error")
    @ResponseBody
    public String handleError() {
        return "An error occurred. Please try again or contact support.";
    }
}
