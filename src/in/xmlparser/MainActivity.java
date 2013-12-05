package in.xmlparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... arg0) {
						readXmlFile("code.xml");
						return null;
					}

				}.execute();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public InputStream readFile(String fileName) throws FileNotFoundException,
			IOException {
		return new FileInputStream(Environment.getExternalStorageDirectory()
				+ "/" + fileName);
	}

	public void readXmlFile(String fileName) {
		try {
			if (fileName.isEmpty())
				throw new NullPointerException();
			readData(new XmlDataParseHelper(readFile(fileName)).getParser());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void readData(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		int eventType = parser.getEventType();
		String tagName = "";
		Log.w("Developer", "Reading file...");
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				Log.w("Developer", "Reading backup file...");
				break;
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				if (tagName.equals("countries")) {
					Log.w("XMLParse", "Start TAG countries");
					// do something when countries tag starts
				}
				if (tagName.equals("country")) {
					Log.w("XMLParse", "Start TAG country");
					// do some when country tag starts
				} else if (tagName.equals("name")) {
					// read tag value using XmlDataParseHelper class
					String countryName = XmlDataParseHelper.readTag(parser,
							"name");
					Log.w("XmlParser", "Country name : " + countryName);
				} else if (tagName.equals("phonecode")) {
					String countryPhoneCode = XmlDataParseHelper.readTag(
							parser, "phonecode");
					Log.w("XmlParser", "Country Phone code : "
							+ countryPhoneCode);
				} else if (tagName.equals("code")) {
					String countryCode = XmlDataParseHelper.readTag(parser,
							"code");
					Log.w("XmlParser", "Country code name : " + countryCode);
				}
				break;
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if (tagName.equals("countries")) {
					// do something when counties tag is close.
				}
				break;
			}
			eventType = parser.next();
		}
		Log.w("Developer", "File parsing complete...");
	}
}
