package com.qichengu.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.qichengu.popmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainDiscoveryActivity extends AppCompatActivity {
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    private class movieListQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL...url) {
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(url[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieSearchResults;
        }
        @Override
        protected void onPostExecute(String response) {
            loadImages(response);
        }
    }
    private void loadImages(String response) {
        try {
            final List<Movie> movies = getMovies(response);
            CustomListAdapter movieAdapter = new CustomListAdapter(this, movies);
            // Get a reference to the ListView, and attach this adapter to it.
            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent i = new Intent();
                    i.putExtra("title", movies.get(position).title);
                    i.putExtra("posterUrl", movies.get(position).posterUrl);
                    i.putExtra("synopsis", movies.get(position).synopsis);
                    i.putExtra("rating", Double.toString(movies.get(position).vote_average) + "/10");
                    i.putExtra("release_date", movies.get(position).release_date);
                    i.setClass(getApplicationContext(), DetailsView.class);
                    startActivity(i);
                }
            });
        } catch (org.json.JSONException e) {
            Log.e("JSON parsing error", e.toString());
        }
    }
    private List<Movie> getMovies(String response) throws org.json.JSONException {
        List<Movie> list = new ArrayList<>();
        JSONObject movieResults = new JSONObject(response);
        JSONArray results = movieResults.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject each = results.getJSONObject(i);
            list.add(new Movie(each.getString("title"), each.getString("poster_path"), each.getDouble("vote_average"), each.getString("overview"), each.getString("release_date")));
        }
        return list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery);
        makePopMoviesSearchQuery("popular");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mostPopular) {
            makePopMoviesSearchQuery("popular");
            return true;
        }
        if (id == R.id.action_topRated) {
            makePopMoviesSearchQuery("topRated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void makePopMoviesSearchQuery(String choice) {
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "not online", Toast.LENGTH_SHORT).show();
            return;
        }
        URL movieSearchUrl = NetworkUtils.buildUrl(choice);
        new movieListQueryTask().execute(movieSearchUrl);
    }

}
