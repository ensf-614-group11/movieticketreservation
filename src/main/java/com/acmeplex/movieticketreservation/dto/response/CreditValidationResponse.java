package com.acmeplex.movieticketreservation.dto.response;

public class CreditValidationResponse {

    private boolean isValid;
    private float newAmount;

    public CreditValidationResponse(boolean isValid, float newAmount){
        this.isValid = isValid;
        this.newAmount = newAmount;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public float getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(float newAmount) {
        this.newAmount = newAmount;
    }
}
