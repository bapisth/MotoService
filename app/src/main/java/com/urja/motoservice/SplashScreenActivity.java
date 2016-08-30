package com.urja.motoservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urja.motoservice.model.Customer;
import com.urja.motoservice.utils.AlertDialog;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.DatabaseConstants;
import com.urja.motoservice.utils.NetworkService;
import com.urja.motoservice.utils.UserSession;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {



    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    // Set Duration of the Splash Screen
    private final  long delay = 1000;
    private FirebaseAuth mAuth;
    private UserSession mUserSession;
    private Customer mCustomer;
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCustomerRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER);// Add Name and Phone number to 'Customer' object
    private String mCurrrentKey;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        NetworkService.Initialize(getApplicationContext());
        Timer runSplash = new Timer();
        TimerTask showSplashScreen = new TimerTask() {
            @Override
            public void run() {
                if (NetworkService.Network){
                    mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser currentUser = mAuth.getCurrentUser();
                    Log.e(TAG, "onCreate: currentUser"+currentUser);
                    if (mAuth != null && currentUser != null){
                        mUserSession = new UserSession();

                        CurrentLoggedInUser.setCurrentFirebaseUser(currentUser);

                        mCustomerRef.orderByKey().equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                                Log.d(TAG, "onChildAdded: "+dataSnapshot.getKey());
                                mCustomer = dataSnapshot.getValue(Customer.class);
                                mCurrrentKey = dataSnapshot.getKey();
                                if (mCurrrentKey!= null && currentUser!=null && mCurrrentKey.equalsIgnoreCase(currentUser.getUid()))
                                    if (mCustomer != null){

                                        Log.d(TAG, "onChildAdded: Name="+ mCustomer.getName());
                                        Log.d(TAG, "onChildAdded: currentKey="+ mCurrrentKey);
                                        Log.d(TAG, "onChildAdded: previousChildKey="+previousChildKey);

                                        CurrentLoggedInUser.setCurrentFirebaseUser(currentUser);
                                        CurrentLoggedInUser.setName(mCustomer.getName());
                                        CurrentLoggedInUser.setMobile(mCustomer.getMobile());
                                        startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                                        finish();

                                    }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }else{
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                }else{
                    AlertDialog.Alert(SplashScreenActivity.this,"", getString(R.string.message_network_not_available));
                    mProgressBar.setVisibility(View.VISIBLE);
                    finish();
                }

            }

        };

        runSplash.schedule(showSplashScreen, delay);
    }
}
