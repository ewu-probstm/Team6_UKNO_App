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

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CourseActivity extends ListActivity {

	// Progress Dialog     
			private ProgressDialog pDialog;       
			// Creating JSON Parser object     
			//JSONParser jParser = new JSONParser();       
			ArrayList<HashMap<String, String>> productsList;       
			// url to get all products list     
			private static String url_all_departments = "http://146.187.135.49/uknoewu/course/course.php";       
			// JSON Node names          
			private static final String TAG_CATALOG_URL = "catalog_url";     
			private static final String TAG_NAME = "Name";       
			// products JSONArray     
			JSONArray jArray = null;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_department_list);
			

		    ActionBar actionBar = this.getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);

			
			// Hashmap for ListView
			productsList = new ArrayList<HashMap<String, String>>();
			
			// Loading products in Background Thread
			new LoadAllProducts().execute();
			
			// Get listview
			ListView lv = getListView();
			
			// on seleting single product
			// launching Edit Product Screen
			lv.setOnItemClickListener(new OnItemClickListener() {               
				
				@Override            
				public void onItemClick(AdapterView<?> parent, View view,                     
						int position, long id) {                 
					// getting values from selected ListItem                 
					String url = ((TextView) view.findViewById(R.id.url)).getText().toString();   
					
					// Starting new intent                 
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse("http://docs.google.com/viewer?url=" + url), "text/html");
					startActivity(intent);           
					}         
				});  
		}     

		// Response from Edit Product Activity    
		
			@Override    
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {         
				super.onActivityResult(requestCode, resultCode, data);         
				// if result code 100         
				if (resultCode == 100) {             
					// if result code 100 is received             
					// means user edited/deleted product             
					// reload this screen again             
					Intent intent = getIntent();             
					finish();             
					startActivity(intent);         
					}       
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
					 pDialog = new ProgressDialog(CourseActivity.this);             
					 pDialog.setMessage("Loading Courses. Please wait...");             
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
								String name = c.getString(TAG_NAME);                         
								String catalogURL = c.getString(TAG_CATALOG_URL);                           
								// creating new HashMap                         
								HashMap<String, String> map = new HashMap<String, String>();                           
								// adding each child node to HashMap key => value               
								map.put(TAG_CATALOG_URL, catalogURL);                         
								map.put(TAG_NAME, name);                           
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
									CourseActivity.this, productsList,                             
									R.layout.list_item, new String[] { TAG_CATALOG_URL,                                     
											TAG_NAME},                             
											new int[] { R.id.url, R.id.name });      
							
							// updating listview                     
							setListAdapter(adapter);                 
							}             
						});           
					}       
				} 
			} 