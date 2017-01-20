package loginpage.tarangparikh.com.loginpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private EditText editTextDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        /*if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }*/


        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textSignIn = (TextView) findViewById(R.id.textSignIn);
        buttonRegister.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
    }
    private void register(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Email Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            //return;
        }
        progressDialog.setMessage("We are adding you buddy.... :)");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 Toast.makeText(MainActivity.this,"You have been Registered",Toast.LENGTH_SHORT).show();
                                 progressDialog.dismiss();
                                 finish();
                                 startActivity(new Intent(getApplicationContext(),ProfileActivity.class));


                             }
                             else{
                                 Toast.makeText(MainActivity.this,"We were not able to Register you.",Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this,ProfileActivity.class));
            //SignIn page
        }
    }


}
