package loginpage.tarangparikh.com.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_scanner_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner_);

        zXingScannerView=new ZXingScannerView(this);
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
