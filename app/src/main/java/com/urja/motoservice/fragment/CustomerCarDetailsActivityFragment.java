package com.urja.motoservice.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urja.motoservice.DashboardActivity;
import com.urja.motoservice.R;
import com.urja.motoservice.adapters.SectionAdapter;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.model.CustomerTransactionAddress;
import com.urja.motoservice.model.ServiceTypeSection;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CustomerCarDetailsActivityFragment extends Fragment {

    private Button mConfirm;
    private EditText mCustomerAddressline1;
    private EditText mCustomerAaddressline2;
    private EditText mCustomerLandmark;
    private EditText mCustomerCity;
    private EditText mCustomerState;
    private EditText mCustomerPin;
    private EditText mCustomerCarNumber;
    private EditText mCustomerMobileNumber;
    private RadioGroup mPaymentTypeGroup;
    private String mCurrentUserId = null;
    private String mPaymentOptionChecked = null;
    private List<ServiceRequest> mServiceRequestList = null;
    private ServiceRequestDao mServiceRequestDao = null;
    private CustomerTransactionAddress mCustomerTransactionAddress = null;

    private static final String IMMEDIATE = "immediate";
    private static final String CASH_ON_DELIVERY = "cashOnDelivery";
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mTransactionRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_TRANSACTION);



    public CustomerCarDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_car_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize UI
        mConfirm = (Button) view.findViewById(R.id.confirm);
        mCustomerAddressline1 = (EditText) view.findViewById(R.id.customer_addressline1);
        mCustomerAaddressline2 = (EditText) view.findViewById(R.id.customer_addressline2);
        mCustomerLandmark = (EditText) view.findViewById(R.id.customer_landmark);
        mCustomerCity = (EditText) view.findViewById(R.id.customer_city);
        mCustomerState = (EditText) view.findViewById(R.id.customer_state);
        mCustomerPin = (EditText) view.findViewById(R.id.customer_pin);
        mCustomerCarNumber = (EditText) view.findViewById(R.id.customer_car_number);
        mCustomerMobileNumber = (EditText) view.findViewById(R.id.customer_mobile_number);
        mPaymentTypeGroup = (RadioGroup) view.findViewById(R.id.payment_type_group);

        //Add Listener to the RadioGroup
        mPaymentTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.immediate_service:
                        mPaymentOptionChecked = IMMEDIATE;
                        break;
                    case R.id.cash_delivery:
                        mPaymentOptionChecked = CASH_ON_DELIVERY;
                        break;
                    default:
                        mPaymentOptionChecked = null;
                        break;
                }
            }
        });

        //Listner for the Confirm Button
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()){
                    mCustomerTransactionAddress = new CustomerTransactionAddress();
                    mCustomerTransactionAddress.setAddressLine1(mCustomerAddressline1.getText().toString());
                    mCustomerTransactionAddress.setAddressLine2(mCustomerAaddressline2.getText().toString());
                    mCustomerTransactionAddress.setLandmark(mCustomerLandmark.getText().toString());
                    mCustomerTransactionAddress.setCity(mCustomerCity.getText().toString());
                    mCustomerTransactionAddress.setState(mCustomerCity.getText().toString());
                    mCustomerTransactionAddress.setPin(mCustomerPin.getText().toString());
                    mCustomerTransactionAddress.setCarNumber(mCustomerCarNumber.getText().toString());
                    mCustomerTransactionAddress.setMobileNumber(mCustomerMobileNumber.getText().toString());
                    addTransactionToServer(getActivity());
                }else
                    Toast.makeText(getActivity(), "Fields Cannont be Blank!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isFormValid(){
        //check AddressLine1
        boolean addressLine1 = checkAddressLine1();

        //check Addressline2
        boolean addressLine2 = checkAddressLine2();

        //check landmark
        boolean landmark = checkLandmark();

        //check city
        boolean city = checkCity();

        //check state
        boolean state = checkState();

        //check Pin
        boolean pin = checkPin();

        //check carNumber
        boolean carNumber = checkCarNumber();

        //check mobileNumber
        boolean mobileNumber = checkMobileNumber();

        //check radio group is checked
        boolean isPaymentChoosen= checkPaymentType();

        return (addressLine1 && addressLine2 && landmark && city && state && pin && carNumber && mobileNumber && isPaymentChoosen);


    }

    private boolean checkPaymentType() {
        if (mPaymentOptionChecked == null || mPaymentOptionChecked=="")
            return false;
        return true;
    }

    private boolean checkEditText(EditText et) {
        String field = et.getText().toString();

        if (TextUtils.isEmpty(field)) {
            et.setError("Required");
            return false;
        } else {
            et.setError(null);
            return true;
        }
    }

    private boolean checkAddressLine1() {
        return checkEditText(mCustomerAddressline1);
    }
    private boolean checkAddressLine2() {
        return checkEditText(mCustomerAaddressline2);
    }

    private boolean checkLandmark() {
        return checkEditText(mCustomerLandmark);
    }

    private boolean checkCity() {
        return checkEditText(mCustomerCity);
    }

    private boolean checkState() {
        return checkEditText(mCustomerState);
    }

    private boolean checkPin() {
        return checkEditText(mCustomerPin);
    }

    private boolean checkCarNumber() {
        return checkEditText(mCustomerCarNumber);
    }
    private boolean checkMobileNumber() {
        return checkEditText(mCustomerMobileNumber);
    }


    private List<ServiceRequest> readServiceRequestData() {
        mServiceRequestDao = DbHelper.getInstance(getActivity()).getServiceRequestDao();
        mServiceRequestList = mServiceRequestDao.loadAll();
        return mServiceRequestList;
    }

    private void addTransactionToServer(FragmentActivity activity) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Adding Transaction data...");
        progressDialog.show();
        if (mCurrentUserId == null)
            mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference transactionDataRef = mTransactionRef.child(mCurrentUserId).push();
        transactionDataRef.setValue(readServiceRequestData())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(getActivity(), "Request Added to the server!!", Toast.LENGTH_SHORT).show();
                mServiceRequestDao.deleteAll();
                transactionDataRef.child("CarPickAddress").push().setValue(mCustomerTransactionAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        startActivity(new Intent(getActivity(), DashboardActivity.class));
                        getActivity().finish();
                    }
                });
            }
        });
    }
}
