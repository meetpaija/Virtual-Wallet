package loginpage.tarangparikh.com.loginpage.QR_Code;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Register.User;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;

public class QR_gen_Activity extends AppCompatActivity {
Bitmap bitmap;
    public final static int QRcodeWidth = 500 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_gen_);

            CheckConnection connection=new CheckConnection(this);

            if(connection.connected()) {
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.userdb));

                final String uid = getIntent().getStringExtra("curr_user");
                //Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_LONG).show();
                mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String mobile = dataSnapshot.getValue(User.class).mobile;
                        try {
                            ImageView imageView = (ImageView) findViewById(R.id.qr_generator);
                            Bitmap bitmap = TextToImageEncode(mobile);

                            imageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
                else {
                Toast.makeText(this,"Check ur connection and try again..",Toast.LENGTH_SHORT).show();

            }
        }

        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }


    }


        Bitmap TextToImageEncode(String Value) throws WriterException
        {
        BitMatrix bitMatrix;
        try {
        bitMatrix = new MultiFormatWriter().encode(
        Value,
        BarcodeFormat.DATA_MATRIX.QR_CODE,
        QRcodeWidth, QRcodeWidth, null
        );

        } catch (IllegalArgumentException Illegalargumentexception) {

        return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
        int offset = y * bitMatrixWidth;

        for (int x = 0; x < bitMatrixWidth; x++) {

        pixels[offset + x] = bitMatrix.get(x, y) ?
        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
        }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
        }
}