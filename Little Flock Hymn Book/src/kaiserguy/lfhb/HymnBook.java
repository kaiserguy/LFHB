package kaiserguy.lfhb;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

public class HymnBook {

	public static class Hymn {
		public final String number;
		/*
		 * public final String meter; public final String ssmeter; public final
		 * String author; public final int year;
		 */

		public final String title;
		public final String titleSort;
		public final String author;
		public final String meter;
		public final String ssmeter;
		public final String stanzaIndent;
		public final String chorusIndent;
		public final List<Stanza> stanzas;

		// public Hymn(String number, String meter, String ssmeter, String
		// author, int year, List<Stanza> stanzas) {
		public Hymn(String number, String title, String titleSort,
				String meter, String ssmeter, String author,
				String stanzaIndent, String chorusIndent) {
			/*
			 * this.number = number; this.meter = meter; this.ssmeter = ssmeter;
			 * this.stanzas = stanzas; this.author = author; this.year = year;
			 */
			this.number = number;
			this.titleSort = titleSort;
			this.title = title;
			this.meter = meter;
			this.ssmeter = ssmeter;
			this.stanzas = new ArrayList<Stanza>();
			this.author = author;
			this.stanzaIndent = stanzaIndent;
			this.chorusIndent = chorusIndent;
		}

		/*
		 * public String getText() { String hymnText = ""; for (int s = 0; s <
		 * stanzas.size() - 1; s++) { hymnText = hymnText + Integer.toString(s +
		 * 1) + " " + stanzas.get(s).text + "\n"; } hymnText = hymnText
		 * .replaceAll("\\|\\|/?i\\|\\|","") .replace("||sp||"," "); return
		 * hymnText; }
		 */
		public String getText() {
			String hymnText = "";
			for (int s = 0; s < stanzas.size() - 1; s++) {
				hymnText += Integer.toString(s + 1) + " ";
				for (int l = 0; l < stanzas.get(s).lines.length; l++) {
					hymnText += stanzas.get(s).lines[l];
				}
				hymnText += "\n";
			}
			hymnText = hymnText.replaceAll("\\|\\|/?i\\|\\|", "").replace(
					"||sp||", " ");
			return hymnText;
		}

		/*
		 * public String getStanzaText(int stanza) { String hymnText = "";
		 * 
		 * hymnText = hymnText + Integer.toString(stanza + 1) + " " +
		 * stanzas.get(stanza).text + "\n"; hymnText = hymnText
		 * .replaceAll("\\|\\|/?i\\|\\|","") .replace("||sp||"," "); return
		 * hymnText; }
		 */
		public String getStanzaText(int stanza) {
			String hymnText = "";

			hymnText = hymnText + Integer.toString(stanza + 1) + " ";
			for (int l = 0; l < stanzas.get(stanza).lines.length; l++) {
				hymnText += stanzas.get(stanza).lines[l];
			}
			hymnText += "\n";
			hymnText = hymnText.replaceAll("\\|\\|/?i\\|\\|", "").replace(
					"||sp||", " ");
			return hymnText;
		}

		/*
		 * public String getHTML(boolean defined) { String hymnTableStart =
		 * "<table style='color:#DDDDDD; font-family:serif; vertical-align:top;'>"
		 * ; String hymnHTML = ""; for (int s = 0; s < stanzas.size(); s++) {
		 * hymnHTML += "<tr><td style='vertical-align:top;'>"; hymnHTML +=
		 * Integer.toString(s + 1) + "</td><td>";
		 * 
		 * if (defined){ hymnHTML +=
		 * stanzas.get(s).text.replaceAll("\\b(\\w{3,40})\\b",
		 * "<a href='http://www.merriam-webster.com/dictionary/$1' style='color:White'>$1</a>"
		 * ); } else { int firstLetter = 0; String stanzaText =
		 * stanzas.get(s).text; String firstHalf =stanzaText.substring(0,
		 * stanzaText.indexOf("\n", (stanzaText.length() / 2) - 30)); if (s==0){
		 * boolean bad = false; Pattern p = Pattern.compile("[|i“’]"); do { bad
		 * = false; firstLetter++; String letter =
		 * firstHalf.substring(firstLetter-1,firstLetter); if
		 * (p.matcher(letter).find()){ bad = true; } } while(bad); 
		 * firstHalf =
		 * "<span style='float: left;font-size: 2.6em;line-height: 1;margin-right: 0.1em;margin-top: -0.1em;'>"
		 * + firstHalf.substring(0,firstLetter) + "</span>" +
		 * firstHalf.substring(firstLetter); } hymnHTML += "<span id='" + s +
		 * "' onclick=\"smoothScroll('" + (s-1) + "');\">" + firstHalf +
		 * "</span><span onclick=\"smoothScroll('" + (s+1) + "');\">" +
		 * stanzaText.substring(stanzaText.indexOf("\n", (stanzaText.length() /
		 * 2) - 30)) + "</a>"; // '#" + (s-1) + "' }
		 * 
		 * hymnHTML += "<br /><br /></td></tr>"; } hymnHTML = hymnHTML
		 * .replace("||sp||", "&nbsp;") .replace("\n", "<br />")
		 * .replace("||i||", "<i>") .replace("||/i||", "</i>");
		 * 
		 * hymnHTML = hymnTableStart + hymnHTML + "</table>";
		 * 
		 * return hymnHTML; }
		 */
		public String getHTML(boolean defined) {
			String hymnTableStart = "<table style='color:#DDDDDD; font-family:serif; vertical-align:top;'>";
			String hymnHTML = "";
			Stanza currentStanza;
			for (int s = 0; s < stanzas.size(); s++) {
				hymnHTML += "<tr><td style='vertical-align:top;'>";
				hymnHTML += Integer.toString(s + 1) + "</td><td>";
				currentStanza = stanzas.get(s);
				if (defined) {
					String stanzaText = "";
					for (int l = 0; l < currentStanza.lines.length; l++) {
						for (int i=0;i < Integer.parseInt(String.valueOf(stanzaIndent.charAt(l)));i++){
							stanzaText += "&nbsp;";
						}
						stanzaText += currentStanza.lines[l] + "<br />";
					}
					hymnHTML += stanzaText
							.replaceAll(
									"\\b(\\w{3,40})\\b",
									"<a href='http://www.merriam-webster.com/dictionary/$1' style='color:White'>$1</a>");
				} else {
					String firstHalf = "";
					String stanzaText = "";
					
					for (int l = 0; l < currentStanza.lines.length; l++) {
						for (int i=0;i < Integer.parseInt(String.valueOf(stanzaIndent.charAt(l)));i++){
							stanzaText += "&nbsp;";
						}
						if (s == 0 && l == 0){
							// Find first letter and add styling for drop caps
							String firstLetterCSS = "<span style='float: left;font-size: 2.6em;line-height: 1;margin-right: 0.1em;margin-top: -0.1em;'>";
							// String firstLetterCSS1 = "<span style='display: block;float: left; margin-top : -0.205em;margin-left : -0.56em; margin-right:0.5em;height:4.5em;'>";
							// String firstLetterCSS2 = "<span style='font-size: 3.33em; line-height  : 1.0em;'>";
							String firstWordCSS = "<div style='float: left;text-transform: uppercase;'>";
							String firstLineCSS = "<span style='margin-left  : -0.5em;'>";
							int firstLetter = 0;
							int firstSpace = 0;
							boolean bad = false;
							Pattern p = Pattern.compile("[|i“\"’]");
							
							do {
								bad = false;
								firstLetter++;
								String letter = currentStanza.lines[l].substring(firstLetter-1,firstLetter);
								if (p.matcher(letter).find()){ 
									bad = true; 
								} 
							} while(bad); 
							
							do {
								bad = false;
								firstSpace++;
								if (!(" ".equals(currentStanza.lines[l].substring(firstSpace-1,firstSpace)))){ 
									bad = true; 
								} 
							} while(bad); 
							
							stanzaText += firstLetterCSS + currentStanza.lines[l].substring(0,firstLetter) + "</span>";
							stanzaText += currentStanza.lines[l].substring(firstLetter,firstSpace).toUpperCase();
							stanzaText += currentStanza.lines[l].substring(firstSpace) + "<br />";
							//stanzaText += currentStanza.lines[l] + "<br />";	
						} else{
							stanzaText += currentStanza.lines[l] + "<br />";	
						}
						
						if (l == (int) (currentStanza.lines.length * 0.5) - 1) {
							firstHalf = stanzaText;
							stanzaText = "";
						}
					}
					hymnHTML += "<span id='" + s + "' onclick=\"smoothScroll('"
							+ (s - 1) + "');\">" + firstHalf
							+ "</span><span onclick=\"smoothScroll('" + (s + 1)
							+ "');\">" + stanzaText + "</a>";
				}

				hymnHTML += "<br /></td></tr>";
			}
			// .replace("||sp||", "&nbsp;")
			// .replace("\n", "<br />");
			// .replace("||i||", "<i>")
			// .replace("||/i||", "</i>");

			hymnHTML = hymnTableStart + hymnHTML + "</table>";

			return hymnHTML;
		}
	}

	/*
	 * public static class Stanza { public final int number; public final String
	 * text; public String[] references;
	 * 
	 * public Stanza(int number, String text) { this.number = number; this.text
	 * = text; } }
	 */
	public static class Stanza {
		public final int number;
		public String[] lines = new String[1];
		public String[] references;

		public Stanza(int number) {
			this.number = number;
		}
	}

	private static final HymnBook sInstance = new HymnBook();

	public static HymnBook getInstance() {
		return sInstance;
	}

	public boolean mLoaded = false;
	public boolean refsLoaded = false;
	public int currentHymnNumber = 0;
	public int totalHymns = 0;

	/**
	 * Loads the hymns and their text if they haven't been loaded already.
	 * 
	 * @param resources
	 *            Used to load the file containing the hymns.
	 */
	public synchronized boolean ensureLoaded(final Resources resources) {
		if (mLoaded) {
			return true;
		}
		new Thread(new Runnable() {
			public void run() {
				loadHymns(resources);
			}
		}).start();
		return true;
	}

	public String timeText = "";

	/**
	 * Reallocates an array with a new size, and copies the contents of the old
	 * array to the new array.
	 * 
	 * @param oldArray
	 *            the old array, to be reallocated.
	 * @param newSize
	 *            the new array size.
	 * @return A new array with the same contents.
	 */
	private static Object resizeArray(Object oldArray, int newSize) {
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType,
				newSize);
		int preserveLength = Math.min(oldSize, newSize);
		if (preserveLength > 0)
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		return newArray;
	}

	/*
	 * private synchronized void loadHymns(Resources resources) { if (mLoaded)
	 * return; String strMeter = ""; String strSSMeter = ""; String
	 * strHymnNumber = ""; int intStanzaNumber = 0; String strStanzaText = "";
	 * String strAuthor = ""; int intYear = 0;
	 * 
	 * XmlResourceParser xrp = resources.getXml(R.xml.hymns); int
	 * currentEventType; try { currentEventType = xrp.getEventType();
	 * 
	 * List<Stanza> stanzas = new ArrayList<Stanza>(); while (currentEventType
	 * != XmlResourceParser.END_DOCUMENT) { String currentTagType;
	 * 
	 * if (currentEventType == XmlResourceParser.START_TAG) { currentTagType =
	 * xrp.getName();
	 * 
	 * if (currentTagType.equals("hymn")) { stanzas = new ArrayList<Stanza>();
	 * strHymnNumber = xrp.getIdAttribute(); strMeter =
	 * xrp.getAttributeValue(null, "meter"); strSSMeter =
	 * xrp.getAttributeValue(null, "ssmeter"); strAuthor =
	 * xrp.getAttributeValue(null, "author"); intYear =
	 * xrp.getAttributeIntValue(null, "year", 0); xrp.next(); }
	 * 
	 * if (currentTagType.equals("stanza")) { intStanzaNumber =
	 * xrp.getIdAttributeResourceValue(0); strStanzaText =
	 * xrp.getAttributeValue(null, "text") .replace("||br||", "\n"); Stanza
	 * stanza = new Stanza(intStanzaNumber, strStanzaText); stanzas.add(stanza);
	 * } } else if (currentEventType == XmlResourceParser.END_TAG) { if
	 * (xrp.getName().equals("hymn")) { addHymn(strHymnNumber, strMeter,
	 * strSSMeter, strAuthor, intYear, stanzas); currentHymn += 1; totalHymns +=
	 * 1; } } else if (currentEventType == XmlResourceParser.TEXT) { // No Text
	 * in this XML } xrp.next(); currentEventType = xrp.getEventType(); } }
	 * catch (XmlPullParserException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } xrp.close();
	 * 
	 * mLoaded = true; }
	 */
	private synchronized void loadHymns(Resources resources) {
		if (mLoaded)
			return;
		String strMeter = "";
		String strSSMeter = "";
		String strTitle = "";
		String strTitleSort = "";
		String strHymnNumber = "";
		String strStanzaIndent = "";
		String strChorusIndent = "";
		String strAuthor = "";
		// int intYear = 0;
		XmlResourceParser xrpTitles = resources.getXml(R.xml.hymntitles);
		try {
			while (xrpTitles.getName() == null) {
				xrpTitles.next();
			}
			while (xrpTitles.getName().equals("hymn") == false) {
				xrpTitles.next();
			}
			while (xrpTitles.getName().equals("hymn")) {
				strHymnNumber = xrpTitles.getAttributeValue(null, "HymnNumStr");
				strTitle = xrpTitles.getAttributeValue(null, "Title");
				strTitleSort = xrpTitles.getAttributeValue(null, "TitleSort");
				strAuthor = xrpTitles.getAttributeValue(null, "Author");
				strMeter = xrpTitles.getAttributeValue(null, "Meter");
				// strSSMeter = xrpTitles.getAttributeValue(null, "ssmeter");
				strStanzaIndent = xrpTitles.getAttributeValue(null,
						"StanzaIndent");
				strChorusIndent = xrpTitles.getAttributeValue(null,
						"ChorusIndent");
				// intYear = xrp.getAttributeIntValue(null, "year", 0);

				addHymn(strHymnNumber, strTitle, strTitleSort, strMeter,
						strSSMeter, strAuthor, strStanzaIndent, strChorusIndent);
				currentHymnNumber += 1;
				totalHymns += 1;
				xrpTitles.nextTag(); // read end tag
				xrpTitles.nextTag(); // read start tag
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xrpTitles.close();
		xrpTitles = null;

		int intStanzaNumber = 0;
		int intLineNumber = 0;
		int intPreviousHymnStanzaNumber = 0;
		int intPreviousHymnNumber = 0;
		int intHymnNumber;
		Hymn currentHymn;
		Stanza currentStanza;
		XmlResourceParser xrpLines = resources.getXml(R.xml.hymnlines);
		try {
			while (xrpLines.getName() == null) {
				xrpLines.next();
			}
			while (xrpLines.getName().equals("line") == false) {
				xrpLines.next();
			}
			while (xrpLines.getName().equals("line")) {
				intHymnNumber = xrpLines
						.getAttributeIntValue(null, "HymnID", 1);
				intStanzaNumber = xrpLines.getAttributeIntValue(null,
						"StanzaID", 1);
				intLineNumber = xrpLines.getAttributeIntValue(null, "LineNum",
						1);
				currentHymn = mHymns.get(intHymnNumber - 1);
				if (intHymnNumber - intPreviousHymnNumber > 1) {
					intPreviousHymnNumber = intHymnNumber - 1;
					intPreviousHymnStanzaNumber = intStanzaNumber - 1;
				}
				intStanzaNumber = intStanzaNumber - intPreviousHymnStanzaNumber;

				switch (intLineNumber) {
				case 1:
					currentStanza = new Stanza(intStanzaNumber);
					String temp = xrpLines.getAttributeValue(null, "Text");
					currentStanza.lines[0] = temp;
					currentHymn.stanzas.add(currentStanza);
					currentHymnNumber += 1;
					break;
				default:
					currentHymn.stanzas.get(intStanzaNumber - 1).lines = (String[]) resizeArray(
							currentHymn.stanzas.get(intStanzaNumber - 1).lines,
							intLineNumber);
					currentHymn.stanzas.get(intStanzaNumber - 1).lines[intLineNumber - 1] = xrpLines
							.getAttributeValue(null, "Text");
				}
				xrpLines.nextTag();
				xrpLines.nextTag();
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		xrpLines.close();
		mLoaded = true;
	}

	private synchronized void loadReferences(Resources resources) {
		if (refsLoaded)
			return;
		Hymn loopHymn = mHymns.get(0);
		int intStanzaNumber = 0;

		XmlResourceParser xrp = resources.getXml(R.xml.scriptures);
		int currentEventType;
		try {
			currentEventType = xrp.getEventType();

			while (currentEventType != XmlResourceParser.END_DOCUMENT) {
				String currentTagType = "";

				if (currentEventType == XmlResourceParser.START_TAG) {
					currentTagType = xrp.getName();
					if (currentTagType.equals("hymn")) {
						loopHymn = this.getHymn(xrp.getIdAttribute());
					} else if (currentTagType.equals("stanza")) {
						intStanzaNumber = Integer
								.parseInt(xrp.getIdAttribute());
						if (intStanzaNumber <= loopHymn.stanzas.size()) {
							loopHymn.stanzas.get(intStanzaNumber - 1).references = xrp
									.getAttributeValue(null, "references")
									.split(";");
						}
					}
				}
				xrp.next();
				currentEventType = xrp.getEventType();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		xrp.close();

		refsLoaded = true;
	}

	public String[] getHymnNumbersByAuthor(String author) {
		List<String> list = new ArrayList<String>();
		int mHymnsSize = mHymns.size();
		for (int i = 0; i < mHymnsSize; i++) {
			Hymn thisHymn = mHymns.get(i);

			if (thisHymn.author.equals(author)) {
				list.add(thisHymn.number
						+ " "
						+ thisHymn.getStanzaText(0).substring(2,
								thisHymn.getStanzaText(0).indexOf("\n")));
				continue;
			}
		}

		String[] aryList = new String[list.size()];
		int mFirstLinesSize = list.size();

		for (int i = 0; i < mFirstLinesSize; i++) {
			aryList[i] = list.get(i);
		}
		// Arrays.sort(aryList);
		return aryList;
	}

	public String strip(String value) {
		return value.replaceAll("[\\W\\d]", "").trim().toLowerCase();
	}

	public String stripPunctuation(String value) {
		return value.replaceAll("\\W", "").trim().toLowerCase();
	}

	public List<Hymn> getMatches(String query, boolean boolSuggest,
			Resources resources) {
		List<Hymn> list = new ArrayList<Hymn>();
		if (query.length() == 0) {
			return list;
		}
		query = query.replace(" in the appendix ", "*");
		query = query.replace(" in the appendix", "*");
		String punctStripped = stripPunctuation(query);
		String stripped = strip(query);
		Pattern patBibleRef;

		Boolean isHymnOrMeter = false;
		Boolean isHymnInAppendix = false;
		Boolean isReference = false;
		if (query.endsWith("*")) {
			if (isInteger(query.substring(0, query.length() - 1))) {
				isHymnInAppendix = true;
			}
		}
		if (isInteger(query)) {
			isHymnOrMeter = true;
		}

		if ((isHymnOrMeter || isHymnInAppendix) != true) {
			patBibleRef = Pattern
					.compile("([1-3] )?(S.{0,3} of S.{0,6})|([A-Z]\\w+)[.]? (\\d+)[:.](\\d+)");
			if (patBibleRef.matcher(query).matches()) {
				isReference = true;
			}
		}

		if (isHymnInAppendix) {
			int mHymnsSize = mHymns.size();
			Hymn thisHymn;
			for (int i = 340; i < mHymnsSize; i++) {
				thisHymn = mHymns.get(i);
				if (thisHymn.number.equals(query)) {
					list.add(thisHymn);
					return list;
				}
			}
		} else if (isHymnOrMeter) {
			/* Search hymn numbers and meters only */
			int mHymnsSize = mHymns.size();
			Hymn thisHymn;
			for (int i = 0; i < mHymnsSize; i++) {
				thisHymn = mHymns.get(i);
				if (thisHymn.number.startsWith(query)) {
					list.add(thisHymn);
					continue;
				} else if (stripPunctuation(thisHymn.meter).startsWith(
						punctStripped)) {
					list.add(thisHymn);
					continue;
				}
				if (boolSuggest) {
					if (list.size() > 5) {
						return list;
					}
				}
			}
		} else if (isReference) {
			/* Search verse references only */
			loadReferences(resources);
			int mHymnsSize = mHymns.size();
			int mStanzaSize;
			int mReferenceLength;
			Hymn thisHymn;
			Stanza thisStanza;
			String[] references;
			for (int i = 0; i < mHymnsSize; i++) {
				thisHymn = mHymns.get(i);
				mStanzaSize = thisHymn.stanzas.size();
				HYMNSEARCH: {
					for (int ii = 0; ii < mStanzaSize; ii++) {
						thisStanza = thisHymn.stanzas.get(ii);
						if (thisStanza.references != null) {
							mReferenceLength = thisStanza.references.length;
							references = thisStanza.references;
							for (int iii = 0; iii < mReferenceLength; iii++) {
								if (references[iii].equals(query)) {
									list.add(thisHymn);
									break HYMNSEARCH;
								}
							}
						}
					}
				}
				if (boolSuggest) {
					if (list.size() > 5) {
						return list;
					}
				}
			}
		} else {
			if (punctStripped.length() < 2 && stripped.length() < 2) {
				return list;
			}
			/* Search hymn text, author, and meter only */
			int mHymnsSize = mHymns.size();
			Hymn thisHymn;
			for (int i = 0; i < mHymnsSize; i++) {
				thisHymn = mHymns.get(i);
				if (thisHymn.meter.equals(query)
						|| stripPunctuation(thisHymn.meter).equals(
								punctStripped)) {
					list.add(thisHymn);
					continue;
				} else if (punctStripped.length() > 1
						&& stripPunctuation(thisHymn.author).contains(
								punctStripped)) {
					list.add(thisHymn);
					continue;
				} else if (stripped.length() > 1) {
					{
						/*
						 * int mStanzasSize = thisHymn.stanzas.size(); for (int
						 * s = 0; s < mStanzasSize; s++) { if
						 * (strip(thisHymn.stanzas
						 * .get(s).text).contains(stripped)) {
						 * list.add(thisHymn); break; } }
						 */
						int mStanzasSize = thisHymn.stanzas.size();
						for (int s = 0; s < mStanzasSize; s++) {
							String stanzaText = "";
							for (int l = 0; l < thisHymn.stanzas.get(s).lines.length; l++) {
								stanzaText += thisHymn.stanzas.get(s).lines[l];
							}
							if (strip(stanzaText).contains(stripped)) {
								list.add(thisHymn);
								break;
							}
						}
					}
				}

				if (boolSuggest) {
					if (list.size() > 5) {
						return list;
					}
				}
			}
		}

		return list;
	}

	public List<Hymn> getHymnsByMeter(String strMeter) {
		List<Hymn> list = new ArrayList<Hymn>();
		/* Search meters only */
		int mHymnsSize = mHymns.size();
		for (int i = 0; i < mHymnsSize; i++) {
			Hymn thisHymn = mHymns.get(i);

			if (thisHymn.meter.equals(strMeter)) {
				list.add(thisHymn);
				continue;
			}
		}
		return list;
	}

	public String[] getFirstLinesByNumber() {
		int mHymnsSize = mHymns.size();
		String[] aryList = new String[mHymnsSize];
		for (int i = 0; i < mHymnsSize; i++) {
			Hymn thisHymn = mHymns.get(i);
			aryList[i] = thisHymn.number
					+ " - "
					+ thisHymn.getStanzaText(0).substring(2,
							thisHymn.getStanzaText(0).indexOf("\n"));
		}
		return aryList;
	}

	public static String trimChars(String str, char ch) {
		return str.replaceAll(ch + "$|^" + ch, "");
	}

	public String[] getFirstLinesByLetter(char chrLetter) {
		List<String> lstFirstLines = new ArrayList<String>();
		int mHymnsSize = mHymns.size();

		for (int i = 0; i < mHymnsSize; i++) {
			Hymn thisHymn = mHymns.get(i);
			int intStanzaSize = thisHymn.stanzas.size();
			for (int s = 0; s < intStanzaSize; s++) {
				Boolean boolFoundLetter = false;
				String stanzaText = thisHymn.getStanzaText(s);
				stanzaText = thisHymn.getStanzaText(s).substring(2,
						stanzaText.indexOf("\n"));
				stanzaText = trimChars(
						trimChars(trimChars(stanzaText, '“'), '’'), '”');
				char chrFirstChar = stanzaText.charAt(0);
				if (chrFirstChar == chrLetter) {
					lstFirstLines.add(stanzaText + " #" + thisHymn.number);
					boolFoundLetter = true;
				} else if (boolFoundLetter) {
					break;
				}
			}
		}

		String[] aryList = new String[lstFirstLines.size()];
		int mFirstLinesSize = lstFirstLines.size();

		if (mFirstLinesSize < 1) {
			lstFirstLines.add("No Stanzas #0");
		}
		for (int i = 0; i < mFirstLinesSize; i++) {
			aryList[i] = lstFirstLines.get(i);
		}
		Arrays.sort(aryList);
		return aryList;
	}

	public String[] getMeters() {
		List<String> list = new ArrayList<String>();
		/* get all meters */
		int mHymnsSize = mHymns.size();
		for (int i = 0; i < mHymnsSize; i++) {
			Hymn thisHymn = mHymns.get(i);

			if (thisHymn.meter.length() > 0) {
				if (list.contains(thisHymn.meter)) {
					// do nothing
				} else {
					list.add(thisHymn.meter);
				}
			}
		}

		int mMetersSize = list.size();
		String[] aryList = new String[mMetersSize];
		for (int i = 0; i < mMetersSize; i++) {
			aryList[i] = list.get(i);
		}
		Arrays.sort(aryList);
		return aryList;
	}

	public String[] getAuthors() {
		List<String> list = new ArrayList<String>();
		/* get all meters */
		int mHymnsSize = mHymns.size();
		for (int i = 0; i < mHymnsSize; i++) {
			Hymn thisHymn = mHymns.get(i);

			if (thisHymn.author.length() > 0) {
				if (list.contains(thisHymn.author)) {
					// do nothing
				} else {
					list.add(thisHymn.author);
				}
			}
		}

		int mAuthorsSize = list.size();
		String[] aryList = new String[mAuthorsSize];
		for (int i = 0; i < mAuthorsSize; i++) {
			aryList[i] = list.get(i);
		}
		Arrays.sort(aryList);
		return aryList;
	}

	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private List<Hymn> mHymns = new ArrayList<Hymn>();

	public Hymn getHymn(String hymnNumber) {
		if (hymnNumber == "0") {
			hymnNumber = "1";
		} else if (hymnNumber == "342") {
			hymnNumber = "1*";
		} else if (hymnNumber == "0*") {
			hymnNumber = "341";
		} else if (hymnNumber == "86*") {
			hymnNumber = "85*";
		}
		if (hymnNumber.endsWith("*")) {
			return mHymns.get(340 + Integer.parseInt(hymnNumber
					.replace("*", "")));
		} else {
			return mHymns.get(Integer.parseInt(hymnNumber) - 1);
		}
	}

	/*
	 * private void addHymn(String number, String meter, String ssmeter, String
	 * author, int year, List<Stanza> stanzas) { final Hymn theHymn = new
	 * Hymn(number, meter, ssmeter, author, year, stanzas); mHymns.add(theHymn);
	 * }
	 */
	private void addHymn(String strHymnNumber, String strTitle,
			String strTitleSort, String strMeter, String strSSMeter,
			String strAuthor, String strStanzaIndent, String strChorusIndent) {
		mHymns.add(new Hymn(strHymnNumber, strTitle, strTitleSort, strMeter,
				strSSMeter, strAuthor, strStanzaIndent, strChorusIndent));
	}
}