package loginpage.tarangparikh.com.loginpage;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Meet Paija on 12-03-2017.
 */



public class Adapter extends RecyclerView.Adapter<Adapter.PersonViewHolder> {

    ArrayList<Request> person;


    public static class PersonViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView mobile;
        ImageView pic;
        TextView content;
        public PersonViewHolder(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cv);
            mobile=(TextView)itemView.findViewById(R.id.person_name);
            pic=(ImageView) itemView.findViewById(R.id.person_pic);
            content=(TextView)itemView.findViewById(R.id.person_content);

        }
    }

    Adapter(ArrayList<Request> persons)
    {
            this.person=persons;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv)
    {
        super.onAttachedToRecyclerView(rv);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        PersonViewHolder pvh=new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.mobile.setText(person.get(position).mobile  +":");
        holder.content.setText(person.get(position).mobile +" requested "+person.get(position).amount +" ruppes from you");
        holder.pic.setImageResource(person.get(position).photoid);
    }

    @Override
    public int getItemCount() {
        return person.size();
    }
}
