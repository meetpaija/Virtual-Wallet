package loginpage.tarangparikh.com.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button mTransfer = (Button) view.findViewById(R.id.button4);
        Button mRequest = (Button) view.findViewById(R.id.button5);
        final TextView mtextview1 = (TextView) view.findViewById(R.id.welcome_tv);
        final TextView mtextview2 = (TextView) view.findViewById(R.id.curr_bal_tv);
       final String uid = this.getArguments().getString("curr_user");

        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("users");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            assert uid != null;
            mdatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    mtextview1.setText("Welcome..  " + user.username);
                    mtextview2.setText("Current Balance.. " + user.curr_balance);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(),"Sorry we can not reach you",Toast.LENGTH_LONG).show();

                }
            });
        }

            mTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TransferActivity.class).putExtra("curr_user",uid);
                    ((WelcomeActivity) getActivity()).startActivity(intent);
                }
            });

        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestActivity.class).putExtra("curr_user",uid);
                ((WelcomeActivity) getActivity()).startActivity(intent);
            }
        });

            return view;
        }



}
