package kaiserguy.lfhb;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

/**
 * Displays an author and other hymns written by the same author.
 */
public class AuthorActivity extends Activity {

	private TextView mHeader;
	private TextView mText;
	private ListView mList;
	private String number = "Number Text";
	private String author = "Author Text";
	private String year = "Year Text";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.author);

		mHeader = (TextView) findViewById(R.id.textField);
		mText = (TextView) findViewById(R.id.introductionField);
		mList = (ListView) findViewById(R.id.list);

		Intent intent = getIntent();

		number = intent.getStringExtra("hymnNumber");
		author = intent.getStringExtra("hymnAuthor");
		//year = Integer.toString(intent.getIntExtra("hymnYear", 0));
		
		loadAuthor();
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
		next.putExtra(SearchManager.QUERY,"");
		startActivity(next);
    }
	public void loadAuthor() {
		Log.d("hymn", "loading author");

		mHeader.setText("Hymn #" + number + " by " + author);
		mText.setText("Hymns written by this author in this hymn book:");
			/*if (!year.equals("0")){
				mHeader.setText(mHeader.getText() + ", " + year);
			}*/
			mList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, HymnBook.getInstance().getHymnNumbersByAuthor(author)));
			mList.setOnItemClickListener(lstHymnListener);
	}
	private OnItemClickListener lstHymnListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
			String strLine = ((TextView) view).getText().toString();
			launchHymn(strLine.substring(0,strLine.indexOf(" ")));
	    }
	};

    private void launchHymn(String number) {
		Intent next = new Intent();
		next.setClass(this, HymnActivity.class);
		next.putExtra("hymnNumber", number);
		startActivity(next);
	}
}
