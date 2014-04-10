package com.ewu.UKNOapp;

import com.ewu.fragments.DepartmentListFragment;
import com.ewu.fragments.SingleFragmentActivity;
import com.ewu.fragments.TeacherListFragment;
import com.ewu.fragments.WebViewFragment;
import com.ewu.helper.Callbacks;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



public class StaffActivity extends SingleFragmentActivity implements Callbacks{

	
	/*
	 * Automatically Sets different layouts for phone or tablet
	 * Phone layout file: activity_fragment
	 * Tablet layout file: activity_twopane
	 */
	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;//for phones: in res/values/refs
											  //for tablets: in res/values-sw600dp/refs
											  //		   & in res/values-xlarge/refs
	}

	/*
	 * Returns the initial fragment hosted by this activity
	 * 
	 */
	@Override
	protected Fragment createFragment() {
		return new DepartmentListFragment();
	}
	

	@Override
	public void onListItemSelected(String url) {
		
		Intent i = this.getIntent();                
		// sending url to next activity                 
		i.putExtra(TeacherListFragment.TAG_LINK, url); 
		
		//Checks if device is a tablet by seeing if it has a detailFragmentContainer
		if (findViewById(R.id.detailFragmentContainer) == null){ //NOT a Tablet device
		// Replace current Fragment with WebViewFragment
			
			

			Fragment newFragment = new WebViewFragment();
			//TODO: NEEDS TO BE getSupportFragmentManager() to be BACKWARDS COMPATABLE
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			
			transaction.replace(R.id.fragmentContainer, newFragment);
			transaction.addToBackStack(null);
			
			transaction.commit();
			
			
			
		} //WebView already pulled up and displayed
		  //This will replace the current WebView with a new WebView (open new url)
		else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
			Fragment newDetail = new WebViewFragment();
			
			if (oldDetail != null){
				ft.remove(oldDetail);
			}
			
			ft.add(R.id.detailFragmentContainer, newDetail);
			ft.commit();
		}
		
	}
	


}
