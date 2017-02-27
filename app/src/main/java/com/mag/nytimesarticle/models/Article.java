package com.mag.nytimesarticle.models;

import com.mag.nytimesarticle.R;
import com.mag.nytimesarticle.util.NYApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import lombok.Data;


@Data
@Parcel
public class Article {
    String webUrl;
    String headline;
    String thumbNail;

    public Article(){}
    public Article(JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray media = jsonObject.getJSONArray("multimedia");
            if (media.length()>0){
                this.thumbNail = NYApplication.getContext().getString(R.string.ny_article_url) + media.getJSONObject(0).getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> articles = new ArrayList<>();

        if (array!=null && array.length()>0){
            for(int i=0; i < array.length(); i++){
                try {
                    articles.add(new Article(array.getJSONObject(i))) ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return articles;
    }


}
