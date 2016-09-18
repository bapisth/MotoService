package com.urja.motoservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private ProgressDialog mProgressDialog;
    private Customer mCustomer;
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCustomerRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER);// Add Name and Phone number to 'Customer' object
    private String mCurrrentKey;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if the Network Connectivity
        NetworkService.Initialize(getApplicationContext());

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = auth.getCurrentUser();

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //UI Initialization
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.keyLogin || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.title_progress_dialog);
        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(false);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            case R.id.btn_login: {
                attemptLogin();
            }
            break;
            case R.id.btn_reset_password:
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;
        }
    }

    private void attemptLogin() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.warning_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.warning_password, Toast.LENGTH_SHORT).show();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);
        mProgressDialog.show();

        //authenticate user
        if (NetworkService.Network) {
            OnCompleteListener<AuthResult> onCompleteListener = getOnCompleteListener(password);
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, onCompleteListener);
        } else {
            AlertDialog.Alert(this, "", getString(R.string.message_network_not_available));
        }
    }

    @NonNull
    private OnCompleteListener<AuthResult> getOnCompleteListener(final String password) {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                //progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    mProgressDialog.dismiss();
                    // there was an error
                    if (password.length() < 6) {
                        inputPassword.setError(getString(R.string.minimum_password));
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    final FirebaseUser currentUser = auth.getCurrentUser();
                    CurrentLoggedInUser.setCurrentFirebaseUser(currentUser);

                    //Get the Customer details from the Customer Object from the server
                    mCustomerRef.orderByKey().equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                            Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey());
                            mCustomer = dataSnapshot.getValue(Customer.class);
                            mCurrrentKey = dataSnapshot.getKey();
                            if (mCurrrentKey != null && currentUser != null && mCurrrentKey.equalsIgnoreCase(currentUser.getUid()))
                                if (mCustomer != null) {

                                    Log.d(TAG, "onChildAdded: Name=" + mCustomer.getName());
                                    Log.d(TAG, "onChildAdded: currentKey=" + mCurrrentKey);
                                    Log.d(TAG, "onChildAdded: previousChildKey=" + previousChildKey);

                                    CurrentLoggedInUser.setCurrentFirebaseUser(currentUser);
                                    CurrentLoggedInUser.setName(mCustomer.getName());
                                    CurrentLoggedInUser.setMobile(mCustomer.getMobile());

                                    //Dismiss the progress bar
                                    mProgressDialog.dismiss();
                                    //startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                                    startActivity(new Intent(LoginActivity.this, WelcomeDashboardActivity.class));
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


                }
            }
        };
    }
}
