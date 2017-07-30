package com.placeholder.newyorktimessearch.models;

import android.text.TextUtils;
import com.placeholder.newyorktimessearch.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by domin on 26-Jul-17.
 */
public class Article implements Serializable {
    private static final String IMAGE_PREFIX = "http://www.nytimes.com/";
    public String webUrl;
    String headline;
    String thumbnail;
    String snippet;
    public String newsDesk;
    public int colorId;
    boolean isArticlePreview;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public String getSnippet(){return snippet;}
    public String getNewsDesk(){return newsDesk;}

    public Article(String webUrl, String headline, String thumbnail, String snippet, String newsDesk) {
        this.webUrl = webUrl;
        this.headline = headline;
        this.thumbnail = thumbnail;
        this.snippet=snippet;
        this.newsDesk=newsDesk;
    }

    // Recuperation des champs dans le fichier json

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            snippet = jsonObject.getString("snippet");
            if (TextUtils.isEmpty(snippet) || snippet.equals("null")) {
                snippet = "";
            }
           // this.news_desck=jsonObject.getString("news_desck");
            //this.snipet=jsonObject.getString("snipet");
            // verifier si un champ posede une image
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                this.thumbnail = IMAGE_PREFIX + multimedia.getJSONObject(0).getString("url");
            } else {
                this.thumbnail = "";
            }

            setNewsDesk(jsonObject);
            setColorId();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Methode couleur atribuer a chaque newDesk
    private void setColorId() {
        colorId = R.color.accent;
        //if (isArticlePreview) {
            colorId = R.color.paid_subscription;
     //  } else {
            if (!TextUtils.isEmpty(newsDesk)) {
                if (newsDesk.equalsIgnoreCase("arts")) {
                    colorId = R.color.news_desk_art;
                } else if (newsDesk.equalsIgnoreCase("sports")) {
                    colorId = R.color.news_desk_sports;
                } else if (newsDesk.equalsIgnoreCase("fashion & style")) {
                    colorId = R.color.news_desk_fashion;
                }
            }
        }
   // }

    private void setNewsDesk(JSONObject jsonObject) throws JSONException {
        newsDesk = jsonObject.getString("news_desk");
        if (newsDesk.equalsIgnoreCase("null") || newsDesk.equalsIgnoreCase("none")) {
            // effacer les donnes de newsDesk
            newsDesk = "";

        }
    }
// creer erray list
    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> results = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++ ) {
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
