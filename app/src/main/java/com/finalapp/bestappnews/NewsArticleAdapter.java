package com.finalapp.bestappnews;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {

    public NewsArticleAdapter(@NonNull Context context, @NonNull List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listofItemView = convertView;
        if (listofItemView == null) {
            listofItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        NewsArticle currentNewsArticle = getItem(position);

        String newsSection = currentNewsArticle.getSectionName();
        TextView sectionNameView = listofItemView.findViewById(R.id.article_section);
        sectionNameView.setText(newsSection);

        String newsTitle = currentNewsArticle.getTitle();
        TextView viewtitle = listofItemView.findViewById(R.id.article_title);
        viewtitle.setText(newsTitle);

        String newsTrail = currentNewsArticle.getTrailText();
        TextView trainview = listofItemView.findViewById(R.id.article_trailtext);

        if (newsTrail != null && !newsTrail.isEmpty()) {
            newsTrail = newsTrail + ".";
            trainview.setText(newsTrail);
        } else {
            trainview.setVisibility(View.GONE);
        }

        String formattedDate = formatDate(currentNewsArticle.getPublishedDate());

        TextView viewdate = listofItemView.findViewById(R.id.article_date);
        viewdate.setText(formattedDate);


        String formattedTime = formatTime(currentNewsArticle.getPublishedDate());

        TextView timeView = listofItemView.findViewById(R.id.article_time);
        timeView.setText(formattedTime);

        String newsAuthor = currentNewsArticle.getAuthor() + " ";
        TextView authorView = listofItemView.findViewById(R.id.article_author);
        authorView.setText(newsAuthor);

        Bitmap photonews = currentNewsArticle.getThumbnail();
        ImageView viewphoto = listofItemView.findViewById(R.id.article_image);
        if (photonews != null && NewsArticleLoader.mThumbnail) {



            viewtitle.setMaxLines(3);
            viewtitle.setMinLines(3);

            ConstraintLayout constraintLayout = listofItemView.findViewById(R.id.newslist_constraint_layout);
            ConstraintSet set = new ConstraintSet();
            set.clone(constraintLayout);
            set.setDimensionRatio(viewphoto.getId(), "16:9");
            set.applyTo(constraintLayout);
            viewphoto.setImageBitmap(photonews);

            set.clear(R.id.article_title, ConstraintSet.START);

            set.connect(R.id.article_title, ConstraintSet.END, R.id.article_image, ConstraintSet.END, 0);
            set.applyTo(constraintLayout);

        } else {
            viewtitle.setMaxLines(4);
            viewtitle.setMinLines(4);

            ConstraintLayout constraintLayout = listofItemView.findViewById(R.id.newslist_constraint_layout);
            ConstraintSet set = new ConstraintSet();

            set.clone(constraintLayout);
            set.setDimensionRatio(viewphoto.getId(), "16:4");
            set.applyTo(constraintLayout);

            viewphoto.setImageResource(R.drawable.no_thumbnail_bg);
            viewphoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

            set.clear(R.id.article_title, ConstraintSet.END);
            set.connect(R.id.article_title, ConstraintSet.END, R.id.article_time, ConstraintSet.START, 0);
            set.connect(R.id.article_title, ConstraintSet.START, R.id.article_image, ConstraintSet.START, 0);
            set.applyTo(constraintLayout);
        }

        return listofItemView;
    }

    private String formatDate(String date) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        Date date_info = null;
        try {
            date_info = inputParser.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM dd ''yy", Locale.US);
        return outputFormatter.format(date_info);
    }

    private String formatTime(String date) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        Date date_info = null;
        try {
            date_info = inputParser.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat outputFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        return outputFormatter.format(date_info);
    }
}