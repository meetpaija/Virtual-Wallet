package loginpage.tarangparikh.com.loginpage.QR_Code;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.Transfer.Transfer;
import loginpage.tarangparikh.com.loginpage.WelcomeActivity;
import loginpage.tarangparikh.com.loginpage.Register.User;

public class QR_scanner_Activity2 extends AppCompatActivity {

    @Bind(R.id.amt) EditText _amount;
    @Bind(R.id.transfer_qr) Button _transferButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_scanner_2);

            ButterKnife.bind(this);

            _transferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transfer();
                }
            });
        }

        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void transfer()
    {
        if (!validate()) {
            onTransferFailed();
            return;
        }
        CheckConnection connection=new CheckConnection(this);
        if(connection.connected())
        {

        final ProgressDialog progressDialog = new ProgressDialog(QR_scanner_Activity2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Transfering Amount...");
        progressDialog.show();

        final String amt = _amount.getText().toString().trim();
        /// TODO: Implement your own signup logic here.
        //final String id=String.valueOf(1);


        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();

        final String uid = getIntent().getStringExtra("curr_user");


        if (cu_user != null) {
            mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Toast.makeText(TransferActivity.this,"done",Toast.LENGTH_LONG).show();
                    User user = dataSnapshot.getValue(User.class);
                    String curr_usr = user.curr_balance;

                    if (Double.valueOf(amt) > Double.valueOf(curr_usr)) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        update_bal_at_receiver(progressDialog);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "We can not move further", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        }
        else {
            Toast.makeText(this,"Check ur connection and try again..",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void update_bal_at_receiver(final ProgressDialog progressDialog)
    {


        final String status="Send";
        final String amt=_amount.getText().toString().trim();
        /// TODO: Implement your own signup logic here.
        //final String id=String.valueOf(1);



            try{
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        final String mob=getIntent().getStringExtra("rec_mobile");

        Query query = mDatabase.orderByChild("mobile").equalTo(mob);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"
                            String receiver = issue.getValue(User.class).username;
                            String rec_amt = issue.getValue(User.class).curr_balance;
                            String new_amt = String.valueOf(Double.valueOf(amt) + Double.valueOf(rec_amt));
                            issue.getRef().child("curr_balance").setValue(new_amt);

                            update_bal_at_sender(progressDialog, receiver);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Can't find the data,plz scan again...", Toast.LENGTH_LONG).show();
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

    public  void update_bal_at_sender(final ProgressDialog progressDialog, final String rec_user)
    {
        try{
        final String amt=_amount.getText().toString().trim();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        final String uid=getIntent().getStringExtra("curr_user");


        Query q1=mDatabase.child(uid);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                String sender_amt=dataSnapshot.getValue(User.class).curr_balance;
                String new_amt=String.valueOf(Double.valueOf(sender_amt)-Double.valueOf(amt));
                dataSnapshot.getRef().child("curr_balance").setValue(new_amt);

                sender_transferDB(progressDialog,rec_user);
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

    public void sender_transferDB(final ProgressDialog progressDialog, final String rec_user)
    {
        try{
        final String sender_status="Debit";
        final String mob =getIntent().getStringExtra("rec_mobile");
        final String amt=_amount.getText().toString().trim();
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final String uid=getIntent().getStringExtra("curr_user");
                Transfer transfer = new Transfer(mob, amt, sender_status,java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()),rec_user);
                mTransferDB.child(uid).push().setValue(transfer);
                reciever_transferDB1(progressDialog);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void reciever_transferDB1(final ProgressDialog progressDialog)
    {
        try{
        final String mob =getIntent().getStringExtra("rec_mobile");
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
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
                        receiver_transferDB2(progressDialog,rec_key);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't find the data,plz scan again...",Toast.LENGTH_LONG).show();
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

    public void receiver_transferDB2(final ProgressDialog progressDialog, final String rec_key )
    {
        try{
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final String uid=getIntent().getStringExtra("curr_user");
        Query q = mDatabase.child(uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                String sen_mobile= dataSnapshot.getValue(User.class).mobile;
                String sen_user= dataSnapshot.getValue(User.class).username;
                receiver_transferDB3(progressDialog,rec_key,sen_mobile,sen_user);
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

    public void receiver_transferDB3(final ProgressDialog progressDialog, final String rec_key , final String sen_mobile, final String sen_user)
    {
        try{
        final String reciever_status="Credit";
        final String amt=_amount.getText().toString().trim();
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final String uid=getIntent().getStringExtra("curr_user");
                Transfer transfer = new Transfer(sen_mobile, amt, reciever_status,java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()),sen_user);
                mTransferDB.child(rec_key).push().setValue(transfer);

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Transfer Done Successfully...",Toast.LENGTH_LONG).show();
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

    public void onTransferFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _transferButton.setEnabled(true);
    }

    public  boolean validate() {
        boolean valid = true;
        String amount =_amount.getText().toString();
        String MobilePattern = "[0-9]{10}";
        //String AmountPattern = "[0-9]";
        if (TextUtils.isEmpty(amount) ) {
            Toast.makeText(this, "pls fill the empty field", Toast.LENGTH_SHORT).show();
            valid=false;

        }
        else
        {
            _amount.setError(null);

            valid=true;
        }

        return valid;




    }
}
