package kaiserguy.lfhb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.*;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * Displays and plays all tunes known for the specified hymn.
 */
public class TunesActivity extends ListActivity implements
		AudioManager.OnAudioFocusChangeListener {
	MediaPlayer mp = new MediaPlayer();
	private final String TUNESURL = "http://stempublishing.com/hymns/data/music/"; // append
																					// the
																					// meter
																					// folder
																					// and
																					// file
																					// name
																					// for
																					// the
																					// tune
																					// to
																					// download
																					// the
																					// file
	private final String HYMNSURL = "http://www.stempublishing.com/hymns/ss/meter/"; // append
																						// hymn
																						// meter
																						// to
																						// get
																						// the
																						// page
																						// with
																						// all
																						// of
																						// the
																						// tunes
																						// for
																						// that
																						// meter
	private String[] lstTuneNames = {""};
	private ArrayList<String> lstTuneFileNames = new ArrayList<String>();
	private int intTuneIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String hymnMeter = getIntent().getStringExtra("hymnSSMeter");
		if (hymnMeter.contains(" or ")) {
			String strTempMeter = hymnMeter.split(" or ")[0];
			getTunes(strTempMeter);
			strTempMeter = hymnMeter.split(" or ")[1];
			getTunes(strTempMeter);
		} else {
			getTunes(hymnMeter);
		}

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lstTuneNames));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mp != null){
					if (mp.isPlaying()) {
						mp.stop();
						mp.reset();
					}
				} else {
					mp = new MediaPlayer();
				}

				String tuneName = ((TextView) view).getText().toString();

				int i = 0;
				while (tuneName != lstTuneNames[i]) {
					i++;
				}

				DownloadFromUrl(lstTuneFileNames.get(i));

				//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				FileInputStream fis;
				try {
					fis = openFileInput(lstTuneFileNames.get(i).replace("/", "_"));
					mp.setDataSource(fis.getFD());
					mp.prepare();
				} catch (FileNotFoundException e1) {
					Toast.makeText(getApplicationContext(),
							"Was not able to find the music I just downloaded to the cache",
							Toast.LENGTH_LONG).show();
				} catch (IllegalArgumentException e) {
					Toast.makeText(getApplicationContext(),
							"Was not able to start streaming music",
							Toast.LENGTH_SHORT).show();
				} catch (IllegalStateException e) {
					Toast.makeText(getApplicationContext(),
							"Was not able to start streaming music",
							Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(),
							"Was not able to start streaming music",
							Toast.LENGTH_SHORT).show();
				}
				requestAudioFocus();
			}
		});
	}

	public void onStop() {
		trashMP();
		super.onStop();
	}

	public void onPause() {
		trashMP();
		super.onPause();
	}
	
	public void trashMP(){
		if (mp != null) {
			if (mp.isPlaying())
				mp.stop();
			mp.release();
			mp = null;
		}
	}

	public void requestAudioFocus() {
		if (android.os.Build.VERSION.SDK_INT >= 8) {
		    //use audio focus
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			int result = audioManager.requestAudioFocus(this,
					AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				// could not get audio focus.
				Toast.makeText(getApplicationContext(),
						"Something else is hogging the audio playback",
						Toast.LENGTH_SHORT).show();
			} else {
				onAudioFocusChange(result);
			}
		} else {
		    //fake audio focus
			onAudioFocusChange(1);
		}
	}

	@Override
	public boolean onSearchRequested() {
		launchSearch();
		return true;
	}

	private void launchSearch() {
		Intent next = new Intent();
		next.setClass(this, SearchableHymnBook.class);
		next.setAction(Intent.ACTION_SEARCH);
		next.putExtra(SearchManager.QUERY, "");
		startActivity(next);
	}

	public void DownloadFromUrl(String fileName) { // this is the file downloader method
		try {
				URL u = new URL(TUNESURL + fileName);
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				FileOutputStream f = openFileOutput(fileName.replace("/", "_"), Context.MODE_PRIVATE);
				
				InputStream in = c.getInputStream();

				byte[] buffer = new byte[1024];
				int len1 = 0;
				while ((len1 = in.read(buffer)) > 0) {
					f.write(buffer, 0, len1);
				}
				f.close();
				in.close();
		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}
	}

	private void getTunes(String hymnMeter) {
		try {
			URL u = new URL(HYMNSURL + hymnMeter.replace(" ", "%20"));
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();

			InputStream is = c.getInputStream();
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			String HTML = writer.toString();

			// Pattern patFirstTuneName =
			// Pattern.compile("<span class=\"tunename\">([^<>]+)</span>");

			Pattern patFileName = Pattern.compile("music/(.{2,30}\\.mid)");
			// Pattern patTuneName =
			// Pattern.compile("<tr><td( class=\"darker\")?>([^<>]+)</td>");
			Pattern patTuneName = Pattern
					.compile("<tr><td( class=\"darker\")?>([^<>]+)</td>");

			Matcher myMatcher = patFileName.matcher(HTML);
			while (myMatcher.find()) {
				lstTuneFileNames.add(myMatcher.group(1));
			}

			// myMatcher = patFirstTuneName.matcher(HTML);
			// myMatcher.find();
			
			lstTuneNames = (String[]) resizeArray(lstTuneNames,lstTuneFileNames.size());
			//lstTuneNames = new String[lstTuneFileNames.size()];
			// lstTuneNames[intTuneIndex]=myMatcher.group(1);
			// intTuneIndex++;

			myMatcher = patTuneName.matcher(HTML);
			while (myMatcher.find()) {
				lstTuneNames[intTuneIndex] = myMatcher.group(2);
				intTuneIndex++;
			}

			if (lstTuneFileNames.size() < 1){
				Toast.makeText(getApplicationContext(), "No tunes for this hymn yet",
						Toast.LENGTH_LONG).show();
				onBackPressed();
			}			
		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}
	}

	/**
	* Reallocates an array with a new size, and copies the contents
	* of the old array to the new array.
	* @param oldArray  the old array, to be reallocated.
	* @param newSize   the new array size.
	* @return          A new array with the same contents.
	*/
	private static Object resizeArray(Object oldArray, int newSize) {
	   int oldSize = java.lang.reflect.Array.getLength(oldArray);
	   Class elementType = oldArray.getClass().getComponentType();
	   Object newArray = java.lang.reflect.Array.newInstance(
	         elementType,newSize);
	   int preserveLength = Math.min(oldSize,newSize);
	   if (preserveLength > 0)
	      System.arraycopy (oldArray,0,newArray,0,preserveLength);
	   return newArray; }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			// Take care of calling this method on earlier versions of
			// the platform where it doesn't exist.
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (mp != null){
			if (mp.isPlaying()) {
				mp.stop();
				mp.reset();
				Toast.makeText(getApplicationContext(), "Stopped",
						Toast.LENGTH_SHORT).show();
			} else {
				this.finish();
			}
		} else {
			this.finish();
		}
		return;
	}

	public void onAudioFocusChange(int focusChange) {
		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			// resume playback
			if (mp == null) {
				// do nothing;
			} else if (!mp.isPlaying()) {
				mp.setVolume(1.0f, 1.0f);
				mp.start();
				Toast.makeText(getApplicationContext(), "Playing",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case AudioManager.AUDIOFOCUS_LOSS:
			// Lost focus for an unbounded amount of time: stop playback and
			// release media player
			trashMP();
			break;

		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			// Lost focus for a short time, but we have to stop
			// playback. We don't release the media player because playback
			// is likely to resume
			if (mp == null) {
				// do nothing;
			} else if (mp.isPlaying()){
				mp.pause();
			}
			break;

		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			// Lost focus for a short time, but it's ok to keep playing
			// at an attenuated level
			if (mp == null) {
				// do nothing;
			} else if (mp.isPlaying())
				mp.setVolume(0.1f, 0.1f);
			break;
		}
	}
}
