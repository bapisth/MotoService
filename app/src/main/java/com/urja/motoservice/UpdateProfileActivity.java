package com.urja.motoservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.urja.motoservice.model.Customer;
import com.urja.motoservice.model.CustomerAddress;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UpdateProfileActivity.class.getSimpleName();
    String currentUserId = "";
    boolean updatedCustomerToServer = false;
    boolean updatedCustomerAddressToServer = false;
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCustomerRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER);
    private DatabaseReference mCustomerAddressRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER_ADDRESS);
    private EditText mName, mMobile, mAddress, mStreet, mPinCode;
    private Button mButton;
    private Customer mCustomer;
    private CustomerAddress mCustomerAddress;
    private Query customerAddressQuery;
    private Query customerQuery;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize UI
        mName = (EditText) findViewById(R.id.name);
        mMobile = (EditText) findViewById(R.id.mobile);
        mAddress = (EditText) findViewById(R.id.address);
        mStreet = (EditText) findViewById(R.id.street);
        mPinCode = (EditText) findViewById(R.id.pincode);

        mButton = (Button) findViewById(R.id.updateProfile);

        //Populate the data from the database
        currentUserId = CurrentLoggedInUser.getCurrentFirebaseUser().getUid();
        Query customerQuery = mCustomerRef.child(currentUserId).orderByKey();
        customerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCustomer = dataSnapshot.getValue(Customer.class);
                String name = mCustomer.getName();
                String mobile = mCustomer.getMobile();
                mName.setText(name);
                mMobile.setText(mobile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        customerAddressQuery = mCustomerAddressRef.child(currentUserId).orderByKey();
        customerAddressQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCustomerAddress = dataSnapshot.getValue(CustomerAddress.class);
                String address = mCustomerAddress.getAddress();
                String street = mCustomerAddress.getStreet();
                String pincode = mCustomerAddress.getPincode();

                mAddress.setText(address);
                mStreet.setText(street);
                mPinCode.setText(pincode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updateProfile:
                updateProfile();
                startActivity(new Intent(this, WelcomeDashboardActivity.class));
                finish();
                break;
        }
    }

    private void updateProfile() {//Code to send the data to the server
        mCustomer = new Customer();
        String updateName = mName.getText().toString();
        String updateMobile = mMobile.getText().toString();
        mCustomer.setName(updateName);
        mCustomer.setMobile(updateMobile);
        mCustomer.setRegToken(FirebaseInstanceId.getInstance().getToken());

        mCustomerAddress = new CustomerAddress();
        String updateAddress = mAddress.getText().toString();
        String updateStreet = mStreet.getText().toString();
        String updatePinCode = mPinCode.getText().toString();
        mCustomerAddress.setAddress(updateAddress);
        mCustomerAddress.setStreet(updateStreet);
        mCustomerAddress.setPincode(updatePinCode);

        Map<String, Customer> stringCustomerMap = new HashMap<>();


        DatabaseReference customerChild = mCustomerRef.child(currentUserId);
        customerChild.setValue(mCustomer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updatedCustomerToServer = true;
            }
        });

        DatabaseReference customerAddressChild = mCustomerAddressRef.child(currentUserId);
        customerAddressChild.setValue(mCustomerAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updatedCustomerAddressToServer = true;
            }
        });


    }
}
