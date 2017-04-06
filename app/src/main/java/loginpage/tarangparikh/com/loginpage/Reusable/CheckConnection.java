package loginpage.tarangparikh.com.loginpage.Reusable;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Meet Paija on 04-04-2017.
 */

public class CheckConnection {

    Context context;
    public CheckConnection(Context context)
    {
        this.context=context;
    }

    public boolean connected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

}
