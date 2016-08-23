package com.urja.motoservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.DatabaseConstants;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        switch (view.getId()){
            case R.id.btn_signup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            case R.id.btn_login:{
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
                OnCompleteListener<AuthResult> onCompleteListener = getOnCompleteListener(password);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, onCompleteListener);
            }
            break;
            case R.id.btn_reset_password:
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;
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
                mProgressDialog.dismiss();
                if (!task.isSuccessful()) {
                    // there was an error
                    if (password.length() < 6) {
                        inputPassword.setError(getString(R.string.minimum_password));
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    CurrentLoggedInUser.setCurrentFirebaseUser(auth.getCurrentUser());
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
}
