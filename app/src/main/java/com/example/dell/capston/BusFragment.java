package com.example.dell.capston;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

public class BusFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE3";

    private int mPage;
    Button renewBtn;
    Button[] btn = new Button[18];; // 주황색
    ImageView[] bus_image = new ImageView[18]; // 타요
    ImageView[] full_cross = new ImageView[18]; // 주황화살표
    ImageView[] empty_cross = new ImageView[18];// 0빈 화살표
    TextView[] show_time = new TextView[18]; // 몇분후도착
    TextView[] bus_stop = new TextView[18];// 정류장이름

    MyApplication myApplication = MyApplication.instance(); // 전역변수 사용을 위한 클래스변수

   /* @Override
    public void onResume() {
        super.onResume();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        renewBtn.performClick();
    }*/

    public static BusFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        BusFragment fragment = new BusFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_push, container, false);
///////////////////////////////////////////////////////////////////////// 변수선언 ///////////////////////////////////////////////
        /*while(myApplication.BusNotifiRefresh==false){
            if(myApplication.BusNotifiRefresh==true)
                renewBtn.performClick();
        }
*/

        btn[0] = (Button) view.findViewById(R.id.button18);
        bus_image[0] = (ImageView) view.findViewById(R.id.bus_image18);
        full_cross[0] = (ImageView) view.findViewById(R.id.full_cross18);
        empty_cross[0] = (ImageView) view.findViewById(R.id.empty_cross18);
        show_time[0] = (TextView) view.findViewById(R.id.show_time18);
        bus_stop[0] = (TextView) view.findViewById(R.id.bus_stop18);

        for (int i = 1; i < 18; i++) {
            btn[i] = (Button) view.findViewById(R.id.button18 - (i * 6));
            bus_image[i] = (ImageView) view.findViewById(R.id.bus_image18 - (i * 6));
            full_cross[i] = (ImageView) view.findViewById(R.id.full_cross18 - (i * 6));
            empty_cross[i] = (ImageView) view.findViewById(R.id.empty_cross18 - (i * 6));
            show_time[i] = (TextView) view.findViewById(R.id.show_time18 - (i * 6));
            bus_stop[i] = (TextView) view.findViewById(R.id.bus_stop18 - (i * 6));
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        renewBtn = (Button) view.findViewById(R.id.refresh);
        renewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파싱

                    new JsonLoadingTask5().execute();

                    for(;;) {
                       if(myApplication.BusParsingEnd ==true) break;
                }
                //new JsonLoadingTask5().execute();
                //그리기


                for (int i = 0; i < 18; i++) {
                    bus_stop[i].setText(myApplication.group[i].StationName);
                    show_time[i].setText(myApplication.group[i].Minute_1);
                    for (int j = 0; j < 18; j++) {
                        if (myApplication.group[i].StationId.equals(myApplication.group[j].NextOrd)) {
                            bus_image[i].setVisibility(View.VISIBLE);
                            full_cross[i].setVisibility(View.VISIBLE);
                            btn[i].setBackgroundColor(Color.GREEN);
                            empty_cross[i].setVisibility(View.GONE);
                            show_time[i].setVisibility(View.VISIBLE);
                            break;
                        } else {
                            bus_image[i].setVisibility(View.INVISIBLE);
                            full_cross[i].setVisibility(View.GONE);
                            btn[i].setBackgroundColor(Color.GRAY);
                            empty_cross[i].setVisibility(View.VISIBLE);
                            show_time[i].setVisibility(View.VISIBLE);
                        }
                    }
                }


                myApplication.BusParsingEnd =false;
            }
        });
        renewBtn.performClick();
        //new JsonLoadingTask5().execute();
        return view;
    }
    public String getStringFromUrl(String pUrl) {
        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer(); //�о�� �����͸� ������ StringBuffer��ü ����

        try {
            java.net.URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
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
    }// getStringFromUrl()-------------------------

    private class JsonLoadingTask5 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(5);

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
            String jsonPage0 = getStringFromUrl("http://113.198.80.214/CapstoneDesign/jsps/Realtime/getBusInfo.jsp");
            //String jsonPage0 = getStringFromUrl("http://113.198.80.214/CapstoneDesign/jsps/Realtime/getBusInfo.jsp");
            JSONObject obj = new JSONObject(jsonPage0);
            JSONArray List = obj.getJSONArray("List");
            JSONObject listObj = List.getJSONObject(0);
            JSONObject busInfos = listObj.getJSONObject("BusInfos");
            JSONArray busInfo = busInfos.getJSONArray("BusInfo");
            for (int i = 0; i < 18; i++) { // 버스자료 넣는부분
                JSONObject Station = busInfo.getJSONObject(i);
                //변수 넣는 부분이다
                myApplication.group[i] = new BusStation();
                myApplication.group[i].StationName = Station.getString("StationName");
                myApplication.group[i].StationId = Station.getString("StationOrd");
                myApplication.group[i].Minute_1 = Station.getString("ArrMsg1");
                myApplication.group[i].NextOrd = Station.getString("nextord1");

                myApplication.tempPoint[i] = new String("");
                myApplication.tempPoint[i] = myApplication.group[i].NextOrd;
            }
            myApplication.BusParsingEnd =true;

        } catch (Exception e) {
            // TODO: handle exception
        }

        return sb1.toString();
    }//getJsonText()----------
}