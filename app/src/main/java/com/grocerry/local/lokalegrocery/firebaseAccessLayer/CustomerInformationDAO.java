package com.grocerry.local.lokalegrocery.firebaseAccessLayer;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocerry.local.lokalegrocery.models.CustomerInformation;
import com.grocerry.local.lokalegrocery.utilities.DateUtils;

/**
 * Created by Prabhakar.K on 7/21/2020.
 */

public class CustomerInformationDAO {

    private static final String TAG = "CustomerInformationDAO";
    DatabaseReference databaseReference;
    FirebaseDatabase mAuth;
    DateUtils dateUtils = new DateUtils();

    public CustomerInformationDAO(){
        mAuth = FirebaseDatabase.getInstance();
        databaseReference = mAuth.getReference();
    }


   /* public void createCustomerWithPhone(PhoneObject phoneObject){
        databaseReference.child("useInfo").child(phoneObject.getCustomerPhoneNumber()).setValue(phoneObject);
    }

    public void createCustomer(String mobileNumber,RegistrationConfirmation registrationConfirmation){
        databaseReference.child("userInfo").child(mobileNumber).setValue(registrationConfirmation);
    }*/

    public void createNewCustomer(String mobileNumber,CustomerInformation customerInformation){
        databaseReference.child("userInfo").child(mobileNumber).setValue(customerInformation);
    }

    public CustomerInformation getCustomerInformation(final String mobileNumber){
        final Boolean[] isUserRegistered = {false};
        final CustomerInformation customerInformation = new CustomerInformation();

        //DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        /*DatabaseReference userNameRef = databaseReference.child("userInfo");
        Query queries=userNameRef.orderByChild("phoneNumber").equalTo(mobileNumber);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //create new user
                if(!dataSnapshot.exists()) {
                    isUserRegistered[0] = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        queries.addListenerForSingleValueEvent(eventListener);*/
        mAuth.getReference("custInfo").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String phoneNumber = postSnapshot.child("phoneNumber").getValue().toString();
                    String customerName = postSnapshot.child("customerName").getValue().toString();
                    String customerAddress = postSnapshot.child("customerAddress").getValue().toString();

                    Log.e(TAG, "======="+phoneNumber);
                    Log.e(TAG, "======="+customerName);
                    Log.e(TAG,"customerAddress"+customerAddress);
                    if(mobileNumber != null && mobileNumber.equals(phoneNumber)){
                        isUserRegistered[0]=true;
                        customerInformation.setPhoneNumber(phoneNumber);
                        customerInformation.setCustomerName(customerName);
                        customerInformation.setCustomerAddress(customerAddress);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        return customerInformation;
    }

}
