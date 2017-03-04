package loginpage.tarangparikh.com.loginpage;

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

import butterknife.ButterKnife;

public class TransferActivity extends AppCompatActivity {
    private static final String TAG = "TransferActivity";

    EditText m_mobile;
    EditText m_amount;
    Button m_trasfer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        m_mobile=(EditText)findViewById(R.id.mobile_id);
        m_amount=(EditText)findViewById(R.id.amount);
        m_trasfer=(Button)findViewById(R.id.transfer);
        ButterKnife.bind(this);


        m_trasfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer();
            }
        });

    }
    public void transfer() {

        if (!validate()) {
            onTransferFailed();
            return;
        }


        m_trasfer.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(TransferActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Transfering Amount...");
        progressDialog.show();

        final String status = "Send";
        final String mob = m_mobile.getText().toString().trim();
        final String amt = m_amount.getText().toString().trim();
        /// TODO: Implement your own signup logic here.
        //final String id=String.valueOf(1);


        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB = FirebaseDatabase.getInstance().getReference("transfers");
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
                        Toast.makeText(TransferActivity.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        check_mobile_no(progressDialog);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(TransferActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            });


        } else {
            Toast.makeText(TransferActivity.this, "We can not move further", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
    }

    public void check_mobile_no(final ProgressDialog progressDialog)
    {

        final String status = "Send";
        final String mob = m_mobile.getText().toString().trim();
        final String amt = m_amount.getText().toString().trim();
        /// TODO: Implement your own signup logic here.
        //final String id=String.valueOf(1);


        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB = FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();

        final String uid = getIntent().getStringExtra("curr_user");
        Query q = mDatabase.child(uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    // do something with the individual "issues"
                    if ((dataSnapshot.getValue(User.class).mobile).equals(mob)) {
                        Toast.makeText(TransferActivity.this, "Mobile no is yours..", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return;
                    } else {
                        update_bal_at_receiver(progressDialog);
                    }
                } else {
                    Toast.makeText(TransferActivity.this, "Register your mobile no first..", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });

    }

    public void update_bal_at_receiver(final ProgressDialog progressDialog)
    {


        final String status="Send";

        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        /// TODO: Implement your own signup logic here.
        //final String id=String.valueOf(1);




        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();

        final String uid=getIntent().getStringExtra("curr_user");


        Query query = mDatabase.orderByChild("mobile").equalTo(mob);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        String receiver=issue.getValue(User.class).username;
                        String rec_amt=issue.getValue(User.class).curr_balance;
                        String new_amt=String.valueOf(Double.valueOf(amt)+Double.valueOf(rec_amt));
                        issue.getRef().child("curr_balance").setValue(new_amt);

                        update_bal_at_sender(progressDialog);
                    }
                }
                else
                {
                    Toast.makeText(TransferActivity.this,"Mobile No not exists...",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this,databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });


    }

    public  void update_bal_at_sender(final ProgressDialog progressDialog)
    {

        final String status="Send";

        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        /// TODO: Implement your own signup logic here.
        //final String id=String.valueOf(1);




        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();

        final String uid=getIntent().getStringExtra("curr_user");


        Query q1=mDatabase.child(uid);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                                String sender_amt=dataSnapshot.getValue(User.class).curr_balance;
                                String new_amt=String.valueOf(Double.valueOf(sender_amt)-Double.valueOf(amt));
                                dataSnapshot.getRef().child("curr_balance").setValue(new_amt);

                                sender_transferDB(progressDialog);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this,"hello",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });

    }


    public void sender_transferDB(final ProgressDialog progressDialog)
    {
        //Toast.makeText(TransferActivity.this,"here",Toast.LENGTH_LONG).show();
        final String sender_status="Debit";
        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final DatabaseReference mTransferDB1;
        final DatabaseReference mTransferDB2;
        final String uid=getIntent().getStringExtra("curr_user");

        //mTransferDB1=mTransferDB.child("uid");
        // mTransferDB2=mTransferDB1.push();

        mTransferDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Transfer transfer = new Transfer(mob, amt, sender_status);
                    mTransferDB.child(uid).push().setValue(transfer);



                    reciever_transferDB1(progressDialog);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this,databaseError.getDetails(),Toast.LENGTH_LONG);
                progressDialog.dismiss();
                return;
            }
        });

    }

    public void reciever_transferDB1(final ProgressDialog progressDialog)
    {
        final String reciever_status="Credit";
        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();
        final String uid=getIntent().getStringExtra("curr_user");

        Query query = mDatabase.orderByChild("mobile").equalTo(mob);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    Toast.makeText(TransferActivity.this,"Mobile No not exists...",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this,databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });


    }

public void receiver_transferDB2(final ProgressDialog progressDialog, final String rec_key )
    {
        final String reciever_status="Credit";
        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();
        final String uid=getIntent().getStringExtra("curr_user");
        Query q = mDatabase.child(uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sen_mobile= dataSnapshot.getValue(User.class).mobile;
                receiver_transferDB3(progressDialog,rec_key,sen_mobile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
    }

    public void receiver_transferDB3(final ProgressDialog progressDialog, final String rec_key ,final String sen_mobile)
    {
        final String reciever_status="Credit";
        final String mob = m_mobile.getText().toString().trim();
        final String amt=m_amount.getText().toString().trim();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference mTransferDB=FirebaseDatabase.getInstance().getReference("transfers");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();
        final String uid=getIntent().getStringExtra("curr_user");

        mTransferDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               Transfer transfer = new Transfer(sen_mobile, amt, reciever_status);
                mTransferDB.child(rec_key).push().setValue(transfer);

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Transfer Done Successfully...",Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),WelcomeActivity.class).putExtra("curr_user",uid);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this,databaseError.getDetails(),Toast.LENGTH_LONG);
                progressDialog.dismiss();
                return;
            }
        });


    }

    public void onTransferSuccess() {


        m_trasfer.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onTransferFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        m_trasfer.setEnabled(true);
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
