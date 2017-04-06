package loginpage.tarangparikh.com.loginpage.chat;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import loginpage.tarangparikh.com.loginpage.R;

public class MessageActivity extends AppCompatActivity {

    private Button send_msg;
    private EditText type_msg;
    private TextView conversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        try {
            ActionBar actionBar = getActionBar();

            send_msg = (Button) findViewById(R.id.buttonSend);
            conversation = (TextView) findViewById(R.id.message_tv);
            type_msg = (EditText) findViewById(R.id.msgtype);

            final String chat_user = getIntent().getExtras().getString("chat_user");
            final String uid = getIntent().getStringExtra("curr_user");
            final String room_id = getIntent().getStringExtra("room_id");
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
/*
        if(!room_id.equals("not_exist"))
        {
            DatabaseReference dr_chat= FirebaseDatabase.getInstance().getReference().child("chat");
            dr_chat.child(room_id).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 //   update_conversion(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                  //  update_conersion(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            dr_chat.child(room_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   //String msg=dataSnapshot.getValue(Chat.class).chat_conversion;//mistake
                    conversation.setText("hii \n hello");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            conversation.setText(null);
        }

        setTitle(chat_user);

        final DatabaseReference dr_chat=FirebaseDatabase.getInstance().getReference().child("chat");

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg=type_msg.getText().toString();
                final DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("users");


                dr.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user=dataSnapshot.getValue(User.class).username;
                        conversation.append(user +" : "+msg+"\n");
                        type_msg.setText(null);


                        Query query = dr.orderByChild("username").equalTo(chat_user);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String rec_key = null;
                                String room_id=null;
                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    // do something with the individual "issues"
                                    rec_key =issue.getKey();
                                    room_id = uid + rec_key;

                                }
                                final String finalRec_key = rec_key;
                                final String finalRoom_id = room_id;
                                dr_chat.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Chat chat = new Chat(uid, finalRec_key,conversation.getText().toString());
                                        dr_chat.child(finalRoom_id).setValue(chat);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        */
    }



/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(this, ChatFragment.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
