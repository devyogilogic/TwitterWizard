package com.developer.twitterwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> {
    Context context;
    ArrayList<String>tweets;
    ArrayList<String>counter;

    public DataAdapter(Context context, ArrayList<String> tweets, ArrayList<String> counter) {
        this.context = context;
        this.tweets = tweets;
        this.counter = counter;

    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View A= LayoutInflater.from(context).inflate(R.layout.data,null);
        return new DataHolder(A);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
      holder.tweet.setText(tweets.get(position));
      holder.tweetcounter.setText(counter.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class  DataHolder extends RecyclerView.ViewHolder{
        TextView tweet;
        TextView tweetcounter;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            tweet=(TextView)itemView.findViewById(R.id.showtweet);
            tweetcounter=(TextView)itemView.findViewById(R.id.tweetCounter);
        }
    }

}
