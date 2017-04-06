package loginpage.tarangparikh.com.loginpage.History;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.Transfer.Transfer;

public class HistoryFragment extends Fragment {

    ListView listView;
    ArrayList<RowItem> rowItems;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_history, container, false);

    listView = (ListView) view.findViewById(R.id.history_list);
        rowItems=new ArrayList<RowItem>();
        final String uid=this.getArguments().getString("curr_user");

        try {

            CheckConnection checkConnection=new CheckConnection(getContext());
            if(checkConnection.connected()) {
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("transfers").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            int count = 0;
                            int child = (int) dataSnapshot.getChildrenCount();
                            final String[] user_mobile_Array = new String[child];
                            final String[] time_Array = new String[child];
                            final String[] content_Array = new String[child];


                            for (DataSnapshot i : dataSnapshot.getChildren()) {

                                Transfer transfer = i.getValue(Transfer.class);
                                if (transfer.status.equals("Credit")) {
                                    content_Array[count] = "You successfully received " + transfer.amount + "/- amount from this user.";
                                } else if (transfer.status.equals("Debit")) {
                                    content_Array[count] = "You successfully transmitted " + transfer.amount + "/- amount to this user.";
                                }

                                time_Array[count] = transfer.datetime;
                                String rec_user = transfer.username;
                                final String mobile = transfer.mobile;
                                user_mobile_Array[count] = rec_user + "(" + mobile + ")";
                                count++;
                            }
                            for (int i = 0; i < count; i++) {
                                RowItem item = new RowItem(user_mobile_Array[i], content_Array[i], time_Array[i]);
                                rowItems.add(item);
                            }
                            CustomHistoryAdapter customAdapter = new CustomHistoryAdapter(getContext(), rowItems);
                            listView.setAdapter(customAdapter);
                        } else {
                            Toast.makeText(getActivity(), "No Transaction Done Yet..", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            else {
                Toast.makeText(getActivity(),"Check ur connection and try again..",Toast.LENGTH_SHORT).show();

            }

        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();

        }
        return view;
    }

}
