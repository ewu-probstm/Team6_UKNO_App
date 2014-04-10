package com.ewu.UKNOapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainPage extends ListActivity {
	public final String LOG = "UKNO DEBUG LOG";


	// Progress Dialog     
	private ProgressDialog pDialog;       
	// Creating JSON Parser object     
	//JSONParser jParser = new JSONParser();       
	ArrayList<HashMap<String, String>> productsList;       
	// url to get all products list     
	private static String url_all_departments = "http://146.187.135.49/uknoewu/events/allImportantNotifications.php";       
	// JSON Node names          
	private static final String TAG_DATE = "Date";
	private static final String TAG_TIME = "Time";
	private static final String TAG_PLACE = "Place";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_TEXT = "Text";
	private static final String TAG_IMG = "img_ref";        
	// products JSONArray     
	JSONArray jArray = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
//		Intent intent = new Intent(this, CourseActivity.class);
//		startActivity(intent);		

		
		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String, String>>();
		
		// Loading products in Background Thread
		new LoadAllProducts().execute();
		
		// Get listview
		ListView lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener() {               
			
			@Override            
			public void onItemClick(AdapterView<?> parent, View view,                     
					int position, long id) {                
					String text = ((TextView) view.findViewById(R.id.text)).getText().toString();
					Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
					intent.putExtra(TAG_TEXT, text);
					startActivity(intent);           
				}         
			}); 
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_campus_map:
			openCampusMap();
			return true;
		case R.id.action_events:
			openEvents();
			return true;
		case R.id.action_sports:
			openSports();
			return true;
		case R.id.action_staff:
			openStaff();
			return true;
		case R.id.action_course:
			openCourse();
			return true;
		case R.id.action_plus:
			openPlus();
			return true;
		case R.id.action_campus_links:
			openLinks();
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}

	private void openLinks() {
		Intent intent = new Intent(this, CampusLinksActivity.class);
		Log.d(LOG, CampusLinksActivity.class.getSimpleName());
		startActivity(intent);
	}

	private void openCourse() {
		Intent intent = new Intent(this, CourseActivity.class);
		Log.d(LOG, CourseActivity.class.getSimpleName());
		startActivity(intent);		
	}
	
	private void openStaff() {
		Intent intent = new Intent(this, StaffActivity.class);
		Log.d(LOG, StaffActivity.class.getSimpleName());
		startActivity(intent);	
	}
	
	private void openPlus() {
		Intent intent = new Intent(this, PlusActivity.class);
		Log.d(LOG, PlusActivity.class.getSimpleName());
		startActivity(intent);
	}

	private void openSports() {
		Intent intent = new Intent(this, SportsActivity.class);
		Log.d(LOG, SportsActivity.class.getSimpleName());
		startActivity(intent);
	}

	private void openEvents() {
		Intent intent = new Intent(this, EventsActivity.class);
		Log.d(LOG, EventsActivity.class.getSimpleName());
		startActivity(intent);
	}

	private void openCampusMap() {
		Intent intent = new Intent(this, CampusMapActivity.class);
		Log.d(LOG, CampusMapActivity.class.getSimpleName());
		startActivity(intent);
	}

	
	/**      
	 * Background Async Task to Load all product by making HTTP Request      
	 **/    
	class LoadAllProducts extends AsyncTask<String, String, String> {           
		/**          * Before starting background thread Show Progress Dialog          
		 * * */       
		@Override        
		protected void onPreExecute() {             
			 super.onPreExecute();             
			 pDialog = new ProgressDialog(MainPage.this);             
			 pDialog.setMessage("Loading notifications. Please wait...");             
			 pDialog.setIndeterminate(false);             
			 pDialog.setCancelable(false);             
			 pDialog.show();        
			 }           
		/**          
		 * getting All products from url          
		 * */        
		protected String doInBackground(String... args) {   
			String result = "";
			InputStream isr = null;
			
			//ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//nameValuePairs.add(new BasicNameValuePair("catalogURL", "CSCD"));
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url_all_departments);
				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();
			}
			catch(Exception e){
				Log.e("log_tag", "Error in http connection " + e.toString());
				//resultView.setText("Couldn't connect to database");
			}
			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null){
					sb.append(line + "\n");
				}
				isr.close();
				
				result=sb.toString();
			}
			catch(Exception e){
				Log.e("log_tag", "Error converting result " +e.toString());
			}
			//parse json data
			try{
				jArray = new JSONArray(result);

					// looping through All Products                     
					for (int i = 0; i < jArray.length(); i++) {                         
						JSONObject c = jArray.getJSONObject(i);                           
						// Storing each json item in variable                         
						String title = c.getString(TAG_TITLE);                     
						String date = c.getString(TAG_DATE);                      
						String time = c.getString(TAG_TIME);                   
						String place = c.getString(TAG_PLACE);                      
						String text = c.getString(TAG_TEXT);                    
						String img = c.getString(TAG_IMG);     
						
						// creating new HashMap                         
						HashMap<String, String> map = new HashMap<String, String>();                           
						// adding each child node to HashMap key => value                        
						map.put(TAG_DATE, date);                                    
						map.put(TAG_TITLE, date + ": " + title);                 
						map.put(TAG_TIME, time);
						map.put(TAG_PLACE, place);
						map.put(TAG_TEXT, text);
						map.put(TAG_IMG, img);
						// adding HashList to ArrayList                         
						productsList.add(map);                     
					}                             
						            
				} catch (JSONException e) {                 
					e.printStackTrace();             
					}               
			return null;         
			}           
		/**          
		 * After completing background task Dismiss the progress dialog          
		 * 
		 **/        
		protected void onPostExecute(String file_url) {             
			// dismiss the dialog after getting all products             
			pDialog.dismiss();             
			// updating UI from Background Thread             
			runOnUiThread(new Runnable() {                 
				public void run() {                    
				/**                      
				 * Updating parsed JSON data into ListView                      
				 * 
				 */                    
					ListAdapter adapter = new SimpleAdapter(                             
							MainPage.this, productsList,                             
							R.layout.activity_main_page, new String[] { TAG_DATE, TAG_TITLE, TAG_TIME, TAG_PLACE,
									TAG_TEXT, TAG_IMG},                             
									new int[] { R.id.date, R.id.title, R.id.time, R.id.place,
									 R.id.text, R.id.img_ref});      
					
					// updating listview                     
					setListAdapter(adapter);                 
					}             
				});           
			}       
		} 
}
