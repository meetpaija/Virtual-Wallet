package loginpage.tarangparikh.com.loginpage.History;

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
import loginpage.tarangparikh.com.loginpage.Reusable.CheckConnection;

/**
 * Created by Meet Paija on 05-04-2017.
 */

public class CustomHistoryAdapter extends BaseAdapter {


    Context context;
    ArrayList<RowItem> rowItems;

    public CustomHistoryAdapter(Context context, ArrayList<RowItem> rowItems) {
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


    public class ViewHolder
    {
        ImageView profile_img;
        TextView user_mobile;
        TextView content;
        TextView time;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            CheckConnection connection=new CheckConnection(context);
            if(connection.connected()) {
                ViewHolder viewHolder = null;

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.history_list_view, null);
                    viewHolder = new ViewHolder();

                    viewHolder.profile_img = (ImageView) convertView.findViewById(R.id.profile_img);
                    viewHolder.user_mobile = (TextView) convertView.findViewById(R.id.user_mobile);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.content);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.datetime);

                    RowItem rowpos = rowItems.get(position);
                    viewHolder.user_mobile.setText(rowpos.getUser_mobile().toString());
                    viewHolder.content.setText(rowpos.getContent().toString());
                    viewHolder.time.setText(rowpos.getTime().toString());
                }
            }
            else {
                Toast.makeText(context,"Check ur connection and try again..",Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return convertView;
    }
}
