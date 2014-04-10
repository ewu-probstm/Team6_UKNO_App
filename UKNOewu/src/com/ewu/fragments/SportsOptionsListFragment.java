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
import com.ewu.helper.FragmentFactory;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//----------------TODO: SKELETON ONLY NEEDS TO BE IMPLEMENTED.-----------------------
public class SportsOptionsListFragment extends ListFragment {

	private FragmentFactory fragmentfactory = new FragmentFactory();
	
	// Progress Dialog     
	private ProgressDialog pDialog; 
	
	
	// HashMap<department id sent as argument for php page, name of department>
	ArrayList<HashMap<String, String>> sportsOptionsList;
	// url to get all departments list
	
	//TODO: change to appropriate link-----------------------------------------
	private static String url_sports_categories = "http://146.187.135.49/uknoewu/sports/getsportscategories.php";
	// JSON Node names
	
	
	//TODO: update Database to include SportID-------------------------------
	private static final String TAG_DBARG = "CategoryID";
	private static final String TAG_NAME = "name";
	
	
	
	// products JSONArray
	JSONArray jArray = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.title_activity_sports);
		
		sportsOptionsList = new ArrayList<HashMap<String, String>>();
		// Loading departments in Background Thread
				new LoadSportsOptions().execute();
	
		
	}//end onCreate
	
	
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		// getting values from selected ListItem
		String sportCategory = ((TextView) v.findViewById(R.id.deptID)).getText()
				.toString();
		
		
		
		
		Fragment newFragment = fragmentfactory.CreateFragment(sportCategory);
		//TODO for all Sports Pages---------------------------------------------------
		//Intent i = getActivity().getIntent();
		//i.putExtra(TAG_DBARG, deptId);
	
		
		
		//CHANGE TO CORRECT FRAGMENT--------------------------------------------
		
		
		
		
		
		//TODO: NEEDS TO BE getSupportFragmentManager() to be BACKWARDS COMPATABLE
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragmentContainer, newFragment);
		transaction.addToBackStack(null);
		
		transaction.commit();
		
	}





	/**      
	 * Background Async Task to Load all product by making HTTP Request   
	 * Is REQUIRED to be a nested class.   
	 **/    
	class LoadSportsOptions extends AsyncTask<String, String, String> {           
		/**          * Before starting background thread Show Progress Dialog          
		 * * */       
		@Override        
		protected void onPreExecute() {             
			 super.onPreExecute();             
			 pDialog = new ProgressDialog(getActivity());             
			 pDialog.setMessage("Loading departments. Please wait...");             
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

			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url_sports_categories);
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
						String name = c.getString("Name");    
						//TODO: This is uneeded... we dont pass an argument to the next fragment
						String sportCategory = c.getString(TAG_DBARG);                           
						// creating new HashMap                         
						HashMap<String, String> map = new HashMap<String, String>();                           
						// adding each child node to HashMap key => value                         
						map.put(TAG_DBARG, sportCategory);                         
						map.put(TAG_NAME, name);                           
						// adding HashList to ArrayList                         
						sportsOptionsList.add(map);                     
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
							getActivity(), sportsOptionsList,                             
							R.layout.dept_list_item, new String[] { TAG_DBARG,                                     
									TAG_NAME},                             
									new int[] { R.id.deptID, R.id.deptname });                     
					// updating listview                     
					setListAdapter(adapter);                 
					}             
				});           
			}
		} 
	
	
	

}
