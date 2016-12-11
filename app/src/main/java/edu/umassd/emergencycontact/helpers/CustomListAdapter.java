package edu.umassd.emergencycontact.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umassd.emergencycontact.R;
import edu.umassd.emergencycontact.classes.Contact;

/**
 * Created by Jayesh on 10/4/16.

 followed this guide on setting up custom adapter
http://stackoverflow.com/questions/36563010/custom-base-adapter-to-listview

 */
public class CustomListAdapter extends BaseAdapter {


    ArrayList<Contact> list;
    Context context;
    LayoutInflater mInflater;

    public CustomListAdapter(Context c, ArrayList<Contact> list)
    {
        context = c;
        this.list = list;
        mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        Contact currentListData = (Contact) getItem(position);
        mViewHolder.tv_name.setText(currentListData.getPname());
        mViewHolder.tv_description.setText(currentListData.getPnumber());
        return convertView;
    }
    private class MyViewHolder {
        TextView tv_name, tv_description;


        public MyViewHolder(View item) {
            tv_name = (TextView) item.findViewById(R.id.cName);
            tv_description = (TextView) item.findViewById(R.id.cNumber);

        }
    }

}
