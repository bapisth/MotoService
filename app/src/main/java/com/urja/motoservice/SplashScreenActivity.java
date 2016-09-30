package com.urja.motoservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.model.Customer;
import com.urja.motoservice.utils.AlertDialog;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.DatabaseConstants;
import com.urja.motoservice.utils.NetworkService;
import com.urja.motoservice.utils.UserSession;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AppCompatActivity {


    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    // Set Duration of the Splash Screen
    private final long delay = 100;
    private FirebaseAuth mAuth;
    private UserSession mUserSession;
    private Customer mCustomer;
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCustomerRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER);// Add Name and Phone number to 'Customer' object
    private String mCurrrentKey;
    private ProgressBar mProgressBar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        NetworkService.Initialize(getApplicationContext());
        Timer runSplash = new Timer();
        TimerTask showSplashScreen = new TimerTask() {
            @Override
            public void run() {
                if (NetworkService.Network) {
                    mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (mAuth != null && currentUser != null) {
                        mUserSession = new UserSession();

                        CurrentLoggedInUser.setCurrentFirebaseUser(currentUser);

                        mCustomerRef.child(currentUser.getUid()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mCustomer = dataSnapshot.getValue(Customer.class);
                                mCurrrentKey = dataSnapshot.getKey();
                                if (mCurrrentKey != null && currentUser != null && mCurrrentKey.equalsIgnoreCase(currentUser.getUid())){
                                    if (mCustomer != null) {
                                        CurrentLoggedInUser.setCurrentFirebaseUser(currentUser);
                                        CurrentLoggedInUser.setName(mCustomer.getName());
                                        CurrentLoggedInUser.setMobile(mCustomer.getMobile());
                                        //startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                                        startActivity(new Intent(SplashScreenActivity.this, WelcomeDashboardActivity.class));
                                        finish();
                                    }
                                }else {
                                    Toast.makeText(SplashScreenActivity.this, "Having Some trouble!!Please try again later.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    AlertDialog.Alert(SplashScreenActivity.this, "", getString(R.string.message_network_not_available));
                    mProgressBar.setVisibility(View.VISIBLE);
                    finish();
                }

            }

        };

        runSplash.schedule(showSplashScreen, delay);
    }
}
