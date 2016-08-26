package com.urja.motoservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urja.motoservice.model.Customer;
import com.urja.motoservice.model.CustomerAddress;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.DatabaseConstants;
import java.util.Date;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail, inputPassword, fullName, mobile;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private Customer mCustomer;
    private CustomerAddress mCustomerAddress;
    private String mCurrentUserId = null;
    //private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mdatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCustomerRef = mdatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER);// Add Name and Phone number to 'Customer' object
    private DatabaseReference mCustomerAddressRef = mdatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER_ADDRESS);
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //UI Initialization
        //btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        fullName = (EditText) findViewById(R.id.fullName);
        //mobile = (EditText) findViewById(R.id.mobile);

        //Event Listeners
        btnResetPassword.setOnClickListener(this);
        //btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_up_button:
                mProgressDialog = new ProgressDialog(this);
                //mProgressDialog.setTitle(R.string.title_progress_dialog);
                mProgressDialog.setMessage("Creating Account...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();



                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.warning_email, Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.warning_password, Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.minimum_password, Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, R.string.info_signup_successful+""+ task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                //progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(SignupActivity.this, R.string.info_signup_failed+":"+ task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //Update phone Number for that User
                                    updateUserData();
                                    mProgressDialog.dismiss();

                                    //startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
                                    finish();
                                }
                            }
                        });
                break;
            /*case R.id.sign_in_button:
                finish();
                break;*/

            case R.id.btn_reset_password:
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
                break;

        }

    }

    private void updateUserData() {
        String fullName = this.fullName.getText().toString();
        //String mobile = this.mobile.getText().toString();
        if (mCurrentUserId == null) {
            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            CurrentLoggedInUser.setCurrentFirebaseUser(mCurrentUser);
            mCurrentUserId = mCurrentUser.getUid();
        }


        mCustomer = new Customer();
        mCustomer.setName(fullName);
        //mCustomer.setMobile(mobile);

        mCustomerAddress = new CustomerAddress();
        mCustomerAddress.setCity("Bhubaneswar");
        mCustomerAddress.setCountry("INDIA");
        mCustomerAddress.setState("ODISHA");

        /*mCustomerRef.child(mCurrentUserId).push().setValue(mCustomer);
        mCustomerAddressRef.child(mCurrentUserId).setValue(mCustomerAddress);*/

        mCustomerRef.child(mCurrentUserId).setValue(mCustomer);
        DatabaseReference child = mCustomerAddressRef.child(mCurrentUserId);
        child.setValue("date", new Date().toString());
        child.setValue(mCustomerAddress);
        child.push();

        CurrentLoggedInUser.setName(fullName);
        //CurrentLoggedInUser.setMobile(mobile);
    }
}