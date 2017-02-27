package com.mag.nytimesarticle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mag.nytimesarticle.ArticleActivity;
import com.mag.nytimesarticle.R;
import com.mag.nytimesarticle.adapters.ArticleRecyclerAdapter;
import com.mag.nytimesarticle.fragments.SettingFragment;
import com.mag.nytimesarticle.listeners.EndlessRecyclerViewScrollListener;
import com.mag.nytimesarticle.models.Article;
import com.mag.nytimesarticle.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity2 extends AppCompatActivity implements ArticleRecyclerAdapter.RecyclerViewClick {

    private static final String TAG = SearchActivity2.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.articleRecyclerView)
    RecyclerView gvResults;

    ArrayList<Article> articles;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private ArticleRecyclerAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.d(TAG, "Query Text Submit = " + query);
                search(query, 0);

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d(TAG, "Query Text Chante = " + newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                FragmentManager fragmentManager = getSupportFragmentManager();
                SettingFragment settingFragment = SettingFragment.newInstance("Setting");
                settingFragment.show(fragmentManager, "setting");
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        articles = new ArrayList<>();
        gvResults.setHasFixedSize(true);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gvResults.setLayoutManager(gridLayoutManager);

        adapter = new ArticleRecyclerAdapter(articles, this);
        gvResults.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "page=" + page);
                Log.d(TAG, "total items count" + totalItemsCount);
                search(null, page);
            }
        };
        gvResults.addOnScrollListener(scrollListener);

        search(null, 0);

    }

    void search(String query, int page) {
//        Log.d(TAG, "search onclick");
        if (query!=null)
            this.query = query;

        if (articles != null)
            articles.clear();

        ANRequest.GetRequestBuilder an = AndroidNetworking.get(getString(R.string.news_url));
        an.addQueryParameter("api-key", getString(R.string.ny_search_api_key));

        if (query!=null && !"".equals(this.query))
                an.addQueryParameter("q", this.query);

        an.addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("begin_date", Utils.getStringDate(-2))
                .addQueryParameter("sort", "newest");

        an.build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            articles.addAll(Article.fromJSONArray(response.getJSONObject("response").getJSONArray("docs")));
                            adapter.setData(articles);

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

    @Override
    public void OnItemClick(Article article) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", Parcels.wrap(article));

        startActivity(intent);
    }
}
