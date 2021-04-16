package com.zybooks.pizzaparty;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.annotation.NonNull;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String KEY_TOTAL_PIZZAS = "totalPizzas";
    private int mTotalPizzas;

    private final static String TAG = "MainActivity";
    private EditText mNumAttendEditText;
    private TextView mNumPizzasTextView;
    private Spinner mHowHungrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate was called");
        // Assign the widgets to fields
        mNumAttendEditText = findViewById(R.id.attendEditText);
        mNumPizzasTextView = findViewById(R.id.answerTextView);
        mHowHungrySpinner = findViewById(R.id.hungerSpinner);

        // add a TextWatcher to attendEditText
        mNumAttendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNumPizzasTextView.setText("");
            }
        });

        // set up the hungerSpinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hunger_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHowHungrySpinner.setAdapter(adapter);
        mHowHungrySpinner.setSelection(0, false);
        mHowHungrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
                mNumPizzasTextView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // restore state
        if (savedInstanceState != null) {
            mTotalPizzas = savedInstanceState.getInt(KEY_TOTAL_PIZZAS);
            displayTotal();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TOTAL_PIZZAS, mTotalPizzas);
    }

    public void calculateClick(View view) {

        // Get how many are attending the party
        int numAttend;
        try {
            String numAttendStr = mNumAttendEditText.getText().toString();
            numAttend = Integer.parseInt(numAttendStr);
        } catch (NumberFormatException ex) {
            numAttend = 0;
        }

        // Get hunger level selection
        String hungerLevel = mHowHungrySpinner.getSelectedItem().toString();
        PizzaCalculator calc;
        if (hungerLevel.equals("Light")) {
            calc = new PizzaCalculator(numAttend, PizzaCalculator.HungerLevel.LIGHT);
        } else if (hungerLevel.equals("Medium")) {
            calc = new PizzaCalculator(numAttend, PizzaCalculator.HungerLevel.MEDIUM);
        } else {
            calc = new PizzaCalculator(numAttend, PizzaCalculator.HungerLevel.RAVENOUS);
        }

        // Get the number of pizzas needed
        mTotalPizzas = calc.getTotalPizzas();
        displayTotal();
    }

    private void displayTotal () {
        String totalText = getString(R.string.total_pizzas, mTotalPizzas);
        mNumPizzasTextView.setText(totalText);
    }
}