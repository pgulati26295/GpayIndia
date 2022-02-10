package com.example.gpayindia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gpayindia.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String amount;
    String name = "Highbrow Director";
    String upiId = "hashimads123@oksbi";
    String transactionNote= "gpay test";
    String status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googlePayButton.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){
            amount = binding.amountEditText.getText().toString();
            if(!amount.isEmpty())
            {
                uri = getUpiPaymentUri(name , upiId,transactionNote , amount);
                payWithGPay();
            }else{
                binding.amountEditText.setError("Amount is required");
                binding.amountEditText.requestFocus();
            }
        }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode,data);
        if(data != null){
            status = data.getStringExtra("Status").toLowerCase();
        }
       // Log.d("myTag","onActivityResult:  " + requestCode+ RESULT_OK );

        if((RESULT_OK == resultCode) && status.equals("success")){
            Toast.makeText(MainActivity.this,"Transaction Successful",Toast.LENGTH_SHORT ).show();
        }else{
            Toast.makeText(MainActivity.this,"Transaction failed",Toast.LENGTH_SHORT ).show();
        }
    }

    private static boolean IsAppInstalled(Context context, String packageName)
    {
        try{
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }
        catch(PackageManager.NameNotFoundException e){
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name , String upiId, String transactionNote, String amount )
    {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();

    }

    private void payWithGPay()
    {
        if (IsAppInstalled(this,GOOGLE_PAY_PACKAGE_NAME)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent,GOOGLE_PAY_REQUEST_CODE);
        }else{
            Toast.makeText(MainActivity.this, "Please install GPay",Toast.LENGTH_SHORT ).show();
        }
    }

}