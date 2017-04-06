package loginpage.tarangparikh.com.loginpage.Register;

/**
 * Created by Meet Paija on 06-02-2017.
 */
public class User {
    public String username;
    public String mobile;
    public String curr_balance;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String mobile ,String curr_balance) {
        this.username = username;
        this.mobile = mobile;
        this.curr_balance=curr_balance;
    }

}
