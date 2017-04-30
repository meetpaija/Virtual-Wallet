package loginpage.tarangparikh.com.loginpage.QR_Code;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.WelcomeActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_scanner_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_scanner_);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("QR Code Scanning");

            zXingScannerView = new ZXingScannerView(this);
            setContentView(zXingScannerView);
            zXingScannerView.setResultHandler(this);

            zXingScannerView.startCamera();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final String uid = getIntent().getStringExtra("curr_user");
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class).putExtra("curr_user",uid);
                overridePendingTransition(R.transition.push_right_in, R.transition.push_right_out);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
