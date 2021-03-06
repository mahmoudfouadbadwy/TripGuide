package com.iti.intake40.tripguide.addTrip;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.model.Trip;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTrip extends AppCompatActivity implements AddTripContract.AddTripView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {

    private EditText tripName;
    private TextView startPoint;
    private TextView endPoint;
    private ImageView calender;
    private ImageView timer;
    private TextView calenderText;
    private TextView timerText;
    private Spinner repeating;
    private Spinner direction;
    private Button addTripButton;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private AddTripContract.AddTripPresenter addTripPresenter;
    private String repeating_text = "No Repeat";
    private String direction_text = "one Way";
    private PendingIntent pendingIntent;
    private Intent broadcastIntent;
    private AlarmManager alarmMgr;
    private android.icu.util.Calendar calendar;
    // map auto complete
    PlacesClient placesClient;
    String apiKey = "AIzaSyBNgF_t8g4xhoVOcM2KAIgVjOwOecnBWcM";
    // edit trip
    private Intent editData;
    private TimePicker selectedTime;
    private DatePicker selectedDate;
    // flag for time or date change
    private boolean changeFlag = false;


    //
    int hour ;
    int minute ;

    int day ;
    int month ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setupViews();
        // load trip data on edit mode
        if (getIntent().hasExtra("tripName")) {
            getTripData();
        }
        addTripPresenter = new AddTripPresenter(AddTrip.this);
        // calender action
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPickerDialog();
            }
        });
        // timer action
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        // save button action
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (checkValidation()) {

                    // edit
                    if (editData.hasExtra("key")) {
                        if (changeFlag) {
                            cancelAlarm();
                        }
                        editTrip();
                        // add
                    } else {
                        addTrip();
                    }

                } else {
                    displayMessage("Unable To Save Trip Please Check Your Data");
                }
            }
        });
    }

    // show Date picker
    @Override
    public void showDataPickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate = view;
        int newMonth = month + 1;
        calenderText.setText(dayOfMonth + "-" + newMonth + "-" + year);
        changeFlag = true;
        this.day = dayOfMonth;
        this.month = month;
    }

    // show time picker
    @Override
    public void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(AddTrip.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selectedTime = view;
        timerText.setText(hourOfDay + ":" + minute);
        changeFlag = true;
        this.hour = hourOfDay;
        this.minute = minute;

    }

    public boolean compareToNotify(){

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        boolean check = false;

        if(d.getDate()== day)
        {
            if(d.getHours() <= hour)
            {
                if(d.getMinutes() <= minute)
                {
                    check = true;
                }
                else {
                    check = false;
                }

            } else {
                check = false;
            }

        } else if(d.getDate()< day) {
                check = true;
        }


    return check;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getCount() >= 4) {
            repeating_text = parent.getSelectedItem().toString();

        } else {
            direction_text = parent.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // checkValidation
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean checkValidation() {
        boolean check = false;
        if (checkEmpty(tripName.getText().toString().trim()) &
                checkEmpty(startPoint.getText().toString().trim()) &
                checkEmpty(endPoint.getText().toString().trim()) &
                checkEmpty(timerText.getText().toString().trim()) &
                checkEmpty(calenderText.getText().toString().trim())
        ) {
            check = true;
        }
        return check;
    }

    // check empty field.
    @Override
    public boolean checkEmpty(String input) {
        boolean check = false;
        if (input.isEmpty() == false) {
            check = true;
        }
        return check;
    }

    // SetupViews
    @Override
    public void setupViews() {
        tripName = findViewById(R.id.tripName);
        startPoint = findViewById(R.id.startPoint);
        endPoint = findViewById(R.id.endPoint);
        calender = findViewById(R.id.calender);
        timer = findViewById(R.id.timer);
        calenderText = findViewById(R.id.calenderText);
        timerText = findViewById(R.id.timeText);
        repeating = findViewById(R.id.repeating);
        direction = findViewById(R.id.direction);
        addTripButton = findViewById(R.id.addTrip);
        repeating.setOnItemSelectedListener(this);
        arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerDirection,
                R.layout.support_simple_spinner_dropdown_item
        );
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        direction.setAdapter(arrayAdapter);
        direction.setOnItemSelectedListener(this);
        editData = getIntent();
        // map auto complete
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        placesClient = Places.createClient(this);
        getLocations(R.id.frag, startPoint);
        getLocations(R.id.frag2, endPoint);
    }

    // select points
    @Override
    public void getLocations(int fragment, final TextView result) {
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        // for edit
        if (editData.hasExtra("from")) {
            switch (result.getId()) {
                case R.id.startPoint:
                    autocompleteSupportFragment.setText(editData.getExtras().getString("from"));
                    break;
                case R.id.endPoint:
                    autocompleteSupportFragment.setText(editData.getExtras().getString("to"));
                    break;
            }
        }
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                result.setText(place.getName());
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    // go to home page
    @Override
    public void goToHomePage() {
        finish();
    }

    // set alarm
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(Trip trip, String key) {
        System.out.println("Alarm Fire");
        System.out.println("Alarm Key :" + trip.getAlarmKey());
        calendar = android.icu.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (selectedDate == null) {
            String date[] = splitString(trip.getDay(), "[-]");
            calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
        } else {
            calendar.set(Calendar.YEAR, selectedDate.getYear());
            calendar.set(Calendar.MONTH, selectedDate.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, selectedDate.getDayOfMonth());
        }
        if (selectedTime == null) {
            String time[] = splitString(trip.getTime(), "[:]");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, selectedTime.getHour());
            calendar.set(Calendar.MINUTE, selectedTime.getMinute());
        }
        calendar.set(Calendar.SECOND, 0);
        broadcastIntent = new Intent(AddTrip.this, AlarmBroadCast.class);
        broadcastIntent.putExtra("tripName", trip.getTripName());
        broadcastIntent.putExtra("key", key);
        broadcastIntent.putExtra("from", trip.getStartPoint());
        broadcastIntent.putExtra("to", trip.getEndPoint());
        broadcastIntent.putExtra("alarmKey", trip.getAlarmKey());

        pendingIntent = PendingIntent.getBroadcast(AddTrip.this, trip.getAlarmKey(), broadcastIntent, 0);
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        addTripPresenter.stop();
    }

    // load trip on edit mode
    @Override
    public void getTripData() {
        int repeat = 0;
        int direction_index = 0;
        editData = getIntent();
        tripName.setText(editData.getExtras().getString("tripName"));
        startPoint.setText(editData.getExtras().getString("from"));
        endPoint.setText(editData.getExtras().getString("to"));
        calenderText.setText(editData.getExtras().getString("tripDate"));
        timerText.setText(editData.getExtras().getString("tripTime"));
        // repeating
        if (editData.getExtras().getString("repeat").equalsIgnoreCase("No Repeat")) {
            repeat = 0;
        } else if (editData.getExtras().getString("repeat").equalsIgnoreCase("Daily")) {
            repeat = 1;
        } else if (editData.getExtras().getString("repeat").equalsIgnoreCase("Weakly")) {
            repeat = 2;
        } else if (editData.getExtras().getString("repeat").equalsIgnoreCase("Monthly")) {
            repeat = 3;
        }
        repeating.setSelection(repeat, true);

        // direction
        if (editData.getExtras().getString("direction").equalsIgnoreCase("One Way")) {
            direction_index = 0;
        } else if (editData.getExtras().getString("direction").equalsIgnoreCase("Round Trip")) {
            direction_index = 1;
        }
        direction.setSelection(direction_index, true);

    }

    // cancel alarm for edit .
    @Override
    public void cancelAlarm() {
        System.out.println("Alarm canceled from editing");
        Intent broadcastIntent = new Intent(AddTrip.this, AlarmBroadCast.class);
        broadcastIntent.putExtra("tripName", editData.getExtras().getString("tripName"));
        broadcastIntent.putExtra("key", editData.getExtras().getString("key"));
        broadcastIntent.putExtra("from", editData.getExtras().getString("from"));
        broadcastIntent.putExtra("to", editData.getExtras().getString("to"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTrip.this, editData.getExtras().getInt("alarmKey"), broadcastIntent, 0);
        AlarmManager alarmMgr = (AlarmManager) AddTrip.this.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(pendingIntent);
    }
    @Override
    public String[] splitString(String txt, String operation) {
        return txt.split(operation);
    }
    @Override
    public void editTrip() {
        addTripPresenter.editTrip(tripName.getText().toString().trim(),
                startPoint.getText().toString().trim(),
                endPoint.getText().toString().trim(),
                timerText.getText().toString().trim(),
                calenderText.getText().toString().trim(),
                "upComing",
                direction_text,
                repeating_text,
                editData.getExtras().getString("key"), changeFlag);

    }
    @Override
    public void addTrip() {

        if(compareToNotify()){
            addTripPresenter.addTrip(tripName.getText().toString().trim(),
                    startPoint.getText().toString().trim(),
                    endPoint.getText().toString().trim(),
                    timerText.getText().toString().trim(),
                    calenderText.getText().toString().trim(),
                    "upComing",
                    direction_text,
                    repeating_text);
        }else{
            displayMessage("Please Enter Valid time As 24 Hours");
        }

    }
}
