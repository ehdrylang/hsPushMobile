package com.example.dell.capston;

import android.support.v4.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.NotificationCompat;

import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class TestMainActivity extends FragmentActivity /*implements BeaconConsumer*/ {
    //FragmentManager manager;  //Fragment를 관리하는 클래스의 참조변수
    android.app.FragmentTransaction tran;  //실제로 Fragment를 추가/삭제/재배치 하는 클래스의 참조변수


    static Bundle bundle;

    int state;
    //json 파싱하는 번호 (1번은 1번비콘정보)
    Intent intent;

    private BeaconManager beaconManager;
    private Context mContext;

    protected String proximityUuid;
    private List<Beacon> beaconList = new ArrayList<>();


    Intent intent1;
    /*static ArrayList<String> urlList = new ArrayList<String>();
    static ArrayList<String> image_urlList = new ArrayList<String>();*/
    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
    private GoogleApiClient client;
    public String responseDetails;

    /*****************************************************
     * Overrided methods
     ******************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //urlList.clear();
        //image_urlList.clear();

        //new JsonLoadingTask1().execute();
        //new JsonLoadingTask2().execute();


//Fragment를 관리하는 FragmentManager 객체 덩어오기
        //manager= (FragmentManager)getFragmentManager();

        setContentView(R.layout.testactivity_main);

// Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(10);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                /*Toast.makeText(TestMainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();*/
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    //디바이스의 Menu 버튼이 처음 눌러졋을 때 한번 호출되는 메소드
    //Menu의 MenuItem을 설정하는 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    //OptionMenu의 MenuItem 중 하나를 눌렀을 때 자동으로 호출되는 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {//눌러진 MenuItem의 Item Id를 얻어와 식별
            case R.id.setting:
                intent = new Intent(getApplicationContext(), // 현재 화면의 제어권자
                        Setting.class);
                startActivity(intent);
                //finish();
                break;

            case R.id.help:
                intent = new Intent(getApplicationContext(), // 현재 화면의 제어권자
                        Help.class);
                startActivity(intent);
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TestMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dell.capston/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TestMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dell.capston/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
