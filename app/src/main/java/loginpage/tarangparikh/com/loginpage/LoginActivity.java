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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textSignUp;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
        }

        editTextEmail=(EditText)findViewById(R.id.editTextEmail1);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword1);
        buttonSignIn=(Button)findViewById(R.id.buttonSignIn);
        textSignUp=(TextView)findViewById(R.id.textSignUp);
        progressDialog=new ProgressDialog(this);


        buttonSignIn.setOnClickListener(this);
        textSignUp.setOnClickListener(this);


    }
    private void userlogin(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Email Address",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        /*else if (!email.matches(emailPattern)) {

            Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!password.matches(PASSWORD_PATTERN))
        {
            Toast.makeText(this,"enter valid password  ",Toast.LENGTH_SHORT).show();
            return;
        }*/
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                        }


                    }
                });
    }

    @Override
    public void onClick(View view) {
      if(view==buttonSignIn) {
          userlogin();
      }
      if(view==textSignUp){
          finish();
          startActivity(new Intent(this,MainActivity.class));
      }
    }
}
