package com.urja.motoservice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.model.Customer;
import com.urja.motoservice.model.CustomerAddress;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UpdateProfileActivity.class.getSimpleName();

    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCustomerRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER);
    private DatabaseReference mCustomerAddressRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CUSTOMER_ADDRESS);

    private EditText mName, mMobile, mAddress, mStreet, mPinCode;
    private ImageView mEditName, mEditMobile, mEditAddress, mEditStreet, mEditPinCode;
    private Button mButton;
    private Customer mCustomer;
    private CustomerAddress mCustomerAddress;
    private Query customerAddressQuery;
    private Query customerQuery;
    String currentUserId="";

    boolean updatedCustomerToServer = false;
    boolean updatedCustomerAddressToServer = false;


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

        mEditName = (ImageView) findViewById(R.id.editName);
        mEditMobile = (ImageView) findViewById(R.id.editMobile);
        mEditAddress = (ImageView) findViewById(R.id.editAddress);
        mEditStreet = (ImageView) findViewById(R.id.editStreet);
        mEditPinCode = (ImageView) findViewById(R.id.editPincode);

        mButton = (Button) findViewById(R.id.updateProfile);


        //Make the EditText readonly untill user taps on Edit Image
        mName.setEnabled(false);
        mMobile.setEnabled(false);
        mAddress.setEnabled(false);
        mStreet.setEnabled(false);
        mPinCode.setEnabled(false);

        //Populate the data from the database
        currentUserId = CurrentLoggedInUser.getCurrentFirebaseUser().getUid();
        Log.e(TAG, "onCreate: CurrentUserId"+currentUserId );
        Query customerQuery = mCustomerRef.child(currentUserId).orderByKey();
        customerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCustomer = dataSnapshot.getValue(Customer.class);
                String name = mCustomer.getName();
                mName.setText(name);
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
                Log.e(TAG, "onDataChange: Customer Address "+mCustomerAddress.getAddress());
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
        mEditAddress.setOnClickListener(this);
        mEditName.setOnClickListener(this);
        mEditStreet.setOnClickListener(this);
        mEditPinCode.setOnClickListener(this);
        mEditMobile.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updateProfile:
                updateProfile();
                finish();
                break;
            case R.id.editName:
                    mName.setEnabled(true);
                    mName.requestFocus();

                    mMobile.setEnabled(false);
                    mAddress.setEnabled(false);
                    mStreet.setEnabled(false);
                    mPinCode.setEnabled(false);

                break;
            case R.id.editAddress:
                mName.setEnabled(false);
                mMobile.setEnabled(false);
                mAddress.setEnabled(true);
                mAddress.requestFocus();
                mStreet.setEnabled(false);
                mPinCode.setEnabled(false);
                break;
            case R.id.editMobile:
                mName.setEnabled(false);
                mMobile.setEnabled(true);
                mMobile.requestFocus();
                mAddress.setEnabled(false);
                mStreet.setEnabled(false);
                mPinCode.setEnabled(false);
                break;
            case R.id.editStreet:
                mName.setEnabled(false);
                mMobile.setEnabled(false);
                mAddress.setEnabled(false);
                mStreet.setEnabled(true);
                mStreet.requestFocus();
                mPinCode.setEnabled(false);
                break;
            case R.id.editPincode:
                mName.setEnabled(false);
                mMobile.setEnabled(false);
                mAddress.setEnabled(false);
                mStreet.setEnabled(false);
                mPinCode.setEnabled(true);
                mPinCode.requestFocus();
                break;
        }
    }

    private void updateProfile() {//Code to send the data to the server
        mCustomer = new Customer();
        String updateName = mName.getText().toString();
        String updateMobile = mMobile.getText().toString();
        mCustomer.setName(updateName);
        mCustomer.setMobile(updateMobile);

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
