package com.placeholder.newyorktimessearch.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.placeholder.newyorktimessearch.R;
import com.placeholder.newyorktimessearch.SpacesItemDecoration;
import com.placeholder.newyorktimessearch.adapters.ArticleAdapter;
import com.placeholder.newyorktimessearch.erreur.ErrorHandler;
import com.placeholder.newyorktimessearch.fragments.SettingsFragment;
import com.placeholder.newyorktimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.placeholder.newyorktimessearch.listeners.RecyclerItemClickListener;
import com.placeholder.newyorktimessearch.models.Article;
import com.placeholder.newyorktimessearch.utils.NetworkUtil;
import com.placeholder.newyorktimessearch.models.SearchFilter;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class SearchActivity extends AppCompatActivity
        implements SettingsFragment.OnSettingsChangeListener, DatePickerDialog.OnDateSetListener {
    public static final String FILENAME = "searchFilter.txt";
    public static final String NYT_API_KEY ="689060939a7741dd8b0051f6ea66a8d3";
    public static final String NYT_ARTICLE_SEARCH_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private Article article;
    // Variable de connexion
    ProgressDialog progressDialog;
private CoordinatorLayout coordinatorLayout;

    RecyclerView rvResults;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    SearchFilter searchFilter;
    String searchQuery;
    String numSearchResults;
    SettingsFragment settingsDialog;
    TextView tvNumResults;
    TextView tvSearchQueryValue;
    TextView tvSearchFilterTitle;
    TextView tvSearchFilterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchFilter = loadSearchFilter();
        setTitle("The New York Times");
        setupViews();
        loadDialog();

    }

    public void setupViews() {
        articles = new ArrayList<>();

        tvSearchQueryValue = (TextView) findViewById(R.id.tvSearchQueryValue);
        tvNumResults = (TextView) findViewById(R.id.tvNumResults);
        tvSearchFilterTitle = (TextView) findViewById(R.id.tvSearchFilterTitle);
        tvSearchFilterValue = (TextView) findViewById(R.id.tvSearchFilterValue);

        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        adapter = new ArticleAdapter(articles);
        rvResults.setAdapter(adapter);

      /*  StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        rvResults.addItemDecoration(decoration);*/

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rvResults,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                               // if(article!=null) {
                                    Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                                   Article article = articles.get(position);
                                    i.putExtra("article", article);
                                    startActivity(i);
                               // }else {
                                        // gestion de la connexion, genner des erreur
                                    //ErrorHandler.logAppError("Peut pas lancer l'activiter, article peut etre vide");
                                    //ErrorHandler.displayError(SearchActivity.this, NetworkUtil.DEFAULT_ERROR_MESSAGE);
                                }

                            //}

                            @Override
                            public void onItemLongClick(View view, int position) {
                                // ...
                            }
                        }));
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override

            public void onLoadMore(int page, int totalItemsCount) {

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreArticles(page);

            }
        });


        searchForArticles("Top Stories");
    }

    public void loadDialog(){

        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        //progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) getActionView(searchItem);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForArticles(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //action sur les menu
        int id = item.getItemId();

        // affichage dialoge de filtre
        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void searchForArticles(String query) {
        boolean connexion = NetworkUtil.checkConnexion(this);
        if (!connexion) {

            Toast.makeText(this, "impossible de continuer, verifier la connexion internet", Toast.LENGTH_LONG).show();
        } else {
            searchQuery = query;
            tvSearchQueryValue.setText(searchQuery);
            if (searchFilter.isFilterSet()) {
                tvSearchFilterTitle.setVisibility(View.GONE);
                tvSearchFilterValue.setVisibility(View.GONE);
                tvSearchFilterValue.setText(searchFilter.toString());
            } else {
                tvSearchFilterTitle.setVisibility(View.GONE);
                tvSearchFilterValue.setVisibility(View.GONE);
            }

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(NYT_ARTICLE_SEARCH_URL,
                    getSearchQueryParams(query, 0),
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            JSONArray articleJsonResults = null;
                            try {
                                JSONObject responseObj = response.getJSONObject("response");
                                articleJsonResults = responseObj.getJSONArray("docs");
                                numSearchResults = responseObj.getJSONObject("meta").getString("hits");
                                tvNumResults.setText(numSearchResults);
                                adapter.swap(Article.fromJsonArray(articleJsonResults));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void loadMoreArticles(int page) {
        boolean connexion= NetworkUtil.checkConnexion(this);
        if(!connexion)
        {

            Toast.makeText(this,"impossible de continuer, verifier la connexion internet", Toast.LENGTH_LONG).show();
        } else {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(NYT_ARTICLE_SEARCH_URL,
                    getSearchQueryParams(searchQuery, page),
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            JSONArray articleJsonResults = null;
                            try {
                                articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                                articles.addAll(Article.fromJsonArray(articleJsonResults));
                                int curSize = adapter.getItemCount();
                                adapter.notifyItemRangeInserted(curSize, articles.size() - 1);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
        }
    }



    private void showSettingsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        settingsDialog = SettingsFragment.newInstance(searchFilter);
        settingsDialog.show(fm, "fragment_edit_name");
    }

    // http://developer.nytimes.com/docs/read/article_search_api_v2
    private RequestParams getSearchQueryParams(String query, int page) {
        RequestParams params = new RequestParams();
        params.put("q", query);
        params.put("page", page);
        params.put("api-key", NYT_API_KEY);

        String beginDateYYYYMMDD = searchFilter.getBeginDate(SearchFilter.FORMAT_YYYYMMDD);
        if (!TextUtils.isBlank(beginDateYYYYMMDD)) {
            params.put("begin_date", beginDateYYYYMMDD);
        }

        String sortOrder = searchFilter.getSortOrder();
        if (!TextUtils.isBlank(sortOrder)) {
            params.put("sort", sortOrder);
        }

        Set ndTopics = searchFilter.getNewsDeskTopics();
        if (ndTopics.size() > 0) {
            // &fq=news_desk:("Sports" "Foreign")
            params.put("fq", "news_desk:(\"" + StringUtils.join(ndTopics.toArray(), "\" \"") + "\")");
        }

        Log.d("DEBUG", ">>>>>" + params.toString());
        return params;
    }

    public void saveSearchFilterSettings(SearchFilter newSearchFilter) {
        searchFilter = newSearchFilter;
        saveSearchFilter(searchFilter);
        searchForArticles(searchQuery);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
        if (settingsDialog != null) {
            settingsDialog.populateSetDate(year, month, day);
        }
    }


    private void saveSearchFilter(SearchFilter filter) {
        try {
            FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(filter);
            os.close();
            fos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public SearchFilter loadSearchFilter() {
        SearchFilter filter = new SearchFilter();
        try {
            FileInputStream fis = this.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            filter = (SearchFilter) is.readObject();
            is.close();
            fis.close();
            return (filter);
        } catch (ClassNotFoundException cnfe) {
            Log.e("Exception", "ClassNotFoundException: " + cnfe.toString());
        } catch (IOException e) {
            Log.e("Exception", "IOException: " + e.toString());
        }
        return filter;
    }

}

