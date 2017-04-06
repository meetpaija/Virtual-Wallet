package loginpage.tarangparikh.com.loginpage.Request;

/**
 * Created by Meet Paija on 08-03-2017.
 */

public class Request {
    public String status;
    public String mobile;
    public String amount;
    public String datetime;
    public String username;

    public Request()
    {}

    public Request(String status, String mobile,String amount,String datetime,String username) {
        this.mobile=mobile;
        this.amount = amount;
        this.status=status;
        this.datetime=datetime;
        this.username=username;
    }
}
