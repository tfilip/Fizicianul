package com.afa.fizicianu.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
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
    private Handler mHandler;
    private CardView mCard;
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
        Picasso.with(getActivity()).load(R.drawable.progress_image).into(iThumbnail);
        //Downloading posts from Reddit
        OkHttpClient client = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        Request request = new Request.Builder()
                .url(REDDIT_URL)
                .build();

        mCard = (CardView) view.findViewById(R.id.cardV);
        if(isNetworkAvailable()) {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), "Pentru a vedea ultimele stiri conecteaza-te la internet!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final Listing listing = new Gson().fromJson(response.body().string().toString(), Listing.class);
                    Log.v("TEST", listing.getPostList().get(0).getTitle().toString());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(listing.getPostList().get(2).getTitle().toString());
                            Picasso.with(getActivity()).load(listing.getPostList().get(2).getThumbnail()).into(iThumbnail);
                            mCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = listing.getPostList().get(2).getPermalink().toString();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Pentru a vedea ultimele stiri conecteaza-te la internet!", Toast.LENGTH_LONG).show();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
