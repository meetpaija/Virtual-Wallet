package loginpage.tarangparikh.com.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import loginpage.tarangparikh.com.loginpage.Register.MainActivity;
import loginpage.tarangparikh.com.loginpage.Register.User;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ProgressBar userProgress;
    ProgressBar emailProgress;
    ProgressBar balanceProgress;
    TextView user;
    TextView email;
    TextView balance;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_profile, container, false);
        user=(TextView)view.findViewById(R.id.username);
        email=(TextView)view.findViewById(R.id.email);
        balance=(TextView)view.findViewById(R.id.curr_bal);
        userProgress=(ProgressBar)view.findViewById(R.id.username_progress);
        emailProgress=(ProgressBar)view.findViewById(R.id.email_progress);
        balanceProgress=(ProgressBar)view.findViewById(R.id.wallet_progress);
        TextView signout=(TextView)view.findViewById(R.id.signouttv);




try {
    CheckConnection connection=new CheckConnection(getContext());
    if(connection.connected())
    {

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(getContext(), "Successfully  Signout..", Toast.LENGTH_SHORT).show();

                    return;
                }
            }
        });

        userProgress.setVisibility(View.VISIBLE);
        emailProgress.setVisibility(View.VISIBLE);
        balanceProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final String uid = this.getArguments().getString("curr_user");

        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String bal = "₹ "+dataSnapshot.getValue(User.class).curr_balance;
                    String curr_user = dataSnapshot.getValue(User.class).username;
                    userProgress.setVisibility(View.GONE);
                    balanceProgress.setVisibility(View.GONE);

                    user.setText(curr_user);
                    balance.setText(bal);

                    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                    if(firebaseUser!=null)
                    {
                        try {
                            emailProgress.setVisibility(View.GONE);
                            email.setText(firebaseUser.getEmail().toString());
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                catch (Exception e)
                {Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                return;}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                return;
            }
        });


        ImageView edit=(ImageView)view.findViewById(R.id.edit_details);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),EditProfileActivity.class).putExtra("curr_user",uid);
                startActivity(i);
                return;
            }
        });
    }
    else {
        Toast.makeText(getActivity(),"Check ur connection and try again..",Toast.LENGTH_SHORT).show();

    }
}
catch (Exception e)
{
    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

}
            return view;
    }

}
