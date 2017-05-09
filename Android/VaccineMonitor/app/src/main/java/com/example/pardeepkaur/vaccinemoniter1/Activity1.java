package com.example.pardeepkaur.vaccinemoniter1;

/**
 * Created by pardeepkaur on 10/18/15.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;





class Bounce extends View {
    int screenW;
    int screenH;
    int X;
    int Y;
    int initialY ;
    int ballW;
    int ballH;
    int angle;
    float dY;
    float acc;
    Bitmap ball, bgr;

    public Bounce(Context context) {
        super(context);
        ball = BitmapFactory.decodeResource(getResources(),R.drawable.l5); //load a ball image
        bgr = BitmapFactory.decodeResource(getResources(),R.drawable.im11); //load a background
        ballW = ball.getWidth();
        ballH = ball.getHeight();
        acc = 0.2f; //acceleration
        dY = 0; //vertical speed
        initialY = 0; //Initial vertical position.
        angle = 0; //Start value for rotation angle.
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
        bgr = Bitmap.createScaledBitmap(bgr, w, h, true); //Resize background to fit the screen.
        X = (int) (screenW /2) - (ballW / 2) ; //Centre ball into the centre of the screen.

        // Y=(int) (screenW /5) - (ballW / 5);
        Y = initialY;
        //  Y = (int) (screenH /2) - (ballH / 2) ;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw background.
        canvas.drawBitmap(bgr, 0, 0, null);

        //Compute roughly ball speed and location.
        //       Y+= (int) dY; //Increase or decrease vertical position.
        if (Y > (screenH - ballH)) {
            dY=(-1)*dY; //Reverse speed when bottom hit.
        }
        dY+= acc; //Increase or decrease speed.

        //Increase rotating angle.
        if (angle++ >360)
            angle =0;

        //Draw ball
        canvas.save(); //Save the position of the canvas.
        canvas.rotate(angle, X + (ballW / 2), Y + (ballH / 2)); //Rotate the canvas.
        canvas.drawBitmap(ball, X, Y, null); //Draw the ball on the rotated canvas.
        canvas.restore(); //Rotate the canvas back so that it looks like ball has rotated.

        //Call the next frame.
        invalidate();
    }
}


public class Activity1 extends AppCompatActivity {


    TourGuide mTourGuideHandler1,mTourGuideHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        RelativeLayout rl1=(RelativeLayout)findViewById(R.id.screen2);
        rl1.addView(new Bounce(getApplicationContext()));



        Button b1= (Button)findViewById(R.id.button1);
        Button b2= (Button)findViewById(R.id.button);

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time1", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time1");
           // Toast.makeText(getApplicationContext(), "firsttttt", Toast.LENGTH_SHORT).show();

            // first time task

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time1", false).commit();

            Animation animation = new TranslateAnimation(0f, 0f, 100f, 0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            animation.setInterpolator(new BounceInterpolator());





        mTourGuideHandler1 = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setDescription("Chose your bluetooth device!").setGravity(Gravity.TOP | Gravity.LEFT))
                .setOverlay(new Overlay().disableClick(false))
                .playOn(b1);

       mTourGuideHandler2 = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setDescription("Get Faults").setGravity(Gravity.BOTTOM | Gravity.RIGHT))
                .setOverlay(new Overlay().disableClick(false))
                .playOn(b2);

        }
        else {


        //    Toast.makeText(getApplicationContext(), "secondddd", Toast.LENGTH_SHORT).show();

        }


       // Read more: http://mrbool.com/how-to-create-tour-guide-tooltips-in-android-apps/33130#ixzz3shZB7jiV

    }


    public void onScreen2Clicked(View view)
    {
        if(mTourGuideHandler1!=null && mTourGuideHandler2!=null ) {
            //  Toast.makeText(getApplicationContext(),"Screen1 clicked",Toast.LENGTH_LONG).show();
            mTourGuideHandler1.cleanUp();
            mTourGuideHandler2.cleanUp();
        }
    }


    public void onConnectGoto(View v)
    {
        Intent intent=new Intent(getApplicationContext(),Activity5.class);
        //startActivity(i);
        String email,pname;
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        pname=extras.getString("name");

        //Intent intent = new Intent(Activity1.this, Activity2.class);
        intent.putExtra("email", email);
        intent.putExtra("name",pname);
        //     Toast.makeText(this, pname, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }




    public void onConnectClicked(View v)
    {
       // mTourGuideHandler1.cleanUp();
      //  mTourGuideHandler2.cleanUp();

        Intent intent=new Intent(getApplicationContext(),Activity2.class);
        //startActivity(i);
        String email,pname;
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        pname=extras.getString("name");

        //Intent intent = new Intent(Activity1.this, Activity2.class);
        intent.putExtra("email", email);
        intent.putExtra("name",pname);
   //     Toast.makeText(this, pname, Toast.LENGTH_SHORT).show();
        startActivity(intent);
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
}
