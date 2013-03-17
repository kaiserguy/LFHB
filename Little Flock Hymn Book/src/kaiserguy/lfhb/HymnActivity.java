package kaiserguy.lfhb;

import java.io.File;

import kaiserguy.lfhb.HymnBook.Hymn;
import android.webkit.WebView;
import android.app.Activity;
import android.app.SearchManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.widget.Toast;

	/**
	 * Displays a hymn and its text.
	 */
	public class HymnActivity extends Activity{

		private static final int MENU_DEFINE = 1;
		private static final int MENU_UNDEFINE = 2;
		private static final int MENU_AUTHOR = 3;
		private static final int MENU_TUNES = 4;
		private static final int MENU_SEARCH = 5;
		private static final int MENU_SHARE_TEXT = 6;
		private static final int MENU_SHARE_LINK = 7;
		static final int PICK_TUNE = 0;
	    private TextView hymnNumberView;
	    private TextView hymnMeterView;
		private ImageButton btnMenu;
	    private WebView mWeb;
	    private static final FrameLayout.LayoutParams ZOOM_PARAMS =
	    	new FrameLayout.LayoutParams(
	    	ViewGroup.LayoutParams.FILL_PARENT,
	    	ViewGroup.LayoutParams.WRAP_CONTENT,
	    	Gravity.BOTTOM);
	    private String text;
	    private Hymn theHymn;
	    private Boolean defining = false;
	    
	    private static final int SWIPE_MIN_DISTANCE = 120;
	    private static final int SWIPE_MAX_OFF_PATH = 250;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	    private GestureDetector gestureDetector;
	    View.OnTouchListener gestureListener;
	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode,
	             Intent data) {
	         if (requestCode == PICK_TUNE) {
	             if (resultCode == RESULT_OK) {
	                 // A contact was picked.  Here we will just display it
	                 // to the user.
	            	 Intent viewMediaIntent = new Intent();
	 				viewMediaIntent.setAction(android.content.Intent.ACTION_DEFAULT);   
	 				File file = new File(data.getStringExtra("fileName"));
	 				viewMediaIntent.setDataAndType(Uri.fromFile(file), "audio/midi");   
	 				viewMediaIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
	 				//viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
	 				startActivity(viewMediaIntent);
	             }
	         }
	     }
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.hymn);

	     // Gesture detection
	           gestureDetector = new GestureDetector(new MyGestureDetector());
	            gestureListener = new View.OnTouchListener() {
	                public boolean onTouch(View v, MotionEvent event) {
	                    if (gestureDetector.onTouchEvent(event)) {
	                        return true;
	                    }
	                    return false;
	                }
	            };
	            hymnNumberView = (TextView) findViewById(R.id.hymnNumberView);
	            hymnMeterView = (TextView) findViewById(R.id.hymnMeterView);
	        mWeb = (WebView) findViewById(R.id.web);

	        hymnMeterView.setOnTouchListener(gestureListener);
	        hymnNumberView.setOnTouchListener(gestureListener);
	        mWeb.setOnTouchListener(gestureListener);
	        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
	        btnMenu.setOnClickListener(new Button.OnClickListener() {
	     	   public void onClick(View v) {
	     		 openOptionsMenu();
	     	   }
	     	  });
	        
	        
	        FrameLayout mContentView = (FrameLayout) getWindow().
	        getDecorView().findViewById(android.R.id.content);
	        if (android.os.Build.VERSION.SDK_INT >=11){
	        	mWeb.getSettings().setBuiltInZoomControls(true);
	        	//mWeb.getSettings().setDisplayZoomControls(false);
	        }
	        else {
	        	final View zoom = this.mWeb.getZoomControls();
		        mContentView.addView(zoom, ZOOM_PARAMS);
	        }
	        final Intent intent = getIntent();

	        hymnMeterView.setOnClickListener(onMeterClick);
	        hymnNumberView.setOnClickListener(onHymnNumberClick);
	        
	        theHymn = HymnBook.getInstance().getHymn(intent.getStringExtra("hymnNumber"));
	        text = theHymn.getHTML(defining);
	        
	        mWeb.setBackgroundColor(Color.BLACK);
	        hymnNumberView.setText(theHymn.number);
	        hymnMeterView.setText(theHymn.meter);
	        
	        Configuration c = getResources().getConfiguration();
	        
	        if(c.orientation == Configuration.ORIENTATION_LANDSCAPE ) {
	        	mWeb.setInitialScale(200);
	        }
	        onUnDefineRequested();
	    }
	    
	    private OnClickListener onMeterClick = new OnClickListener() {
	    	public void onClick(View v) {
	        	Toast.makeText(getApplicationContext(), "Listing all hymns meter of " + ((TextView) v).getText(),
	  		          Toast.LENGTH_SHORT).show();
	  		Intent next = new Intent();
	  		next.putExtra("strMeter",((TextView) v).getText());
	  		next.setClass(v.getContext(), MeterHymnPicker.class);
	  		startActivity(next);
	    	}
	    };
	    
	    private OnClickListener onHymnNumberClick = new OnClickListener() {
	    	public void onClick(View v) {
	  		Intent next = new Intent();
	  		next.setClass(v.getContext(), NumberPad.class);
	  		startActivity(next);
	    	}
	    };

		@Override
		public boolean onSearchRequested() {
		     launchSearch();
		     return true;
		}

	    private void launchSearch() {
	    	Intent next = new Intent();
			next.setClass(this, SearchableHymnBook.class);
			next.setAction(Intent.ACTION_SEARCH);
			next.putExtra(SearchManager.QUERY,"");
			startActivity(next);
	    }
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	if (defining){
	    		menu.add(0, MENU_UNDEFINE, 0, R.string.menu_undefine)
	            .setIcon(android.R.drawable.ic_menu_sort_alphabetically);
	    	}
	    	else{
	    		menu.add(0, MENU_DEFINE, 0, R.string.menu_define)
	            .setIcon(android.R.drawable.ic_menu_sort_alphabetically);
	    	}
	    	menu.add(1, MENU_AUTHOR, 0, R.string.menu_author)
            .setIcon(android.R.drawable.ic_menu_info_details);
	    	menu.add(2, MENU_TUNES, 0, R.string.menu_tunes)
            .setIcon(android.R.drawable.ic_media_play);
	    	menu.add(3, MENU_SEARCH, 0, R.string.menu_search)
            .setIcon(android.R.drawable.ic_search_category_default);
	    	menu.add(4, MENU_SHARE_TEXT, 0, "Share Text")
            .setIcon(android.R.drawable.ic_menu_send);
	    	menu.add(5, MENU_SHARE_LINK, 0, "Share Link")
            .setIcon(android.R.drawable.ic_menu_send);
	    	
	        return super.onCreateOptionsMenu(menu);
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case MENU_DEFINE:
                onDefineRequested();
                return true;
	        case MENU_UNDEFINE:
	        	onUnDefineRequested();
	        	return true;
	        case MENU_AUTHOR:
	        	launchAuthor(theHymn.author);
	        	return true;
	        case MENU_SEARCH:
	        	launchSearch();
	        	return true;
	        case MENU_SHARE_TEXT:
	        	launchShareText();
	        	return true;
	        case MENU_SHARE_LINK:
	        	launchShareLink();
	        	return true;
	        case MENU_TUNES:
	        	launchTunes(theHymn.number);
	        	return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

	    private void launchShareText(){
	    	Intent i=new Intent(android.content.Intent.ACTION_SEND);

	    	i.setType("text/plain");
	    	//i.setType("text/xml"); only allows email
	    	i.putExtra(Intent.EXTRA_SUBJECT, "Little Flock Hymn Book #" + theHymn.number + " by " + theHymn.author);
	    	i.putExtra(Intent.EXTRA_TEXT, theHymn.getText());
	    	startActivity(Intent.createChooser(i, "Share this hymn"));
	    }
	    
	    private void launchShareLink(){
	    	Intent i=new Intent(android.content.Intent.ACTION_SEND);

	    	i.setType("text/plain");
	    	//i.setType("text/xml"); only allows email
	    	i.putExtra(Intent.EXTRA_SUBJECT, "Little Flock Hymn Book #" + theHymn.number + " by " + theHymn.author);
	    	i.putExtra(Intent.EXTRA_TEXT, getHymnURL());
	    	startActivity(Intent.createChooser(i, "Share a link to this hymn"));
	    }
	    
	    private String getHymnURL(){
	    	String pageNumber = "1";
	    	String hymnNumber = theHymn.number;
	    	int intHymnNumber = Integer.parseInt(theHymn.number.replace("*", ""));
	    	if (theHymn.number.contains("*")){
	    		if (intHymnNumber <= 50){
		    		pageNumber = "8";
		    		hymnNumber = hymnNumber.replace("*", "A");
		    	} else {
		    		pageNumber = "9";
		    		hymnNumber = hymnNumber.replace("*", "A");
		    	}
	    	} else {	    		
	    	if (intHymnNumber <= 50){
	    		pageNumber = "1";
	    	} else if (intHymnNumber <= 100){
	    		pageNumber = "2";
	    	} else if (intHymnNumber <= 150){
	    		pageNumber = "3";
	    	} else if (intHymnNumber <= 200){
	    		pageNumber = "4";
	    	} else if (intHymnNumber <= 250){
	    		pageNumber = "5";
	    	} else if (intHymnNumber <= 300){
	    		pageNumber = "6";
	    	} else if (intHymnNumber <= 341){
	    		pageNumber = "7";
	    	}
	    	}
	    	return "http://www.stempublishing.com/hymns/data/Dv1881_" + pageNumber + ".htm#" + hymnNumber;
	    }
	    
	    private void launchAuthor(String hymnAuthor) {
			Intent next = new Intent();
			next.setClass(this, AuthorActivity.class);
			next.putExtra("hymnAuthor", theHymn.author);
			//next.putExtra("hymnYear", theHymn.year);
			next.putExtra("hymnNumber", theHymn.number);
			startActivity(next);
		}
	    
	    private boolean isNetworkAvailable() {
	        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        return connectivityManager.getActiveNetworkInfo() != null;
	    }
	    
	    private void launchTunes(String hymnNumber) {
	    	// Skip if no connection, or background data disabled
	    if (theHymn.ssmeter.length() > 0){
		    if (isNetworkAvailable()){
		    	Intent next = new Intent();
				next.setClass(this, TunesActivity.class);
				next.putExtra("hymnMeter", theHymn.meter);
				next.putExtra("hymnSSMeter", theHymn.ssmeter);
				next.putExtra("hymnNumber", theHymn.number);
				startActivityForResult(next, PICK_TUNE);
				//Toast.makeText(getApplicationContext(), "Feature disabled until next update",
  			          //Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "No interwebs found. Need interwebs.",
	    			          Toast.LENGTH_LONG).show();
	    	}
	    } else {
	    	Toast.makeText(getApplicationContext(), "No tunes available for this hymn at this time",
			          Toast.LENGTH_LONG).show();
	    }
		}
	    
	    public void onDefineRequested(){
	    	if (defining){
	    		onUnDefineRequested();
	    	}
	    	else{
	    		if (isNetworkAvailable()){
		    		defining = true;
		    		String textDefined = HymnBook.getInstance().getHymn(getIntent().getStringExtra("hymnNumber")).getHTML(true);
			    	mWeb.loadData("<html><head><META http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body>"
			    			+ textDefined + "</body></html>", "text/html", "UTF-8");
			    	Toast.makeText(getApplicationContext(), "Tap on the word you would like defined",
			    			          Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getApplicationContext(), "No interwebs found. Need interwebs.",
		    			          Toast.LENGTH_LONG).show();
		    	}
	    	}
	    }
	    
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	    	//float oldHeight = (float) mWeb.getContentHeight();
    		//float currentY = (float) mWeb.getScrollY() / oldHeight;
	    	
	      // We're handling this to keep orientation
	      // or keyboard hiding from causing the WebView activity to restart.
	    	if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ) {
	    		mWeb.zoomIn();
	    		mWeb.zoomIn();
	    		//mWeb.scrollTo(0,(int)(currentY * mWeb.getHeight()));
	    		//mWeb.loadUrl("javascript:window.scrollTo(0," + (int)(currentY * mWeb.getContentHeight()) + ");");
	    		//mWeb.scrollTo(1,1);
	        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
	        	//float currentY =  (float) mWeb.getScrollY() / (float) mWeb.getHeight();
	    		mWeb.zoomOut();
	    		mWeb.zoomOut();
	    		//mWeb.scrollTo(0,(int)(currentY * mWeb.getHeight()));
	    		
	        }
	    	super.onConfigurationChanged(newConfig);
	    	//mWeb.loadUrl("javascript:window.scrollTo(0," + (int)(currentY * mWeb.getContentHeight()) + ");");
	    }
	    
	    public void onUnDefineRequested(){
	    	defining = false;
	    	mWeb.getSettings().setJavaScriptEnabled(true);
	    	String smoothScroll = "function smoothScroll(eID) { var startY = currentYPosition(); var stopY = elmYPosition(eID); var distance = stopY > startY ? stopY - startY : startY - stopY; var speed = 100; var step = Math.round(distance / 5); var leapY = stopY > startY ? startY + step : startY - step; var timer = 0; if (stopY > startY) { for ( var i=startY; i<stopY; i+=step ) { setTimeout(\"window.scrollTo(0, \"+leapY+\")\", timer * speed); leapY += step; if (leapY > stopY) leapY = stopY; timer++; } return; } for ( var i=startY; i>stopY; i-=step ) { setTimeout(\"window.scrollTo(0, \"+leapY+\")\", timer * speed); leapY -= step; if (leapY < stopY) leapY = stopY; timer++; }}function elmYPosition(eID) { var elm = document.getElementById(eID); var y = elm.offsetTop; var node = elm; while (node.offsetParent && node.offsetParent != document.body) { node = node.offsetParent; y += node.offsetTop; } return y;}function currentYPosition() { if (self.pageYOffset) return self.pageYOffset; if (document.documentElement && document.documentElement.scrollTop) return document.documentElement.scrollTop; if (document.body.scrollTop) return document.body.scrollTop; return 0;}";
	    	mWeb.loadDataWithBaseURL(null, "<html><head><META http-equiv='Content-Type' content='text/html; charset=UTF-8'><script language='javascript'>" + smoothScroll + "</script></head><body>" 
	    			+ text + "<hr style='width:15em;text-align:left;margin-left:2em;color:aaa;border-color:555555;' /><hr style='width:13em;text-align:left;margin-left:3em;color:888;border-color:444444;' /><br /><br /><br /></body></html>", "text/html", "utf-8", null);
	    }
	    class MyGestureDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            try {
	                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                    return false;
	                // right to left swipe
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                    //Toast.makeText(HymnActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
	                	Intent next = new Intent();
	            		next.setClass(HymnActivity.this, HymnActivity.class);
	            		String hymnNumber = HymnActivity.this.getIntent().getStringExtra("hymnNumber");
	            		if (hymnNumber.endsWith("*")){
	            			hymnNumber = (Integer.parseInt(hymnNumber.replace("*", ""))+1) + "*";
	            		}else{
	            			hymnNumber = (Integer.parseInt(hymnNumber)+1)+"";
	            		}
	            		next.putExtra("hymnNumber", hymnNumber);
	            		startActivity(next);
	            		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                    //Toast.makeText(HymnActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
	                	Intent next = new Intent();
	            		next.setClass(HymnActivity.this, HymnActivity.class);
	            		String hymnNumber = HymnActivity.this.getIntent().getStringExtra("hymnNumber");
	            		if (hymnNumber.endsWith("*")){
	            			hymnNumber = (Integer.parseInt(hymnNumber.replace("*", ""))-1) + "*";
	            		}else{
	            			hymnNumber = (Integer.parseInt(hymnNumber)-1)+"";
	            		}
	            		next.putExtra("hymnNumber", hymnNumber) ;
	            		startActivity(next);
	            		overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
	                }
	            } catch (Exception e) {
	                // nothing
	            }
	            return false;
	        }
	}}
	
