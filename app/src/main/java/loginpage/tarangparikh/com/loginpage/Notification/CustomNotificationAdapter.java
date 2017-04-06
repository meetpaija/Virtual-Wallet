package loginpage.tarangparikh.com.loginpage.Notification;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import loginpage.tarangparikh.com.loginpage.R;

/**
 * Created by Meet Paija on 05-04-2017.
 */
public class CustomNotificationAdapter extends BaseAdapter {


    Context context;
    ArrayList<RowItemNotify> rowItems;

    public CustomNotificationAdapter(Context context, ArrayList<RowItemNotify> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }


    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }


    private class ViewHolderNotify
    {
        ImageView profile_img;
        TextView user_mobile;
        TextView content;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            ViewHolderNotify viewHolder = null;

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.notify_list_view, null);
                viewHolder = new ViewHolderNotify();

                viewHolder.profile_img = (ImageView) convertView.findViewById(R.id.profile_img_notify);
                viewHolder.user_mobile = (TextView) convertView.findViewById(R.id.user_mobile_notify);
                viewHolder.content = (TextView) convertView.findViewById(R.id.content_notify);
                viewHolder.time = (TextView) convertView.findViewById(R.id.datetime_notify);

                RowItemNotify rowpos = rowItems.get(position);
                String user_mobile= rowpos.getUser()+ "(" + rowpos.getMobile() + ")";
                String content=rowpos.getUser() + " requested " + rowpos.getAmount() + "/- amount from you.";

                viewHolder.user_mobile.setText(user_mobile);
                viewHolder.content.setText(content);
                viewHolder.time.setText(rowpos.getTime().toString());
            }

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return convertView;
    }
}
