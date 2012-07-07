package kaiserguy.lfhb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class AuthorHymnPicker 
    extends Activity
    implements ListView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.string_list);
        
        final ListView list;
        final TextView txtHeader;

        list = (ListView)findViewById(R.id.list);
        txtHeader = (TextView)findViewById(R.id.txtHeader);

        String strAuthor = getIntent().getStringExtra("strAuthor");
        txtHeader.setText("Listing all hymns by " + strAuthor);
        
        list.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, HymnBook.getInstance().getHymnNumbersByAuthor(strAuthor)));
        list.setOnItemClickListener(this);
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


    private void launchHymn(String number) {
		Intent next = new Intent();
		next.setClass(this, HymnActivity.class);
		next.putExtra("hymnNumber", number);
		startActivity(next);
	}

	public void onItemClick(AdapterView<?> arg0, View myView, int arg2, long arg3) {
		// from click on string
		String strLine = ((TextView) myView).getText().toString();
		launchHymn(strLine.substring(0, strLine.indexOf(" ")));
	}
} // class
