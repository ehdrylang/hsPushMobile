package com.example.dell.capston;

/**
 * Created by lee on 2016-05-07.
 */


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SurveyFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE3";
    private int mPage;
    MyApplication myApplication = MyApplication.instance();
    TextView[] title = new TextView[5]; // 제목
    TextView[] writer = new TextView[5];// 작성자
    TextView[] registerDay = new TextView[5];// 등록일
    LinearLayout[] layoutSurvey = new LinearLayout[5]; // 이벤트 레이아웃
    Button refreshBtn;

    public static SurveyFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SurveyFragment fragment = new SurveyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }
    /*@Override
    public void onResume() {
        super.onResume();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        refreshBtn.performClick();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.survey_push, container, false);

        //new JsonLoadingTask6().execute();

        title[0] = (TextView) view.findViewById(R.id.surveyName1);
        writer[0] = (TextView) view.findViewById(R.id.writerName1);
        registerDay[0] = (TextView) view.findViewById(R.id.day1);
        layoutSurvey[0] = (LinearLayout)view.findViewById(R.id.survey1);
        for(int i=1; i<5;i++){
            title[i] = (TextView) view.findViewById(R.id.surveyName1+(4*i));
            writer[i] = (TextView) view.findViewById(R.id.writerName1+(4*i));
            registerDay[i] = (TextView) view.findViewById(R.id.day1+(4*i));
            layoutSurvey[i] = (LinearLayout)view.findViewById(R.id.survey1+(4*i));
        }
        for (int i = 0; i < 5; i++) {
            title[i].setText("제목 : " + myApplication.surveyControler[i].surveyTitle);
            writer[i].setText("작성자 : " + myApplication.surveyControler[i].surveyWriter);
            // registerDay[i].setText("등록일 : " + myApplication.surveyControler[i].survey;
        }
        refreshBtn = (Button)view.findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonLoadingTask6().execute();
                //new JsonLoadingTask6().execute();

                for (int i = 0; i < 5; i++) {
                    title[i].setText("제목 : " + myApplication.surveyControler[i].surveyTitle);
                    writer[i].setText("작성자 : " + myApplication.surveyControler[i].surveyWriter);
                    registerDay[i].setText("등록일 : " + myApplication.surveyControler[i].surveyDate);
                }
            }
        });
        layoutSurvey[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myApplication.surveyControler[0].surveyUrl));
                startActivity(intent);
            }
        });
        layoutSurvey[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myApplication.surveyControler[1].surveyUrl));
                startActivity(intent);
            }
        });
        layoutSurvey[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myApplication.surveyControler[2].surveyUrl));
                startActivity(intent);
            }
        });
        layoutSurvey[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myApplication.surveyControler[3].surveyUrl));
                startActivity(intent);
            }
        });
        layoutSurvey[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myApplication.surveyControler[4].surveyUrl));
                startActivity(intent);
            }
        });
        refreshBtn.performClick();
        return view;
    }
    private class JsonLoadingTask6 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            getJsonText(6);
            return null;

        } // doInBackground : ��׶��� �۾��� �����Ѵ�.

        @Override
        protected void onPostExecute(String result) {
            // et_webpage_src.setText(result);
        } // onPostExecute :
    } // JsonLoadingTask

    public String getJsonText(int chk) {
        StringBuffer sb1 = new StringBuffer();
        MyApplication myApplication = MyApplication.instance();

        try {
            String jsonPage0 = getStringFromUrl("http://113.198.80.214/CapstoneDesign/jsps/Realtime/getSurveyInfo.jsp");
            JSONObject obj = new JSONObject(jsonPage0);
            JSONArray List = obj.getJSONArray("List");
            for(int i=0;i<List.length();i++) {
                JSONObject listObj = List.getJSONObject(i);
                JSONObject surveyInfos = listObj.getJSONObject("Survey");
                myApplication.surveyControler[i] = new Survey();
                myApplication.surveyControler[i].surveyTitle = surveyInfos.getString("Title");
                myApplication.surveyControler[i].surveyUrl = surveyInfos.getString("URL");
                myApplication.surveyControler[i].surveyWriter = surveyInfos.getString("Writer");
                myApplication.surveyControler[i].surveyDate = listObj.getString("Dates");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return sb1.toString();
    }//getJsonText()----------
    public String getStringFromUrl(String pUrl) {
        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;
        StringBuffer page = new StringBuffer();

        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            /*urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);*/
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream, "UTF-8"));
            String line = null;

            while ((line = bufreader.readLine()) != null) {
                Log.d("line:", line);
                page.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufreader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return page.toString();
    }
}

