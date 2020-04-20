package com.midtrans.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.VaNumber;
import com.midtrans.sdk.corekit.models.snap.Authentication;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Installment;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.demo.BuildConfig.BASE_URL;
import static com.midtrans.demo.BuildConfig.CLIENT_KEY;

public class MainActivity extends AppCompatActivity {

    private Button btnPayment, btnWithToken, btnTestShared;
    private MidtransSDK midtransSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testSharedPred();

        bindView();
        initializeMidtransUiKitSdk();
        midtransSDK = MidtransSDK.getInstance();

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payTransaction();
            }
        });

        btnWithToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithToken();
            }
        });

    }

    private void bindView() {
        btnPayment = findViewById(R.id.btn_payment_ui);
        btnWithToken = findViewById(R.id.btn_with_token);
        btnTestShared = findViewById(R.id.buttonShared);
    }

    private void initializeMidtransUiKitSdk() {

        SdkUIFlowBuilder.init()
                .setClientKey(CLIENT_KEY) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(result -> {
                    // Handle finished transaction here.

                    //Add global va number
                    List<VaNumber> vaNumber = result.getResponse().getAccountNumbers();
                    String ssss = result.getResponse().getTransactionStatus();

                    //get va number by bank
                    System.out.println(result.getResponse().getBcaVaNumber());
                    System.out.println(result.getResponse().getBniVaNumber());
                    System.out.println(result.getResponse().getTransactionStatus());
                }) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .buildSDK();
    }

    /*
     * Show snap with detail request from mobile SDK. The mobile SDK will request the snap token with detail to merchant backend
     */

    private void payTransaction() {

        CreditCard creditCard = new CreditCard();
        creditCard.setAuthentication(Authentication.AUTH_3DS);


        final UUID idRand = UUID.randomUUID();
        TransactionRequest transactionRequest = new TransactionRequest(idRand.toString(), 202020);
        transactionRequest.setCreditCard(creditCard);

        midtransSDK.setTransactionRequest(transactionRequest);
        midtransSDK.startPaymentUiFlow(this);
        midtransSDK.setTransactionFinishedCallback(result -> {
            Intent transactionResult = new Intent(this, TransactionResult.class);
            System.out.println("status message: " +result.getStatusMessage());
            System.out.println("QR Code : " +result.getResponse().getQrCodeUrl());
            System.out.println("status : " +result.getStatus());


            transactionResult.putExtra("QR URL", result.getResponse().getQrCodeUrl());
            startActivity(transactionResult);
        });


    }

    private void testSharedPred() {
        String valueShared = "Ini value dari SharedPreference";
        SharedPreferences mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("key_key", valueShared );
        editor.apply();
    }

    private void showDataFromSharedPref(){
        SharedPreferences mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String valueShared = mSettings.getString("key_key", "yah ilang valuenya");

        Intent newIntent = new Intent(this, TransactionResult.class);
        newIntent.putExtra("valueSharedPref", valueShared);
        startActivity(newIntent);
    }

    /*
        If you want show Snap with only snap token
     */
    private void payWithToken() {

        //Get Finish callback transaction, when clicked finish button
        midtransSDK.setTransactionFinishedCallback(result -> {
            Intent transactionResult = new Intent(this, TransactionResult.class);
            transactionResult.putExtra("va_number", result.getResponse().getStatusCode());
            startActivity(transactionResult);
        });

        // New object CC for set transaction is 3DS
        CreditCard creditCard = new CreditCard();
        creditCard.setAuthentication(Authentication.AUTH_3DS);

        final UUID idRand = UUID.randomUUID();
        TransactionRequest transactionRequest = new TransactionRequest(idRand.toString(), 202020);
        transactionRequest.setCreditCard(creditCard);

        // The snap token from your backend. You need get snap token request first from your backend before use this method.
        midtransSDK.startPaymentUiFlow(this, "3e5f5361-b892-4699-9d2b-e5da55d1f649");
    }
}
