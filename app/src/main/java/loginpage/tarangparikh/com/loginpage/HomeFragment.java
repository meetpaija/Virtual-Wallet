package loginpage.tarangparikh.com.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import loginpage.tarangparikh.com.loginpage.Request.RequestActivity;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.Transfer.TransferActivity;
import loginpage.tarangparikh.com.loginpage.Register.User;


public class HomeFragment extends Fragment {

    ProgressBar user_progressBar;
    ProgressBar bal_progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            CheckConnection connection=new CheckConnection(getContext());

            if(connection.connected())
            {
            Button mTransfer = (Button) view.findViewById(R.id.btn_transfer);
            Button mRequest = (Button) view.findViewById(R.id.btn_request);
            final TextView mtextview1 = (TextView) view.findViewById(R.id.name);
            final TextView mtextview2 = (TextView) view.findViewById(R.id.curr_bal);
            final String uid = this.getArguments().getString("curr_user");
            final String username = this.getArguments().getString("name");
            final String mobile = this.getArguments().getString("mobile");

            bal_progressBar=(ProgressBar)view.findViewById(R.id.curr_bal_progress);
            user_progressBar=(ProgressBar)view.findViewById(R.id.user_progress);
            user_progressBar.setVisibility(View.VISIBLE);
            bal_progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users");

            mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String bal=dataSnapshot.getValue(User.class).curr_balance;
                    String user=dataSnapshot.getValue(User.class).username;
                    user_progressBar.setVisibility(View.GONE);
                    bal_progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    mtextview1.setText(user);
                    mtextview2.setText(bal);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            });




        mTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TransferActivity.class).putExtra("curr_user", uid);
                    ((WelcomeActivity) getActivity()).startActivity(intent);
                }
            });

            mRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RequestActivity.class).putExtra("curr_user", uid);
                    ((WelcomeActivity) getActivity()).startActivity(intent);
                }
            });
            }
            else {
                Toast.makeText(getActivity(),"Check ur connection and try again..",Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

        }
        return view;

    }
}


