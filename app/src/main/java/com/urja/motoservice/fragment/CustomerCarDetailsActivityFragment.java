package com.urja.motoservice.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.R;
import com.urja.motoservice.TransactionListActivity;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.UserTransactionAddress;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.database.dao.UserTransactionAddressDao;
import com.urja.motoservice.model.AdminNotification;
import com.urja.motoservice.model.CustomerTransactionAddress;
import com.urja.motoservice.model.OrderForServicesTransaction;
import com.urja.motoservice.model.TransactionComplete;
import com.urja.motoservice.utils.AppConstants;
import com.urja.motoservice.utils.FirebaseRootReference;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class CustomerCarDetailsActivityFragment extends Fragment implements AddressDialogFragment.UserAddressDialogListener {

    private static final String TAG = CustomerCarDetailsActivityFragment.class.getSimpleName();
    private static final String IMMEDIATE = "immediate";
    private static final String CASH_ON_DELIVERY = "cashOnDelivery";
    private static String PREVIOUS_KEY = "";
    DateFormat df = null;
    Date today = null;
    String transactionDate = "";
    private Button mConfirm;
    private EditText mCustomerAddressline1;
    private EditText mCustomerAaddressline2;
    private EditText mCustomerLandmark;
    private EditText mCustomerCity;
    private EditText mCustomerState;
    private EditText mCustomerPin;
    private EditText mCustomerCarNumber;
    private EditText mCustomerMobileNumber;
    private TextView mTotalAmount;
    private RadioGroup mPaymentTypeGroup;
    private String mCurrentUserId = null;
    private String mPaymentOptionChecked = null;
    private List<ServiceRequest> mServiceRequestList = null;
    private ServiceRequestDao mServiceRequestDao = null;
    private CustomerTransactionAddress mCustomerTransactionAddress = null;
    private boolean addedToServer = false;
    private int countNumberOfCar = 0;
    private int carAddedToServerCounter = 0;
    private String carNumber;
    private ServiceRequest carServiceRequest;
    private String customerName = "";
    private DatabaseReference mTransactionRef = FirebaseRootReference.get_instance().getmTransactionDatabaseRef();
    private DatabaseReference mAdminNotificationRef = null;
    private DatabaseReference mCustomerRef = null;
    private ServiceRequestDao serviceRequestDao = null;
    private UserTransactionAddressDao userTransactionAddressDao = null;
    private List<ServiceRequest> serviceRequestList = null;
    private int TOTAL_AMOUNT = 0;


    public CustomerCarDetailsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceRequestDao = DbHelper.getInstance(getActivity()).getServiceRequestDao();
        userTransactionAddressDao = DbHelper.getInstance(getActivity()).getUserTransactionAddressDao();
        serviceRequestList = serviceRequestDao.loadAll();
        for (ServiceRequest serviceRequest : serviceRequestList) {
            int val = Integer.valueOf(serviceRequest.getVehiclegroup());
            TOTAL_AMOUNT += val;
        }

        long count = userTransactionAddressDao.count();
        if (count > 0)
            showAddressDialog(getActivity());

        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mTransactionRef = FirebaseRootReference.get_instance().getmTransactionDatabaseRef();
        mTransactionRef.child(mCurrentUserId).limitToLast(1).addChildEventListener(new ChildEventListener() {
            int counter = 0;

            @Override
            public void onChildAdded(DataSnapshot transactionDataSnapshot, String s) {
                counter++;
                final String transactionId = transactionDataSnapshot.getKey();
                final String rootRefpath = transactionDataSnapshot.getRef().getRoot().toString();
                final String transactionRefpath = transactionDataSnapshot.getRef().toString();
                mCustomerRef = FirebaseRootReference.get_instance().getmCustomerDatabaseRef();

                mCustomerRef.child(mCurrentUserId).child(AppConstants.TableColumns.CustomerTable.NAME).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot customerDataSnapshot) {
                        if (!(PREVIOUS_KEY.equalsIgnoreCase(transactionId))){
                            PREVIOUS_KEY = transactionId;
                            mAdminNotificationRef = FirebaseRootReference.get_instance().getmAdminNotificationRef();
                            customerName = customerDataSnapshot.getValue(String.class);
                            AdminNotification adminNotification = new AdminNotification();
                            adminNotification.setUnread(true);
                            adminNotification.setTransactionId(transactionId);
                            adminNotification.setCustomerId(mCurrentUserId);
                            adminNotification.setCustomerName(customerName);
                            adminNotification.setCustomerVehicleNumber(carNumber);
                            adminNotification.setRootRef(rootRefpath);
                            adminNotification.setTransactionRef(transactionRefpath);
                            mAdminNotificationRef.push().setValue(adminNotification);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Do Nothing
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

    private void showAddressDialog(FragmentActivity activity) {
        DialogFragment addressDialogFragment = AddressDialogFragment.newInstance(getString(R.string.choose_previous_address));
        FragmentManager fragmentManager = getFragmentManager();

        /*SETS the target fragment for use later when sending results*/
        addressDialogFragment.setTargetFragment(CustomerCarDetailsActivityFragment.this, 300);
        addressDialogFragment.show(fragmentManager, "fragment_user_address");
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
        mTotalAmount = (TextView) view.findViewById(R.id.total_amount);

        mTotalAmount.setText("Rs. " + TOTAL_AMOUNT);
        //mPaymentTypeGroup = (RadioGroup) view.findViewById(R.id.payment_type_group);

        //Listner for the Confirm Button
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    mCustomerTransactionAddress = new CustomerTransactionAddress();
                    mCustomerTransactionAddress.setAddressLine1(mCustomerAddressline1.getText().toString());
                    mCustomerTransactionAddress.setAddressLine2(mCustomerAaddressline2.getText().toString());
                    mCustomerTransactionAddress.setLandmark(mCustomerLandmark.getText().toString());
                    mCustomerTransactionAddress.setCity(mCustomerCity.getText().toString());
                    mCustomerTransactionAddress.setState(mCustomerState.getText().toString());
                    mCustomerTransactionAddress.setPin(mCustomerPin.getText().toString());
                    mCustomerTransactionAddress.setCarNumber(mCustomerCarNumber.getText().toString());
                    mCustomerTransactionAddress.setMobileNumber(mCustomerMobileNumber.getText().toString());

                    /*Add to the database*/
                    UserTransactionAddress transactionAddress = new UserTransactionAddress();
                    transactionAddress.setAddressLine1(mCustomerAddressline1.getText().toString());
                    transactionAddress.setAddressLine2(mCustomerAaddressline2.getText().toString());
                    transactionAddress.setLandmark(mCustomerLandmark.getText().toString());
                    transactionAddress.setCity(mCustomerCity.getText().toString());
                    transactionAddress.setState(mCustomerState.getText().toString());
                    transactionAddress.setPin(mCustomerPin.getText().toString());
                    transactionAddress.setCarNumber(mCustomerCarNumber.getText().toString());
                    transactionAddress.setMobileNumber(mCustomerMobileNumber.getText().toString());

                    userTransactionAddressDao.deleteAll();
                    userTransactionAddressDao.insertOrReplace(transactionAddress);

                    addTransactionToServer(getActivity());
                } else
                    Toast.makeText(getActivity(), "Fields Cannont be Blank!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*private void listenCustomerTransactionEvent(final String mCurrentUserId, final String carNumber) {
        mTransactionRef = FirebaseRootReference.get_instance().getmTransactionDatabaseRef();
        //limitToLast(countNumberOfCar) : That is fetch last number of car added records
        mTransactionRef.child(mCurrentUserId).limitToLast(1).addChildEventListener(new ChildEventListener() {
            int counter = 0;

            @Override
            public void onChildAdded(DataSnapshot transactionDataSnapshot, String s) {
                counter++;
                final String transactionId = transactionDataSnapshot.getKey();
                final String rootRefpath = transactionDataSnapshot.getRef().getRoot().toString();
                final String transactionRefpath = transactionDataSnapshot.getRef().toString();
                mCustomerRef = FirebaseRootReference.get_instance().getmCustomerDatabaseRef();

                mCustomerRef.child(mCurrentUserId).child(AppConstants.TableColumns.CustomerTable.NAME).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot customerDataSnapshot) {
                        PREVIOUS_KEY = transactionId;
                        mAdminNotificationRef = FirebaseRootReference.get_instance().getmAdminNotificationRef();
                        customerName = customerDataSnapshot.getValue(String.class);
                        AdminNotification adminNotification = new AdminNotification();
                        adminNotification.setUnread(true);
                        adminNotification.setTransactionId(transactionId);
                        adminNotification.setCustomerId(mCurrentUserId);
                        adminNotification.setCustomerName(customerName);
                        adminNotification.setCustomerVehicleNumber(carNumber);
                        adminNotification.setRootRef(rootRefpath);
                        adminNotification.setTransactionRef(transactionRefpath);
                        mAdminNotificationRef.push().setValue(adminNotification);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Do Nothing
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
    }*/

    private boolean isFormValid() {
        //check AddressLine1
        boolean addressLine1 = checkAddressLine1();

        //check Addressline2
        //boolean addressLine2 = checkAddressLine2();

        //check landmark
        boolean landmark = checkLandmark();

        //check city
        boolean city = checkCity();

        //check state
        boolean state = checkState();

        //check Pin
        boolean pin = checkPin();

        //check carNumber
        //boolean carNumber = checkCarNumber();

        //check mobileNumber
        boolean mobileNumber = checkMobileNumber();

        //check radio group is checked
        //boolean isPaymentChoosen = checkPaymentType();

        //return (addressLine1 && addressLine2 && landmark && city && state && pin && carNumber && mobileNumber && isPaymentChoosen);
        //return (addressLine1 && addressLine2 && landmark && city && state && pin && mobileNumber && isPaymentChoosen);
        //return (addressLine1 && landmark && city && state && pin && mobileNumber && isPaymentChoosen);
        return (addressLine1 && landmark && city && state && pin && mobileNumber);


    }

    private boolean checkPaymentType() {
        return !(mPaymentOptionChecked == null || mPaymentOptionChecked == "");
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
    /*private boolean checkAddressLine2() {
        return checkEditText(mCustomerAaddressline2);
    }*/

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
        mServiceRequestList = mServiceRequestDao.queryBuilder().orderDesc(ServiceRequestDao.Properties.Carnumber).list();
        return mServiceRequestList;
    }

    private void addTransactionToServer(FragmentActivity activity) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Requesting Services...");
        progressDialog.show();
        if (mCurrentUserId == null)
            mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference transactionDataRef = mTransactionRef.child(mCurrentUserId).push(); //Create Only One Transaction Id
        List<ServiceRequest> readServiceRequestData = readServiceRequestData();
        OrderForServicesTransaction orderForServicesTransaction = new OrderForServicesTransaction(readServiceRequestData, true, new Date());


        String carNum = "";
        Map<String, List<ServiceRequest>> carWithServiceMap = new HashMap<>();//Map<CarNumber, ServiceList>
        StringBuilder carServiceList = null;
        for (ServiceRequest serviceRequest : readServiceRequestData) {//Extract the Car Number as per the CarNumber
            if (!(carNum.equalsIgnoreCase(serviceRequest.getCarnumber()))) {
                carNum = serviceRequest.getCarnumber();
                mServiceRequestDao = DbHelper.getInstance(getActivity()).getServiceRequestDao();
                mServiceRequestList = mServiceRequestDao.queryBuilder().where(ServiceRequestDao.Properties.Carnumber.eq(carNum)).list();
                carWithServiceMap.put(carNum, mServiceRequestList);

            }
        }

        countNumberOfCar = carWithServiceMap.size();

        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        today = Calendar.getInstance().getTime();
        transactionDate = df.format(today);

        for (Map.Entry<String, List<ServiceRequest>> entry : carWithServiceMap.entrySet()) {
            carNumber = entry.getKey();
            List<ServiceRequest> entryValue = entry.getValue();
            System.out.println(carNumber + "/" + entryValue);

            final DatabaseReference carNumberPush = transactionDataRef.child(carNumber);
            final Task<Void> transactionDateTask = carNumberPush.child(AppConstants.Transaction.COLUMN_SERVICE_REQUEST_DATE).setValue(transactionDate);
            final Task<Void> transactionStatusTask = carNumberPush.child(AppConstants.Transaction.COLUMN_REQUEST_STATUS).setValue(AppConstants.STATUS_OPEN);
            final Task<Void> value = carNumberPush.child(AppConstants.Transaction.COLUMN_SERVICE_REQUESTLIST).setValue(entryValue);

            value.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(getActivity(), "Request Added to the server!!", Toast.LENGTH_SHORT).show();
                    carNumberPush.child(AppConstants.Transaction.COLUMN_CARPICKADDRESS).setValue(mCustomerTransactionAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            carAddedToServerCounter += 1;
                            //listenCustomerTransactionEvent(mCurrentUserId, carNumber); // Add data to Notification Object
                            if (carAddedToServerCounter == countNumberOfCar) {
                                mServiceRequestDao.deleteAll();
                                progressDialog.dismiss();
                                EventBus.getDefault().post(new TransactionComplete(true, ""));
                                ActivityCompat.finishAffinity(getActivity());
                                //Intent intent = new Intent(getActivity(), WelcomeDashboardActivity.class);
                                Intent intent = new Intent(getActivity(), TransactionListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onOkPopulateAddress(boolean populate) {
        UserTransactionAddress userTransactionAddress = userTransactionAddressDao.queryBuilder().unique();
        mCustomerAddressline1.setText(userTransactionAddress.getAddressLine1());
        mCustomerAaddressline2.setText(userTransactionAddress.getAddressLine2());
        mCustomerLandmark.setText(userTransactionAddress.getLandmark());
        mCustomerCity.setText(userTransactionAddress.getCity());
        mCustomerState.setText(userTransactionAddress.getState());
        mCustomerPin.setText(userTransactionAddress.getPin());
        mCustomerCarNumber.setText(userTransactionAddress.getCarNumber());
        mCustomerMobileNumber.setText(userTransactionAddress.getMobileNumber());

    }
}
