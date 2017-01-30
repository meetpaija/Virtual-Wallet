package loginpage.tarangparikh.com.loginpage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Meet Paija on 27-01-2017.
 */

public class home_fragment extends Fragment{
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view= inflater.inflate(R.layout.home_layout,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
