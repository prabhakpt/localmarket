package com.grocerry.local.lokalegrocery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grocerry.local.lokalegrocery.configuration.SharedPreferencesConfig;
import com.grocerry.local.lokalegrocery.firebaseAccessLayer.CustomerInformationDAO;
import com.grocerry.local.lokalegrocery.models.CustomerInformation;
import com.grocerry.local.lokalegrocery.models.RegistrationConfirmation;

public class UserRegisterConfirmation extends AppCompatActivity {

    String mobile;
    EditText userName, userAddress;
    Button confirmRegistraiton;

   CustomerInformationDAO customerInformationDAO;

    SharedPreferencesConfig sharedPrefConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_confirmation);

        userName = (EditText)findViewById(R.id.userName);
        userAddress = (EditText)findViewById(R.id.address);
        confirmRegistraiton = (Button)findViewById(R.id.confirmRegistration);

        sharedPrefConfig = new SharedPreferencesConfig(getApplicationContext());

        customerInformationDAO =new CustomerInformationDAO();
        //Intent intent = getIntent();
        //mobile = intent.getStringExtra("mobile");
        mobile = sharedPrefConfig.getCustomerInfo("phoneNumber");
        storeUserInDataBase();
    }

    public CustomerInformation readUserRegistrationConfirmationInfo(){

        CustomerInformation customerInformation = new CustomerInformation();
        customerInformation.setCustomerName(userName.getText().toString());
        customerInformation.setCustomerAddress(userAddress.getText().toString());
        return customerInformation;
    }

    public void storeUserInDataBase(){
        confirmRegistraiton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomerInformation customerInformation = readUserRegistrationConfirmationInfo();
                        customerInformation.setPhoneNumber(sharedPrefConfig.getCustomerInfo("phoneNumber"));
                        customerInformationDAO.createNewCustomer(mobile,customerInformation);
                        Intent intent = new Intent(UserRegisterConfirmation.this, UserPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sharedPrefConfig.storeDataInData("customerName",customerInformation.getCustomerName());
                        sharedPrefConfig.storeDataInData("customerAddress",customerInformation.getCustomerAddress());
                        //String[] userInfo = {mobile,customerInformation.getCustomerName()};
                        //intent.putExtra("userInfo",userInfo);
                        startActivity(intent);
                    }
                }
        );
    }
}
