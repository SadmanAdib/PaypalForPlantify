package com.adib.paypalforplantify;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private Button paymentBtn;

    private int PAYPAL_REQ_CODE = 12;

    private static PayPalConfiguration paypalConfig = new PayPalConfiguration().environment( PayPalConfiguration.ENVIRONMENT_SANDBOX ).clientId( PaypalClient.PAYPAL_CLIENT_ID );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        paymentBtn = findViewById( R.id.paymentBtn );

        Intent intent = new Intent(MainActivity.this, PayPalService.class );
        intent.putExtra( PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService( intent );


        paymentBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaypalPaymentMethod();
            }

            private void PaypalPaymentMethod() {

                PayPalPayment payment = new PayPalPayment( new BigDecimal( 100 ),  "usd",  "Test Payment", PayPalPayment.PAYMENT_INTENT_SALE );

                Intent intent = new Intent(MainActivity.this, PaymentActivity.class );
                intent.putExtra( PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig );
                intent.putExtra( PaymentActivity.EXTRA_PAYMENT, payment);

                startActivityForResult( intent, PAYPAL_REQ_CODE );

            }

            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
            {
                MainActivity.super.onActivityResult( requestCode, resultCode, data );

                if(requestCode == PAYPAL_REQ_CODE){

                    if(resultCode== Activity.RESULT_OK)
                    {
                        Toast.makeText( MainActivity.this, "Payment made Successfully", Toast.LENGTH_SHORT ).show();
                    }
                    else{
                        Toast.makeText( MainActivity.this, "Payment Unsuccessfull", Toast.LENGTH_SHORT ).show();
                    }
                }

            }


            protected void onDestroy(){
                stopService( new Intent(MainActivity.this, PayPalService.class) );
                MainActivity.super.onDestroy();
            }




        } );

    }
}