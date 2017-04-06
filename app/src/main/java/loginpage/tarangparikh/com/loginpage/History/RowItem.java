package loginpage.tarangparikh.com.loginpage.History;

/**
 * Created by Meet Paija on 05-04-2017.
 */

public class RowItem {

    String user_mobile;
    String content;
    String time;

    public RowItem(String user_mobile, String content, String time) {
        this.user_mobile = user_mobile;
        this.content = content;
        this.time = time;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
