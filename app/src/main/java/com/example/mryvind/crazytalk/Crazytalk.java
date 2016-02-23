package com.example.mryvind.crazytalk;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Crazytalk extends AppCompatActivity {

    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;
    private double tipAmount;
    private double finalBill;

    EditText billBeforeTipET;
    EditText tipAmounttipET;
    EditText finalBillET;
    SeekBar tipSeekBar;

    private int[] checklistValues = new int[12];

    CheckBox friendlyCheckBox;
    CheckBox opinionCheckBox;
    CheckBox specialsCheckBox;

    RadioGroup availableRadioGroup;
    RadioButton availableOkRadio;
    RadioButton availableBadRadio;
    RadioButton availableGoodRadio;

    Spinner ProblemSpinner;

    Button startChronometerButton;
    Button pauseChronometerButton;
    Button resetChronometerButton;

    Chronometer timeWaitingChronometer;

    long secondsYouWaited = 0;

    TextView timeWaitingTextView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazytalk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null){
            billBeforeTip = 0.0;
            tipAmount = 0.15;
            finalBill = 0.0;
        }
        else {
            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        }

        billBeforeTipET = (EditText) findViewById(R.id.BilleditText);
        tipAmounttipET = (EditText) findViewById(R.id.TipeditText);
        finalBillET = (EditText) findViewById(R.id.FinalBilleditText);
        tipSeekBar = (SeekBar) findViewById(R.id.seekBar);

        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);
        billBeforeTipET.addTextChangedListener(billBeforeTipListener);

        friendlyCheckBox = (CheckBox) findViewById(R.id.friendlycheckBox);
        opinionCheckBox = (CheckBox) findViewById(R.id.opinioncheckBox);
        specialsCheckBox = (CheckBox) findViewById(R.id.SpecialscheckBox);

        setupIntroCheckBoxes();

        availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);
        availableOkRadio = (RadioButton) findViewById(R.id.availableOkRadio);
        availableBadRadio = (RadioButton)findViewById(R.id.availableBadRadio);
        availableGoodRadio = (RadioButton)findViewById(R.id.availableGoodRadio);

        addChangeListenersRadio();

        ProblemSpinner = (Spinner)findViewById(R.id.problemSpinner);

        addItmeselectedListenerToSpinner();

        startChronometerButton = (Button)findViewById(R.id.StartButton);
        pauseChronometerButton = (Button)findViewById(R.id.pauseButton);
        resetChronometerButton = (Button)findViewById(R.id.resetButton);

        setButtonOnClickListener();

        timeWaitingChronometer = (Chronometer)findViewById(R.id.timeWaitingchronometer);

        timeWaitingTextView = (TextView)findViewById(R.id.timeWaitingtextView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private SeekBar.OnSeekBarChangeListener tipSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tipAmount = (seekBar.getProgress()) * 0.01;
            tipAmounttipET.setText(String.format("%.02f", tipAmount));
            updateTipandFinalBill();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private TextWatcher billBeforeTipListener = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        try{
            billBeforeTip = Double.parseDouble(s.toString());
           }
        catch(NumberFormatException e){
            billBeforeTip = 0.0;
           }
            updateTipandFinalBill();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateTipandFinalBill(){

        double tipAmount = Double.parseDouble(tipAmounttipET.getText().toString());
        double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
        finalBillET.setText(String.format("%.02f",finalBill));
    }
    protected void onSaveInstanceState(Bundle outState){
    super.onSaveInstanceState(outState);
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP,tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
    }
    private void setupIntroCheckBoxes(){
        friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checklistValues[0] = friendlyCheckBox.isChecked() ? 4 : 0;
                setTipFromWaitressChecklist();
                updateTipandFinalBill();
            }
        });
        specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checklistValues[1] = specialsCheckBox.isChecked() ? 1 : 0;
                setTipFromWaitressChecklist();
                updateTipandFinalBill();
            }
        });
        opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checklistValues[2] = opinionCheckBox.isChecked() ? 2 : 0;
                setTipFromWaitressChecklist();
                updateTipandFinalBill();
            }
        });
    }
    private void setTipFromWaitressChecklist(){

        int checklistTotal = 0;
        for (int item : checklistValues){
            checklistTotal += item;
        }
       tipAmounttipET.setText(String.format("%.02f", checklistTotal * 0.01));
    }

    private void addChangeListenersRadio(){
        availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                checklistValues[3] = availableBadRadio.isChecked() ? -1 : 0;
                checklistValues[4] = availableOkRadio.isChecked() ? 2 : 0;
                checklistValues[5] = availableGoodRadio.isChecked() ? 4 : 0;
                setTipFromWaitressChecklist();
                updateTipandFinalBill();
            }
        });
    }

    private void addItmeselectedListenerToSpinner(){

      ProblemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              checklistValues[6] = ProblemSpinner.getSelectedItem().equals("Bad") ? -1 : 0;
              checklistValues[7] = ProblemSpinner.getSelectedItem().equals("Ok") ? 3 : 0;
              checklistValues[8] = ProblemSpinner.getSelectedItem().equals("Good") ? 6 : 0;
              setTipFromWaitressChecklist();
              updateTipandFinalBill();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });
    }
    private void setButtonOnClickListener(){
        startChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stoppedMilliseconds = 0;
                String ChronoText = timeWaitingChronometer.getText().toString();
                String array[] = ChronoText.split(":");

                if (array.length == 2){
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;

                }
                else if(array.length == 3){
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1])* 60 * 1000
                    + Integer.parseInt(array[2])* 1000;
                }
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                secondsYouWaited = Long.parseLong(array[1]);
                updateTipBasedOnTimeWaited(secondsYouWaited);
                timeWaitingChronometer.start();
            }
        });
        pauseChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWaitingChronometer.stop();
            }
        });
        resetChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
                secondsYouWaited = 0;
            }
        });
    }

    private void updateTipBasedOnTimeWaited(long secondsYouWaited){

        checklistValues[9] = (secondsYouWaited > 10) ? -2 : 2;
        setTipFromWaitressChecklist();
        updateTipandFinalBill();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crazytalk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
