package edu.umassd.emergencycontact;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jayesh on 10/4/16.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater Inflater;
    private List<Contact> items;

    public CustomListAdapter(Activity activity, List<Contact> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (Inflater == null) {
            Inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View itemView = Inflater.inflate(R.layout.list_item,parent,false);
        TextView cName = (TextView) convertView.findViewById(R.id.cName);
        TextView cNumber = (TextView) convertView.findViewById(R.id.cNumber);

        if (cName!=null) {
            //cName.setText(items.get(position));
        }
        if (cNumber!=null) {

        }


        return null;
    }
}
