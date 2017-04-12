package loginpage.tarangparikh.com.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import loginpage.tarangparikh.com.loginpage.Register.MainActivity;
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_profile, container, false);

try {
    CheckConnection connection=new CheckConnection(getContext());
    if(connection.connected())
    {
    Button btn = (Button) view.findViewById(R.id.button3);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                Toast.makeText(getActivity(), "Successfully  Signout..", Toast.LENGTH_SHORT).show();
                return;
            }
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
