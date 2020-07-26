package com.grocerry.local.lokalegrocery.firebaseAccessLayer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grocerry.local.lokalegrocery.models.CustomerInformation;
import com.grocerry.local.lokalegrocery.models.FireBaseUserStatus;
import com.grocerry.local.lokalegrocery.utilities.DateUtils;

import java.util.Iterator;

/**
 * Created by Prabhakar.K on 7/21/2020.
 */

public class CustomerInformationDAO {

    private static final String TAG = "CustomerInformationDAO";
    private DatabaseReference databaseReference;
    private FirebaseDatabase mAuth;
    DateUtils dateUtils = new DateUtils();
   public CustomerInformation customerInformation = new CustomerInformation();
   public FireBaseUserStatus fireBaseUserStatus = new FireBaseUserStatus();

    public CustomerInformationDAO(){
        mAuth = FirebaseDatabase.getInstance();
        databaseReference = mAuth.getReference("userInfo");
    }


   /* public void createCustomerWithPhone(PhoneObject phoneObject){
        databaseReference.child("useInfo").child(phoneObject.getCustomerPhoneNumber()).setValue(phoneObject);
    }

    public void createCustomer(String mobileNumber,RegistrationConfirmation registrationConfirmation){
        databaseReference.child("userInfo").child(mobileNumber).setValue(registrationConfirmation);
    }*/

    public void createNewCustomer(String mobileNumber,CustomerInformation customerInformation){
        databaseReference.child(mobileNumber).setValue(customerInformation);
    }

    public FireBaseUserStatus getCustomerInformation(final String mobileNumber){
        final Boolean[] isUserRegistered = {false};
        final CustomerInformation[] customerInformation = {new CustomerInformation()};
        FireBaseUserStatus fireBaseUserStatus = new FireBaseUserStatus();
        //databaseReference = mAuth.getReference("userInfo");
        //databaseReference = mAuth.getReference("userInfo");

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
        //-------------The below logic didn't work to read data ---------------
        /*mAuth.getReference().child("userInfo").getRef().addValueEventListener(new ValueEventListener() {
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
        });*/

        databaseReference.child(mobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                customerInformation[0] = dataSnapshot.getValue(CustomerInformation.class);

                System.out.print("User name:  Name " + customerInformation[0].getPhoneNumber() + customerInformation[0].getCustomerName());
                isUserRegistered[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        fireBaseUserStatus.setUserExist(isUserRegistered[0]);
        fireBaseUserStatus.setCustomerInformation(customerInformation[0]);
        return fireBaseUserStatus;
    }

    public FireBaseUserStatus readCustomerInformation(final String mobile){
      //  databaseReference = databaseReference.child("userInfo");
        final Boolean[] isUserRegistered = {false};
        // Write a message to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference("userInfo");

//Setting firebase unique key for Hashmap list
        //String userId = mDbRef.push().getKey();
        //databaseReference.push().child("912345678121").setValue("hello");
       /* mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user:dataSnapshot.getChildren()){
                   CustomerInformation customerInformation1 =  user.getValue(CustomerInformation.class);
                   if(null!=customerInformation1 && mobile.equals(customerInformation1.getPhoneNumber())) {
                       customerInformation.setPhoneNumber(customerInformation1.getPhoneNumber());
                       customerInformation.setCustomerAddress(customerInformation1.getCustomerAddress());
                       customerInformation.setCustomerName(customerInformation1.getCustomerName());
                       isUserRegistered[0] = true;
                   }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.print("......Data Base Error..........."+databaseError.getMessage());
            }
        });*/

        Query mQuery = mDbRef.orderByKey();

        ValueEventListener mQueryValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    String phoneNumber = (String)next.child("phoneNumber").getValue();
                    String customerName = (String)next.child("customerName").getValue();
                    String customerAddress = (String)next.child("customerAddress").getValue();
                    Log.i(TAG, "Value = " + next.child("phoneNumber").getValue());
                    if(mobile.equals(phoneNumber)){
                        isUserRegistered[0]=true;
                        customerInformation.setPhoneNumber(phoneNumber);
                        customerInformation.setCustomerName(customerName);
                        customerInformation.setCustomerAddress(customerAddress);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,"Error message:"+databaseError.getMessage());
            }
        };

        mQuery.addListenerForSingleValueEvent(mQueryValueListener);


        fireBaseUserStatus.setCustomerInformation(customerInformation);
        fireBaseUserStatus.setUserExist(isUserRegistered[0]);
        return fireBaseUserStatus;
    }

}
