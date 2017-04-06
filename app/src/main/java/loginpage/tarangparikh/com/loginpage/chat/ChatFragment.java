package loginpage.tarangparikh.com.loginpage.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Register.User;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;


public class ChatFragment extends Fragment {
        private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chat, container, false);

        final String uid = this.getArguments().getString("curr_user");
        listView = (ListView) view.findViewById(R.id.chat_list);
        try {
            CheckConnection connection=new CheckConnection(getContext());

            if(connection.connected())
            {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            ref.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get map of users in datasnapshot
                            ArrayList<String> user = new ArrayList<String>();

                            //iterate through each user, ignoring their UID
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {

                                //Get user map
                                User u = issue.getValue(User.class);
                                String rec_id = issue.getKey();

                                if (!uid.equals(rec_id)) {
                                    user.add(u.username);
                                }
                            }

                            ArrayAdapter<String> ArAd = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, user);
                            listView.setAdapter(ArAd);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                        }
                    });



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, final View view, int position, long arg) {

                    final String chat_user = ((TextView) view).getText().toString();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference dr = firebaseDatabase.getReference("Users");
                    final DatabaseReference dr_chat = FirebaseDatabase.getInstance().getReference().child("chat");


                    Intent i = new Intent(getActivity(), MessageActivity.class).putExtra("chat_user", chat_user).putExtra("curr_user", uid);
                    startActivity(i);

                }
            });
            }
            else
            {
                Toast.makeText(getContext(),"Check ur Connection and try again..",Toast.LENGTH_SHORT).show();
            }
        }


        catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }
    private void selectItem()
    {

        final String uid=this.getArguments().getString("curr_user");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, final View view, int position, long arg) {
                // TODO Auto-generated method stub
                try{
                final String chat_user = ((TextView) view).getText().toString();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dr = firebaseDatabase.getReference("Users");
                final DatabaseReference dr_chat = FirebaseDatabase.getInstance().getReference().child("chat");



                Query query = dr.orderByChild("username").equalTo(((TextView) view).getText().toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String rec_key = null;

                        String room_id_1 = null;
                        String room_id_2 = null;
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"
                            rec_key = issue.getKey();
                            room_id_1 = uid + rec_key;
                            room_id_2 = rec_key + uid;


                            final String finalRoom_id_2 = room_id_2;
                            final String finalRoom_id_1 = room_id_1;

                            dr_chat.child(room_id_1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent i = new Intent(getActivity(), MessageActivity.class).putExtra("chatuser", chat_user).putExtra("curr_user", uid).putExtra("room_id", finalRoom_id_1);
                                        startActivity(i);
                                    } else {

                                        dr_chat.child(finalRoom_id_2).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Intent i = new Intent(getActivity(), MessageActivity.class).putExtra("chatuser", chat_user).putExtra("curr_user", uid).putExtra("room_id", finalRoom_id_2);
                                                    startActivity(i);
                                                } else {
                                                    Intent i = new Intent(getActivity(), MessageActivity.class).putExtra("chatuser", chat_user).putExtra("curr_user", uid).putExtra("room_id", "not_exist");
                                                    startActivity(i);

                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

}

