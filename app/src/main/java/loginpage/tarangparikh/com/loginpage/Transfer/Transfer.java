package loginpage.tarangparikh.com.loginpage.Transfer;

/**
 * Created by Meet Paija on 06-02-2017.
 */
public class Transfer {
    public String status;
    public String mobile;
    public String amount;
    public String username;
    public String datetime;

    public Transfer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Transfer(String mobile, String amount,String status,String datetime,String username) {
        this.mobile=mobile;
        this.amount = amount;
        this.status = status;
        this.datetime= datetime;
        this.username=username;
    }

}
