package com.example.dell.capston;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import com.example.dell.capston.contents.MacroManager;
import com.example.dell.capston.service.BlueWaveService;
import com.example.dell.capston.utils.Constants;
import com.example.dell.capston.utils.Logs;
import com.example.dell.capston.service.PushService;
import com.example.dell.capston.utils.RecycleUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class IntroActivity extends Activity {

    private Context mContext;
    private Timer mRefreshTimer;
    int siganpyo = 0;
    Boolean Beacon1Poster = false;
    Boolean Beacon1EmptyClass = false;

    Boolean Beacon2Poster = false;
    Boolean Beacon2EmptyClass = false;
    MyApplication myApplication = MyApplication.instance();

    /*****************************************************
     * Overrided methods
     ******************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getActionBar().setIcon(R.drawable.actionbar_icon);
        //this.getActionBar().setLogo(R.drawable.actionbar_icon);


        new JsonLoadingTask().execute();

        mContext = this;//.getApplicationContext();

        //----- UI
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);

        initialize();

    }

    @Override
    public synchronized void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // Stop the timer
        if (mRefreshTimer != null) {
            mRefreshTimer.cancel();
            mRefreshTimer = null;
        }
        super.onDestroy();
        finalizeActivity();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // onDestroy is not always called when applications are finished by Android system.
        finalizeActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_discoverable:
            //	return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();		// TODO: Disable this line to run below code
        finalizeActivity();
        finishActivity();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // This prevents reload after configuration changes
        super.onConfigurationChanged(newConfig);
    }


    /**
     * Initialization / Finalization
     */
    private void initialize() {


        // Check bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bt_ble_not_supported), Toast.LENGTH_LONG).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);


            //startBeaconPushManager();


            /*startServiceAndManager();
            reserveActivityChange(2*1000);*/
            //JsonParsing();

            return;
        }
        // 비콘푸쉬 서비스 시작시킴


        //startParsingManager();
        startBeaconPushManager();
        startServiceAndManager();
        while (myApplication.ParsingEnd == false) {
            //Log.e("파싱확인", String.valueOf(myApplication.ParsingEnd));
            if (myApplication.ParsingEnd == true) {

                break;
            }
        }
        reserveActivityChange(2 * 1000);
    }


    private void finalizeActivity() {
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    public void finishActivity() {
        finish();
    }

    private void startServiceAndManager() {
        startService(new Intent(this, BlueWaveService.class));
        MacroManager.getInstance(getApplicationContext());        // To make macro manager instance
    }

    private void startBeaconPushManager() {
        startService(new Intent(this, PushService.class)); // To make macro manager instance
    }
   /* private void startParsingManager() {
        startService(new Intent(this, ParsingService.class)); // To make macro manager instance
    }*/


    private void reserveActivityChange(long delay) {
        if (mRefreshTimer != null) {
            mRefreshTimer.cancel();
        }
        mRefreshTimer = new Timer();
        mRefreshTimer.schedule(new RefreshTimerTask(), delay);
    }

    /*****************************************************
     *	Public classes
     ******************************************************/
    /**
     * Receives result from external activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    startServiceAndManager();

                    while (myApplication.ParsingEnd == false) {
                        Log.e("파싱확인", String.valueOf(myApplication.ParsingEnd));
                        if (myApplication.ParsingEnd == true) {
                            //reserveActivityChange(5 * 1000);
                            break;
                        }

                    }
                    reserveActivityChange(3 * 1000);

                } else {
                    // User did not enable Bluetooth or an error occured
                    Logs.e("BT is not enabled");
                    Toast.makeText(this, "블루투스를 허용하지 않는다면 알람을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    //reserveActivityChange(3 * 1000);
                    startBeaconPushManager();
                    startServiceAndManager();
                    while (myApplication.ParsingEnd == false) {
                        Log.e("파싱확인", String.valueOf(myApplication.ParsingEnd));
                        if (myApplication.ParsingEnd == true) {
                            //reserveActivityChange(3 * 1000);
                            break;
                        }
                    }

                    reserveActivityChange(3 * 1000);

                }
                break;
        }    // End of switch(requestCode)
    }


    /*****************************************************
     * Sub classes
     ******************************************************/
    private class RefreshTimerTask extends TimerTask {
        public RefreshTimerTask() {
        }

        public void run() {
            startActivity(new Intent(mContext, TestMainActivity.class));
            finishActivity();
        }
    }


    public String getJsonText() {

        //MyApplication myApplication = MyApplication.instance();

        int BeaconNum = 0;

        StringBuffer TempBuffer = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();

        StringBuffer ClassBuffer = new StringBuffer();

        StringBuffer[][] GonghakBuffer = new StringBuffer[6][16];

        Map SortMap = new HashMap();
        TreeMap SortTreeMap;




        StringBuffer MondayPut = new StringBuffer();
        StringBuffer TusedayPut = new StringBuffer();
        StringBuffer WendsdayPut = new StringBuffer();
        StringBuffer ThurdayPut = new StringBuffer();
        StringBuffer FridayPut = new StringBuffer();
        StringBuffer SaturdayPut = new StringBuffer();


        Map Monday = new HashMap();
        Map Tuseday = new HashMap();
        Map Wendsday = new HashMap();
        Map Thurday = new HashMap();
        Map Friday = new HashMap();
        Map Saturday = new HashMap();

        String Todaylunch = "";
        String TodayDinner = "";

        String Tomorrowlunch = "";
        String TomorrowDinner = "";


        try {

            // String chkString = String.valueOf(chk);
            //113.198.80.214 - 101호 서버
            //115.21.3.126 - 예찬노트북
            // 59.15.234.45 - 예찬컴

           /* String url = "http://113.198.80.214/CapstoneDesign/jsps/getBeacon.jsp?&Beacon=";
            if ((myApplication.MajorID.equals("4660")) && (myApplication.MinorID.equals("64001"))) {
                BeaconNum = 1;
            } else if ((myApplication.MajorID.equals("4660")) && (myApplication.MinorID.equals("64002"))) {
                BeaconNum = 2;
            } else if ((myApplication.MajorID.equals("4660")) && (myApplication.MinorID.equals("64003"))) {
                BeaconNum = 3;
            } else if ((myApplication.MajorID.equals("4660")) && (myApplication.MinorID.equals("64004"))) {
                BeaconNum = 4;
            } else if ((myApplication.MajorID.equals("4660")) && (myApplication.MinorID.equals("64005"))) {
                BeaconNum = 5;
            }*/

            String jsonPage = getStringFromUrl("http://113.198.80.214/CapstoneDesign/jsps/getAllBeacon.jsp");

            JSONObject obj = new JSONObject(jsonPage);
            JSONArray List = obj.getJSONArray("List");
            Log.e("TAG11", "URL파싱");

            int p = 0;
            int s = 0;
            for (int i = 0; i < List.length(); i++) {
                JSONObject info = List.getJSONObject(i);
                String TypeCheck = info.getString("Type");

                if (TypeCheck.equals("Poster")) {
                    JSONObject Poster = info.getJSONObject("Poster");
                    myApplication.images[p] = Poster.getString("image_URL");
                    myApplication.urls[p++] = Poster.getString("URL");

                } else if (TypeCheck.equals("Siganpyo")) { //타입이 시간표이면

                    myApplication.ClassGroup[0] = new Building("진리관");
                    Log.e("TAGG123", myApplication.ClassGroup[0].BuildingName);

                    myApplication.ClassGroup[1] = new Building("탐구관");
                    myApplication.ClassGroup[2] = new Building("공학관");
                    myApplication.ClassGroup[3] = new Building("학송관");
                    myApplication.ClassGroup[4] = new Building("창의관");
                    myApplication.ClassGroup[5] = new Building("낙산관");
                    myApplication.ClassGroup[6] = new Building("우촌관");
                    myApplication.ClassGroup[7] = new Building("미래관");
                    myApplication.ClassGroup[8] = new Building("지선관");
                    // Log.e("TAGGG1", myApplication.ClassGroup[0].BuildingName);

                    JSONObject Empty_Siganpyo = (JSONObject) info.getJSONObject("Empty_Siganpyo");
                    JSONArray buildings = (JSONArray) Empty_Siganpyo.getJSONArray("buildings");


                    for (int j = 0; j < buildings.length(); j++) { //

                        JSONObject buildingsOBJ = (JSONObject) buildings.getJSONObject(j);
                        JSONArray weeks = (JSONArray) buildingsOBJ.getJSONArray("weeks");
                        String Building = buildingsOBJ.getString("building");
                        Log.e("TAGGG1", Building);
                        for (int k = 0; k < weeks.length(); k++) {
                            JSONObject WeeksOBJ = (JSONObject) weeks.getJSONObject(k);
                            String Week = WeeksOBJ.getString("week");
                            Log.e("TAGGG1", Week);
                            JSONArray times = (JSONArray) WeeksOBJ.getJSONArray("times");

                            for (int l = 0; l < times.length(); l++) {
                                JSONObject TimesOBJ = (JSONObject) times.getJSONObject(l);
                                String Time = TimesOBJ.getString("time");
                                Log.e("TAGGG1", Time);
                                JSONArray classrooms = (JSONArray) TimesOBJ.getJSONArray("classrooms");

                                for (int n = 0; n < classrooms.length(); n++) {
                                    JSONObject classroomsOBJ = (JSONObject) classrooms.getJSONObject(n);
                                    String ClassRoom = classroomsOBJ.getString("c");
                                    //ClassBuffer.append(classroomsOBJ.getString("c") + " ");
                                    //강의실을 오름차순 정렬

                                    SortMap.put(ClassRoom, ClassRoom);
                                }

                                SortTreeMap = new TreeMap(SortMap);
                                Iterator MondayMapIter = SortTreeMap.keySet().iterator();
                                while (MondayMapIter.hasNext()) {
                                    String key = (String) MondayMapIter.next();
                                    String value = (String) SortTreeMap .get(key);
                                    ClassBuffer.append(value+" ");
                                }
                                Log.e("1133", ClassBuffer.toString());

                                for (int a = 0; a < myApplication.ClassGroup.length; a++) {
                                    Log.e("1134", ClassBuffer.toString());
                                    if (Building.equals(myApplication.ClassGroup[a].BuildingName)) {
                                        Log.e("1135", ClassBuffer.toString());
                                        int timeValue = Integer.parseInt(Time);
                                        if (Week.equals("월")) {
                                            Log.e("1136", ClassBuffer.toString());
                                            myApplication.ClassGroup[a].ClassNum[0][timeValue] = ClassBuffer.toString();
                                            Log.e("TAGG123", ClassBuffer.toString());
                                        } else if (Week.equals("화")) {
                                            myApplication.ClassGroup[a].ClassNum[1][timeValue] = ClassBuffer.toString();
                                        } else if (Week.equals("수")) {
                                            myApplication.ClassGroup[a].ClassNum[2][timeValue] = ClassBuffer.toString();
                                        } else if (Week.equals("목")) {
                                            myApplication.ClassGroup[a].ClassNum[3][timeValue] = ClassBuffer.toString();
                                        } else if (Week.equals("금")) {
                                            myApplication.ClassGroup[a].ClassNum[4][timeValue] = ClassBuffer.toString();
                                        } else if (Week.equals("토")) {
                                            myApplication.ClassGroup[a].ClassNum[5][timeValue] = ClassBuffer.toString();
                                            Log.e("TAGGG1233", myApplication.ClassGroup[a].ClassNum[5][timeValue]);

                                        }
                                    }
                                }
                                SortMap.clear();
                                SortTreeMap.clear();
                                ClassBuffer.delete(0, ClassBuffer.length());
                            }
                        }
                    }

                } else if (TypeCheck.equals("Library")) {


                } else if (TypeCheck.equals("Parte")) {


                    SimpleDateFormat dateFormat = new SimpleDateFormat("M월 d일(EEE)", java.util.Locale.getDefault());
                    Date date = new Date();
                    String strDate = dateFormat.format(date);
                    myApplication.TodayDate = strDate;

                    Log.e("TAG11", strDate);

                    Calendar today = Calendar.getInstance();
                    today.add(Calendar.DATE, 1);
                    Date tomorrow = today.getTime();
                    String tomorrowDate = dateFormat.format(tomorrow);
                    myApplication.TomorrowDate = tomorrowDate;

                    Log.e("TAG11", tomorrowDate);

                    //String strDate ="5월 23일(월)";

                    JSONObject Parte = (JSONObject) info.getJSONObject("Parte");
                    JSONArray dates = (JSONArray) Parte.getJSONArray("dates");
                    for (int j = 0; j < dates.length(); j++) {
                        JSONObject dateOBJ = (JSONObject) dates.getJSONObject(j);
                        String dateChk = dateOBJ.getString("date");
                        if (dateChk.equals(strDate)) {
                            JSONArray menus = (JSONArray) Parte.getJSONArray("menus");
                            JSONObject lunchOBJ = (JSONObject) menus.getJSONObject(j);
                            JSONObject DinnerOBJ = (JSONObject) menus.getJSONObject(j + 5);

                            Todaylunch = lunchOBJ.getString("menu");
                            //Log.e("TAG11", Todaylunch);
                            //lunch.replaceAll(" ","asdasd");
                            //Log.e("TAG11", TodayDinner);
                            TodayDinner = DinnerOBJ.getString("menu");
                            //Dinner.replaceAll(" ","asdasd");


                        } else if (dateChk.equals(tomorrowDate)) {
                            JSONArray menus = (JSONArray) Parte.getJSONArray("menus");
                            JSONObject lunchOBJ = (JSONObject) menus.getJSONObject(j);
                            JSONObject DinnerOBJ = (JSONObject) menus.getJSONObject(j + 5);

                            Tomorrowlunch = lunchOBJ.getString("menu");
                            Log.e("TAG11", Tomorrowlunch);
                            //lunch.replaceAll(" ","asdasd");
                            Log.e("TAG11", Tomorrowlunch);
                            TomorrowDinner = DinnerOBJ.getString("menu");

                        }

                    }
                    myApplication.TodayLunchMenu = Todaylunch;
                    //myApplication.TodayLunchMenu.replaceAll(" ","\n");

                    myApplication.TodayDinnerMenu = TodayDinner;
                    //myApplication.TodayDinnerMenu.replaceAll(" ", "\n");

                    myApplication.TomorrowLunchMenu = Tomorrowlunch;
                    //myApplication.TomorrowLunchMenu.replaceAll(" ","\n");

                    myApplication.TomorrowDinnerMenu = TomorrowDinner;
                    ///myApplication.TomorrowDinnerMenu.replaceAll(" ","\n");
                    //Log.e("TAG1", lunch);
                    /*myApplication.LunchMenu=lunch;
                    myApplication.DinnerMenu=Dinner;*/
                } else if (TypeCheck.equals("Bus")){

                    JSONObject busInfos = info.getJSONObject("BusInfos");
                    JSONArray busInfo = busInfos.getJSONArray("BusInfo");
                    for (int j = 0; j < 18; j++) { // 버스자료 넣는부분
                        JSONObject Station = busInfo.getJSONObject(j);
                        //변수 넣는 부분이다
                        myApplication.group[j] = new BusStation();
                        myApplication.group[j].StationName = Station.getString("StationName");
                        myApplication.group[j].StationId = Station.getString("StationOrd");
                        myApplication.group[j].Minute_1 = Station.getString("ArrMsg1");
                        myApplication.group[j].NextOrd = Station.getString("nextord1");

                        myApplication.tempPoint[j] = new String("");
                        myApplication.tempPoint[j] = myApplication.group[j].NextOrd;
                    }
                }else if (TypeCheck.equals("Survey")){
                    JSONObject surveyInfos = info.getJSONObject("Survey");

                    myApplication.surveyControler[s] = new Survey();
                    myApplication.surveyControler[s].surveyTitle = surveyInfos.getString("Title");
                    myApplication.surveyControler[s].surveyUrl = surveyInfos.getString("URL");
                    myApplication.surveyControler[s].surveyWriter = surveyInfos.getString("Writer");
                    myApplication.surveyControler[s++].surveyDate = info.getString("Dates");

                }

            } // List 배열 반복문 끝
           /* myApplication.LunchMenu=lunch;
            myApplication.DinnerMenu=Dinner;*/
            Log.e("파싱확인중", String.valueOf(myApplication.ParsingEnd));
            myApplication.ParsingEnd = true;
            Log.e("파싱확인중", String.valueOf(myApplication.ParsingEnd));


        } catch (Exception e) {

            // TODO: handle exception
        }

        return sb1.toString();
    }//getJsonText()----------

    public String getStringFromUrl(String pUrl) {

        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer(); //�о�� �����͸� ������ StringBuffer��ü ����

        try {

            //[Type1]
            /*
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(pUrl));
            InputStream contentStream = response.getEntity().getContent();
            */

            //[Type2]
            java.net.URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();
            /*urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);*/

            bufreader = new BufferedReader(new InputStreamReader(contentStream, "UTF-8"));
            String line = null;

            //������ ������ �ҽ��� �ٴ����� �о�(line), Page�� ������
            while ((line = bufreader.readLine()) != null) {
                Log.d("line:", line);
                page.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //�ڿ�����
            try {
                bufreader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return page.toString();
    }// getStringFromUrl()-------------------------

    /**
     * 스레드의 실행 부분
     */

  /*  public void run() {
        while (run) { //run 플래그변수 true 일때만 쓰레드 실행 적용
            try {
                new JsonLoadingTask().execute();


                Log.i(TAG, "my service called #" + count);
                count++;

                Thread.sleep(1000*180);


            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
    }*/


    private class JsonLoadingTask extends AsyncTask<String, Void, String> {
        /* TestMainActivity context;
         ProgressDialog dialog;

         public JsonLoadingTask(){
             this.context=(TestMainActivity)context;

         }*/
        //비동기방식으로 작동할 메서드이며, 주로 메인쓰레드와는 별도로
        //웹사이트의 연동이나 지연이 발생하는 용도로 사용하면 된다.
        //사실상 개발자가 정의 쓰레드에서 run메서드와 비슷하다
        //  'String...' 가변형 파라미터로 파라미터 개수 상관없이 넣을 수 있다.
        @Override
        protected String doInBackground(String... strs) {
            getJsonText();
            return null;

        } // doInBackground : ��׶��� �۾��� �����Ѵ�.

        //백그라운드 작업 수행전에 해야할 업무등을 이 메서드에 작성하며 되는데,
        //이 메서드는 UI쓰레드에 의해 작동하므로 UI를 제어할 수 있다.
        //따라서 이 타이밍에 진행바를 보여주는 작업등을 할 수 있다.
       /* @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(context);
            //dialog.setCancelable(false);
            dialog.show();
        }*/
        //백그라운드 메서드가 업무수행을 마칠때 호출되는 메서드.
        //UI쓰레드에 의해 호출되므로, UI쓰레드를 제어할 수 있다.
        //따라서 진행바를 그만 나오게 할 수 있다.
        @Override
        protected void onPostExecute(String result) {
           /* super.onPostExecute(result);
            dialog.dismiss();*/
            // et_webpage_src.setText(result);
        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask

    /*private class JsonLoadingTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {

            return getJsonText(2);

        } // doInBackground : ��׶��� �۾��� �����Ѵ�.

        @Override
        protected void onPostExecute(String result) {
            // et_webpage_src.setText(result);
        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask

    private class JsonLoadingTask3 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {

            return getJsonText(3);

        } // doInBackground : ��׶��� �۾��� �����Ѵ�.

        @Override
        protected void onPostExecute(String result) {
            // et_webpage_src.setText(result);
        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask

    private class JsonLoadingTask4 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {

            return getJsonText(4);

        } // doInBackground : ��׶��� �۾��� �����Ѵ�.

        @Override
        protected void onPostExecute(String result) {
            // et_webpage_src.setText(result);
        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask
    // getStringFromUrl : �־��� URL�� ������ ������ ���ڿ��� ��ȯ

    private class BackgroundPush extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {

            //sendNotification("HS Beacon","공모전 " );

            return null;
        } // doInBackground : ��׶��� �۾��� �����Ѵ�.

        @Override
        protected void onPostExecute(String result) {
            // et_webpage_src.setText(result);
        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask


*/
}
