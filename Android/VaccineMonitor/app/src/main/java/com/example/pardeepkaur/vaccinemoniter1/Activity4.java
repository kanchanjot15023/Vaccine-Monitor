package com.example.pardeepkaur.vaccinemoniter1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;



public class Activity4 extends Activity {


    Button btnTemp, btnLight,btnAir;
    EditText txtTemp,txtLight,txtAir;


    String email,pname;


    int bluetoothgone=0;

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

        setContentView(R.layout.activity_4);

        //Link the buttons and textViews to respective views


registerReceiver(bluetooth, new IntentFilter(
                BluetoothDevice.ACTION_ACL_DISCONNECTED));



        btnTemp = (Button) findViewById(R.id.button1);

        btnLight = (Button) findViewById(R.id.button2);
        btnAir = (Button) findViewById(R.id.button3);


        txtTemp = (EditText) findViewById(R.id.editText1);
        txtLight = (EditText) findViewById(R.id.editText2);
        txtAir = (EditText) findViewById(R.id.editText3);


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~


                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED




        btnLight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String txt=txtLight.getText().toString();
                if(txt.equals(""))
                    Toast.makeText(getBaseContext(), "Set Light Value", Toast.LENGTH_SHORT).show();
                else
                {mConnectedThread.write("l:"+txt);    // Send "0" via Bluetooth
                    txtLight.setText("");
                }
                //Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });



        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
        btnTemp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                String txt=txtTemp.getText().toString();
                if(txt.equals(""))
                    Toast.makeText(getBaseContext(), "Set Temperature Value", Toast.LENGTH_SHORT).show();
                else
                {
                    mConnectedThread.write("t:"+txt);
                    txtTemp.setText("");
                }



                // mConnectedThread.write("0");    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });

        btnAir.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {


                String txt=txtAir.getText().toString();
                if(txt.equals(""))
                    Toast.makeText(getBaseContext(), "Set Air Quality Value", Toast.LENGTH_SHORT).show();
                else
                {
                    mConnectedThread.write("a:"+txt);
                    txtAir.setText("");
                }

                //mConnectedThread.write("1");    // Send "1" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });








    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID

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
               // Toast.makeText(getApplicationContext(), "NO Bluetooth Connection", Toast.LENGTH_SHORT).show();
                //sensorView0.setText("NO bluetooth Connection");
                //sensorView1.setText("No Bluetooth Connection");
                //sensorView2.setText("No bluetooth Connection");
               bluetoothgone=1;
            }

        }
    };




    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra("EXTRA_DEVICE_ADDRESS");
        email=intent.getStringExtra("email");
        pname=intent.getStringExtra("name");

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();


        if(bluetoothgone==1) {
            Intent i = new Intent(getApplicationContext(), Activity2.class);
            i.putExtra("email",email);
            i.putExtra("name",pname);
            startActivity(i);
        }


        Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_SHORT).show();

        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }


        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
}