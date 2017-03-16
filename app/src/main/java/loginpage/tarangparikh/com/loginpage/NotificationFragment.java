package loginpage.tarangparikh.com.loginpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Request> A_notification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();

        Adapter adapter=new Adapter(A_notification);
        recyclerView.setAdapter(adapter);
        return view;

    }



    private  void  initializeData()
    {
        A_notification=new ArrayList<Request>();
        final DatabaseReference mRequestDB= FirebaseDatabase.getInstance().getReference("pending_request");
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser cu_user = firebaseAuth.getCurrentUser();
        final String uid = this.getArguments().getString("curr_user");

        mRequestDB.child(uid).orderByChild("status").equalTo("Request_sent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        String mobile = issue.getValue(Request.class).mobile;
                        String amount = issue.getValue(Request.class).amount;
                        int photoid = R.drawable.noavatar;

                        A_notification.add(new Request(photoid, mobile, amount));
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"No Request Yet....",Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

