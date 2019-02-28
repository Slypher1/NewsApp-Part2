package com.example.slypher.newsapp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.slypher.newsapp.databinding.ItemListBinding;

import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter<News>{

    private static final String STRING_EMPTY = " ";
    ItemListBinding binding;

    public NewsAdapter(Activity context, ArrayList<News> news){
        super(context,0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_list, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        News currentNews = getItem(position);

        /** check if present the author's name.
         *  if present the authorTextView is visible and show the author's name
         *  if not present, author name contain " " (see {@link News})
          */

        if(currentNews.getAuthorName().equals(STRING_EMPTY))
        {
            binding.authorTextView.setVisibility(View.GONE);
        } else {
            binding.authorTextView.setVisibility(View.VISIBLE);
        }
        // check if present the date of publication
        if(currentNews.getPublicationDate().isEmpty()){
            binding.publicationDateTextView.setVisibility(View.GONE);
        } else {
            binding.publicationDateTextView.setVisibility(View.VISIBLE);
        }

        setBackgroundCardView(currentNews.getTitle().toLowerCase());

        binding.setNews(currentNews);

        binding.executePendingBindings();

        return binding.getRoot();
    }

    /**
     * This method changes the background based on the news of the heroes of the Marvel cinematic universe
     * @param title is the title of news
     */
    private void setBackgroundCardView(String title) {
        if(title.contains(getContext().getString(R.string.fantastic4))){
            binding.backgroundImageView.setImageResource(R.drawable.fantastic4);
            return;
        }
        if(title.contains(getContext().getString(R.string.blackPanther))){
            binding.backgroundImageView.setImageResource(R.drawable.blackpanther);
            return;
        }
        if(title.contains(getContext().getString(R.string.blackWidow))){
            binding.backgroundImageView.setImageResource(R.drawable.blackwidow);
            return;
        }
        if(title.contains(getContext().getString(R.string.xmen))){
            binding.backgroundImageView.setImageResource(R.drawable.xmen);
            return;
        }
        if(title.contains(getContext().getString(R.string.avengers))){
            binding.backgroundImageView.setImageResource(R.drawable.avengers);
            return;
        }
        if(title.contains(getContext().getString(R.string.antman))){
            binding.backgroundImageView.setImageResource(R.drawable.antman);
            return;
        }
        if(title.contains(getContext().getString(R.string.doctorStrange))){
            binding.backgroundImageView.setImageResource(R.drawable.doctorstrange);
            return;
        }
        if(title.contains(getContext().getString(R.string.hulk))){
            binding.backgroundImageView.setImageResource(R.drawable.hulk);
            return;
        }
        if(title.contains(getContext().getString(R.string.spiderman))){
            binding.backgroundImageView.setImageResource(R.drawable.spiderman);
            return;
        }
        if(title.contains(getContext().getString(R.string.thor))){
            binding.backgroundImageView.setImageResource(R.drawable.thor);
            return;
        }
        if(title.contains(getContext().getString(R.string.capitainamerica))){
            binding.backgroundImageView.setImageResource(R.drawable.captainamerica);
            return;
        }
        if(title.contains(getContext().getString(R.string.ironman))){
            binding.backgroundImageView.setImageResource(R.drawable.ironman);
            return;
        }
        if(title.contains(getContext().getString(R.string.guardiansOfTheGalaxy))){
            binding.backgroundImageView.setImageResource(R.drawable.guardiansofthegalaxy);
            return;
        }
        if(title.contains(getContext().getString(R.string.venom))){
            binding.backgroundImageView.setImageResource(R.drawable.venom);
            return;
        }

        // default
        binding.backgroundImageView.setImageResource(R.drawable.mcu);
    }
}