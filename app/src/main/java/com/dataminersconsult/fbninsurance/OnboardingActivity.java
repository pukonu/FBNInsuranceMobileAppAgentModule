package com.dataminersconsult.fbninsurance;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dataminersconsult.fbninsurance.lib_onboarding.CustomerFactory;
import com.dataminersconsult.fbninsurance.lib_onboarding.OnboardPagerAdapter;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import co.paystack.android.model.Transaction;

public class OnboardingActivity extends AppCompatActivity {
    private static final String TAG = "OnboardingActivity";

    private ViewPager viewPager;
    public CustomerFactory mCustomerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mCustomerFactory = new CustomerFactory();

        // TABVIEW
        // Create new tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Basic Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Policy Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Image Capture"));
        tabLayout.addTab(tabLayout.newTab().setText("Save Information"));

        viewPager = (ViewPager) findViewById(R.id.pager);

        // Set fragments for each tab
        OnboardPagerAdapter adapter = new OnboardPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabSelected: Tab has been selected at position" + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO pass bitmap into fragment
            }
        });

        // TOOLBAR
        ImageView btnBack = (ImageView) findViewById(R.id.button_back_toolbar_onboarding);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnboardingActivity.this.finish();
            }
        });
    }

    public void pushPay(String cardNumber, int month, int year, String cvv2, String email, int amount) {
        PaystackSdk.initialize(getApplicationContext());
        Charge charge = new Charge();
        charge.setCard(new Card.Builder(cardNumber, month, year, cvv2).build());
        charge.setEmail(email);
        charge.setAmount(amount);

        PaystackSdk.setPublicKey("pk_test_3b537cef92446fa39c306aac6112f67eadb349bc");
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                Log.d(TAG, "onSuccess: Transaction success " + transaction.toString());
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                Log.d(TAG, "beforeValidate: Transaction success " + transaction.toString());
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onError: Transaction error " + error.toString());
            }
        });
    }
}
