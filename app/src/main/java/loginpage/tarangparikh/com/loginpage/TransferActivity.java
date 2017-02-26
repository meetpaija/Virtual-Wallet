package loginpage.tarangparikh.com.loginpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

    EditText phonenumber;
    EditText amount;
    Button tra;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getActionBar().setDisplayHomeAsUpEnabled(true);

       phonenumber=(EditText)findViewById(R.id.editText);
       amount=(EditText)findViewById(R.id.editText2);
        tra=(Button)findViewById(R.id.button2);


        tra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tranfer();
            }
        });
    }

    public void tranfer()
    {
        final String num=phonenumber.getText().toString().trim();
        String amt=amount.getText().toString().trim();
        String MobilePattern = "[0-9]{10}";

        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(amt) ) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!num.matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        ArrayList<Long> mob_no;
                        mob_no=collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        tv=(TextView)findViewById(R.id.textView2);
                        tv.setText(mob_no.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        }

    private ArrayList<Long> collectPhoneNumbers(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get("mobile"));
        }
        return (phoneNumbers);
    }
    }

