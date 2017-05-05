package loginpage.tarangparikh.com.loginpage.Notification;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import loginpage.tarangparikh.com.loginpage.R;
import loginpage.tarangparikh.com.loginpage.Register.User;
import loginpage.tarangparikh.com.loginpage.Request.Request;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;
import loginpage.tarangparikh.com.loginpage.Transfer.Transfer;
import loginpage.tarangparikh.com.loginpage.WelcomeActivity;


public class NotificationFragment extends Fragment {
    private ListView listView;
    CheckConnection connection=new CheckConnection(getContext());
    public String uid;
    ArrayList<RowItemNotify> rowItemNotify;
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_notification, container, false);
        listView = (ListView) view.findViewById(R.id.notify_list);
        rowItemNotify=new ArrayList<RowItemNotify>();

            uid=this.getArguments().getString("curr_user");

            mDatabase.child("request").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.exists()) {
                            int count = 0;
                            int child = (int) dataSnapshot.getChildrenCount();
                            final String[] time_Array = new String[child];
                            final String[] user_Array = new String[child];
                            final String[] amount_Array = new String[child];
                            final String[] mobile_Array = new String[child];
                            final String[] status_Array = new String[child];
                            final String[] reqest_key = new String[child];

                            for (DataSnapshot i : dataSnapshot.getChildren()) {
                                    Request request = i.getValue(Request.class);

                                    if (request.status.equals("arrivedRequest")) {
                                        reqest_key[count] = i.getKey();
                                        time_Array[count] = request.datetime;
                                        user_Array[count] = request.username;
                                        status_Array[count] = request.status;
                                        amount_Array[count] = request.amount;
                                        mobile_Array[count] = request.mobile;
                                        count++;
                                    }
                                }

                                for (int i = 0; i < count; i++) {
                                    RowItemNotify item = new RowItemNotify(user_Array[i], mobile_Array[i], status_Array[i], time_Array[i], amount_Array[i], reqest_key[i]);
                                    rowItemNotify.add(item);
                                }
                                CustomNotificationAdapter customAdapter = new CustomNotificationAdapter(getContext(), rowItemNotify);
                                listView.setAdapter(customAdapter);

                            } else {
                                Toast.makeText(getActivity(), "No Requests Yet..", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            return;
                        }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        return;
                    }

                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_LONG).show();
                    return;
                }
            });




listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String rec_user=rowItemNotify.get(position).getUser();
                final String amount=rowItemNotify.get(position).getAmount();
                String status=rowItemNotify.get(position).getStatus();
                String time=rowItemNotify.get(position).getTime();
                final String mobile=rowItemNotify.get(position).getMobile();
                final String reqest_key=rowItemNotify.get(position).getReqest_key();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Are You Sure??");
                alertDialogBuilder.setMessage("You want to transfer "+amount+"/- amount to "+rec_user+" ??");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                mDatabase.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //Toast.makeText(TransferActivity.this,"done",Toast.LENGTH_LONG).show();
                                        User user = dataSnapshot.getValue(User.class);
                                        String curr_balance = user.curr_balance;

                                        if (Double.valueOf(amount) > Double.valueOf(curr_balance)) {
                                            Toast.makeText(getActivity(), "Insufficient Balance", Toast.LENGTH_LONG).show();
                                            return;
                                        } else {
                                            update_bal_at_receiver(rec_user,mobile,amount,uid,reqest_key);
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                });

                            }
                        });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }


        });
        return view;
    }

    private void update_bal_at_receiver(final String user, final String mobile, final String amount, final String uid, final String request_key) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Transfering Amount...");
        progressDialog.show();
        try {
            Query query = mDatabase.child("Users").orderByChild("mobile").equalTo(mobile);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        ArrayList<String> data=new ArrayList<String>();
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"
                            String receiver = issue.getValue(User.class).username;
                            String rec_amt = issue.getValue(User.class).curr_balance;
                            String new_amt = String.valueOf(Double.valueOf(amount) + Double.valueOf(rec_amt));
                            issue.getRef().child("curr_balance").setValue(new_amt);

                            update_bal_at_sender(progressDialog, receiver, user, mobile, amount, uid, request_key);
                            return;
                        }
                    } else {
                        Toast.makeText(getActivity(), "Mobile No not exists...", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void update_bal_at_sender(final ProgressDialog progressDialog, String receiver, final String user, final String mobile, final String amount, final String uid, final String request_key) {

        Query q1=mDatabase.child("Users").child(uid);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            try{

                String sender_amt=dataSnapshot.getValue(User.class).curr_balance;
                String new_amt=String.valueOf(Double.valueOf(sender_amt)-Double.valueOf(amount));
                dataSnapshot.getRef().child("curr_balance").setValue(new_amt);
                sender_transferDB(progressDialog,user,mobile,amount,uid,request_key);
                return;
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                return;
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
    }

    private void sender_transferDB(final ProgressDialog progressDialog, final String user, final String mobile, final String amount, final String uid, final String reqest_key) {
       try {
           final String sender_status = "Debit";
           Transfer transfer = new Transfer(mobile, amount, sender_status, java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()), user);
           mDatabase.child("transfers").child(uid).push().setValue(transfer);
           reciever_transferDB1(progressDialog, uid, mobile, amount, reqest_key);
           return;
       }
       catch (Exception e)
       {
           Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
           return;
       }

    }

    private void reciever_transferDB1(final ProgressDialog progressDialog, final String uid, String mobile, final String amount, final String reqest_key) {
        Query query = mDatabase.child("Users").orderByChild("mobile").equalTo(mobile);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        String rec_key = issue.getKey();
                        receiver_transferDB2(progressDialog,rec_key,uid,amount,reqest_key);
                        return;
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Mobile No not exists...",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
    }

    private void receiver_transferDB2(final ProgressDialog progressDialog, final String rec_key, String uid, final String amount, final String reqest_key) {
        Query q = mDatabase.child("Users").child(uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                String sen_mobile= dataSnapshot.getValue(User.class).mobile;
                String sen_user= dataSnapshot.getValue(User.class).username;
                receiver_transferDB3(progressDialog,rec_key,sen_mobile,sen_user,amount,reqest_key);
                    return;
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getDetails(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
        });
    }

    private void receiver_transferDB3(final ProgressDialog progressDialog, final String rec_key, final String sen_mobile, final String sen_user, final String amount, final String reqest_key) {
        final String reciever_status="Credit";
       try {


           Transfer transfer = new Transfer(sen_mobile, amount, reciever_status, java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()), sen_user);
           mDatabase.child("transfers").child(rec_key).push().setValue(transfer);
           deleteRequestRec(progressDialog, reqest_key, amount, uid);
           return;
       }
       catch (Exception e)
       {
           Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
           return;
       }

    }

    private void deleteRequestRec(final ProgressDialog progressDialog, final String reqest_key, String amount, final String uid) {
        mDatabase.child("request").child(uid).child(reqest_key).orderByChild("amount").equalTo(amount).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getRef().setValue(null);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Transfer Done Successfully...", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), WelcomeActivity.class).putExtra("curr_user", uid);
                    getActivity().finish();
                    startActivity(i);
                    return;
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }


}

