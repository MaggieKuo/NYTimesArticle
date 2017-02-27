package com.mag.nytimesarticle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mag.nytimesarticle.models.Article;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = ArticleActivity.class.getSimpleName();
    @BindView(R.id.webView)
    WebView webView;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        article = Parcels.unwrap(getIntent().getParcelableExtra("article"));

        loadArticle();

    }

    private void loadArticle() {
        if (article == null) return;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(article.getWebUrl());
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());
    }
}
