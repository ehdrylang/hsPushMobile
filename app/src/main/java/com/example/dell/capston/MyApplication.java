package com.example.dell.capston;

import android.app.Application;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2016-05-08.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;


    public MyApplication() {
        myApplication = this;
    }

    public static MyApplication instance() {
        return myApplication;
    }

    public static String value;

    //인트로 파싱여부를 판단
    public static boolean ParsingEnd = false;

    //버스 리플레쉬 판단
    public static boolean BusParsingEnd = false;

    public static boolean BusNotifiRefresh=false;


    public static Boolean beacon1Poster;
    public static Boolean beacon1Empty;

    public static String[] images = new String[5];
    public static String[] urls = new String[5];

    public static String Building;

    public static String BeaconPoster;
    public static String BeaconLib;
    public static String BeaconBus;
    public static String BeaconDiet;

    public static String TodayDate;
    public static String TomorrowDate;

    public static String TodayLunchMenu="";
    public static String TodayDinnerMenu;
    public static String TomorrowLunchMenu;
    public static String TomorrowDinnerMenu;



    public static String MajorID;
    public static String MinorID;

    public static BusStation[] group = new BusStation[18]; // 한성대로부터 보여줄 정류장 개수
    //    public static List<String> tempPoint = new ArrayList<String>(); // 버스 위치를 보여주기위한 리스트
//    public static List<String> realPoint = new ArrayList<String>(); // 버스 위치를 보여주기위한 리스트
    public static String[] tempPoint = new String[18];  // 버스위치를 정렬전 배열

    public static Building[] ClassGroup = new Building[9];  //건물 개수만큼 강의실

    public static Survey[] surveyControler = new Survey[5]; // 설문조사개수

    public String getGroup() {
        BusStation busStation = group[17];
        return busStation.getGroup();
    }

    public static boolean setting_Notification = false;

}

class BusStation {
    public String Minute_1; // 두번째 버스 몇분후도착 arrmsg1
    //    public String Minute_2; // 두번째 버스 몇분후도착 arrmsg2
    public String StationId; // 버스정류장 아이디(번호)
    public String StationName; // 버스정류장 이름
    public String NextOrd; // 버스위치를 위한 변수, 오는 버스가 다음에 도착할 역 아이디(번호)

    BusStation() {
        Minute_1 = new String("");
        //       Minute_2 = new String("");
        StationId = new String("");
        StationName = new String("");
        NextOrd = new String("");
    }
    public String getGroup() {

        return Minute_1;
    }
}

class Building {
    public String BuildingName; // 건물명
    public String[][] ClassNum = new String[6][15]; // 강의실번호

    Building(String name) {
        BuildingName = name;
        //[요일][교시]
        for (int i = 0; i < ClassNum.length; i++) {
            for (int j = 0; j < ClassNum[i].length; j++)
                ClassNum[i][j] = "";

        }
    }
}
class Survey{
    public String surveyUrl; // 구글독스 url링크
    public String surveyWriter; // 작성자이름
    public String surveyTitle; // 타이틀
    public String surveyDate; // 설문등록일
    Survey(){
        surveyTitle = new String("");
        surveyUrl = new String("");
        surveyWriter = new String("");
        surveyDate = new String("");
    }
}




