package com.urja.motoservice.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.urja.motoservice.R;
import com.urja.motoservice.WelcomeDashboardActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ask4CarNumberDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = Ask4CarNumberDialogFragment.class.getSimpleName();
    private Ask4CarNumberDialogListener mListener;
    private EditText mCarNumber;
    private Button mOkButton;
    private Button mCancelButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.car_number_fragment_layout, null);
        this.getDialog().setTitle("Car Number");
        Window window = this.getDialog().getWindow();
        window.setTitleColor(getResources().getColor(R.color.colorAccent));

        mCarNumber = (EditText) view.findViewById(R.id.car_number);
        mCarNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mOkButton = (Button) view.findViewById(R.id.ok_button);
        mCancelButton = (Button) view.findViewById(R.id.cancel_button);

        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    startActivity(new Intent(getActivity(), WelcomeDashboardActivity.class));
                    getActivity().finish();
                    return true;
                } else// Otherwise, do nothing else
                    return false;
            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (Ask4CarNumberDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AuthenticationDialogListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                if ((mCarNumber.getText().toString().length() < 1 || mCarNumber.getText().toString().equals(""))) {
                    mCarNumber.setError("Required!!");
                    return;
                }else if (isValidCarNumber(mCarNumber.getText().toString())){
                    mListener.onSubmit(mCarNumber.getText().toString(), "");
                    this.dismiss();
                }
                else {
                    return;
                    /*mListener.onSubmit(mCarNumber.getText().toString(), "");
                    this.dismiss();*/
                }
                break;
            case R.id.cancel_button:
                startActivity(new Intent(getActivity(), WelcomeDashboardActivity.class));
                getActivity().finish();
                break;
        }

    }

    private boolean isValidCarNumber(String vehicleNumber) {
        //String patternType = "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$";
        String patternType = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";
        Pattern pattern = Pattern.compile(patternType);
        Matcher m = pattern.matcher(vehicleNumber);
        if (m.find()){
            return true;
        }
        mCarNumber.setError("Not a Valid Car Number!!");
        return false;
    }

    public interface Ask4CarNumberDialogListener {
        void onSubmit(String carNumber, String carType);
    }


}