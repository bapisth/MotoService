package com.urja.motoservice.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urja.motoservice.R;

public class Ask4CarNumberDialogFragment extends DialogFragment implements View.OnClickListener {

    public interface Ask4CarNumberDialogListener {
        void onSubmit(String carNumber);
    }

    private Ask4CarNumberDialogListener mListener;

    private EditText mCarNumber;
    private EditText mPassword;
    private Button mReset;
    private Button mOkButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.car_number_fragment_layout, null);
        this.getDialog().setTitle("Car Number");

        mCarNumber = (EditText) view.findViewById(R.id.car_number);
        mOkButton = (Button) view.findViewById(R.id.ok_button);

        mOkButton.setOnClickListener(this);

        return view;
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

    public void onClick(View v) {
            if (mCarNumber.getText().toString().length() < 1 || mCarNumber.getText().toString().equals("")) {
                mCarNumber.setError("Required!!");
                return;
            }else {
                this.dismiss();
            }
    }
}