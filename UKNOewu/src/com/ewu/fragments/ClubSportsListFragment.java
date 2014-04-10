package com.ewu.fragments;

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

import com.ewu.UKNOapp.R;
import com.ewu.helper.Callbacks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
//----------------TODO: SKELETON ONLY NEEDS TO BE IMPLEMENTED.-----------------------
public class ClubSportsListFragment extends ListFragment {
	//Hosting activity MUST implement Callbacks
	
	
		private Callbacks mCallbacks;
		
		// Progress Dialog     
				private ProgressDialog pDialog; 
				
				
				// HashMap<department id sent as argument for php page, name of department>
				ArrayList<HashMap<String, String>> teacherList;
				// url to get all departments list
				private static String url_teachers_by_dept = "http://146.187.135.49/uknoewu/sports/getallclubsports.php";  
				// JSON Node names          
				public static final String TAG_LINK = "url_link";     
				private static final String TAG_NAME = "name"; 
				// products JSONArray
				JSONArray jArray = null;
			
				private String dbArg;
				
		
		//Connects mCallbacks to Hosting Activity's implementation of the Callbacks Interface
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			mCallbacks = (Callbacks)activity;
		}


		@Override
		public void onDetach() {
			super.onDetach();
			mCallbacks = null;
		}

		/*
		 * (non-Javadoc)
		 * Uses the hosting Activity's Intent Extras to retrieve the DeptID
		 * which was set when the DepartmentListFragment had a list item selected
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getActivity().setTitle(R.string.teachers_title);
			
			//Retrieve specific DeptID set by FragmentDepartmentList
			//Intent i = getActivity().getIntent();
			//dbArg = i.getExtras().getString("DeptID");
			
			teacherList = new ArrayList<HashMap<String, String>>();
			// Loading departments in Background Thread
			new LoadAllTeachers().execute();
		}
		
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

			// getting values from selected ListItem                 
			String url = ((TextView) v.findViewById(R.id.url)).getText().toString();                   

			mCallbacks.onListItemSelected(url);

		}



		/**      
		 * Background Async Task to Load all product by making HTTP Request      
		 **/    
		class LoadAllTeachers extends AsyncTask<String, String, String> {           
			/**          * Before starting background thread Show Progress Dialog          
			 * * */       
			@Override        
			protected void onPreExecute() {             
				 super.onPreExecute();             
				 pDialog = new ProgressDialog(getActivity());             
				 pDialog.setMessage("Loading Club Sports...");             
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
				//nameValuePairs.add(new BasicNameValuePair("CsID", dbArg));
				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url_teachers_by_dept);
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
					String s = "";
					jArray = new JSONArray(result);
	                
						// looping through All Products                     
						for (int i = 0; i < jArray.length(); i++) {                         
							JSONObject c = jArray.getJSONObject(i);                           
							// Storing each json item in variable                         
							String name = c.getString("Name");                         
							String url = c.getString(TAG_LINK);                           
							// creating new HashMap                         
							HashMap<String, String> map = new HashMap<String, String>();                           
							// adding each child node to HashMap key => value                         
							map.put(TAG_LINK, url);                         
							map.put(TAG_NAME, name);                           
							// adding HashList to ArrayList                         
							teacherList.add(map);                     
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
				getActivity().runOnUiThread(new Runnable() {                 
					public void run() {                    
					/**                      
					 * Updating parsed JSON data into ListView                      
					 * 
					 */                    
						ListAdapter adapter = new SimpleAdapter(                             
								getActivity(), teacherList,                             
								R.layout.list_item, new String[] { TAG_LINK,                                     
										TAG_NAME},                             
										new int[] { R.id.url, R.id.name });                     
						// updating listview                     
						setListAdapter(adapter);                 
						}             
					});           
				}       
			} 
		} 



