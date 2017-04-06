package loginpage.tarangparikh.com.loginpage.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.WelcomeActivity;

public class SignupActivity extends AppCompatActivity {

   public EditText _nameText;
   public EditText _emailText;
   public EditText _mobileText;
   public EditText _passwordText;
  public   EditText _reEnterPasswordText;
   public Button _signupButton;
   public TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            _mobileText=(EditText)findViewById(R.id.input_mobile);
            _nameText=(EditText)findViewById(R.id.input_name);
            _emailText=(EditText)findViewById(R.id.input_email);
            _passwordText=(EditText)findViewById(R.id.input_password);
            _reEnterPasswordText=(EditText)findViewById(R.id.input_reEnterPassword);
            _signupButton = (Button) findViewById(R.id.btn_signup);
            _loginLink = (TextView) findViewById(R.id.link_login);

            FirebaseAuth auth=FirebaseAuth.getInstance();
            FirebaseUser user=auth.getCurrentUser();
            if(user!=null)
            {
                auth.signOut();
                return;
            }

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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.transition.push_right_in, R.transition.push_right_out);
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void signup() {

        if (!validate()) {
            return;
        }
        CheckConnection checkConnection=new CheckConnection(this);

        if(checkConnection.connected())
        {
            final String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            final String mobile = _mobileText.getText().toString();
            String password = _passwordText.getText().toString();
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            final String bal = "100.00";

            //Check mobile no????????

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser cu_user = firebaseAuth.getCurrentUser();
                    if (cu_user != null) {
                        final String Uid = cu_user.getUid();

                        User user = new User(name,mobile,bal);
                        mDatabase.child("Users").child(Uid).setValue(user);


                        Toast.makeText(SignupActivity.this, "You have been Registered", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class).putExtra("curr_user", cu_user.getUid()).putExtra("name", name).putExtra("mobile", mobile);
                        startActivity(intent);


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, "Registration Error..Plz try again", Toast.LENGTH_SHORT).show();
                        return;
                    }



                } else {
                    Toast.makeText(SignupActivity.this, "Email Address is already exist..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

            }
        });

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check Your Connection..",Toast.LENGTH_SHORT).show();
            return;
        }


    }


    public boolean validate() {
        boolean valid = true;
        String username = _nameText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String mobile = _mobileText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String confirm_pass = _reEnterPasswordText.getText().toString().trim();
        String MobilePattern = "[0-9]{10}";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



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

        } else if (password.length() < 6 || password.length() > 20) {
            _passwordText.setError("Password length must between 6 and 20 ");
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
