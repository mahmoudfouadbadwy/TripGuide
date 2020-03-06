package com.iti.intake40.tripguide.addTrip;

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

import com.iti.intake40.tripguide.R;
import java.util.Calendar;

public class AddTrip extends AppCompatActivity implements AddTripContract.AddTripView , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {

    EditText tripName ;
    EditText startPoint ;
    EditText endPoint ;

    ImageView calender ;
    ImageView timer ;

    TextView calenderText ;
    TextView timerText ;

    Spinner repeating ;
    Spinner direction ;

    Button addTripButton ;

    ArrayAdapter<CharSequence> arrayAdapter ;

    AddTripContract.AddTripPresenter addTripPresenter ;

    String rrepating = "No Repeat" ;
    String ddirection = "one Way";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        setupViews();

        addTripPresenter = new AddTripPresenter();

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
                checkValidation();

            }
        });


    }


    // show Date picker
    @Override
    public void showDataPickerDialog(){
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
        String s = dayOfMonth +"-"+month+"-"+ year;
        calenderText.setText(s);
    }





    // show time picker
    @Override
    public void showTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String s = hourOfDay+":"+minute;
        timerText.setText(s);
    }





    //show snipper
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getCount() >= 4)
        {
            rrepating = parent.getSelectedItem().toString();

        }else{
            ddirection = parent.getSelectedItem().toString();
        }

        System.out.println(parent.getSelectedItem().toString() +""+ position);


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    // checkValidation
    @Override
    public void checkValidation(){

        if( noEmptyText(tripName.getText().toString().trim()) &&
                noEmptyText(startPoint.getText().toString().trim()) &&
                noEmptyText(endPoint.getText().toString().trim()) &&
                noEmptyText(timerText.getText().toString().trim()) &&
                noEmptyText(calenderText.getText().toString().trim()) ){

            // go to data base fire base and save it ;


            TripBojo tripBojo = new TripBojo(tripName.getText().toString().trim(),
                    startPoint.getText().toString().trim(),
                    endPoint.getText().toString().trim(),
                    timerText.getText().toString().trim(),
                    calenderText.getText().toString().trim(),
                    "upComing",
                    ddirection,
                    rrepating);


            addTripPresenter.addTripToDataBase(tripBojo);

            Toast.makeText(this , "go to firebase data" , Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(this , "please complete data" , Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean noEmptyText(String input){
        boolean check = false;

        if(input.isEmpty() == false)
        {
            check = true;
        }
        return check;
    }




    // SetupViews
    private void setupViews(){

        tripName = findViewById(R.id.tripName);
        startPoint = findViewById(R.id.startPoint);
        endPoint = findViewById(R.id.endPoint);

        calender = findViewById(R.id.calender);
        timer  = findViewById(R.id.timer);

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
    }


    // go to home page
    @Override
    public void goToHomePage(){
        finish();
    }


}
