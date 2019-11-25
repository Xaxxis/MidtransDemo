package com.midtrans.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TransactionResult extends AppCompatActivity {

    private TextView tvVaNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);

        tvVaNumber = findViewById(R.id.tv_va_number);

        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("va_number")!= null) {
            tvVaNumber.setText(bundle.getString("va_number"));
        } else {
            tvVaNumber.setText("gak dapet data");
        }
    }
}
