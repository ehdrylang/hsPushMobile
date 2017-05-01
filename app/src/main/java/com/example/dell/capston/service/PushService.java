package com.example.dell.capston.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

//import com.example.dell.capston.Building;
//import com.example.dell.capston.BusStation;
import com.example.dell.capston.MyApplication;
import com.example.dell.capston.R;
import com.example.dell.capston.TestMainActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PushService extends Service implements BeaconConsumer {
    private BeaconManager beaconManager;

    public MyApplication myApplication = MyApplication.instance();

    String Majorid;
    String Minorid;
    private Context mContext = null;

    private Timer mRefreshTimer;

    protected String proximityUuid;
    private List<Beacon> beaconList = new ArrayList<>();


    StringBuffer Libbuffer;
    StringBuffer Dietbuffer;

    int remaintime1 = 0;
    int remaintime2 = 0;
    int remaintime3 = 0;
    int remaintime4 = 0;

    int parsing_count = 0;
    boolean parse_type1 = false;
    boolean parse_type2 = false;
    boolean parse_type3 = false;
    boolean parse_type4 = false;

    class MyThread extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                remaintime1 = remaintime1 - 1000;
                remaintime2 = remaintime2 - 1000;
                remaintime3 = remaintime3 - 1000;
                remaintime4 = remaintime4 - 1000;

            }
        }
    }
    Bitmap bitmap;
    StringBuffer BeaconInfoMessage = new StringBuffer();
    ArrayList<String> arr = new ArrayList<String>();

    MyThread t1 = new MyThread();
    boolean BitmapCheck=false;

    boolean BeaconParsing = false;

    public PushService() {


    }

    @Override
    public void onCreate() {
        Log.d("TAG2", "# Service - onCreate() starts here");



        new BitmapLoadingTask(myApplication.images[0]).execute();

        //BitmapCheck=true;

        mContext = getApplicationContext();

        t1.start();
        //MyThread t1 = new MyThread();


        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        // 필자의 경우에는 아래처럼 하니 잘 동작했음.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        //new BeaconBackground().execute();
        beaconManager.bind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                        //푸쉬핸들러 실행
                        handler.sendEmptyMessage(0);
                        //handler.sendEmptyMessageDelayed(0, 1000);

                    }

                }

            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }


    /*****************************************************
     * Handler, Callback, Sub-classes
     ******************************************************/

    //노티피케이션 하는 소스
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            for (Beacon beacon : beaconList) {
                Majorid = beacon.getId2().toString();
                Minorid = beacon.getId3().toString();
                //MyApplication myApplication = MyApplication.instance();
                myApplication.MajorID = Majorid;
                myApplication.MinorID = Minorid;

                //1번비콘일 경우
                if (Majorid.equals("4660") && Minorid.equals("64001")) {
                    if (remaintime1 <= 0) {

                        //이미지 가져오는로 --------------------------------------지우지마셈
                        /*new JsonLoadingTask(1).execute();
                        while(true) { //파싱됫는지 계속확인
                            if (BeaconParsing == true) {직
                                new BitmapLoadingTask(myApplication.BeaconPoster).execute();
                                Log.e("이미지 확인", myApplication.BeaconPoster);
                                while (true) {
                                    // Log.e("타입파싱", "ㅎㅇ");
                                    if (*//*myApplication.ParsingEnd == true &&*//* BitmapCheck == true)
                                        break;
                                }
                                break;
                            }

                        }*/

                       // new BitmapLoadingTask(myApplication.images[0]).execute();
                        //BitmapCheck=true;
                        while (true) {

                            Log.e("확인1", String.valueOf(BeaconParsing));
                            Log.e("확인2", String.valueOf(BitmapCheck));
                            // new BitmapLoadingTask(myApplication.images[0]).execute();
                            if (myApplication.ParsingEnd && BitmapCheck ==true) break;
                        }
                        if(myApplication.setting_Notification == false)
                        sendNotification("오늘의 공모전", "드래그 하세요 ! ", bitmap);
                        BeaconParsing = false;

                    }
                    remaintime1 = 20 * 1000;
                } else if (Majorid.equals("4660") && Minorid.equals("64002")) {
                    if (remaintime2 <= 0) {


                        new JsonLoadingTask(2).execute();
                        while (true) {
                            //Log.e("타입2파싱", "ㅎㅇ2");

                            if (/*myApplication.ParsingEnd == true &&*/ BeaconParsing == true)
                                break;
                        }

                        //parse_type1=false;
                        //String Message = BeaconInfoMessage.toString();
                        if(myApplication.setting_Notification == false)
                        sendNotification("미래관 좌석 현황", myApplication.BeaconLib);
                        BeaconParsing = false;


                    }
                    remaintime2 = 20 * 1000;
                } else if (Majorid.equals("4660") && Minorid.equals("64003")) {
                    Log.e("타이머값", String.valueOf(remaintime3));
                    // new BitmapLoadingTask(myApplication.images[0]).execute();
                    if (remaintime3 <= 0) {

                        Log.e("식단확인", "1번");

                        new JsonLoadingTask(3).execute();
                        Log.e("식단확인", "1번");
                        while (true) {

                            if (myApplication.ParsingEnd == true && BeaconParsing == true)
                                break;

                        }

                        if(myApplication.setting_Notification == false)
                            sendNotification("오늘의 학식메뉴", myApplication.BeaconDiet);

                        BeaconParsing = false;
                        //parse_type1=false;
                        //String Message = BeaconInfoMessage.toString();
                    }

                    remaintime3 = 20 * 1000;
                } else if (Majorid.equals("4660") && Minorid.equals("64004")) {
                    if (remaintime4 <= 0) {

                        new JsonLoadingTask(4).execute();
                        while (true) {
                            if (/*myApplication.ParsingEnd == true &&*/ BeaconParsing == true) {

                                break;
                            }
                        }
                        if(myApplication.setting_Notification == false)
                        sendNotification("한성대학교 정문", myApplication.BeaconBus);
                        BeaconParsing = false;
                        myApplication.BusNotifiRefresh=true;
                        //BeaconInfoMessage.delete(0,BeaconInfoMessage.length());
                    }
                    remaintime4 = 20 * 1000;
                }

            }
        }
    };

    private void sendNotification(String Title, String message) {  //알림패널에 나타나게하는 코드
        Intent intent = new Intent(this, TestMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(Title)
                .setContentText("아래로 드래그")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setSummaryText("HS Beacon (한성비콘)");
        style.setBigContentTitle(Title);
        style.bigText(message);


        notificationBuilder.setStyle(style);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private void sendNotification(String Title, String message,Bitmap photo) {  //알림패널에 나타나게하는 코드
        Intent intent = new Intent(this, TestMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //photo =getBitmap(myApplication.images[0]);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(Title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                // BitPictureStyle을 적용하는 코드
                .setStyle(new NotificationCompat.BigPictureStyle()
                        // bigPicture의 이미지 파일은 Bitmap으로 처리되어야 합니다.
                        .bigPicture(photo)
                        // 화면이 펼쳐진 상태에서 보여질 아이콘을 Bitmap으로 처리해줍니다.
                        .bigLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_gallery)));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public String getJsonText(int BeaconNumber) {
        Log.e("타입2파싱", "ㅎㅇ2");
        Libbuffer = new StringBuffer();
        Dietbuffer = new StringBuffer();

        try {
            Log.e("타입2파싱", "ㅎㅇ3");
            String url="http://113.198.80.214/CapstoneDesign/jsps/Notification/getNotification.jsp?&Beacon=";
            Log.e("타입2파싱", url+String.valueOf(BeaconNumber));
            String jsonPage = getStringFromUrl(url+String.valueOf(BeaconNumber));
            Log.e("타입2파싱", "ㅎㅇ5");

            JSONObject obj = new JSONObject(jsonPage);
            Log.e("타입2파싱", "ㅎㅇ6");
            JSONObject List = obj.getJSONObject("List");
            Log.e("타입2파싱", "ㅎㅇ7");


            String TypeCheck = List.getString("Type");

            if (TypeCheck.equals("Poster")) {
                JSONObject Poster = List.getJSONObject("Poster");
                String PosterUrl = Poster.getString("Poster");
                myApplication.BeaconPoster=PosterUrl;
                Log.e("포스터 파싱",myApplication.BeaconPoster);


            } else if (TypeCheck.equals("lib")) {
                JSONArray lib = List.getJSONArray("lib");
                Log.e("타입2파싱", "ㅎㅇ7");
                for(int j=0;j<lib.length();j++){
                    Log.e("타입2파싱", "ㅎㅇ8");
                    JSONObject libname = lib.getJSONObject(j);
                    String Lib = libname.getString("libname");
                    String Empty = libname.getString("empty");
                    Libbuffer.append(Lib + " 빈좌석 :" + Empty +"\n");

                }
                myApplication.BeaconLib=Libbuffer.toString();
                Libbuffer.delete(0,Libbuffer.length());

            } else if (TypeCheck.equals("Parte")) {
                JSONArray Parte = List.getJSONArray("Parte");
                for(int j=0;j<Parte .length();j++) {
                    JSONObject ParteOBJ = Parte .getJSONObject(j);
                    String menu =ParteOBJ.getString("menu");
                    if(j==0)
                    Dietbuffer.append("중식 : "+ menu +"\n");
                    else
                        Dietbuffer.append("석식 : "+ menu);
                }
                myApplication.BeaconDiet=Dietbuffer.toString();
                Dietbuffer.delete(0,Dietbuffer.length());

            } else if (TypeCheck.equals("bus")) {
                myApplication.BeaconBus=List.getString("bus");
                Log.e("버스확인",myApplication.BeaconBus);

            }



            BeaconParsing=true;

        } catch (Exception e) {
            Log.e("타입2파싱", "파싱실패");
            e.printStackTrace();
            // TODO: handle exception
        }

        return null;
    }//getJsonText()----------

    public String getStringFromUrl(String pUrl) {

        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer(); //�о�� �����͸� ������ StringBuffer��ü ����

        try {
            //[Type2]
            java.net.URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

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


    private class BitmapLoadingTask extends AsyncTask<String, Void, Bitmap> {
        String urls="";

        public BitmapLoadingTask(String url){
            this.urls=url;
        }
        //비동기방식으로 작동할 메서드이며, 주로 메인쓰레드와는 별도로
        //웹사이트의 연동이나 지연이 발생하는 용도로 사용하면 된다.
        //사실상 개발자가 정의 쓰레드에서 run메서드와 비슷하다
        //  'String...' 가변형 파라미터로 파라미터 개수 상관없이 넣을 수 있다.
        @Override
        protected Bitmap doInBackground(String... strs) {

            try {
                URL myFileUrl = new URL(urls);
                Log.d("URLImange", String.valueOf(myFileUrl));

                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

                BitmapCheck=true;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;


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
        protected void onPostExecute(Bitmap result) {
           // BitmapCheck=true;
           /* super.onPostExecute(result);
            dialog.dismiss();*/
            // et_webpage_src.setText(result);

        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask

    private void Beacon_timer1(long delay) {
        if (mRefreshTimer != null) {
            mRefreshTimer.cancel();
        }
        mRefreshTimer = new Timer();
        mRefreshTimer.schedule(new RefreshTimerTask1(), delay);
    }

    private class RefreshTimerTask1 extends TimerTask {
        public RefreshTimerTask1() {
        }

        public void run() {
            remaintime1 = remaintime1 - 1000;

        }
    }

    /*public Bitmap getBitmap(String urls) {
        Bitmap bmImg = null;
        try {
            URL myFileUrl = new URL(urls);
            Log.d("URLImange", String.valueOf(myFileUrl));

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmImg = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmImg;
    }*/
    private class JsonLoadingTask extends AsyncTask<String, Void, String> {
        int Num;

        public JsonLoadingTask(int Num){
            this.Num = Num;


        }
        //비동기방식으로 작동할 메서드이며, 주로 메인쓰레드와는 별도로
        //웹사이트의 연동이나 지연이 발생하는 용도로 사용하면 된다.
        //사실상 개발자가 정의 쓰레드에서 run메서드와 비슷하다
        //  'String...' 가변형 파라미터로 파라미터 개수 상관없이 넣을 수 있다.
        @Override
        protected String doInBackground(String... strs) {
            getJsonText(Num);
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
            //BitmapCheck=true;
           /* super.onPostExecute(result);
            dialog.dismiss();*/
            // et_webpage_src.setText(result);
        } // onPostExecute : ��׶��� �۾��� ���� �� UI �۾��� �����Ѵ�.
    } // JsonLoadingTask


}

