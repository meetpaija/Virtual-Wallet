package loginpage.tarangparikh.com.loginpage.QR_Code;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import loginpage.tarangparikh.com.loginpage.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_scanner_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_scanner_);

            zXingScannerView = new ZXingScannerView(this);
            setContentView(zXingScannerView);
            zXingScannerView.setResultHandler(this);

/*
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
*/
            zXingScannerView.startCamera();
        }

        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleResult(Result result) {
        Log.w("handleResult",result.getText());
        final String uid = getIntent().getStringExtra("curr_user");
        Intent i=new Intent(getApplicationContext(),QR_scanner_Activity2.class).putExtra("curr_user",uid).putExtra("rec_mobile",result.getText());
        startActivity(i);
        finish();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        zXingScannerView.stopCamera();
    }

}
