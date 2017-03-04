package loginpage.tarangparikh.com.loginpage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.transition.push_right_in, R.transition.push_right_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.



        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");

       final  FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final double bal=100.00;

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser cu_user = firebaseAuth.getCurrentUser();
                            if(cu_user!=null)
                            {
                                String Uid=cu_user.getUid();
                                User user = new User(name,mobile,String.valueOf(bal));
                                mDatabase.child(Uid).setValue(user);

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this,"user not found",Toast.LENGTH_SHORT).show();
                                return;
                            }






                            Toast.makeText(SignupActivity.this, "You have been Registered", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),WelcomeActivity.class).putExtra("curr_user",cu_user.getUid());
                            startActivity(intent);
                            finish();

                        } else if(!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "We were not able to Register you...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            onSignupFailed();
                        }

                    }
                });




        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String username = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirm_pass = _reEnterPasswordText.getText().toString();
        String MobilePattern = "[0-9]{10}";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_pass) || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            valid=false;
        }
        else if (username.length() > 10 || username.length()<3) {

            _nameText.setError("Between 3 to 10 characters");
            valid=false;
        }
        else if (!email.matches(emailPattern)) {

            _emailText.setError("Please Enter Valid Email Address");
            valid=false;
        } else if (!mobile.matches(MobilePattern)) {
            _mobileText.setError("Please Enter Valid Mobile Number");
            valid=false;

        } else if (password.length() < 5 || password.length() > 20) {
            _passwordText.setError("Password length must between 5 and 20 ");
            valid=false;

        }
        else if(!password.matches(PASSWORD_PATTERN))
        {
            _passwordText.setError("Password Must Be::   " +
                    "->Atleast One Capital Alphabet  " +
                    "->Atleast One Small Alphabet  " +
                    "->Atleast One Number(0-9)  " +
                    "->Atleast One From (@,#,$,%,^,&,+,=)");
            valid=false;
        }

        else if (!password.equals(confirm_pass)) {
            _reEnterPasswordText.setError("Passwords does not match ");
            valid=false;
        }
        else
        {
            _reEnterPasswordText.setError(null);
            _passwordText.setError(null);
            _mobileText.setError(null);
            _emailText.setError(null);
            _nameText.setError(null);
            valid=true;
        }

        return valid;




    }
}