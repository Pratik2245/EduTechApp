package com.example.javacp.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.javacp.R;
import com.example.javacp.model.SubscribedModelStudent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubscribedCourseAdapter extends RecyclerView.Adapter<SubscribedCourseAdapter.ViewHolder> {

    private Context context;
    private List<SubscribedModelStudent> courseList;

    public SubscribedCourseAdapter(Context context, List<SubscribedModelStudent> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCourseThumbnail;
        TextView textCourseTitle, textTeacherName, textSubscribedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCourseThumbnail = itemView.findViewById(R.id.imageCourseThumbnail);
            textCourseTitle = itemView.findViewById(R.id.textCourseTitle);
            textTeacherName = itemView.findViewById(R.id.textTeacherName);
            textSubscribedAt = itemView.findViewById(R.id.textSubscribedAt);
        }
    }

    @NonNull
    @Override
    public SubscribedCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_subscribedcourse_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull SubscribedCourseAdapter.ViewHolder holder, int position) {
        SubscribedModelStudent course = courseList.get(position);

        holder.textCourseTitle.setText(course.getCourseTitle());

        holder.textTeacherName.setText("By " +
                (course.getTeacherName() != null ? course.getTeacherName() : "Unknown"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(course.getSubscribedAt()));
        holder.textSubscribedAt.setText("Subscribed on " + formattedDate);
          String secureUrl=course.getCourseThumbnailUrl().replace("http://", "https://");
        Glide.with(context)
                .load(secureUrl)  // or getCourseThumbnailUrl() if using annotation
                .into(holder.imageCourseThumbnail);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
