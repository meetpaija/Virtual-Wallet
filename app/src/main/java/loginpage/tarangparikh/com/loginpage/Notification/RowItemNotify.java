package loginpage.tarangparikh.com.loginpage.Notification;

/**
 * Created by Meet Paija on 05-04-2017.
 */
public class RowItemNotify {

    String user;
    String mobile;
    String status;
    String time;
    String amount;
    String reqest_key;

    public RowItemNotify(String user,String mobile, String status, String time,String amount,String reqest_key) {
        this.user= user;
        this.mobile=mobile;
        this.status = status;
        this.time = time;
        this.amount=amount;
        this.reqest_key=reqest_key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReqest_key() {
        return reqest_key;
    }

    public void setReqest_key(String reqest_key) {
        this.reqest_key = reqest_key;
    }
}
