package kaiserguy.lfhb;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import java.util.List;

	/**
	 * Provides search suggestions for a list of hymns and their meters.
	 */
	public class HymnBookProvider extends ContentProvider {

	    public static String AUTHORITY = "lfhbhymnbook";

	    private static final int SEARCH_SUGGEST = 0;
	    private static final int SHORTCUT_REFRESH = 1;
	    private static final UriMatcher sURIMatcher = buildUriMatcher();

	    /**
	     * The columns we'll include in our search suggestions.  There are others that could be used
	     * to further customize the suggestions, see the docs in {@link SearchManager} for the details
	     * on additional columns that are supported.
	     */
	    private static final String[] COLUMNS = {
	            "_id",  // must include this column
	            SearchManager.SUGGEST_COLUMN_TEXT_1,
	            SearchManager.SUGGEST_COLUMN_TEXT_2,
	            SearchManager.SUGGEST_COLUMN_INTENT_DATA,
	            };


	    /**
	     * Sets up a uri matcher for search suggestion and shortcut refresh queries.
	     */
	    private static UriMatcher buildUriMatcher() {
	        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
	        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
	        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
	        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, SHORTCUT_REFRESH);
	        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SHORTCUT_REFRESH);
	        return matcher;
	    }

	    @Override
	    public boolean onCreate() {
	        Resources resources = getContext().getResources();
	        HymnBook.getInstance().ensureLoaded(resources);
	        return true;
	    }

	    @Override
	    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
	            String sortOrder) {
	        if (!TextUtils.isEmpty(selection)) {
	            throw new IllegalArgumentException("selection not allowed for " + uri);
	        }
	        if (selectionArgs != null && selectionArgs.length != 0) {
	            throw new IllegalArgumentException("selectionArgs not allowed for " + uri);
	        }
	        if (!TextUtils.isEmpty(sortOrder)) {
	            throw new IllegalArgumentException("sortOrder not allowed for " + uri);
	        }
	        switch (sURIMatcher.match(uri)) {
	            case SEARCH_SUGGEST:
	                String query = null;
	                if (uri.getPathSegments().size() > 1) {
	                    query = uri.getLastPathSegment().toLowerCase();
	                }
					return getSuggestions(query, projection);
				
	            case SHORTCUT_REFRESH:
	                String shortcutId = null;
	                if (uri.getPathSegments().size() > 1) {
	                    shortcutId = uri.getLastPathSegment();
	                }
	                return refreshShortcut(shortcutId, projection);
	            default:
	                throw new IllegalArgumentException("Unknown URL " + uri);
	        }
	    }

	    private Cursor getSuggestions(String query, String[] projection){
	        String processedQuery = query == null ? "" : query.toLowerCase();
	        List<HymnBook.Hymn> hymns = HymnBook.getInstance().getMatches(processedQuery, true, getContext().getResources());

	        MatrixCursor cursor = new MatrixCursor(COLUMNS);
	        long id = 0;
	        for (HymnBook.Hymn hymn : hymns) {
	            cursor.addRow(columnValuesOfHymn(id++, hymn));
	        }
	        if (hymns.size() > 5){
	        	cursor.addRow(new Object[]{id++,"Only " + Integer.toString(hymns.size()) + " results suggested","Perform full search for more results","FullSearch " + query});	
	        }return cursor;
	    }

	    private Object[] columnValuesOfHymn(long id, HymnBook.Hymn hymn) {
	        return new Object[] {
	                id,                 // _id
	                hymn.number + "  -  " + hymn.meter + ",  by " + hymn.author,           // text1
	                hymn.getText(),     // text2
	                hymn.number,        // intent_data (included when clicking on item)
	        };
	    }

	    /**
	     * Note: this is unused as is, but if we included
	     * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our results, we
	     * could expect to receive refresh queries on this uri for the id provided, in which case we
	     * would return a cursor with a single item representing the refreshed suggestion data.
	     */
	    private Cursor refreshShortcut(String shortcutId, String[] projection) {
	        return null;
	    }

	    /**
	     * All queries for this provider are for the search suggestion and shortcut refresh mime type.
	     */
	    public String getType(Uri uri) {
	        switch (sURIMatcher.match(uri)) {
	            case SEARCH_SUGGEST:
	                return SearchManager.SUGGEST_MIME_TYPE;
	            case SHORTCUT_REFRESH:
	                return SearchManager.SHORTCUT_MIME_TYPE;
	            default:
	                throw new IllegalArgumentException("Unknown URL " + uri);
	        }
	    }

	    public Uri insert(Uri uri, ContentValues values) {
	        throw new UnsupportedOperationException();
	    }

	    public int delete(Uri uri, String selection, String[] selectionArgs) {
	        throw new UnsupportedOperationException();
	    }

	    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
	        throw new UnsupportedOperationException();
	    }
	}