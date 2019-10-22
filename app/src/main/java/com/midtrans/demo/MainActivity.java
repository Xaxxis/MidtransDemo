package com.midtrans.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.VaNumber;
import com.midtrans.sdk.corekit.models.snap.Authentication;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.List;
import java.util.UUID;

import static com.midtrans.demo.BuildConfig.BASE_URL;
import static com.midtrans.demo.BuildConfig.CLIENT_KEY;

public class MainActivity extends AppCompatActivity {

    private Button btnPayment, btnWithToken;
    private MidtransSDK midtransSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    private void initializeMidtransUiKitSdk(){
        SdkUIFlowBuilder.init()
                .setClientKey(CLIENT_KEY) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(result -> {
                    // Handle finished transaction here.

                    //Add global va number
                    List<VaNumber> vaNumber = result.getResponse().getAccountNumbers();

                    //get va number by bank
                    System.out.println(result.getResponse().getBcaVaNumber());
                    System.out.println(result.getResponse().getBniVaNumber());
                    System.out.println(result.getResponse().getPermataVANumber());
                }) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .buildSDK();
    }

    private void payTransaction() {

//        List<String> enablePayment = new ArrayList<>();
//        enablePayment.add("gopay");
//        enablePayment.add("credit_card");

        CreditCard creditCard = new CreditCard();
        creditCard.setAuthentication(Authentication.AUTH_3DS);


        final UUID idRand = UUID.randomUUID();
        TransactionRequest transactionRequest = new TransactionRequest(idRand.toString(),202020);
      //  transactionRequest.setCreditCard(creditCard);

        midtransSDK.setTransactionRequest(transactionRequest);
        midtransSDK.startPaymentUiFlow(this);


    }

    private void payWithToken() {
        midtransSDK.startPaymentUiFlow(this, "cb488292-121f-4387-99cd-eb4b570bb827");
    }
}
