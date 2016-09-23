package com.vkrakatitsa.radio.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.vkrakatitsa.radio.Activities.ChooseTimeActivity;
import com.vkrakatitsa.radio.Listeners.RepeatListener;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.ToolsAndConstants.Connects.BaseConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import io.apptik.widget.MultiSlider;


public class ChooseTimeAndDateFragment extends Fragment implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener,MultiSlider.OnThumbValueChangeListener {

    private static final String FLAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    public static final String SAVE_INSTANCE_TIME_FROM_KEY="SaveInstanceTimeFrom";
    public static final String SAVE_INSTANCE_TIME_TO_KEY="SaveInstanceTimeTo";
    public static final String SAVE_INSTANCE_DATE_YEAR_KEY="SaveInstanceDateYear";
    public static final String SAVE_INSTANCE_DATE_MONTH_KEY="SaveInstanceDateMonth";
    public static final String SAVE_INSTANCE_DATE_DAY_KEY ="SaveInstanceDateDAy";


    private TextView m_textDate, m_textTimeFrom, m_textTimeTo;
    private MultiSlider m_multislider;

    private String m_strDateFormate;  // String Date to send in BaseConnection
    private String m_strTimeFrom;     // String Time From to send in BaseConnection
    private String m_strTimeTo;       // String Time To to send in BaseConnection

    private ProgressDialog m_progressDialog;

    private Calendar m_calendarFrom, m_calendarTo,  m_calendarCurentTime;

    private BaseConnection m_baseConnection;
    private java.text.DateFormat m_dateFormat;
    private Date m_date;
    private  long m_nId;
    AsyncTask<Void, Void , LinkedHashMap<String,String>> m_async;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Look","ChooseTimeAndDateFragment Start ");

        View view = inflater.inflate(R.layout.fragment_choose_time_and_date,container,false);

        m_textDate = (TextView) view.findViewById(R.id.set_date);
        m_textTimeFrom =(TextView)view.findViewById(R.id.ShowTimeFrom);
        m_textTimeTo = (TextView)view.findViewById(R.id.ShowTimeTo);
        ImageButton buttonDate=(ImageButton)view.findViewById(R.id.buttom_set_date);
        Button buttonStart = (Button)view.findViewById(R.id.chooseTimeAndDateFragmentButtonStart);
        Button buttonPlus= (Button) view.findViewById(R.id.timePlus);
        Button buttonMinus = (Button) view.findViewById(R.id.timeMinus);
        m_multislider =(MultiSlider)view.findViewById(R.id.TimeRangeBar);

        m_multislider.setOnThumbValueChangeListener(this);

        buttonDate.setOnClickListener(this);
        buttonStart.setOnClickListener(this);

        //Button +3 minut Listener
        buttonMinus.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MultiSlider.Thumb thumb = m_multislider.getThumb(0);        //get Thumb From
                int value = thumb.getValue();                           //get thumbValue
                thumb.setValue(value+3);                                //set Thumb value +3
            }
        }));

        //Button -3 minut Listener
        buttonPlus.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiSlider.Thumb thumb = m_multislider.getThumb(1);       //get Thumb To
                int value = thumb.getValue();                           //get thumbValue
                thumb.setValue(value-3);                                //set Thumb value -3
            }
        }));


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        m_calendarFrom = m_calendarTo = m_calendarCurentTime = Calendar.getInstance();
        m_date = new Date();
        m_dateFormat = new SimpleDateFormat(getResources().getString(R.string.time_format_string));
        m_progressDialog = new ProgressDialog(getContext());                                          // Wait Dialog

        // Restore savedInstance
        if(savedInstanceState!=null&&(savedInstanceState.getInt(SAVE_INSTANCE_DATE_YEAR_KEY,-1)!=-1)){

            Log.d("Look","ChooseTimeAndDateFragment- onActivityCreated start restore instance");
            m_multislider.getThumb(0).setValue(savedInstanceState.getInt(SAVE_INSTANCE_TIME_FROM_KEY));
            m_multislider.getThumb(1).setValue(savedInstanceState.getInt(SAVE_INSTANCE_TIME_TO_KEY));

            int year =savedInstanceState.getInt(SAVE_INSTANCE_DATE_YEAR_KEY,-1);
            int month =savedInstanceState.getInt(SAVE_INSTANCE_DATE_MONTH_KEY,-1);
            int day =savedInstanceState.getInt(SAVE_INSTANCE_DATE_DAY_KEY,-1);
            setDate(year,month,day);

            Log.d("Look","ChooseTimeAndDateFragment- onActivityCreated thumb1 "+ m_multislider.getThumb(0).getValue()+"  |  thumb2->"+ m_multislider.getThumb(1).getValue()+"  |  year -> "+year+"  |  month ->"+month+"  |  day->"+day);

        }else{

            int nValue = m_calendarCurentTime.get(Calendar.HOUR_OF_DAY)*60+ m_calendarCurentTime.get(Calendar.MINUTE); // get Curent Time in Minutes for Thumb1
            m_multislider.getThumb(1).setValue(nValue);       //Set thumbTo in Position Curent Time
            m_multislider.getThumb(0).setValue(0);            //Set thumbTo in Position Time 00:00

            setDate();      //setDefaultDate
        }

        Log.d("Look","year-> "+ m_calendarCurentTime.getTime());
    }

    //----------------------------------------------------------------------------------------------OnClick
    @Override
    public void onClick(final View view) {

        //Blocking view while action processed
        view.setEnabled(false);

        switch (view.getId()){
            case(R.id.buttom_set_date):
                showDatePickerDialog();
                break;
            case R.id.chooseTimeAndDateFragmentButtonStart:
                startSearch();
        }
        view.setEnabled(true);
    }



    //----------------------------------------------------------------------------------------------ShowDatePicker
    public void showDatePickerDialog( ){

        long lWeek= 7*24*60*60*1000;        //lWeek is Week in milliseconds
        Calendar cNow = Calendar.getInstance();
        long tfrom = cNow.getTimeInMillis() - lWeek;    // Time from in milliseconds. Equal time  current time minus 7days

        // Set calendar boundaries
        MonthAdapter.CalendarDay from = new MonthAdapter.CalendarDay(tfrom);
        MonthAdapter.CalendarDay now = new MonthAdapter.CalendarDay(Calendar.getInstance());

        CalendarDatePickerDialogFragment calendar = new CalendarDatePickerDialogFragment(); //Create datePeaker

        calendar.setDateRange(from,now);          //set boundaries
        calendar.setOnDateSetListener(this);
        calendar.setThemeDark();                   // set DatePeaker Theme

        calendar.show(getActivity().getSupportFragmentManager(), FLAG_TAG_DATE_PICKER);

    }

    public void startSearch(){

        m_async = new AsyncTask<Void, Void , LinkedHashMap<String,String>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //Ban change orientation
                int nCurentOrientation = getResources().getConfiguration().orientation;
                if(nCurentOrientation== Configuration.ORIENTATION_LANDSCAPE){
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }else {
                    getActivity(). setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }

                //Create waitDialog
                m_progressDialog.setCancelable(false);
                m_progressDialog.setMessage(getResources().getString(R.string.wait_message));

                m_progressDialog.show();
            }

            @Override
            protected LinkedHashMap<String,String> doInBackground(Void... voids) {

                //Find selected radio station ID in intent from MainActivity
                Intent intent = getActivity().getIntent();
                if(intent!=null) {
                    m_nId = intent.getLongExtra(ChooseTimeActivity.GET_ITEM_ID_FOR_INTENT, -2);
                }else{
                    Log.e("Look","Error:\n No radio in intent in ChooseTimeAndDateFragment");
                }


                Log.d("Look","ChooseTimeAndDateFragment-handler: Do in Background");

                //start Searching
                m_baseConnection = BaseConnection.getConnection(getContext(), m_nId, m_strDateFormate, m_strTimeFrom, m_strTimeTo);
                m_baseConnection.createNewConnection();

                // search Result
                return  m_baseConnection.getArray();
            }


            @Override
            protected void onPostExecute(LinkedHashMap<String,String> linkedHashMap) {

                Log.d("Look","ChooseTimeAndDateFragment-handler: onPostExecute");

                //stop waitDialog
                if(m_progressDialog.isShowing()){
                    m_progressDialog.dismiss();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                }

                //Run onStartNewFragment (showResultFragment) in Activity
                ((OnStartNewFragment)getActivity()).onStartNewFragment(linkedHashMap, m_strDateFormate, m_strTimeFrom, m_strTimeTo);// Start New Fragment in Activity
                super.onPostExecute(linkedHashMap);
            }
        };
        m_async.execute();
    }

    //----------------------------------------------------------------------------------------------SetTime
    public void setTime(int nThumb , int hour , int minutes){

        //set thumb time from
        if(nThumb==0){

            Log.d("Look","ChooseTimeAndDate-setTime(From): hour is ->"+hour);
            //set time : Hour & Minutes
            m_calendarFrom.set(Calendar.HOUR_OF_DAY, hour);
            m_calendarFrom.set(Calendar.MINUTE,minutes );

            m_date.setTime(m_calendarFrom.getTimeInMillis());       //Convert Calendar in m_date
            m_strTimeFrom = m_dateFormat.format(m_date);              // format Time
            m_textTimeFrom.setText(m_strTimeFrom);

        }else if(nThumb==1){            //set thumb time to

            //set time : Hour & Minutes
            m_calendarTo.set(Calendar.HOUR_OF_DAY, hour);
            m_calendarTo.set(Calendar.MINUTE,minutes );

            m_date.setTime(m_calendarTo.getTimeInMillis());         //Convert Calendar in m_date
            m_strTimeTo = m_dateFormat.format(m_date);                // format Time
            m_textTimeTo.setText(m_strTimeTo);
        }
    }


    //----------------------------------------------------------------------------------------------setDate
    // Set default m_date
    public void setDate(){

        String strDateFormatTyPe = getResources().getString(R.string.date_format_string);    // String m_date format(dd-MM-yyyy)
        m_strDateFormate =(String) DateFormat.format(strDateFormatTyPe, m_calendarTo);
        m_textDate.setText(m_strDateFormate);
        Log.d("m_date ","ChooseTimeAndDatefRagment - setDate : m_strDateFormate->"+ m_strDateFormate);
    }

    // set given m_date
    public void setDate(int year,int month,int day){
        Calendar cDate = Calendar.getInstance();

        cDate.set(Calendar.YEAR,year);
        cDate.set(Calendar.MONTH,month);
        cDate.set(Calendar.DAY_OF_MONTH,day);

        String strDateFormatTyPe = getResources().getString(R.string.date_format_string);    // String m_date format(dd-MM-yyyy)
        m_strDateFormate =(String) DateFormat.format(strDateFormatTyPe,cDate);

        m_textDate.setText(m_strDateFormate);
    }

    //----------------------------------------------------------------------------------------------onDateSet
    //Interface CalendarDatePickerDialogFragment.OnDateSetListener
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

        setDate(year,monthOfYear,dayOfMonth);       //set given m_date
    }

    //----------------------------------------------------------------------------------------------onValueChanged
    //MultiSlider Interface OnThumbValueChangeListener
    @Override
    public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {

        int nHour=0;
        int nMinutes=value;

        //Convert minute in hours and minutes
        while(value>60){
            nMinutes = value= value-60;
            nHour++;
        }
            setTime(thumbIndex,nHour,nMinutes);     //set given time
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("Look","ChoosetimeAndDAteFragment-onSaveInstanceState:  outState=null->"+(outState==null)+"  | m_multislider==nul->"+(m_multislider ==null));
        super.onSaveInstanceState(outState);
        if(m_multislider !=null) {
            Log.d("Look","ChooseTimeAndDateFragment-onSaveInstanceState : thumb 0 ->"+ m_multislider.getThumb(0).getValue()+"  |  thumb1 ->"+ m_multislider.getThumb(1).getValue());
            outState.putInt(SAVE_INSTANCE_TIME_FROM_KEY, m_multislider.getThumb(0).getValue());
            outState.putInt(SAVE_INSTANCE_TIME_TO_KEY, m_multislider.getThumb(1).getValue());
            outState.putInt (SAVE_INSTANCE_DATE_YEAR_KEY, m_calendarTo.get(Calendar.YEAR));
            outState.putInt (SAVE_INSTANCE_DATE_MONTH_KEY, m_calendarTo.get(Calendar.MONTH));
            outState.putInt (SAVE_INSTANCE_DATE_DAY_KEY, m_calendarTo.get(Calendar.DAY_OF_MONTH));
           // outState.putBoolean(ON_REPEAT_SEARCH_KEY, m_isConnected);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(m_async !=null){
            m_async.cancel(true);
        }
        if((m_progressDialog !=null)&& m_progressDialog.isShowing()){
            m_progressDialog.dismiss();
        }

    }


    //Interface for Choose Time Activity. Pass information from ChooseTimeAndDateFragment to
    // ShowResultFragment through ChooseTimeActivity
    public interface OnStartNewFragment{


        void onStartNewFragment( LinkedHashMap<String, String> arrResult, String date, String timeFrom ,String TimeTo);
    }
}
