package team7663.project.camword;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
 
@SuppressLint("RtlHardcoded")
public class Overlay extends Service implements OnTouchListener, OnClickListener {
    
    private View topLeftView;
 
    static ImageButton overlayedButton;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;
 
    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }
    
	@Override
    public void onCreate() {
	super.onCreate();
	wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	overlayedButton = new ImageButton(this);
	overlayedButton.setBackgroundResource(R.drawable.speech);
	overlayedButton.setOnTouchListener(this);
	overlayedButton.setAlpha(0.0f);
	overlayedButton.setOnClickListener(this);
 
	WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
	params.gravity = Gravity.CENTER | Gravity.LEFT;
	params.x = 0;
	params.y = 0;
	wm.addView(overlayedButton, params);
 
	topLeftView = new View(this);
	WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
	topLeftParams.gravity = Gravity.CENTER | Gravity.LEFT;
	topLeftParams.x = 0;
	topLeftParams.y = 0;
	topLeftParams.width = 0;
	topLeftParams.height = 0;
	wm.addView(topLeftView, topLeftParams);
 
    }
 
    @Override
    public void onDestroy() {
	super.onDestroy();
	if (overlayedButton != null) {
	    wm.removeView(overlayedButton);
	    wm.removeView(topLeftView);
	    overlayedButton = null;
	    topLeftView = null;
	}
    }
 
    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouch(View v, MotionEvent event) {
 
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    float x = event.getRawX();
	    float y = event.getRawY();
 
	    moving = false;
 
	    int[] location = new int[2];
	    overlayedButton.getLocationOnScreen(location);
 
	    originalXPos = location[0];
	    originalYPos = location[1];
 
	    offsetX = originalXPos - x;
	    offsetY = originalYPos - y;
 
	} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    int[] topLeftLocationOnScreen = new int[2];
	    topLeftView.getLocationOnScreen(topLeftLocationOnScreen);
	    float x = event.getRawX();
	    float y = event.getRawY();
	    WindowManager.LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();
	    int newX = (int) (offsetX + x);
	    int newY = (int) (offsetY + y);
	    if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
		return false;
	    }
	    params.x = newX - (topLeftLocationOnScreen[0]);
	    params.y = newY - (topLeftLocationOnScreen[1]);
	    wm.updateViewLayout(overlayedButton, params);
	    moving = true;
	} 
	else if (event.getAction() == MotionEvent.ACTION_UP)
	{
	    if(moving)
	    {
		return true;
	    }
	}
	return false;
    }
 
    @Override
    public void onClick(View v) {
    	overlayedButton.setBackgroundResource(R.drawable.speech_clicked);
    	Intent newactivity = new Intent("team7663.project.camword.VOICE");
    	newactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(newactivity);
    }
 
}