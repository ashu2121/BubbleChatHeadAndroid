package com.example.bubblechathead;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ChatHeadService extends Service {
    private static final String TAG ="ChatHeadService" ;
    private WindowManager mWindowManager;
    private View mChatHeadView;
    public static boolean firstTime=true;
    SharedPrefrenceClass sharedPrefrenceClass;
    public ImageView chatHeadImage;

    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Inflate the chat head layout we created
     * set layout params to window
     * Specify the chat head position
     * Initially view will be added to top-left corner
     * Add the view to the window
     * Set the close button
     * on click close button close the service and remove the chat head from the window
     * Drag and move chat head using user's touch action.
     * on touch listener,
     *  in case, ACTION DOWN
        * remember the initial position
        * get the touch location
     * in case, ACTION UP
        * As we implemented on touch listener with ACTION_MOVE,
        * we have to check if the previous action was ACTION_DOWN
        * to identify if the user clicked the view or not.
        * if previous action is action down
            * Open the chat conversation click
            * close the service and remove the chat heads
     * in case, ACTION MOVE
        * Calculate the X and Y coordinates of the view.
        * Update the layout with new X & Y coordinate
     */

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(TAG, "onCreate: called");

        sharedPrefrenceClass=new SharedPrefrenceClass(ChatHeadService.this);
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);

        ImageView closeButton = (ImageView) mChatHeadView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                firstTime=true;
            }
        });

        chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        //((GradientDrawable)chatHeadImage.getBackground()).setColor(setChatHeadShadowColor(shadowColor));
        //LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.id.first_color);

        chatHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(ChatHeadService.this, BubbleChatHead.class));
            }
        });
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    /*case MotionEvent.ACTION_UP:

                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            Intent intent = new Intent(ChatHeadService.this,MainThread.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            stopSelf();
                        }
                        lastAction = event.getAction();
                        return true;*/
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            /*//If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);*/
            Toast.makeText(ChatHeadService.this, "Please allow DRAW OVER APP permission from settings", Toast.LENGTH_SHORT).show();
        } else {
            initializeView(ChatHeadService.this);
        }
    }

    /**
     * set and initialize the view elements.
     * set firstTime(boolean) to false
     */
    public void initializeView(Context cxt) {
        cxt.startService(new Intent(cxt, ChatHeadService.class));
        firstTime = false;
    }

    public void setChatHeadShadowColor(Context cxt,final String newShadowColor){
        new SharedPrefrenceClass(cxt).saveShadowColor(newShadowColor);
       // Log.d(TAG, "setChatHeadShadowColor: save newShadowColor=====>" + newShadowColor);
        //shadowColor= Color.parseColor(shdw_clr);
        //Log.d(TAG, "setChatHeadShadowColor: shadowColor=====>" + shadowColor);

    }

    public void setShadowDepthLayers(Context cxt, float dper, float dper1, float dper2, float dper3, float dper4, float dper5, float dper6){
        int shdw_clr= Color.parseColor(new SharedPrefrenceClass(cxt).retrieveShadowColor());
       // Log.d(TAG, "setChatHeadShadowColor: retrieve newShadowColor=====>" + shdw_clr);
        LayerDrawable layerDrawable = (LayerDrawable)cxt.getResources()
                .getDrawable(R.drawable.profile_img_bckstyle);
        GradientDrawable gradientDrawable1 = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.first_color);
        GradientDrawable gradientDrawable2 = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.second_color);
        GradientDrawable gradientDrawable3 = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.third_color);
        GradientDrawable gradientDrawable4 = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.fourth_color);
        GradientDrawable gradientDrawable5 = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.fifth_color);
        GradientDrawable gradientDrawable6 = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.sixth_color);

       // Log.d(TAG, "setChatHeadShadowColor: layes depth=====>" + dper/100 + dper1/100+ dper2/100 + dper3/100 + dper4/100 + dper5/100 + dper6/100);
        gradientDrawable1.setColor(getColorWithAlpha(shdw_clr,dper/100));
        gradientDrawable2.setColor(getColorWithAlpha(shdw_clr,dper1/100));
       // Log.d(TAG, "onCreate: shadow color with 10% opacity=====>" + getColorWithAlpha(shdw_clr,dper2/100));
        gradientDrawable3.setColor(getColorWithAlpha(shdw_clr,dper3/100));
        gradientDrawable4.setColor(getColorWithAlpha(shdw_clr,dper4/100));
        gradientDrawable5.setColor(getColorWithAlpha(shdw_clr,dper5/100));
        gradientDrawable6.setColor(getColorWithAlpha(shdw_clr,dper6/100));

        Drawable drawableArray[]= new Drawable[]{gradientDrawable1,gradientDrawable2,gradientDrawable3,
                gradientDrawable4,gradientDrawable5,gradientDrawable6};
        LayerDrawable layerDraw = new LayerDrawable(drawableArray);
        layerDraw.setLayerInset(1,1,0,1,3);//set offset of 2 layer
        //layerDraw.setLayerInset(2,40,40,0,0);//set offset for third layer
        mChatHeadView = LayoutInflater.from(cxt).inflate(R.layout.layout_chat_head, null);
        chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        chatHeadImage.setBackground(layerDrawable);
        //closeButton.setBackground(layerDrawable);
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    /**
     * when on destroy called
     * removing the bubble chat head view from the window
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
    }
}
