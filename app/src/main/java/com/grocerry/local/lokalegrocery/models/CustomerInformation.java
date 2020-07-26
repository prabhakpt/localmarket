package com.grocerry.local.lokalegrocery.models;

/**
 * Created by Prabhakar.K on 7/22/2020.
 */

public class CustomerInformation {
    String phoneNumber;
    String customerName;
    String customerAddress;

    public CustomerInformation(){

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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

    public CustomerInformation(String phoneNumber,String customerName,String customerAddress){
        this.phoneNumber = phoneNumber;
        this.customerName=customerName;
        this.customerAddress=customerAddress;
    }
}
