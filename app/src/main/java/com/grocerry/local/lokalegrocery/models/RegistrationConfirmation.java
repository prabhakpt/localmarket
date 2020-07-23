package com.grocerry.local.lokalegrocery.models;

/**
 * Created by Prabhakar.K on 7/20/2020.
 */

public class RegistrationConfirmation {
    String customerName;
    String customerAddress;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
