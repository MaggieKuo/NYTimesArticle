package com.mag.nytimesarticle.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mag.nytimesarticle.R;
import com.mag.nytimesarticle.models.Article;

import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ArticleHolder>{
    ArrayList<Article> articles;
    RecyclerViewClick recyclerViewClick;


    public ArticleRecyclerAdapter(ArrayList<Article> articles, RecyclerViewClick recyclerViewClick) {
        this.articles = articles;
        this.recyclerViewClick = recyclerViewClick;
    }

    public void setData(ArrayList<Article> articles){
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_result, null);
        return new ArticleHolder(view, recyclerViewClick);
    }

    @Override
    public void onBindViewHolder(ArticleHolder holder, int position) {
        if (position < 0) return;

        holder.setData(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return (articles==null ? 0 : articles.size());
    }

    public interface RecyclerViewClick{
        void OnItemClick(Article article);

    }

    public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        RecyclerViewClick recyclerViewClick;
        Article article;

        public ArticleHolder(View itemView, RecyclerViewClick recyclerViewClick) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.recyclerViewClick = recyclerViewClick;
            itemView.setOnClickListener(this);
        }

        public void setData(Article article){
            this.article = article;

            tvTitle.setText(article.getHeadline());
            if (!TextUtils.isEmpty(article.getThumbNail())){
                Glide.with(itemView.getContext())
                        .load(article.getThumbNail())
                        .into(ivImage);

            }else{
                ivImage.setImageResource(0);
            }
        }

        @Override
        public void onClick(View v) {
            recyclerViewClick.OnItemClick(article);

        }
    }
}
