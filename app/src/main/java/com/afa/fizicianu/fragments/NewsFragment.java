package com.afa.fizicianu.fragments;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afa.fizicianu.R;
import com.afa.fizicianu.models.Listing;
import com.afa.fizicianu.models.Post;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


public class NewsFragment extends Fragment {


    private String REDDIT_URL = "https://www.reddit.com/r/science.json?limit=5";
    private ImageView iThumbnail;
    private TextView textView;
    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        textView = (TextView) view.findViewById(R.id.title);
        //Downloading posts from Reddit
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(REDDIT_URL)
                .build();
        Listing listing;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getActivity(),"Pentru a vedea ultimele stiri conecteaza-te la internet!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Listing listing = new Gson().fromJson(response.body().string().toString(), Listing.class);
                Log.v("TEST",listing.getPostList().get(0).getTitle().toString());
            }
        });

       // Picasso.with(getActivity()).load(listing.getPostList().get(1).getThumbnail()).into(iThumbnail);



    }


}
