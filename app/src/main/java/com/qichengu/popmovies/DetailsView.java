package com.qichengu.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class DetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        Intent intent = getIntent();
        ((TextView) findViewById(R.id.title)).setText(intent.getStringExtra("title"));
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("posterUrl")).into((ImageView) findViewById(R.id.thumbnail));
        ((TextView) findViewById(R.id.release_date)).setText(intent.getStringExtra("release_date"));
        ((TextView) findViewById(R.id.user_rating)).setText(intent.getStringExtra("rating"));
        ((TextView) findViewById(R.id.synopsis)).setText(intent.getStringExtra("synopsis"));
    }
}
