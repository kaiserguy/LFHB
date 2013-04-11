package kaiserguy.lfhb;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

public class hymnView extends WebView {
	  public hymnView(Context context) {
	   super(context);
	  }

	     public interface SwipeListener 
	        {
	            void onSwipe(Boolean forward);
	        }
	     
	     ArrayList<SwipeListener> listeners = new ArrayList<SwipeListener> (); 
	     public void setOnNewsUpdateListener (SwipeListener listener) 
	        {
	            // Store the listener object
	            this.listeners.add(listener);
	        }
	     
	@Override 
	     public boolean onTouchEvent(MotionEvent evt) {   

	         boolean consumed = super.onTouchEvent(evt); 
	     if (isClickable()) { 
	         switch (evt.getAction()) { 
	     case MotionEvent.ACTION_DOWN: 
	         lastTouchX = evt.getX(); 
	         lastTouchY = evt.getY();
	         downXValue = evt.getX();
	         downTime = evt.getEventTime();
	         hasMoved = false; 
	         break; 
	     case MotionEvent.ACTION_MOVE: 
	         hasMoved = moved(evt); 
	         break; 
	     case MotionEvent.ACTION_UP: 
	        float currentX = evt.getX();
	        long currentTime = evt.getEventTime();
	        float difference = Math.abs(downXValue - currentX);
	        long time = currentTime - downTime;

	        Log.i("Touch Event:",  "Distance: " + difference + "px Time: " + time + "ms");

	        if ( (downXValue < currentX) && (time < 220) && (difference > 100) ) {
	            //go_back();
	            for (SwipeListener listener : listeners) 
	            {
	                listener.onSwipe(false);
	            }
	        }

	             if ( (downXValue > currentX) && (time < 220) && (difference > 100) ) {
	                   //go_forward();

	 	            for (SwipeListener listener : listeners) 
	 	            {
	 	                listener.onSwipe(true);
	 	            }

	                  }

	                 //if (!moved(evt)) performClick(); 
	                 break; 
	             } 
	         } 
	         return consumed || isClickable(); 
	     } 
	  float downXValue;
	  long downTime;
	     private float lastTouchX, lastTouchY; 
	     private boolean hasMoved = false; 
	     private boolean moved(MotionEvent evt) { 
	         return hasMoved || 
	             Math.abs(evt.getX() - lastTouchX) > 10.0 || 
	             Math.abs(evt.getY() - lastTouchY) > 10.0; 
	     }
	 }