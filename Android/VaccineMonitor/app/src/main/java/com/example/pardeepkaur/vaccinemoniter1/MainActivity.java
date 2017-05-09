package com.example.pardeepkaur.vaccinemoniter1;


import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.File;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    TourGuide mTourGuideHandler;

    Person currentPerson;
    String personName;
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;

    private static final String TAG = "SignIn Log";
    private String email;

    private Bundle savedInstanceState1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ONCREATE","ONCREATE");
        savedInstanceState1 = savedInstanceState;

        setContentView(R.layout.activity_main);

        StartAnimations();


        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        //on click listener for sign in button
  /*      findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShouldResolve = true;
                mGoogleApiClient.connect();
            }
        });  */


        Button b = (Button) findViewById(R.id.sign_in_button);

        /* mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle("Welcome!").setDescription("Click to chose your account..."))
                .setOverlay(new Overlay().disableClick(false))
                .playOn(b);*/

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            //     Toast.makeText(getApplicationContext(), "firsttttt", Toast.LENGTH_SHORT).show();

            // first time task

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();

            mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                    .setPointer(new Pointer())
                    .setToolTip(new ToolTip().setTitle("Hey!").setDescription("Click to chose your account"))
                    .setOverlay(new Overlay().disableClick(false))
                    .playOn(b);
        } else {


            //   Toast.makeText(getApplicationContext(), "secondddd", Toast.LENGTH_SHORT).show();

        }


        //   Read more: http://mrbool.com/how-to-create-tour-guide-tooltips-in-android-apps/33130#ixzz3sg6pWIfq


    }

    public void onConnectClicked(View view)
    {

    mShouldResolve=true;
    mGoogleApiClient.connect();
}

    public void onScreen1Clicked(View v)
    {


      //  Toast.makeText(getApplicationContext(),"CLICKEDD",Toast.LENGTH_SHORT).show();

if(mTourGuideHandler!=null) {
    mTourGuideHandler.cleanUp();
}

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout)findViewById(R.id.screen1);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.img);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }

    /*private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout)findViewById(R.id.screen1);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }*/

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ONSTART", "ONSTART");

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ONSTOP", "ONSTOP");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ONPAUSE", "ONPAUSE");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ONRESUME", "ONRESUME");

    }


@Override
    protected void onRestart()
    {
        super.onRestart();


        Log.i("ONRESTART", "ONRESTART");

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

@Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.i("ONDESTROY", "ONDESTROY");

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
        if (requestCode == RC_PERM_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                Toast.makeText(getApplicationContext(),"%%"+currentPerson,Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
            }
        }
    }






    @Override
    public void onConnected(Bundle bundle) {


        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;


        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        //  Plus.AccountApi.getAccountName(mGoogleApiClient).
        //  personName = currentPerson.getDisplayName();
        //    Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);

        // Plus.API.getName();
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
        {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            personName = currentPerson.getDisplayName();
            // String personPhoto = currentPerson.getImage().getUrl();
            //  String personGooglePlusProfile = currentPerson.getUrl();
//Toast.makeText(getApplicationContext(),".....###"+personName,Toast.LENGTH_LONG).show();
            //  Person currentPerson=   Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        }
        else {
   //         Toast.makeText(getApplicationContext(), "else",Toast.LENGTH_LONG).show();

        }

        Log.d("email", email);

        String domain = "iiitd.ac.in";
        String domain1 ="gmail.com";
        if (!isUserOnline(this)) {
            //toast("No Internet Connection");
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_LONG).show();

            Log.i("noti::::","no net");
/////-------
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
            }


            mGoogleApiClient.connect();
//////------
          //onStart();
          //  mGoogleApiClient.connect();
            //onPause();
            //onResume();
//            this.onCreate(savedInstanceState1);
        }
        else{
            if(!email.split("@")[1].equals(domain) && !email.split("@")[1].equals(domain1)){
                // Clear the default account so that GoogleApiClient will not automatically
                // connect in the future.
                if (mGoogleApiClient.isConnected()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please use gmail/iiitd account", Toast.LENGTH_SHORT);
                    toast.show();
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }
            else{
                // Open the Geofencing Activity
                Intent intent = new Intent(MainActivity.this, Activity1.class);
                intent.putExtra("email", email);
                intent.putExtra("name",personName);
//Toast.makeText(this,personName,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

        }
    }


    public static boolean isUserOnline(Context context) {
        try {
            ConnectivityManager nConManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (nConManager != null) {
                NetworkInfo nNetworkinfo = nConManager.getActiveNetworkInfo();

                if (nNetworkinfo != null) {
                    return nNetworkinfo.isConnected();
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
  /*  void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }*/
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }

    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a toast to the user that we are signing in.
        Toast toast = Toast.makeText(getApplicationContext(), "Signing In", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}