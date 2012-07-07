package kaiserguy.lfhb;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import kaiserguy.lfhb.HymnBook.Hymn;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HymnSearchPicker 
    extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.string_list);
        
        final ListView list;
        final TextView txtHeader;

        list = (ListView)findViewById(R.id.list);
        txtHeader = (TextView)findViewById(R.id.txtHeader);

        String strQuery = getIntent().getStringExtra("strQuery");
        
        txtHeader.setText(getString(R.string.search_results, strQuery));
                
        List<Hymn> mResults;
		
        mResults = HymnBook.getInstance().getMatches(strQuery, false, getBaseContext().getResources());
		
        HymnAdapter hymnAdapter = new HymnAdapter(mResults);
		if (mResults.size() > 1){	        
			list.setAdapter(hymnAdapter);
			list.setOnItemClickListener(hymnAdapter);
		} else if (mResults.size() == 1) {
			launchHymn(mResults.get(0).number);
		} else {
			txtHeader.setText("There were no results for '" + strQuery + "'");
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
		next.putExtra(SearchManager.QUERY,"");
		startActivity(next);
    }

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
			mInflater = (LayoutInflater) HymnSearchPicker.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			next.setClass(HymnSearchPicker.this, HymnActivity.class);
			next.putExtra("hymnNumber", number);
			startActivity(next);
		}
	}
} // class
