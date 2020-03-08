package com.iti.intake40.tripguide.addTrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.Arrays;
import java.util.Calendar;

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

    // map auto complete
    PlacesClient placesClient;
    String apiKey = "AIzaSyBNgF_t8g4xhoVOcM2KAIgVjOwOecnBWcM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setupViews();
        getLocations(R.id.frag,startPoint);
        getLocations(R.id.frag2,endPoint);
        addTripPresenter = new AddTripPresenter(AddTrip.this);
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPickerDialog();
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    addTripPresenter.addTrip(tripName.getText().toString().trim(),
                            startPoint.getText().toString().trim(),
                            endPoint.getText().toString().trim(),
                            timerText.getText().toString().trim(),
                            calenderText.getText().toString().trim(),
                            "upComing",
                            direction_text,
                            repeating_text);
                }
                else {
                    displayMessage("Unable To Add Trip");
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
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String s = dayOfMonth + "-" + month + "-" + year;
        calenderText.setText(s);
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
        String s = hourOfDay + ":" + minute;
        timerText.setText(s);
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
    @Override
    public boolean checkValidation() {
        boolean check = false;
        if (checkEmpty(tripName.getText().toString().trim()) &
                checkEmpty(startPoint.getText().toString().trim()) &
                checkEmpty(endPoint.getText().toString().trim()) &
                checkEmpty(timerText.getText().toString().trim()) &
                checkEmpty(calenderText.getText().toString().trim())) {
            check = true;
        }
        return check;
    }

    @Override
    public boolean checkEmpty(String input) {
        boolean check = false;
        if (input.isEmpty() == false) {
            check = true;
        }
        return check;
    }

    // SetupViews
    private void setupViews() {
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

        // map auto complete
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext() , apiKey);
        }
        placesClient = Places.createClient(this);


    }
    // select points
    private void getLocations(int fragment, final TextView result){
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID , Place.Field.LAT_LNG , Place.Field.NAME));
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

    @Override
    protected void onStop() {
        super.onStop();
        addTripPresenter.stop();
    }
}
