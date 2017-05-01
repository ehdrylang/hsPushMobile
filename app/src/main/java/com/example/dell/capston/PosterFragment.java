package com.example.dell.capston;

/**
 * Created by dell on 2016-04-02.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PosterFragment extends Fragment {
    public static final String ARG_PAGE = "PAGE";

    Context context;

    RadioGroup radioGroup;
    RadioButton radioButton_1, radioButton_2, radioButton_3, radioButton_4;

    private TextView titleview1,titleview2,titleview3,titleview4,titleview5;

    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private ImageView small_imageView1,small_imageView2,small_imageView3,small_imageView4,small_imageView5;
    private int mPage;

    ScrollView scrollView;
   /* private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> image_urlList = new ArrayList<String>();*/


    public static PosterFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PosterFragment fragment = new PosterFragment();
        fragment.setArguments(args);
        return fragment;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.poster_beta, container, false);

        MyApplication myApplication = MyApplication.instance();
        //temp = myApplication.value;

        scrollView =(ScrollView)view.findViewById(R.id.scrollView);
        small_imageView1=(ImageView) view.findViewById(R.id.small_imageView1);
        small_imageView2=(ImageView) view.findViewById(R.id.small_imageView2);
        small_imageView3=(ImageView) view.findViewById(R.id.small_imageView3);
        small_imageView4=(ImageView) view.findViewById(R.id.small_imageView4);
        small_imageView5=(ImageView) view.findViewById(R.id.small_imageView5);



        titleview1 = (TextView) view.findViewById(R.id.titleView1);
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);

        titleview2 = (TextView) view.findViewById(R.id.titleView2);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);

        titleview3 = (TextView) view.findViewById(R.id.titleView3);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);

        titleview4 = (TextView) view.findViewById(R.id.titleView4);
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);

        titleview5 = (TextView) view.findViewById(R.id.titleView5);
        imageView5 = (ImageView) view.findViewById(R.id.imageView5);


       /* MyApplication myApplication = MyApplication.instance();
        temp = myApplication.value;*/
        //for문으로 바꿀것
        titleview1.setText(myApplication.urls[0]);
        Glide.with(this).load(myApplication.images[0])
                .into(small_imageView1);

        Glide.with(this).load(myApplication.images[0]).into(imageView1);

        titleview2.setText(myApplication.urls[1]);
        Glide.with(this).load(myApplication.images[1]).into(small_imageView2);
        Glide.with(this).load(myApplication.images[1]).into(imageView2);

        titleview3.setText(myApplication.urls[2]);
        Glide.with(this).load(myApplication.images[2]).into(small_imageView3);
        Glide.with(this).load(myApplication.images[2]).into(imageView3);

        titleview4.setText(myApplication.urls[3]);
        Glide.with(this).load(myApplication.images[3]).into(small_imageView4);
        Glide.with(this).load(myApplication.images[3]).into(imageView4);

        titleview5.setText(myApplication.urls[4]);
        Glide.with(this).load(myApplication.images[4]).into(small_imageView5);
        Glide.with(this).load(myApplication.images[4]).into(imageView5);

        //Log.e("TAGGG",myApplication.image4);


        small_imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.scrollTo(0,0);
            }
        });

        small_imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0,imageView1.getHeight());

            }
        });

        small_imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0,imageView1.getHeight()+imageView2.getHeight());

            }
        });

        small_imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0,imageView1.getHeight()+imageView2.getHeight()+imageView3.getHeight());

            }
        });

        small_imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0,imageView1.getHeight()+imageView2.getHeight()+imageView3.getHeight()+imageView4.getHeight());

            }
        });


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://" + (String) titleview1.getText()));
                startActivity(myIntent);

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://"+(String) titleview2.getText()));
                startActivity(myIntent);

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://"+(String) titleview3.getText()));
                startActivity(myIntent);

            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://"+(String) titleview4.getText()));
                startActivity(myIntent);

            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://"+(String) titleview5.getText()));
                startActivity(myIntent);

            }
        });

        return view;
    }
}
