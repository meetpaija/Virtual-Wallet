package loginpage.tarangparikh.com.loginpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class WelcomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
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
                        fragment = new HomeFragment();
                        break;
                    case R.id.chat:
                        fragment = new HomeFragment();
                        break;
                    case R.id.notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }
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