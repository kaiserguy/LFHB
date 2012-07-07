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

public class LetterPicker 
    extends Activity
    implements ListView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.string_list);
        
        final ListView mList;
        final TextView txtHeader;
        txtHeader = (TextView)findViewById(R.id.txtHeader);
        mList = (ListView)findViewById(R.id.list);
        txtHeader.setText("Pick the starting letter");
        
        mList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",")));
		mList.setOnItemClickListener(this);
    }

    private void launchLetter(char chrLetter) {
		Intent next = new Intent();
		next.setClass(this, FirstLinesByLetterPicker.class);
		next.putExtra("chrLetter", chrLetter);
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
	public void onItemClick(AdapterView<?> arg0, View myView, int arg2, long arg3) {
		// from click on letter
		launchLetter(((TextView) myView).getText().charAt(0));
	}
} // class
