package in.xmlparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final String TAG = "Developer";

	private static final String RSS_311 = "311Today.rss";

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
						//readXmlFile("code.xml");
						readXmlFile(RSS_311);
						return null;
					}

				}.execute();

			}
		});
		
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... arg0) {
						readWebpage(null);
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
		return new FileInputStream(
				
				// Environment.getExternalStorageDirectory()

				MainActivity.this.getFilesDir()
		        .getAbsolutePath()
				+ "/" + fileName
				);
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
		Log.w("Developer", "QQQ: Reading file...");
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				Log.w("Developer", "Reading backup file...");
				break;
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				Log.i(TAG, "start tag: "+ tagName);
				
				if (tagName.equals("content:encoded")) {
				String content = XmlDataParseHelper.readTag(
						parser, "content:encoded");
				Log.w(TAG, "content : "
						+ content);
				}
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
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
             String response = "";
             
             for (String url : urls) {
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
                    try {
                               HttpResponse execute = client.execute(httpGet);
                               InputStream content = execute.getEntity().getContent();
                                          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                               //String file_name = MainActivity.this.getFilesDir().getAbsolutePath() + "/311Today.rss";
                               String file_name = RSS_311;
                             // file_name = Environment.getExternalStorageDirectory(
                                                               // "/textFile.txt";
                               
                               FileOutputStream fos;
                               try {
                                   fos = getApplicationContext().openFileOutput(file_name, Context.MODE_PRIVATE);


                                   //ObjectOutputStream oos = new ObjectOutputStream(fos);
                                   
                                   //oos.writeObject(theObjectAr);
                                   String s = "";
                                   while ((s = buffer.readLine()) != null) {
                                       //response += s;
                                	   //out.write(s);
                                	   fos.write(s.getBytes());
                                  }
                                   fos.close();
                               } catch (FileNotFoundException e) {
                                   e.printStackTrace();
                               }catch(IOException e){
                                   e.printStackTrace();
                               }
/*

                                     File file = new File(file_name);
                               boolean exist = file.createNewFile();
                               if (!exist){
                                System.out.println("File already exists.");
                                Log.i(TAG,"File already exists.");
                                
                               }
                                         FileWriter fstream = new FileWriter(file_name);
                                  BufferedWriter out = new BufferedWriter(fstream);
                                  String s = "";
                                  while ((s = buffer.readLine()) != null) {
                                       response += s;
                                   out.write(s);
                                  }
                                  out.close();
                                  */
                                  System.out.println("File created successfully.");
                                  Log.i(TAG,"File created successfully.");
  
                      } catch (Exception e) {
                       e.printStackTrace();
                      }
                     }
                         Log.d("\nResponse========: ", "String" + response);
                     return response;
                }

                @Override
                protected void onPostExecute(String response) {
                 // textView.setText(response);
                	/*
                 String result = "";
                 String strLine;
                 String file_name =  MainActivity.this.getFilesDir()
                         .getAbsolutePath() + "/311Today.rss";
                 
                 FileOutputStream fos;
                 try {
                     fos = getApplicationContext().openFileOutput(file_name, Context.MODE_PRIVATE);


                     ObjectOutputStream oos = new ObjectOutputStream(fos);
                     oos.writeObject(theObjectAr); 
                     oos.close();
                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }catch(IOException e){
                     e.printStackTrace();
                 }
                 */
                 /*
                 File file = new File(file_name);
                 FileInputStream fin;
 
                 try {
                      fin = new FileInputStream(file);
                      BufferedReader br = new BufferedReader(new InputStreamReader(fin));
                      while ((strLine = br.readLine()) != null) {
                       result += strLine;
                  }
                  //textView.setText(result);
                      Log.i(TAG,"QQQ: result: " + result);
                 } catch (FileNotFoundException e) {
                         e.printStackTrace();
                 } catch (IOException e) {
                             e.printStackTrace();
                 }
                 */
                }
               }

	
	 public void readWebpage(View view) {
         DownloadWebPageTask task = new DownloadWebPageTask();
         //http://www.nyc.gov/apps/311/311Today.rss    
         task.execute(new String[] { "http://www.nyc.gov/apps/311/" + RSS_311 });
         //task.execute(new String[] { "http://www.vogella.de" });
    }
}
