package kaiserguy.lfhb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class NumberPicker 
    extends Activity
    implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout (this);
        linearLayout.setOrientation(1);
        ScrollView scrollView = new ScrollView (this);
        TableLayout tableLayout = new TableLayout (this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setBackgroundColor(Color.rgb( 0, 0, 0));
        //layout.setPadding(1,1,1,1);

        final Intent intent = getIntent();
        int intFirst = intent.getIntExtra("first", 0);
        int intLast = intent.getIntExtra("last", 99);
        Boolean isAppendix = intent.getBooleanExtra("appendix", false);
        int intCurrentNumber = intFirst;
        
        int rows = 20;
        int columns = 5;
        
        for (int r=0; r<rows; r++) {
            TableRow tr = new TableRow(this);
            for (int c=0; c<columns; c++) {
            	if (intCurrentNumber + (c * rows) <= intLast){
            		Button b = new Button (this);
            		if (intCurrentNumber + (c * rows) > 0) {
            			b.setText("" + (intCurrentNumber + (c * rows)));
                		if (isAppendix) {
                			b.setText(b.getText().toString() + "*");
                		}
                        b.setOnClickListener(this);
            		} else {
            			b.setText(" ");
            		}
                    b.setTextSize(20);
                    b.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.blackbutton));
                    b.setTypeface(Typeface.SERIF);
                    b.setTextColor(Color.rgb( 255, 255, 255));
                    b.setGravity(1);
                    b.setPadding(0,10,0,10);
                    tr.addView(b);
            	}
            } // for
            tableLayout.addView(tr);
            intCurrentNumber++;
        } // for

        scrollView.addView(tableLayout,new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        linearLayout.addView(scrollView,new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        super.setContentView(linearLayout);
    } // ()

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
    public void onClick(View view) {
		// from click on number
    	//((TextView) view).setBackgroundColor(Color.rgb(0, 255, 0));
		HymnBook.Hymn theHymn = HymnBook.getInstance().getHymn(((Button) view).getText().toString());
		launchHymn(theHymn.number);
    }

    private void launchHymn(String number) {
		Intent next = new Intent();
		next.setClass(this, HymnActivity.class);
		next.putExtra("hymnNumber", number);
		startActivity(next);
	}
} // class
