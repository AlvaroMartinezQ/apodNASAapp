package com.urjc.nasaapod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button seeApodImage;
    private Button seeApodImageDate;
    private TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seeApodImage = findViewById(R.id.seeApodImage);
        seeApodImageDate = findViewById(R.id.seeApodImageDate);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Enable buttons
        seeApodImage.setEnabled(true);
        seeApodImageDate.setEnabled(true);
        // Reset loading text status
        loading.setVisibility(View.INVISIBLE);
    }

    public void toApod(View view) {
        // Disable buttons
        seeApodImage.setEnabled(false);
        seeApodImageDate.setEnabled(false);
        // Show loading text
        loading.setVisibility(View.VISIBLE);
        // Create the new intent
        Intent apod = new Intent(this, APOD.class);
        startActivity(apod);
    }

    public void openDialog(View view) {
        DialogFragment datePicker = new CustomDatePicker();
        datePicker.show(getSupportFragmentManager(), "Select a date");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        // Year - month - date
        boolean badDate = false;
        Calendar cal = Calendar.getInstance();
        if (y < cal.get(Calendar.YEAR)) {
            // Skip - valid date
        } else if (y == cal.get(Calendar.YEAR)) {
            if (m < cal.get(Calendar.MONTH)) {
                // Skip - valid date
            } else if (m == cal.get(Calendar.MONTH)) {
                if (d <= cal.get(Calendar.DAY_OF_MONTH)) {
                    // Skip - valid date
                } else { badDate = true; }
            } else { badDate = true; }
        } else { badDate = true; }
        if (badDate) {
            Toast.makeText(this, "Bad dates introduced!", Toast.LENGTH_SHORT).show();
        } else {
            seeApodImage.setEnabled(false);
            seeApodImageDate.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
            Intent apod = new Intent(this, APOD.class);
            apod.putExtra("date", y + "-" + (m + 1) + "-" + d); // (m+1) because Java API is super well designed
            // Read more about the previous comment here:
            // https://stackoverflow.com/questions/344380/why-is-january-month-0-in-java-calendar
            startActivity(apod);
        }
    }
}