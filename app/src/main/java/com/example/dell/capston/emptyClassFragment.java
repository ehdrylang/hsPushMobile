package com.example.dell.capston;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016-05-15.
 */

public class  emptyClassFragment extends Fragment  {


    public static final String ARG_PAGE = "ARG_PAGE";
    RadioGroup radioGroup;
    RadioButton radioButton_Mon, radioButton_Tue, radioButton_Web, radioButton_Tur, radioButton_Fri, radioButton_Sat;
    //LinearLayout linearLayout_Mon, linearLayout_Tue, linearLayout_Web, linearLayout_Tur, linearLayout_Fri, linearLayout_Sat;
    TextView timetable;
    // Button button;

    String Mon, Tuse, Wends, Thur, Fri, Satur;


    String Category = "진리관";
    private int mPage;

    int CheckBuildingNum = 0;

    TextView ClassView[] = new TextView[15];
    private MyApplication myApplication;


    public static emptyClassFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        emptyClassFragment fragment = new emptyClassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_room_push, container, false);

        radioGroup = (RadioGroup) view.findViewById(R.id.rGroup);
        radioButton_Mon = (RadioButton) view.findViewById(R.id.rButton_Mon);
        radioButton_Tue = (RadioButton) view.findViewById(R.id.rButton_Tue);
        radioButton_Web = (RadioButton) view.findViewById(R.id.rButton_Web);
        radioButton_Tur = (RadioButton) view.findViewById(R.id.rButton_Tur);
        radioButton_Fri = (RadioButton) view.findViewById(R.id.rButton_Fri);
        radioButton_Sat = (RadioButton) view.findViewById(R.id.rButton_Sat);

        SimpleDateFormat dateFormat = new SimpleDateFormat("E", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        if(strDate.equals("월")){
            radioGroup.check(R.id.rButton_Mon);
        }else if(strDate.equals("화")){
            radioGroup.check(R.id.rButton_Tue);
        }else if(strDate.equals("수")){
            radioGroup.check(R.id.rButton_Web);
        }else if(strDate.equals("목")){
            radioGroup.check(R.id.rButton_Tur);
        }else if(strDate.equals("금")){
            radioGroup.check(R.id.rButton_Fri);
        }else if(strDate.equals("토")){
            radioGroup.check(R.id.rButton_Sat);
        }





        for (int i = 0; i < 15; i++) {
            ClassView[i] = (TextView) view.findViewById(R.id.class0 + i);

        }

        myApplication = MyApplication.instance();

        final ArrayList<String> items = new ArrayList<String>();
        items.add("공학관");
        items.add("낙산관");
        items.add("미래관");
        items.add("우촌관");
        items.add("지선관");
        items.add("진리관");
        items.add("창의관");
        items.add("탐구관");
        items.add("학송관");

        Spinner spi = (Spinner) view.findViewById(R.id.spinner);
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //화면 조지기

                Category = items.get(arg2);
                setBuilding(Category);


                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rButton_Mon:
                        for (int i = 0; i < 15; i++) {
                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[0][i]);

                            // Log.e("체크빌딩넘", String.valueOf(CheckBuildingNum));
                        }

                        break;
                    case R.id.rButton_Tue:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[1][i]);
                        }

                        break;
                    case R.id.rButton_Web:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[2][i]);
                        }

                        break;
                    case R.id.rButton_Tur:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[3][i]);
                        }

                        break;
                    case R.id.rButton_Fri:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[4][i]);
                        }

                        break;
                    case R.id.rButton_Sat:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[5][i]);
                        }

                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<String> spiAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        spi.setAdapter(spiAdapter);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rButton_Mon:
                        for (int i = 0; i < 15; i++) {
                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[0][i]);

                            // Log.e("체크빌딩넘", String.valueOf(CheckBuildingNum));
                        }

                        break;
                    case R.id.rButton_Tue:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[1][i]);
                        }

                        break;
                    case R.id.rButton_Web:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[2][i]);
                        }

                        break;
                    case R.id.rButton_Tur:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[3][i]);
                        }

                        break;
                    case R.id.rButton_Fri:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[4][i]);
                        }

                        break;
                    case R.id.rButton_Sat:
                        for (int i = 0; i < 15; i++) {

                            ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[5][i]);
                        }

                        break;
                }

            }
        });


        return view;

    }

    //건물명을 셋팅해줌
    public void setBuilding(String a) {
        for (int i = 0; i < 9; i++) {
            if (myApplication.ClassGroup[i].BuildingName.equals(a)) {
                CheckBuildingNum = i;
                Log.e("체크빌딩넘", String.valueOf(CheckBuildingNum));
            }
        }
    }
}

  /*  //셋팅된 건물명과 원하는 요일에 0교시부터 14교시까지 차례대로 텍스브튜에 set 시킴
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //MyApplication myApplication = MyApplication.instance();
        switch(group.getCheckedRadioButtonId()) {
            case R.id.rButton_Mon:
                for(int i = 0; i<15; i++){
                    ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[0][i]);
                    Log.e("TAG11",myApplication.ClassGroup[CheckBuildingNum].ClassNum[0][i]);
                }

                break;
            case R.id.rButton_Tue:
                for(int i=0;i<15;i++){

                    ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[1][i]);
                }

                break;
            case R.id.rButton_Web:
                for(int i=0;i<15;i++){

                    ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[2][i]);
                }

                break;
            case R.id.rButton_Tur:
                for(int i=0;i<15;i++){

                    ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[3][i]);
                }

                break;
            case R.id.rButton_Fri:
                for(int i=0;i<15;i++){

                    ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[4][i]);
                }

                break;
            case R.id.rButton_Sat:
                for(int i=0;i<15;i++){

                    ClassView[i].setText(myApplication.ClassGroup[CheckBuildingNum].ClassNum[5][i]);
                }

                break;
        }
        }




}
*/
