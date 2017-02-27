package com.mag.nytimesarticle.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mag.nytimesarticle.R;
import com.mag.nytimesarticle.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by perfumekuo on 2017/2/24.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    Context context;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = this.getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
            viewHolder = new ViewHolder(convertView, context);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(article);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        Context context;

        ViewHolder(View view, Context context) {
            ButterKnife.bind(this, view);
            this.context = context;
        }

        public void setData(Article article){
            tvTitle.setText(article.getHeadline());
            if (!TextUtils.isEmpty(article.getThumbNail())){
                Glide.with(context)
                        .load(article.getThumbNail())
                        .into(ivImage);

            }else{
                ivImage.setImageResource(0);
            }
        }
    }
}
