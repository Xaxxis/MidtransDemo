package com.midtrans.sdk.corekit.models;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.Constants;

import java.util.ArrayList;

/**
 * model class to hold card information.
 *
 * Created by shivam on 10/26/15.
 */
public class CardTransfer extends TransactionModel {

    public static final String PAYMENT_TYPE = Constants.PAYMENT_CREDIT_DEBIT;


    @SerializedName("credit_card")
    private CardPaymentDetails cardPaymentDetails;


    public CardTransfer(CardPaymentDetails cardPaymentDetails, TransactionDetails
            transactionDetails,
                        ArrayList<ItemDetails> itemDetails, ArrayList<BillingAddress>
                                billingAddresses, ArrayList<ShippingAddress>
                                shippingAddresses, CustomerDetails customerDetails) {

        this.paymentType = PAYMENT_TYPE;

        this.cardPaymentDetails = cardPaymentDetails;
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.billingAddresses = billingAddresses;
        this.shippingAddresses = shippingAddresses;
        this.customerDetails = customerDetails;
    }


    public String getPayment_type() {
        return paymentType;
    }

    public CardPaymentDetails getCardPaymentDetails() {
        return cardPaymentDetails;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public ArrayList<BillingAddress> getBillingAddresses() {
        return billingAddresses;
    }

    public ArrayList<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public String getString() {
        String json = "";
        try {
            Gson gson = new Gson();
            json = gson.toJson(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return json;
    }

}