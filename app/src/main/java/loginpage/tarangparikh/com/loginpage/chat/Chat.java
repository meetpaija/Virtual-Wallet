package loginpage.tarangparikh.com.loginpage.chat;

/**
 * Created by Meet Paija on 17-03-2017.
 */

public class Chat {
    public String sender_id;
    public String receiver_id;
    public String chat_conversion;

    public Chat(String sender_id, String receiver_id, String chat_conversion) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.chat_conversion = chat_conversion;
    }
}
