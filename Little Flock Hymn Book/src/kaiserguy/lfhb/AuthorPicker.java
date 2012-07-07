package kaiserguy.lfhb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class AuthorPicker 
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

        txtHeader.setText("Author Index");
                
        list.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, HymnBook.getInstance().getAuthors()));
        list.setOnItemClickListener(this);
    }

	public void onItemClick(AdapterView<?> arg0, View myView, int arg2, long arg3) {
		// from click on string
    	Toast.makeText(AuthorPicker.this, "Listing all hymns by " + ((TextView) myView).getText().toString(),
		          Toast.LENGTH_SHORT).show();
		Intent next = new Intent();
		next.putExtra("strAuthor", ((TextView) myView).getText().toString());
		next.setClass(this, AuthorHymnPicker.class);
		startActivity(next);
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
} // class
