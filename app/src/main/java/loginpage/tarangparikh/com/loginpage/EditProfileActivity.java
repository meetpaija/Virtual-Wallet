package loginpage.tarangparikh.com.loginpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import loginpage.tarangparikh.com.loginpage.Register.User;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;

public class EditProfileActivity extends AppCompatActivity {
    EditText user;
    EditText mobile;
    EditText oldpassword;
    EditText newpassword;
    DatabaseReference databaseReference;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done){
            user=(EditText)findViewById(R.id.username);
            mobile=(EditText)findViewById(R.id.mobile);
            oldpassword=(EditText)findViewById(R.id.oldpassword);
            newpassword=(EditText)findViewById(R.id.password);
            String uid=getIntent().getStringExtra("curr_user");
           // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
            if (!validate()) {
                return false;
            }

            CheckConnection checkConnection=new CheckConnection(this);

            if(checkConnection.connected()) {
                final String username = user.getText().toString();
                final String mob = mobile.getText().toString();
                final String oldpwd = oldpassword.getText().toString();
                final String newpwd =newpassword.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Details...");
                progressDialog.show();

                databaseReference=FirebaseDatabase.getInstance().getReference();

                databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            dataSnapshot.getRef().child("username").setValue(username);
                            dataSnapshot.getRef().child("mobile").setValue(mob);

                            if(!TextUtils.isEmpty(oldpwd) && !TextUtils.isEmpty(newpwd)) {
                                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                final String email = firebaseUser.getEmail();
                                AuthCredential credential = EmailAuthProvider.getCredential(email, oldpwd);

                                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        try {
                                            if (task.isSuccessful()) {
                                                firebaseUser.updatePassword(newpwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        try {
                                                            if (!task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                return;
                                                            } else {
                                                                String uid = getIntent().getStringExtra("curr_user");
                                                                Intent i = new Intent(getApplicationContext(), WelcomeActivity.class).putExtra("curr_user", uid);
                                                                progressDialog.dismiss();
                                                                startActivity(i);
                                                                Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        catch(Exception e){
                                                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Your old password is incorrect", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        }
                                        catch(Exception e){
                                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }
                            else {

                                String uid = getIntent().getStringExtra("curr_user");
                                Intent i = new Intent(getApplicationContext(), WelcomeActivity.class).putExtra("curr_user", uid);
                                progressDialog.dismiss();
                                startActivity(i);
                                Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Check your connection and Try again",Toast.LENGTH_SHORT).show();
                return true;
            }
        } return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Edit Profile Details");

        user=(EditText)findViewById(R.id.username);
        mobile=(EditText)findViewById(R.id.mobile);
        oldpassword=(EditText)findViewById(R.id.oldpassword);
        newpassword=(EditText)findViewById(R.id.password);
        TextView pwd_hint=(TextView)findViewById(R.id.pass_hint);
        final RelativeLayout pwdlayout=(RelativeLayout)findViewById((R.id.changepwdlayout));

        pwd_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdlayout.setVisibility(View.VISIBLE);
            }
        });

        String uid=getIntent().getStringExtra("curr_user");

        CheckConnection conn=new CheckConnection(getApplicationContext());
        if(conn.connected())
        {
            databaseReference= FirebaseDatabase.getInstance().getReference();

            databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                        user.setText(dataSnapshot.getValue(User.class).username.toString());
                        mobile.setText(dataSnapshot.getValue(User.class).mobile.toString());
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Check Your Connection and try again..",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = user.getText().toString().trim();
        String mob=mobile.getText().toString().trim();
        String oldpwd=oldpassword.getText().toString().trim();
        String newpwd=newpassword.getText().toString().trim();
        String MobilePattern = "[0-9]{10}";



        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(mob) ) {
            Toast.makeText(this, "pls fill the username and mobile_no fields", Toast.LENGTH_SHORT).show();
            valid=false;
        }
        else if (username.length() > 10 || username.length()<3) {

            user.setError("Between 3 to 10 characters");
            valid=false;
        } else if (!mob.matches(MobilePattern)) {
            mobile.setError("Please Enter Valid Mobile Number");
            valid=false;

        } else if(TextUtils.isEmpty(oldpwd) || TextUtils.isEmpty(newpwd)) {
            if (TextUtils.isEmpty(oldpwd) && TextUtils.isEmpty(newpwd)) {
            } else {
                Toast.makeText(this, "pls fill the passwords fields", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        else if(!TextUtils.isEmpty(oldpwd) && !TextUtils.isEmpty(newpwd))
        {
            if (oldpwd.length() < 6 || oldpwd.length() > 20) {
                oldpassword.setError("Password length must between 6 and 20 ");
                valid = false;
            }
            if (newpwd.length() < 6 || newpwd.length() > 20) {
                newpassword.setError("Password length must between 6 and 20 ");
                valid = false;
            }
        }
        else
        {
            user.setError(null);
            mobile.setError(null);
            oldpassword.setError(null);
            newpassword.setError(null);
            valid=true;
        }

        return valid;
    }
}
