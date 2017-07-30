package com.placeholder.newyorktimessearch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.placeholder.newyorktimessearch.models.Article;
import com.placeholder.newyorktimessearch.R;

public class ArticleActivity extends AppCompatActivity {

    ProgressDialog loadweb;
   private WebView webView; private Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("The New York Times");
        // Recuperation de l'url et le Webview
         article = (Article) getIntent().getSerializableExtra("article");
         webView = (WebView) findViewById(R.id.wvArticle);

        //Methode d'inisialisation
       webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

           @Override
           public void onPageStarted(WebView view, String url, Bitmap favicon) {
               super.onPageStarted(view, url, favicon);
               loadweb = new ProgressDialog(ArticleActivity.this);
               loadweb.setMessage("Chargement ...");
               loadweb.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               loadweb.show();
           }
           @Override
           public void onPageFinished(WebView view, String url) {
               super.onPageFinished(view, url);
               if(loadweb!=null){
                   loadweb.dismiss();
               }
           }

        });
       this.webView.loadUrl(article.getWebUrl());
    }
// recuperation du menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        android.support.v7.widget.ShareActionProvider miShare = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // recupere l'url courant
        shareIntent.putExtra(Intent.EXTRA_TEXT, this.article.getWebUrl());

        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }



    private void loadArticle(Article article) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());

    }


}
