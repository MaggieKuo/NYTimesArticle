package com.mag.nytimesarticle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mag.nytimesarticle.ArticleActivity;
import com.mag.nytimesarticle.R;
import com.mag.nytimesarticle.adapters.ArticleArrayAdapter;
import com.mag.nytimesarticle.models.Article;
import com.mag.nytimesarticle.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.etQuery)
    TextInputLayout etQuery;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.gvResults)
    GridView gvResults;

//    @BindView(R.id.app_bar_search)
//    MenuItem searchItem;

    private ArrayList<Article> articles;
    private ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }

    private void setupViews() {
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
/*        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
//            case R.id.action_settings:
//
//                break;
            case R.id.action_search:

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @OnClick(R.id.btnSearch)
    void search() {
//        Log.d(TAG, "search onclick");
        String query = etQuery.getEditText().getText().toString();

        if (articles != null)
            articles.clear();
        adapter.clear();

        AndroidNetworking.get(getString(R.string.news_url))
                .addQueryParameter("api-key", getString(R.string.ny_search_api_key))
                .addQueryParameter("q", query)
                .addQueryParameter("page", String.valueOf(0))
                .addQueryParameter("begin_date", Utils.getStringDate(-2))
                .addQueryParameter("end_date", Utils.getStringDate(0))
                .addQueryParameter("sort", "newest")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            adapter.addAll(Article.fromJSONArray(response.getJSONObject("response").getJSONArray("docs")));
//                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "response docs size= " + articles.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Snackbar.make(coordinatorLayout, "Error", Snackbar.LENGTH_LONG)
                                .setText("error")
                                .show();
                    }
                });


    }

    @OnItemClick(R.id.gvResults)
    void viewArticle(int position){
        Log.d(TAG, "position=" + position);
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", Parcels.wrap(adapter.getItem(position)));

        startActivity(intent);

    }



}
