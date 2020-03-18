package com.example.cindy31715.linetv_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DramaDetailActivity extends AppCompatActivity {
    private ImageView detail_image;
    private TextView detail_name, detail_created_at, detail_total_views, detail_rating;
    private RatingBar detail_ratingBar;
    private ClassDramaInfo drama_detail;
    private DecimalFormat mDecimalFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_detail);

        init();

        Intent it = getIntent();
        drama_detail = (ClassDramaInfo) it.getSerializableExtra("drama_detail");
        Log.d("---","name : "+drama_detail.getName());
        setTitle(drama_detail.getName());
        showInfo();
    }

    private void showInfo(){
        String views = mDecimalFormat.format(drama_detail.getTotal_views());
        detail_name.setText("劇名："+drama_detail.getName());
        detail_created_at.setText("出品時間："+drama_detail.getCreated_at().substring(0,10)+" "+drama_detail.getCreated_at().substring(11,19));
        detail_total_views.setText("觀看次數："+views);
        detail_rating.setText("評價："+String.valueOf(drama_detail.getRating()));
        detail_ratingBar.setRating((float) drama_detail.getRating());
        new MAsyncTask(detail_image).execute(drama_detail.getThumb());
    }

    private void init(){
        detail_image = findViewById(R.id.detail_dramaimage);
        detail_name = findViewById(R.id.detail_name);
        detail_created_at = findViewById(R.id.detail_created_at);
        detail_total_views = findViewById(R.id.detail_total_views);
        detail_rating = findViewById(R.id.detail_rating);
        detail_ratingBar = findViewById(R.id.detail_ratingBar);
        detail_ratingBar.setNumStars(5);
        detail_ratingBar.setIsIndicator(true);
    }
}
