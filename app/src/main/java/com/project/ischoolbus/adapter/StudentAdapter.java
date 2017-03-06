package com.project.ischoolbus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Students;
import com.project.ischoolbus.tools.Tools;
import com.squareup.picasso.Picasso;

/**
 * Created by Puttipong New on 20/3/2559.
 */
public class StudentAdapter extends BaseAdapter {

    Students students;
    Context context;

    public void setStudents(Students students) {
        this.students = students;
    }

    public StudentAdapter(Students students) {
        this.students = students;
    }

    @Override
    public int getCount() {
        if (students == null)
            return 0;
        if (students.getStudent() == null)
            return 0;
        return students.getStudent().size();
    }

    @Override
    public Object getItem(int position) {
        return students.getStudent().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup student) {

        convertView = LayoutInflater.from(student.getContext()).inflate(R.layout.adapter_list_student, student, false);

        ImageView imgList = (ImageView) convertView.findViewById(R.id.imgList);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
        TextView tvSchoolName = (TextView) convertView.findViewById(R.id.tvSchoolName);

        Picasso.with(this.context).load(R.drawable.sid_1).into(imgList);


        tvId.setText(students.getStudent().get(position).getStudentId());
        tvNickName.setText(students.getStudent().get(position).getStudentNickname());
        tvSchoolName.setText(students.getStudent().get(position).getSchoolName());
        return convertView;
    }

}

