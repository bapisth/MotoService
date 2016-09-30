package com.urja.motoservice;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.urja.motoservice.model.TransactionComplete;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerCarDetailsActivity extends AppCompatActivity {
    public static final String TAG = CustomerCarDetailsActivity.class.getSimpleName();
    private String mCarNumber = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_car_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TransactionComplete transactionComplete) {
        if (transactionComplete.isTransactionComplete()) //If the transaction is completed, then stop the Activity
            finish();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
