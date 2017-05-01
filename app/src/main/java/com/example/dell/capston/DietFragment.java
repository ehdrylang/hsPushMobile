package com.example.dell.capston;

/**
 * Created by lee on 2016-05-07.
 */


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DietFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE3";

    TextView TodayDate,TomorrowDate;
    TextView TodayLunch,TodayDinner,TomorrowLunch,TomorrowDinner;
    private int mPage;
    Button go_to_btn;

    public static DietFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DietFragment fragment = new DietFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diet_view, container, false);
        String lunch;
        String dinner;

        /*SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);


*/      MyApplication myApplication = MyApplication.instance();


        go_to_btn = (Button)view.findViewById(R.id.go_to_diet);
        TodayDate=(TextView)view.findViewById(R.id.today_date);
        TomorrowDate=(TextView)view.findViewById(R.id.tomorrow_date);

        TodayLunch=(TextView)view.findViewById(R.id.today_lunch);
        TodayDinner=(TextView)view.findViewById(R.id.today_dinner);
        TomorrowLunch=(TextView)view.findViewById(R.id.tomorrow_lunch);
        TomorrowDinner=(TextView)view.findViewById(R.id.tomorrow_dinner);

        /*lunch=myApplication.LunchMenu;
        dinner=myApplication.DinnerMenu;*/

        TodayDate.setText(myApplication.TodayDate);
        TomorrowDate.setText(myApplication.TomorrowDate);

        TodayLunch.setText(myApplication.TodayLunchMenu.replaceAll(" ","\n"));
        TodayDinner.setText(myApplication.TodayDinnerMenu.replaceAll(" ","\n"));

        TomorrowLunch.setText(myApplication.TomorrowLunchMenu.replaceAll(" ","\n"));
        TomorrowDinner.setText(myApplication.TomorrowDinnerMenu.replaceAll(" ","\n"));

        go_to_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.hansung.ac.kr/web/www/life_03_01_t1"));
                startActivity(myIntent);

            }
        });

      /*  Lunch.setText(lunch.replaceAll(" ","\n"));
        Dinner.setText(dinner.replaceAll(" ","\n"));*/




        return view;
    }
}
