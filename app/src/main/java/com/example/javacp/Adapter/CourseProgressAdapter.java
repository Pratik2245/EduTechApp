package com.example.javacp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.javacp.R;
import com.example.javacp.model.CourseModelProgressFragment;

import java.util.List;

public class CourseProgressAdapter extends RecyclerView.Adapter<CourseProgressAdapter.ViewHolder> {
    private List<CourseModelProgressFragment> courseList;

    public CourseProgressAdapter(List<CourseModelProgressFragment> courseList) {
        this.courseList = courseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, teacher, percent;
        ProgressBar progressBar;
        ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.courseTitle);
            teacher = view.findViewById(R.id.teacherName);
            percent = view.findViewById(R.id.progressPercent);
            progressBar = view.findViewById(R.id.progressBar);
            thumbnail = view.findViewById(R.id.thumbnailImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_progress, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseModelProgressFragment course = courseList.get(position);
        holder.title.setText(course.courseTitle);
        holder.teacher.setText("By: " + course.teacherName);
        holder.percent.setText((int) course.progressPercent + "%");
        holder.progressBar.setProgress((int) course.progressPercent);
        Glide.with(holder.thumbnail.getContext()).load(course.thumbnail).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}

