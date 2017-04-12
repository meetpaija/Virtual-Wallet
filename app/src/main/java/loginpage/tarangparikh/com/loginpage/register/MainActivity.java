package loginpage.tarangparikh.com.loginpage.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.WelcomeActivity;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

try{
    CheckConnection check=new CheckConnection(this);
    if(check.connected()) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            if (firebaseUser != null) {

                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                final String uid = firebaseUser.getUid();


                mDatabase.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {


                            String mobile = dataSnapshot.getValue(User.class).mobile.toString();
                            String name = dataSnapshot.getValue(User.class).username.toString();

                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class).putExtra("curr_user", uid).putExtra("name", name).putExtra("mobile", mobile);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Network Issue..Try Again..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Turn on the connection and restart the app",Toast.LENGTH_SHORT).show();
                    return;
                }


   // progressBar.setVisibility(View.GONE);

            _loginButton = (Button) findViewById(R.id.btn_login);
            _signupLink = (TextView) findViewById(R.id.link_signup);

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.transition.push_left_in, R.transition.push_left_out);
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        }
    }

    public void login() {

        try {

            if (!validate()) {

                return;
            }

            CheckConnection check=new CheckConnection(this);

            if(check.connected()) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                _emailText = (EditText) findViewById(R.id.input_email_login);
                _passwordText = (EditText) findViewById(R.id.input_password_login);


                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    final String uid = firebaseUser.getUid();

                                    if (firebaseUser != null) {

                                        mDatabase.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String mobile = dataSnapshot.getValue(User.class).mobile.toString();
                                                String name = dataSnapshot.getValue(User.class).username.toString();

                                                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class).putExtra("curr_user", firebaseUser.getUid()).putExtra("name", name).putExtra("mobile", mobile);
                                                startActivity(intent);

                                                progressDialog.dismiss();
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                                Toast.makeText(getApplicationContext(), "Network Issue..Try Again..", Toast.LENGTH_SHORT).show();

                                                progressDialog.dismiss();
                                                return;
                                            }
                                        });


                                    } else {

                                        Toast.makeText(getApplicationContext(), "User not exist, plz register first", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } else if (!task.isSuccessful()) {
                                    Toast.makeText(getBaseContext(), "Check ur email or password", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Check Your Connection",Toast.LENGTH_SHORT).show();
                return;
            }

        }
        catch (Exception e)

        {
            Toast.makeText(getApplication(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }

        }



    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        _emailText=(EditText)findViewById(R.id.input_email_login);
        _passwordText=(EditText)findViewById(R.id.input_password_login);

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fill Empty Details", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (!email.matches(emailPattern)) {
            _emailText.setError("Please Enter Valid Email Address");
           valid=false;
        }
        else
        {
            _emailText.setError(null);
            _passwordText.setError(null);
            valid=true;
        }
        return valid;
    }



}
