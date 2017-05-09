package com.example.pardeepkaur.vaccinemoniter1;

import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Activity6 extends AppCompatActivity {
    public int mYear,mMonth,mDay;
    EditText e1;
    EditText mEdit;
    EditText mEdite;
    String text;
    String sdate="",edate="";
   int monthS,monthE,dayS,dayE,yearS,yearE;
    String temp="";
    String light="";
    String insu="";
    CheckBox tempCheck;
    CheckBox lightCheck;
    CheckBox insuCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);


    }
    public void itemClicked(View v)
    {
         tempCheck = (CheckBox)findViewById(R.id.temprature);
        lightCheck = (CheckBox)findViewById(R.id.brightness);
        insuCheck = (CheckBox)findViewById(R.id.insulation);
        if(tempCheck.isChecked())
        {
    //        Toast.makeText(getApplicationContext(), "yoooo", Toast.LENGTH_SHORT).show();
            temp="Temperature:";
        }
        else temp="";
        if(lightCheck.isChecked())
        {
            light="Light:";
        }
        else light="";
        if(insuCheck.isChecked())
        {
            insu="Air Quality:";
        }
        else insu="";

    }

    public void onConnectClicked(View v) throws IOException {
        String csvFile = "";
        csvFile = "/storage/emulated/0/logFile.csv";
        String line = "";
        String cvsSplitBy = ",";
        File root = Environment.getExternalStorageDirectory();

        String baseDir=root.getAbsolutePath();


        File fileee = new File(root,"logFile.csv");
        FileWriter writer;

        sdate=mEdit.getText().toString();
        edate=mEdite.getText().toString();



        if (fileee.exists()) {
            if ((!temp.equals("")) || (!light.equals("")) || (!insu.equals(""))) {
                if (!(sdate.equals(""))) {
                    if (!(edate.equals(""))){
                        if (yearS <= yearE && monthS <= monthE && dayS <= dayE) {
                            String TAG = "SignIn Log";
                            String email, pname;
                            Bundle extras = getIntent().getExtras();
                            email = extras.getString("email");
                            pname = extras.getString("name");
                            // String csvFile = "/storage/emulated/0/logFile.csv";
                            BufferedReader br = null;

                            int tempF = 0, lightF = 0, insuF = 0;
                            try {

                                br = new BufferedReader(new FileReader(csvFile));

                                while (yearS <= yearE && monthS <= monthE && dayS <= dayE) {
                                    br = new BufferedReader(new FileReader(csvFile));
                                    while ((line = br.readLine()) != null) {

                                        // use comma as separator
                                        String[] index = line.split(cvsSplitBy);
                                        if (sdate.equals(index[1])) {
                                            if (temp.equals(index[2]))
                                                tempF = tempF + 1;
                                            if (light.equals(index[2]))
                                                lightF = lightF + 1;
                                            if (insu.equals(index[2]))
                                                insuF = insuF + 1;
                                        }

                                        System.out.println(index[2]
                                                + " , name=" + index[3] + "]");
                                        Log.d(TAG, "values" + index[1] + "^^^^^^^^^^" + index[2] + "***********" + index[3] + "****" + tempF);

                                    }
                                    if (dayS == 31 && monthS == 12) {
                                        monthS = 1;
                                        dayS = 1;
                                        yearS++;
                                    } else if (monthS == 1 || monthS == 3 || monthS == 5 || monthS == 7 || monthS == 8 || monthS == 10 || monthS == 12) {
                                        if (dayS == 31) {
                                            dayS = 1;
                                            monthS++;
                                        } else dayS++;
                                    } else if (monthS == 4 || monthS == 6 || monthS == 9 || monthS == 11) {
                                        if (dayS == 30) {
                                            dayS = 1;
                                            monthS++;
                                        } else dayS++;
                                    } else if (monthS == 2) {
                                        if (dayS == 28) {
                                            dayS = 1;
                                            monthS++;
                                        } else dayS++;
                                    }
                                    sdate = dayS + "/" + monthS + "/" + yearS;
                                }

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (br != null) {
                                    try {
                                        br.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            mEdit.setText("");
                            mEdite.setText("");
                            edate="";
                            sdate="";
                            if (tempCheck.isChecked())
                                tempCheck.toggle();
                            if (lightCheck.isChecked())
                                lightCheck.toggle();
                            if (insuCheck.isChecked())
                                insuCheck.toggle();
                            temp = "";
                            light = "";
                            insu = "";

                            System.out.println("Done");
                            String temppp = "" + tempF;
                            String lighttt = "" + lightF;
                            String insuuu = "" + insuF;
                            Intent intent1 = new Intent(getApplicationContext(), Activity7.class);
                            intent1.putExtra("email", email);
                            intent1.putExtra("name", pname);
                            intent1.putExtra("temppp", temppp);
                            intent1.putExtra("lighttt", lighttt);
                            intent1.putExtra("insuuu", insuuu);
                            //     Toast.makeText(this, pname, Toast.LENGTH_SHORT).show();
                            startActivity(intent1);
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter a valid date", Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a valid date", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter a valid date", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter appropriate data", Toast.LENGTH_SHORT).show();
        }
    }
        else {
            Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
        }
    }
    public void selectDate(View view) {

        SelectDateFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    public void selectDateE(View view) {

        SelectDateFragmentE newFragment = new SelectDateFragmentE();
        newFragment.show(getSupportFragmentManager(), "DatePicker1");


    }
    public void populateSetDate(int year, int month, int day) {
        //dobyear=year;
        mEdit = (EditText)findViewById(R.id.sdate);
        mEdit.setText(day+"/"+month+"/"+year);
        monthS=month;
        dayS=day;
        yearS=year;

            sdate=day+"/"+month+"/"+year;
  }
    public void populateSetDateE(int year, int month, int day) {
        mEdite = (EditText)findViewById(R.id.edate);

        mEdite.setText(day+"/"+month+"/"+year);
        monthE=month;
        dayE=day;
        yearE=year;
        edate=day+"/"+month+"/"+year;
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, com.example.pardeepkaur.vaccinemoniter1.SelectDateFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

    }
    public class SelectDateFragmentE extends DialogFragment implements DatePickerDialog.OnDateSetListener, com.example.pardeepkaur.vaccinemoniter1.SelectDateFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDateE(yy, mm + 1, dd);
        }




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
