package loginpage.tarangparikh.com.loginpage;

/**
 * Created by Meet Paija on 06-02-2017.
 */
public class User {
    public String username;
    public String mobile;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String mobile) {
        this.username = username;
        this.mobile = mobile;
    }
}
