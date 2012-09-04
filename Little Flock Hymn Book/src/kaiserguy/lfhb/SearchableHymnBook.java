package kaiserguy.lfhb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.List;

/**
 * The main activity for the hymn book. Also displays search results triggered
 * by the search dialog.
 */
public class SearchableHymnBook extends Activity {

	/*private static final int MENU_SEARCH = 1;
	private static final int MENU_METERS = 2;
	private static final int MENU_FIRSTLINES = 3;*/
	private Handler mHandler = new Handler();

	public TextView mTextView;
	public TextView mIntroView;
	public TextView txtHomeInstructions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		mHandler.removeCallbacks(mUpdateLoadingMessage);
		
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			HymnBook.getInstance().ensureLoaded(getResources());
			String strIntentData = intent.getDataString();
			if (strIntentData.startsWith("FullSearch ")){
				Intent next = new Intent();
				next.setClass(SearchableHymnBook.this, HymnSearchPicker.class);
				next.putExtra("strQuery", strIntentData);
				startActivity(next);
			}else{
				// from click on search results
				HymnBook.Hymn theHymn = HymnBook.getInstance().getHymn(strIntentData);
				launchHymn(theHymn.number);
			}			
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String strQuery = intent.getStringExtra(SearchManager.QUERY);
			if (strQuery.length() > 0){
				Intent next = new Intent();
				next.setClass(SearchableHymnBook.this, HymnSearchPicker.class);
				next.putExtra("strQuery", strQuery);
				startActivity(next);
			} else {
				onSearchRequested();
			}
		}
		setContentView(R.layout.home);
		
		mIntroView = (TextView) findViewById(R.id.introductionField);
		txtHomeInstructions = (TextView) findViewById(R.id.txtHomeInstructions);
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		Button btnNumberPad = (Button)findViewById(R.id.btnNumberPad);
		Button btnNumberPicker = (Button)findViewById(R.id.btnNumberPicker);
		Button btnFirstLineAlpha = (Button)findViewById(R.id.btnFirstLineAlpha);
		Button btnFirstLineNumeric = (Button)findViewById(R.id.btnFirstLineNumeric);
		Button btnAuthor = (Button)findViewById(R.id.btnAuthor);
		Button btnMeter = (Button)findViewById(R.id.btnMeter);

		btnSearch.setOnClickListener(btnSearchListener);
		btnNumberPad.setOnClickListener(btnNumberPadListener);
		btnNumberPicker.setOnClickListener(btnNumberPickerListener);
		btnFirstLineAlpha.setOnClickListener(btnFirstLineAlphaListener);
		btnFirstLineNumeric.setOnClickListener(btnFirstLineNumericListener);
		btnAuthor.setOnClickListener(btnAuthorListener);
		btnMeter.setOnClickListener(btnMeterListener);
		
		DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);

		  // this.showAlert(dm.widthPixels +" "+ dm.heightPixels, dm.widthPixels
		  // +" "+ dm.heightPixels, dm.widthPixels +" "+ dm.heightPixels, false);

		  int height = dm.heightPixels;
		  int width = dm.widthPixels;
		  int instructionsSize = 10;
		  int buttonSize = 20;

		  if (width > 300) {
			  instructionsSize = 10;
			  buttonSize = 15;
		  }
		  if (width > 400) {
			  instructionsSize = 20;
			  buttonSize = 25;
		  }
		  if (width > 600) {
			  instructionsSize = 25;
			  buttonSize = 25;
		  }
		  if (width > 1000) {
			  instructionsSize = 30;
			  buttonSize = 30;
		  }
		  if (width > 1500) {
			  instructionsSize = 35;
			  buttonSize = 35;
		  }
		  txtHomeInstructions.setTextSize(instructionsSize);
		  btnSearch.setTextSize(buttonSize);
		  btnNumberPad.setTextSize(buttonSize);
		  btnNumberPicker.setTextSize(buttonSize);
		  btnFirstLineAlpha.setTextSize(buttonSize);
		  btnFirstLineNumeric.setTextSize(buttonSize);
		  btnAuthor.setTextSize(buttonSize);
		  btnMeter.setTextSize(buttonSize);
		  
		mHandler.postDelayed(mUpdateLoadingMessage, 100);
	}
	private OnClickListener btnSearchListener = new OnClickListener() {
	    public void onClick(View v) {
			onSearchRequested();
	    }
	};
	private OnClickListener btnNumberPadListener = new OnClickListener() {
	    public void onClick(View v) {
	    	onNumberPadRequested();
	    }
	};
	private OnClickListener btnNumberPickerListener = new OnClickListener() {
	    public void onClick(View v) {
	    	onNumberPickerRequested();
	    }
	};
	private OnClickListener btnFirstLineAlphaListener = new OnClickListener() {
	    public void onClick(View v) {
			onFirstLinesAlphaRequested();
	    }
	};
	private OnClickListener btnFirstLineNumericListener = new OnClickListener() {
	    public void onClick(View v) {
	    	onFirstLinesNumericRequested();
	    }
	};
	private OnClickListener btnAuthorListener = new OnClickListener() {
	    public void onClick(View v) {
	    	onAuthorsRequested();
	    }
	};
	private OnClickListener btnMeterListener = new OnClickListener() {
	    public void onClick(View v) {
	    	onMetersRequested();
	    }
	};
	
	private void onFirstLinesAlphaRequested() {
		Intent next = new Intent();
		next.setClass(this, LetterPicker.class);
		startActivity(next);
	}
	
	private void onNumberPadRequested() {
		Intent next = new Intent(this, NumberPad.class);
		startActivity(next);
	}
	
	private void onNumberPickerRequested() {
		Intent next = new Intent(this, NumberGroupPicker.class);
		startActivity(next);
	}
	
	private void onFirstLinesNumericRequested() {
		Intent next = new Intent();
		next.setClass(this, FirstLinesByNumberPicker.class);
		startActivity(next);
	}
	
	private void onAuthorsRequested() {
		Intent next = new Intent();
		next.setClass(this, AuthorPicker.class);
		startActivity(next);
	}
	
	private void onMetersRequested() {
		Intent next = new Intent();
		next.setClass(this, MeterPicker.class);
		startActivity(next);
	}
		
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH, 0, R.string.menu_search)
				.setIcon(android.R.drawable.ic_search_category_default)
				.setAlphabeticShortcut(SearchManager.MENU_KEY);
		menu.add(1, MENU_METERS, 0, R.string.menu_meters)
        .setIcon(android.R.drawable.ic_menu_sort_by_size);
		menu.add(2, MENU_FIRSTLINES, 0, R.string.menu_firstlines)
        .setIcon(android.R.drawable.ic_menu_agenda);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SEARCH:
			onSearchRequested();
			return true;
		case MENU_METERS:
			onMetersRequested();
	    	return true;
		case MENU_FIRSTLINES:
			onFirstLinesAlphaRequested();
	    	return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	
	private Runnable mUpdateLoadingMessage = new Runnable() {
		public void run() {
			HymnBook mHymnBook = HymnBook.getInstance();
			if (mHymnBook.mLoaded) {
				mTextView = (TextView) findViewById(R.id.textField);
				mTextView.setVisibility(View.GONE);
			} else {
				mTextView = (TextView) findViewById(R.id.textField);
				mTextView.setVisibility(View.VISIBLE);
				mTextView.setText(R.string.loading_message);
				mTextView.setText(mTextView.getText() + " "
						+ Double.toString(mHymnBook.currentHymnNumber / 4.25).substring(0,2) + "%");
				//mHandler.postAtTime(this, SystemClock.uptimeMillis() + 200);
				mHandler.postDelayed(this, 50);
			}
		}
	};
	
    private void launchHymn(String number) {
		Intent next = new Intent();
		next.setClass(this, HymnActivity.class);
		next.putExtra("hymnNumber", number);
		startActivity(next);
	}
	class HymnAdapter extends BaseAdapter implements
	AdapterView.OnItemClickListener {

		private final List<HymnBook.Hymn> mHymns;
		private final LayoutInflater mInflater;

		public HymnAdapter(List<HymnBook.Hymn> hymns) {
			mHymns = hymns;
			mInflater = (LayoutInflater) SearchableHymnBook.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mHymns.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView
					: createView(parent);
			bindView(view, mHymns.get(position));
			return view;
		}

		private TwoLineListItem createView(ViewGroup parent) {
			TwoLineListItem item = (TwoLineListItem) mInflater.inflate(
					android.R.layout.simple_list_item_2, parent, false);
			item.getText2().setSingleLine();
			item.getText2().setEllipsize(TextUtils.TruncateAt.END);
			return item;
		}

		private void bindView(TwoLineListItem view, HymnBook.Hymn hymn) {
			view.getText1().setText(hymn.number + "  -  " + hymn.meter + ",  by " + hymn.author);
			view.getText2().setText(hymn.getStanzaText(0).substring(0, 50));
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			launchHymn(mHymns.get(position).number);
		}

	    private void launchHymn(String number) {
			Intent next = new Intent();
			next.setClass(SearchableHymnBook.this, HymnActivity.class);
			next.putExtra("hymnNumber", number);
			startActivity(next);
		}
	}
}
	
