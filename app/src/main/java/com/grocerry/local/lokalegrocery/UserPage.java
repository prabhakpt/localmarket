package com.grocerry.local.lokalegrocery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.grocerry.local.lokalegrocery.configuration.SharedPreferencesConfig;

import org.w3c.dom.Text;

public class UserPage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView welcomeText;
    Button signoutButton;

    SharedPreferencesConfig sharedPreferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        firebaseAuth = FirebaseAuth.getInstance();
        //Intent intent = getIntent();
        //String[] userInfo = intent.getStringArrayExtra("userInfo");

        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        String customerName = sharedPreferencesConfig.getCustomerInfo("customerName");
        String address = sharedPreferencesConfig.getCustomerInfo("customerAddress");
        signoutButton = (Button)findViewById(R.id.signOutButton);
        welcomeText = (TextView)findViewById(R.id.welcomeText);
        if(!(null == customerName)) {
            welcomeText.setText("Welcome " + customerName + "datat from sharedPreferencesconfig::"+address);
            sharedPreferencesConfig.writeLoginStatus(true);
        }else{
            welcomeText.setText("Welcome Anonymous User");
        }

        signoutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(UserPage.this,LoginActivity.class);
                        sharedPreferencesConfig.clearSharedPreferenceObject();
                        startActivity(intent);
                    }
                }
        );
    }
}
