package com.urja.motoservice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.urja.motoservice.fragment.Ask4CarNumberDialogFragment;

public class CustomerCarDetailsActivity extends AppCompatActivity implements Ask4CarNumberDialogFragment.Ask4CarNumberDialogListener {
    public static final String TAG = CustomerCarDetailsActivity.class.getSimpleName();
    private String mCarNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_car_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Ask4CarNumberDialogFragment  ask4CarNumberDialogFragment = new Ask4CarNumberDialogFragment();
        ask4CarNumberDialogFragment.show(getSupportFragmentManager(), TAG);
        ask4CarNumberDialogFragment.setCancelable(false);
    }

    @Override
    public void onSubmit(String carNumber) {
        this.mCarNumber =  carNumber;
    }
}
