package com.example.javacp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.javacp.R;
import com.example.javacp.Teacher.TeacherManageCourseActivity;
import com.example.javacp.model.CoursesModelTeacher;

import java.util.List;

public class CourseAdapterTeacher extends RecyclerView.Adapter<CourseAdapterTeacher.CourseViewHolder> {
    private Context context;
    private List<CoursesModelTeacher> courseList;

    public CourseAdapterTeacher(Context context, List<CoursesModelTeacher> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_item_teacher, parent, false);
        return new CourseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CoursesModelTeacher course = courseList.get(position);
        holder.title.setText(course.getTitle());
        holder.description.setText(course.getDescription());
        holder.price.setText("₹" + course.getPrice());
        Log.d("GlideDebug", "Thumbnail URL: " + course.getThumbnailUrl());


        // Load thumbnail from Cloudinary URL using Glide
        String secureUrl = course.getThumbnailUrl().replace("http://", "https://");
        Glide.with(context)
                .load(secureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Image Load Failed: " + e.getMessage());
                        return false;  // Keep default error handling
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }


                })
                .into(holder.thumbnail);

          holder.EditCourse.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeacherManageCourseActivity.class);
            intent.putExtra("title", course.getTitle());
            intent.putExtra("description", course.getDescription());
            intent.putExtra("price", course.getPrice());
            intent.putExtra("thumbnailUrl", course.getThumbnailUrl());
            intent.putExtra("videoUrl", course.getVideoUrl());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price;
        ImageView thumbnail;
        Button EditCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvCourseTitle);
            description = itemView.findViewById(R.id.tvCourseDescription);
            price = itemView.findViewById(R.id.tvCoursePrice);
            thumbnail = itemView.findViewById(R.id.ivCourseThumbnail);
            EditCourse=itemView.findViewById(R.id.EditCourse);
        }
    }
}
