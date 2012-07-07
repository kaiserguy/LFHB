package kaiserguy.lfhb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NumberGroupPicker extends Activity
implements ListView.OnItemClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.string_list);
        
        final ListView mList;

        final TextView txtHeader;

        txtHeader = (TextView)findViewById(R.id.txtHeader);

        txtHeader.setText("Pick a number range");
        mList = (ListView)findViewById(R.id.list);
        
        mList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, "1-99,100s,200s,300s,Appendix".split(",")));
		mList.setOnItemClickListener(this);
    }

	public void onItemClick(AdapterView<?> arg0, View myView, int arg2, long arg3) {
		// from click on letter
		numberRangePicked(((TextView) myView).getText().toString());
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
	private void numberRangePicked(String strRange) {		
		    	Intent next = new Intent();
		    	if (strRange.equals("1-99")) {
		    		next.putExtra("first", 0);
		    		next.putExtra("last", 99);
		    	} else if (strRange.equals("100s")){
		    		next.putExtra("first", 100);
		    		next.putExtra("last", 199);
		    	} else if (strRange.equals("200s")){
		    		next.putExtra("first", 200);
		    		next.putExtra("last", 299);
		    	} else if (strRange.equals("300s")){
		    		next.putExtra("first", 300);
		    		next.putExtra("last", 341);
		    	} else if (strRange.equals("Appendix")){
		    		next.putExtra("first", 0);
		    		next.putExtra("last", 85);
		    		next.putExtra("appendix", true);
		    	}
	    		next.setClass(this, NumberPicker.class);
	    		startActivity(next);
	}
}
