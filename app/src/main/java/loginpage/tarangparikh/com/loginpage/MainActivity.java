package loginpage.tarangparikh.com.loginpage;

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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private EditText editTextUsername;
    private EditText editTextConfirm;
    private EditText editTextMobile;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        /*if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }*/


        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.Register);

        editTextEmail = (EditText) findViewById(R.id.edittextemail);
        editTextPassword = (EditText) findViewById(R.id.edittextpass);
        editTextUsername = (EditText) findViewById(R.id.edittextuser);
        editTextConfirm = (EditText) findViewById(R.id.edittextconfirm);
        editTextMobile = (EditText) findViewById(R.id.edittextmobile);
        textSignIn = (TextView) findViewById(R.id.textSignIn);
        buttonRegister.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void register() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirm_pass = editTextConfirm.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String mobile = editTextMobile.getText().toString().trim();


        String MobilePattern = "[0-9]{10}";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_pass) || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (username.length() > 10 || username.length()<3) {

            Toast.makeText(getApplicationContext(), "username should between 3 to 10 character ", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!email.matches(emailPattern)) {

            Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        } else if (!mobile.matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 5 || password.length() > 20) {
            Toast.makeText(this, "Password length must between 5 and 20 ", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!password.matches(PASSWORD_PATTERN))
        {
            Toast.makeText(this,"enter valid password  ",Toast.LENGTH_SHORT).show();
            return;
        }

        else if (!password.equals(confirm_pass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference("users");



        progressDialog.setMessage("We are adding you buddy.... :)");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //String userId = mDatabase.push().getKey();

                            FirebaseUser cu_user = firebaseAuth.getCurrentUser();
                            if(cu_user!=null)
                            {
                                String Uid=cu_user.getUid();
                                // creating user object
                                User user = new User(username,mobile);

                                // pushing user to 'users' node using the userId
                                mDatabase.child(Uid).setValue(user);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"user not found",Toast.LENGTH_SHORT).show();
                                return;
                            }






                            Toast.makeText(MainActivity.this, "You have been Registered", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));


                        } else {
                            Toast.makeText(MainActivity.this, "We were not able to Register you.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister)
            register();
        if (view == textSignIn) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            //SignIn page
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /*
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }*/
}
