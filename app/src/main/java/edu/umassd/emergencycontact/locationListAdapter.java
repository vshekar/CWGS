package edu.umassd.emergencycontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jayesh on 11/19/16.
 */

public class locationListAdapter extends BaseAdapter {


    ArrayList<Location> list;
    Context context;
    LayoutInflater mInflater;

    public locationListAdapter(Context c, ArrayList<Location> list)
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
    public long getItemId(int i) {
        return i;
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
        Location currentListData = (Location) getItem(position);
        mViewHolder.tv_name.setText(currentListData.getlName());
        mViewHolder.tv_description.setText(currentListData.getLatLng().toString());
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
