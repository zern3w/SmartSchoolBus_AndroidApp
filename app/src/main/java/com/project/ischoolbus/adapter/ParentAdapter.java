package com.project.ischoolbus.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Parents;
import com.project.ischoolbus.tools.Tools;

/**
 * Created by Puttipong New on 20/3/2559.
 */
public class ParentAdapter extends BaseAdapter {

    Parents parents;

    public void setParents(Parents parents) {
        this.parents = parents;
    }

    public ParentAdapter(Parents parents) {
        this.parents = parents;
    }

    @Override
    public int getCount() {
        if (parents == null)
            return 0;
        if (parents.getParent() == null)
            return 0;
        return parents.getParent().size();
    }

    @Override
    public Object getItem(int position) {
        return parents.getParent().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_parent, parent, false);

        ImageView imgList = (ImageView) convertView.findViewById(R.id.imgList);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvTel = (TextView) convertView.findViewById(R.id.tvTel);

        String stringImg = parents.getParent().get(position).getPhoto();
//        Log.wtf("photo", stringImg);
        Bitmap imgParent = Tools.decodeBase64(stringImg);

        imgList.setImageBitmap(imgParent);
            tvId.setText(parents.getParent().get(position).getParentId());
            tvName.setText(parents.getParent().get(position).getParentFirstname() + "  " + parents.getParent().get(position).getParentLastname());
            tvTel.setText(parents.getParent().get(position).getMobileTel());
            return convertView;
    }

}
