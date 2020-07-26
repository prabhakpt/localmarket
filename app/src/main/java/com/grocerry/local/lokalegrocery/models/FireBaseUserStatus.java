package com.grocerry.local.lokalegrocery.models;

/**
 * Created by Prabhakar.K on 7/24/2020.
 */

public class FireBaseUserStatus {
    Boolean isUserExist;
    CustomerInformation customerInformation;

    public Boolean getUserExist() {
        return isUserExist;
    }

    public void setUserExist(Boolean userExist) {
        isUserExist = userExist;
    }

    public CustomerInformation getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(CustomerInformation customerInformation) {
        this.customerInformation = customerInformation;
    }




}
