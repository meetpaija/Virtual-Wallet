package loginpage.tarangparikh.com.loginpage;

/**
 * Created by Meet Paija on 08-03-2017.
 */

public class Request {
    public String status;
    public String mobile;
    public String amount;
    public int photoid;

    public Request(int photoId,String mobile,String amount)
    {
        this.amount=amount;
        this.photoid=photoId;
        this.mobile=mobile;
    }

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Request(String status, String mobile,String amount) {
        this.mobile=mobile;
        this.amount = amount;
        this.status=status;

    }
}
