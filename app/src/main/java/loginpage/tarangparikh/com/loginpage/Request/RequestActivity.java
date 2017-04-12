package loginpage.tarangparikh.com.loginpage.Request;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.WelcomeActivity;
import loginpage.tarangparikh.com.loginpage.Register.User;

public class RequestActivity extends AppCompatActivity {

    EditText m_mobile;
    EditText m_amount;
    Button m_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_request);


            m_mobile = (EditText) findViewById(R.id.mobile_id);
            m_amount = (EditText) findViewById(R.id.amount);
            m_request = (Button) findViewById(R.id.request);

            m_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request();
                }
            });
        }

        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    public void request()

    {
        if (!validate()) {
            return;
        }
        try{
        CheckConnection connection=new CheckConnection(this);
        if(connection.connected())
        {
        final ProgressDialog progressDialog = new ProgressDialog(RequestActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Requesting Money...");
        progressDialog.show();

        final String mob = m_mobile.getText().toString().trim();
        final String amt = m_amount.getText().toString().trim();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();

        final String uid = getIntent().getStringExtra("curr_user");
        Query q = mDatabase.child(uid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    // do something with the individual "issues"
                    if ((dataSnapshot.getValue(User.class).mobile).equals(mob)) {
                        Toast.makeText(RequestActivity.this, "Mobile no is yours..", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return;
                    } else {
                            check_mobile(progressDialog);

                    }
                } else {
                    Toast.makeText(RequestActivity.this, "Mobile no doesn't register..", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RequestActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
        }
        else {
            Toast.makeText(this,"Check ur connection and try again..",Toast.LENGTH_SHORT).show();
            return;
        }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void check_mobile(final ProgressDialog progressDialog)
    {
        try{
        final String mob = m_mobile.getText().toString().trim();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");

        Query query = mDatabase.orderByChild("mobile").equalTo(mob);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        sender_requestDB(progressDialog);
                    }
                }
                else
                {
                    Toast.makeText(RequestActivity.this,"Mobile No not exists...",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RequestActivity.this,databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void sender_requestDB(final ProgressDialog progressDialog) {
    try{
        final String mob = m_mobile.getText().toString().trim();
        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").orderByChild("mobile").equalTo(mob).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    sender_requestDB2(progressDialog,ds.getValue(User.class).username);
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    catch (Exception e)
    {
        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        return;
    }
    }

    public void sender_requestDB2(final ProgressDialog progressDialog, final String rec_username)
    {
        try{
        final String sender_status="Pending";
        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        final DatabaseReference mRequestDB=FirebaseDatabase.getInstance().getReference("request");
        final String uid=getIntent().getStringExtra("curr_user");
                Request request = new Request(sender_status,mob, amt,java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()),rec_username);
                mRequestDB.child(uid).push().setValue(request);
                reciever_requestDB1(progressDialog);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void reciever_requestDB1(final ProgressDialog progressDialog)
    {
        try{
        final String mob = m_mobile.getText().toString().trim();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");

        Query query = mDatabase.orderByChild("mobile").equalTo(mob);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        String rec_key = issue.getKey();

                        receiver_requestDB2(progressDialog,rec_key);
                    }
                }
                else
                {
                    Toast.makeText(RequestActivity.this,"Mobile No not exists...",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RequestActivity.this,databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void receiver_requestDB2(final ProgressDialog progressDialog, final String rec_key )
    {
        try{
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        final String uid=getIntent().getStringExtra("curr_user");
        Query q = mDatabase.child(uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                String sen_mobile= dataSnapshot.getValue(User.class).mobile;
                String sen_username=dataSnapshot.getValue(User.class).username;
                receiver_requestDB3(progressDialog,rec_key,sen_mobile,sen_username);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void receiver_requestDB3(final ProgressDialog progressDialog, final String rec_key ,final String sen_mobile,final String sen_username)
    {
        try{
        final String reciever_status="arrivedRequest";
        final String amt=m_amount.getText().toString().trim();
        final DatabaseReference mRequestDB=FirebaseDatabase.getInstance().getReference("request");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final String uid=getIntent().getStringExtra("curr_user");
                Request request = new Request(reciever_status,sen_mobile, amt,java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()),sen_username);
                mRequestDB.child(rec_key).push().setValue(request);

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Your Request is Successfully Send...",Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),WelcomeActivity.class).putExtra("curr_user",uid);
                startActivity(i);
                finish();


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }

    }


    public boolean validate() {
        boolean valid = true;
        String mobile = m_mobile.getText().toString();
        String amount = m_amount.getText().toString();
        String MobilePattern = "[0-9]{10}";
        //String AmountPattern = "[0-9]";
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(amount) ) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            valid=false;

        } else if (!mobile.matches(MobilePattern)) {
            m_mobile.setError("Please Enter Valid Mobile Number");
            valid=false;

        }
        else
        {
            m_mobile.setError(null);
            m_amount.setError(null);

            valid=true;
        }

        return valid;




    }

}
