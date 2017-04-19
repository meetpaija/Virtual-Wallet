package loginpage.tarangparikh.com.loginpage.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Register.User;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;

public class MessageActivity extends AppCompatActivity {

    private Button send_msg;
    private EditText type_msg;
    private TextView conversation;
    private ActionBar actionBar;
    private DatabaseReference databaseReference;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        try {

            actionBar = getSupportActionBar();
            send_msg = (Button) findViewById(R.id.buttonSend);
            conversation = (TextView) findViewById(R.id.message_tv);
            type_msg = (EditText) findViewById(R.id.msgtype);

            final String chat_user = getIntent().getExtras().getString("chat_user");
            final String uid = getIntent().getStringExtra("curr_user");
            actionBar.setTitle(chat_user);
            final CheckConnection conn=new CheckConnection(getApplicationContext());

            send_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (conn.connected())
                    {
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                try {
                                    String curr_user = dataSnapshot.getValue(User.class).username;
                                    String chat_room = create_roomname(curr_user.toLowerCase(), chat_user.toLowerCase());
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    String temp_key = databaseReference.child("chat").child(chat_room).push().getKey();

                                    DatabaseReference message_root = databaseReference.child("chat").child(chat_room).child(temp_key);
                                    Map<String, Object> map2 = new HashMap<String, Object>();
                                    map2.put("name", curr_user);
                                    map2.put("msg", type_msg.getText().toString() + "\n");

                                    message_root.updateChildren(map2);
                                    type_msg.setText(null);

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Check your connection and then Restart it..", Toast.LENGTH_SHORT).show();
                    return;
                }

                }
            });


            if(conn.connected()) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String curr_user = dataSnapshot.getValue(User.class).username;

                            String chat_room = create_roomname(curr_user.toLowerCase(), chat_user.toLowerCase());

                            databaseReference.child("chat").child(chat_room).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    try {
                                        append_chat_conversion(dataSnapshot);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    try {
                                        append_chat_conversion(dataSnapshot);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(),"Check your connection and then Restart it..", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    private String append_chat_msg,append_username;
    private void append_chat_conversion(DataSnapshot dataSnapshot)
    {
        try{
        Iterator i=dataSnapshot.getChildren().iterator();
        //Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
        while(i.hasNext())
        {
            append_chat_msg=(String )((DataSnapshot)i.next()).getValue();
            append_username=(String )((DataSnapshot)i.next()).getValue();
            conversation.append(append_username +" : " +append_chat_msg+ " ");
        }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private String create_roomname(String user1,String user2)
    {
        List<String> strings = Arrays.asList(user1,user2);
        Collections.sort(strings, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        return strings.get(0)+strings.get(1);
    }
}
