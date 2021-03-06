package com.grocerry.local.lokalegrocery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.grocerry.local.lokalegrocery.configuration.SharedPreferencesConfig;
import com.grocerry.local.lokalegrocery.firebaseAccessLayer.CustomerInformationDAO;
import com.grocerry.local.lokalegrocery.models.CustomerInformation;
import com.grocerry.local.lokalegrocery.models.FireBaseUserStatus;
import com.grocerry.local.lokalegrocery.models.PhoneObject;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {

    //this is the verification id that will be sent to the user
    private String mVerificationId;
    //fireBase authentication object
    private FirebaseAuth mAuth;
    //ditText to input the code
    private EditText editTextCode;
    Button btnSignIn;

    public static final String SHARED_PREFS = "sharedprefs";
    CustomerInformationDAO customerInformationDAO = new CustomerInformationDAO();
    PhoneObject phoneObject = new PhoneObject();
    CustomerInformation customerInfo = new CustomerInformation();
    String mobile;

    SharedPreferencesConfig sharedPrefConfig;

    FireBaseUserStatus fireBaseUserStatus = new FireBaseUserStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        editTextCode = findViewById(R.id.editTextCode);
        btnSignIn = findViewById(R.id.buttonSignIn);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefConfig = new SharedPreferencesConfig(getApplicationContext());

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        //phoneObject.setCustomerID("Cust".concat(mobile));
        //phoneObject.setCustomerPhoneNumber(mobile);
        customerInfo.setPhoneNumber(mobile);
        verifyUserExistInFireBaseThenRegister(mobile);
        //sendVerificationCode(mobile);
    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+" + mobile,                 //phoneNo that is given by user
                60,                             //Timeout Duration
                TimeUnit.SECONDS,                   //Unit of Timeout
                TaskExecutors.MAIN_THREAD,          //Work done on main Thread
                mCallbacks);                       // OnVerificationStateChangedCallbacks
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    //Getting the code sent by SMS
                    String code = phoneAuthCredential.getSmsCode();

                    //sometime the code is not detected automatically
                    //in this case the code will be null
                    //so user has to manually enter the code
                    if (code != null) {
                        editTextCode.setText(code);
                        //verifying the code
                        verifyVerificationCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(PhoneVerification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("TAG",e.getMessage() );
                }

                //when the code is generated then this method will receive the code.
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);

                    //storing the verification id that is sent to the user
                    mVerificationId = s;
                }
            };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    //used for signing the user
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneVerification.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //verification successful we will start the profile activity

                                    fireBaseUserStatus = customerInformationDAO.readCustomerInformation(mobile);
                                    if(fireBaseUserStatus.getUserExist()){
                                        sharedPrefConfig.storeDataInData("phoneNumber",fireBaseUserStatus.getCustomerInformation().getPhoneNumber());
                                        sharedPrefConfig.storeDataInData("customerName",fireBaseUserStatus.getCustomerInformation().getCustomerName());
                                        sharedPrefConfig.storeDataInData("customerAddress",fireBaseUserStatus.getCustomerInformation().getCustomerAddress());
                                        Toast.makeText(PhoneVerification.this,"Customer Already exists. Redirecting to User Page",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PhoneVerification.this, UserPage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(PhoneVerification.this, UserRegisterConfirmation.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        customerInformationDAO.createNewCustomer(customerInfo.getPhoneNumber(), customerInfo);

                                        //intent.putExtra("mobile", mobile);
                                        sharedPrefConfig.storeDataInData("phoneNumber", mobile);
                                        startActivity(intent);
                                    }

                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(PhoneVerification.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void saveCustomerInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneNumber","");
        editor.commit();
    }

    public void verifyUserExistInFireBaseThenRegister(String mobile){
        fireBaseUserStatus = customerInformationDAO.readCustomerInformation(mobile);
        if(fireBaseUserStatus.getUserExist()){
            sharedPrefConfig.storeDataInData("phoneNumber",fireBaseUserStatus.getCustomerInformation().getPhoneNumber());
            sharedPrefConfig.storeDataInData("customerName",fireBaseUserStatus.getCustomerInformation().getCustomerName());
            sharedPrefConfig.storeDataInData("customerAddress",fireBaseUserStatus.getCustomerInformation().getCustomerAddress());
            Toast.makeText(PhoneVerification.this,"Customer Already exists. Redirecting to User Page",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PhoneVerification.this, UserPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(PhoneVerification.this, UserRegisterConfirmation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            customerInformationDAO.createNewCustomer(customerInfo.getPhoneNumber(), customerInfo);

            //intent.putExtra("mobile", mobile);
            sharedPrefConfig.storeDataInData("phoneNumber", mobile);
            startActivity(intent);
        }
    }

}
