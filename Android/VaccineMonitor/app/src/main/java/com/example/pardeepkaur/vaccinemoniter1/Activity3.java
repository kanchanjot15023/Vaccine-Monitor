package com.example.pardeepkaur.vaccinemoniter1;

/**
 * Created by himanipande on 15/10/15.
 */

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import au.com.bytecode.opencsv.CSVWriter;

public class Activity3 extends Activity {

    String email,pname;

    int counter_for_header=0;
    static GregorianCalendar today=new GregorianCalendar();

    static long today_ms=today.getTimeInMillis();



    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    //CSV file header
    private static final Object [] FILE_HEADER = {"Timestamp","Tradable_type","Trade_Type","Ticker_Name","Price","Units"};


    int flag=1,flagForThread=1;
    int counter_L=1;
    int counter_T=1;
    int counter_A=1;


    int bluetoothgone=0;
    Thread myThread;




    int intentdoing=0;


    //////
    NotificationCompat.Builder notification;
    private static int uniqueID_L=500;
    private static int uniqueID_T=600;
    private static int uniqueID_A=700;

    /////
    Random r=new Random();


    Button btnOn, btnOff;
    TextView txtArduino, txtString, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    Handler bluetoothIn;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    // String for MAC address
    private static String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        registerReceiver(bluetooth, new IntentFilter(
                BluetoothDevice.ACTION_ACL_DISCONNECTED));


        /////
        notification=new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        /////

        setContentView(R.layout.activity_3);

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        pname=extras.getString("name");
   //     Toast.makeText(getApplicationContext(), pname, Toast.LENGTH_SHORT).show();

        //Link the buttons and textViews to respective views
        btnOn = (Button) findViewById(R.id.buttonOn);
       // btnOff = (Button) findViewById(R.id.buttonOff);
        txtString = (TextView) findViewById(R.id.txtString);
        txtStringLength = (TextView) findViewById(R.id.testView1);
        sensorView0 = (TextView) findViewById(R.id.sensorView0);
        sensorView1 = (TextView) findViewById(R.id.sensorView1);
        sensorView2 = (TextView) findViewById(R.id.sensorView2);
       // sensorView3 = (TextView) findViewById(R.id.sensorView3);



        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {


                if (bluetoothgone == 1)
                    Toast.makeText(getApplicationContext(), "No bluetooth connection", Toast.LENGTH_SHORT).show();

                else
                {
                    Intent i = new Intent(getApplicationContext(), Activity4.class);
                i.putExtra("EXTRA_DEVICE_ADDRESS", address);
                    i.putExtra("email",email);
                    i.putExtra("name",pname);

                intentdoing = 1;

                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(i);


                //  mConnectedThread.write("1");    // Send "1" via Bluetooth
                // Toast.makeText(getBaseContext(), "Set Threshold..", Toast.LENGTH_SHORT).show();
            }
        }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID

    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private BroadcastReceiver bluetooth=new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub


            String action=intent.getAction();
            String msg;
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
            {
                Toast.makeText(getApplicationContext(), "NO Bluetooth Connection", Toast.LENGTH_SHORT).show();
                sensorView0.setText("NO bluetooth Connection");
                sensorView1.setText("No Bluetooth Connection");
                sensorView2.setText("No bluetooth Connection");
                bluetoothgone=1;
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();

        //bluetoothgone=0;
if(bluetoothgone==1)
    Toast.makeText(getApplicationContext(),"No Bluetooth Connection",Toast.LENGTH_SHORT).show();
        else
        {
//        if(flagForThread==0)  myThread.stop();
        if (flag == 1) {


//Get MAC address from DeviceListActivity via intent
            Intent intent = getIntent();

            //Get the MAC address from the DeviceListActivty via EXTRA
            address = intent.getStringExtra("EXTRA_DEVICE_ADDRESS");


            //create device and set the MAC address

            BluetoothDevice device = btAdapter.getRemoteDevice(address);


            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    //insert code to deal with this
                }
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

            //I send a character when resuming.beginning transmission to check device is connected
            //If it is not an exception will be thrown in the write method and finish() will be called
            mConnectedThread.write("x");

        }


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        //   String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //   txtString.setText("Data Received = " + dataInPrint);
                        //  int dataLength = dataInPrint.length();                          //get length of data received
                        // txtStringLength.setText("String Length = " + String.valueOf(dataLength));


                        if (recDataString.charAt(0) == 'L')                             //if it starts with # we know it is what we are looking for
                        {


                            String sensor0 = recDataString.substring(2, endOfLineIndex);//get sensor value from string between indices 1-5


                            sensorView0.setText("Brightness = " + sensor0);    //update the textviews with sensor values
                            writeCSVFile2("logFile.csv", "Light:", recDataString.substring(2, endOfLineIndex), recDataString.toString());

                        }

                        if (recDataString.charAt(0) == 'T')                             //if it starts with # we know it is what we are looking for
                        {

                            //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(2, endOfLineIndex);            //same again...
                            sensorView1.setText("Temperature = " + sensor1);
                            writeCSVFile2("logFile.csv", "Temperature:", recDataString.substring(2, endOfLineIndex), recDataString.toString());

                        }

                        if (recDataString.charAt(0) == 'A')                             //if it starts with # we know it is what we are looking for
                        {
                            int l = recDataString.length();
                            // String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            //  String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(2, endOfLineIndex);
                            //  String sensor3 = recDataString.substring(16, 20);

                            // sensorView0.setText(" Sensor 0 Voltage = " + sensor0 + "V");    //update the textviews with sensor values
                            // sensorView1.setText(" Sensor 1 Voltage = " + sensor1 + "V");
                            sensorView2.setText("Insulation = " + sensor2);
                            //  sensorView3.setText(" Sensor 3 Voltage = " + sensor3 + "V");

                            writeCSVFile2("logFile.csv", "Air Quality:", recDataString.substring(2, endOfLineIndex), recDataString.toString());

                        }
                        recDataString.delete(0, recDataString.length());

                        //clear all string data
                        //  strIncom =" ";
                        //  dataInPrint = " ";
                    }
                }
            }
        };


        counter_A = counter_T = counter_L = 1;
    }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        try {

            if (this.isFinishing()) {
                //Insert your finishing code here
                flag = 1;
                //Don't leave Bluetooth sockets open when leaving activity
                btSocket.close();
            } else {

                if (intentdoing == 1)
                {
                    flag = 1;
                    intentdoing=0;
                }


                else
                {
                    flag = 0;
                flagForThread = 0;


                ////////
                Runnable runnable = new Runnable() {
                    public void run() {

                        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        ////
                        bluetoothIn = new Handler(Looper.getMainLooper()) {
                            public void handleMessage(android.os.Message msg) {
                                if (msg.what == handlerState) {                                     //if message is what we want
                                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                                    recDataString.append(readMessage);                                      //keep appending to string until ~
                                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                                        //   String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                                        //   txtString.setText("Data Received = " + dataInPrint);
                                        //  int dataLength = dataInPrint.length();                          //get length of data received
                                        // txtStringLength.setText("String Length = " + String.valueOf(dataLength));


                                        if (recDataString.charAt(0) == 'L')                             //if it starts with # we know it is what we are looking for
                                        {


                                            int l = recDataString.length();

                                            //  String sensor0 = new String(recDataString);
                                            String sensor0 = recDataString.substring(2, endOfLineIndex);//get sensor value from string between indices 1-5


                                            // Toast.makeText(getApplicationContext(),"A new thread created",Toast.LENGTH_SHORT);

                                            if (sensor0.compareTo("NA") != 0) {


                                                /////
                                                notification.setSmallIcon(R.drawable.v13);  ////
                                                notification.setTicker("Vaccine Monitor Alert!");
                                                notification.setWhen(System.currentTimeMillis());
                                                notification.setContentTitle("Vaccine Monitor Alert");
                                                notification.setContentText("Too much light!");


                                                notification.setSound(soundUri);
                                                notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


                                                // counter_L++;
                                                // notification.setDefaults()


                                                Intent i2 = new Intent(getApplicationContext(), Activity3.class);
                                                i2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //  i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                i2.putExtra("EXTRA_DEVICE_ADDRESS", address);
                                                //finish();
                                                PendingIntent pendingIntent = PendingIntent.getActivity(Activity3.this, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);

                                                notification.setContentIntent(pendingIntent);

                                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                                if (counter_L == 1) {
                                                    nm.notify(uniqueID_L, notification.build());

                                                    writeCSVFile2("logFile.csv", "Light:", recDataString.substring(2, endOfLineIndex), recDataString.toString());

                                                }

                                                counter_L++;
                                                /////

                                            } else if (sensor0.compareTo("NA") == 0) {
                                                uniqueID_L = 1 + r.nextInt(100);
                                                counter_L = 1;
                                            }


                                        }

                                        if (recDataString.charAt(0) == 'T')                             //if it starts with # we know it is what we are looking for
                                        {

                                            int l = recDataString.length();
                                            // String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                                            String sensor1 = recDataString.substring(2, endOfLineIndex);            //same again...


                                            if (sensor1.compareTo("NA") != 0) {

                                                /////
                                                notification.setSmallIcon(R.drawable.v13);  ////
                                                notification.setTicker("Vaccine Monitor Alert!");
                                                notification.setWhen(System.currentTimeMillis());
                                                notification.setContentTitle("Vaccine Monitor Alert");
                                                notification.setContentText("Too hot for the vaccines!");
                                                notification.setSound(soundUri);
                                                notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                                                //notification.


                                                Intent i2 = new Intent(getApplicationContext(), Activity3.class);
                                                i2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //  i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                i2.putExtra("EXTRA_DEVICE_ADDRESS", address);
                                                //finish();
                                                PendingIntent pendingIntent = PendingIntent.getActivity(Activity3.this, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);

                                                notification.setContentIntent(pendingIntent);

                                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                                                if (counter_T == 1) {
                                                    nm.notify(uniqueID_T, notification.build());

                                                    writeCSVFile2("logFile.csv", "Temperature:", recDataString.substring(2, endOfLineIndex), recDataString.toString());

                                                }

                                                counter_T++;
                                                //  nm.notify(uniqueID_T, notification.build());
                                                /////

                                            } else if (sensor1.compareTo("NA") == 0) {
                                                uniqueID_T = 101 + r.nextInt(100);
                                                counter_T = 1;
                                            }


                                        }

                                        if (recDataString.charAt(0) == 'A')                             //if it starts with # we know it is what we are looking for
                                        {
                                            int l = recDataString.length();
                                            // String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                                            //  String sensor1 = recDataString.substring(6, 10);            //same again...
                                            String sensor2 = recDataString.substring(2, endOfLineIndex);
                                            //  String sensor3 = recDataString.substring(16, 20);


                                            if (sensor2.compareTo("NA") != 0) {


                                                /////
                                                notification.setSmallIcon(R.drawable.v13);  ////
                                                notification.setTicker("Vaccine Monitor Alert!");
                                                notification.setWhen(System.currentTimeMillis());
                                                notification.setContentTitle("Vaccine Monitor Alert");
                                                notification.setContentText("Improper Insulation!");
                                                notification.setSound(soundUri);
                                                notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                                                //notification.


                                                Intent i2 = new Intent(getApplicationContext(), Activity3.class);
                                                i2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //  i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                i2.putExtra("EXTRA_DEVICE_ADDRESS", address);
                                                //finish();
                                                PendingIntent pendingIntent = PendingIntent.getActivity(Activity3.this, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);

                                                notification.setContentIntent(pendingIntent);

                                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                                                if (counter_A == 1) {
                                                    nm.notify(uniqueID_A, notification.build());

                                                    writeCSVFile2("logFile.csv", "Air Quality:", recDataString.substring(2, endOfLineIndex), recDataString.toString());

                                                }

                                                counter_A++;
                                                //   nm.notify(uniqueID_A, notification.build());
                                                /////

                                            } else if (sensor2.compareTo("NA") == 0) {
                                                uniqueID_A = 101 + r.nextInt(100);
                                                counter_A = 1;
                                            }


                                        }
                                        recDataString.delete(0, recDataString.length());

                                        //clear all string data
                                        //  strIncom =" ";
                                        //  dataInPrint = " ";
                                    }
                                }
                            }
                        };


                    }
                };
                myThread = new Thread(runnable);
                myThread.start();


            }
        }
        } catch (IOException e2) {
            //insert code to deal with this
        }




    }


    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }




    private void writeCSVFile2(String filename,String type,String data,String full) {
        String s1 = null;

        int day1 = today.get(Calendar.DAY_OF_MONTH);
        int month1 = today.get(Calendar.MONTH) + 1;
        int year1 = today.get(Calendar.YEAR);

        s1 = day1 + "/" + month1 + "/" + year1;

        //     Toast.makeText(getApplicationContext(), "in func", Toast.LENGTH_SHORT).show();
        //File sdcard = Environment.getExternalStorageDirectory();
        //File file = new File(sdcard, "/DataLogger/filename");


        //if (!file.exists()) {
        try {

            //       Toast.makeText(getApplicationContext(), "in try", Toast.LENGTH_SHORT).show();


            //   if (!file.exists()) {
            File root = Environment.getExternalStorageDirectory();

            String baseDir=root.getAbsolutePath();


            File fileee = new File(root, filename);
            FileWriter writer;

            if (fileee.exists())
               {

             //      Toast.makeText(getApplicationContext(), "in file exitss", Toast.LENGTH_SHORT).show();

                    writer = new FileWriter(fileee, true);


                if (data.equals("NA")) {}
                else
                      {

                //        Toast.makeText(getApplicationContext(), "in DATA not NA", Toast.LENGTH_SHORT).show();



                       writer.append(String.valueOf(System.currentTimeMillis()));
                       writer.append(",");
                       writer.append(s1);
                       writer.append(",");

                       writer.append(type);
                       writer.append(",");
                       writer.append(data);
                       writer.append("\n");


//generate whatever data you want

                        writer.flush();
                        writer.close();

                      }
             }


            else
            {
             //   Toast.makeText(getApplicationContext(), "in no file exists", Toast.LENGTH_SHORT).show();

                //    baseDir + File.separator + fileee
                writer = new FileWriter(fileee);

                //writer = new FileWriter(fileee);




             //   Toast.makeText(getApplicationContext(), "putting header", Toast.LENGTH_SHORT).show();



                writer.write("TIMESTAMP");
                writer.write(",");
                writer.write("DATE");
                writer.write(",");

                writer.write("TYPE");
                writer.write(",");
                writer.write("VALUE");
                writer.write("\n");

                writer.flush();
                writer.close();

                //  counter_for_header++;

            }

        }//try ends

        catch (IOException e1) {

            e1.printStackTrace();
        }



    }//func ends
}