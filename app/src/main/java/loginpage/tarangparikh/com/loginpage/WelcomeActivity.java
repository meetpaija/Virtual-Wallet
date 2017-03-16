package loginpage.tarangparikh.com.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import loginpage.tarangparikh.com.loginpage.chat.ChatFragment;

public class WelcomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String s=getIntent().getStringExtra("curr_user");
        //noinspection SimplifiableIfStatement
        if (id == R.id.qr_gen) {
            Intent i=new Intent(WelcomeActivity.this,QR_gen_Activity.class).putExtra("curr_user",s);
            startActivity(i);
            return true;
        }
        else if (id == R.id.qr_sca) {
            Intent i=new Intent(WelcomeActivity.this,QR_scanner_Activity.class).putExtra("curr_user",s);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        String s=getIntent().getStringExtra("curr_user");
        final Bundle bundle = new Bundle();
        bundle.putString("curr_user",s);
        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.main_container,fragment).commit();
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                         fragment = new HomeFragment();
                            break;
                    case R.id.history:
                        fragment = new HistoryFragment();
                        break;
                    case R.id.chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }
                fragment.setArguments(bundle);
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });


/*

                if(item.getItemId()==R.id.profile) {
                    FrameLayout frameLayout=(FrameLayout)findViewById(R.id.frame);
                    Button logout = (Button)frameLayout.findViewById(R.id.logout_btn);
                    logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(WelcomeActivity.this, SignupActivity.class)); //Go back to home page
                                Toast.makeText(WelcomeActivity.this, "Successfully  Signout..", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
                }
                return true;
            }
        }
        );*/
    }

}