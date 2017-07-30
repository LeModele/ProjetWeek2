package com.placeholder.newyorktimessearch.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.placeholder.newyorktimessearch.R;
import com.placeholder.newyorktimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by domin on 26-Jul-17.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>  {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        TextView headline;
        TextView snippet;
        TextView newsDesk;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.ivImage);
            headline = (TextView) itemView.findViewById(R.id.tvTitle);
            snippet=(TextView)itemView.findViewById(R.id.snippetTextView);
            newsDesk=(TextView)itemView.findViewById(R.id.newsDeskTextView);
        }
    }

    // Store a member variable for the contacts
    private List<Article> mArticles;
    private Context context;

    // Pass in the contact array into the constructor
    public ArticleAdapter(List<Article> articles) {
        mArticles = articles;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        // instancier view holder
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {

        Article article = mArticles.get(position);

        // clear out recycled image
        viewHolder.thumbnail.setImageResource(0);

        viewHolder.headline.setText(article.getHeadline());
        viewHolder.snippet.setText(article.getSnippet());
        viewHolder.newsDesk.setText(article.getNewsDesk());

        if (article != null) {
            String thumbnail = article.getThumbnail();
            if (!TextUtils.isEmpty(thumbnail)) {
                viewHolder.thumbnail.setVisibility(View.VISIBLE);
               Glide.with(context).load(thumbnail).placeholder(R.drawable.loading_placeholder)
                        .fitCenter().centerCrop()
                        .into(viewHolder.thumbnail);

            } else {
                viewHolder.thumbnail.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(article.newsDesk)) {
                viewHolder.newsDesk.setVisibility(View.VISIBLE);
                viewHolder.newsDesk.setBackgroundColor(ContextCompat.getColor(context, article.colorId));
            } else {
                viewHolder.newsDesk.setVisibility(View.GONE);
            }

        }
    }
    public void swap(List<Article> articles){
        mArticles.clear();
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
