package com.ewu.fragments;

import com.ewu.UKNOapp.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {

	private String currentURL;	
	
	// Progress Dialog     
	private ProgressDialog pDialog;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.webview_title);
		
		Intent i = getActivity().getIntent();
		currentURL = i.getExtras().getString("info_url");
		
		
	}




	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,			
			Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.web_layout, container, false);		
		if (currentURL != null) {			
			Log.d("SwA", "Current URL  1["+currentURL+"]");			
			WebView wv = (WebView) v.findViewById(R.id.webPage);			
			wv.getSettings().setJavaScriptEnabled(true);			
			wv.setWebViewClient(new SwAWebClient());			
			wv.loadUrl(currentURL);		
			}		
		return v;	
		}	
	
	public void updateUrl(String url) {		
		Log.d("SwA", "Update URL ["+url+"] - View ["+getView()+"]");		
		currentURL = url;		
		WebView wv = (WebView) getView().findViewById(R.id.webPage);		
		wv.getSettings().setJavaScriptEnabled(true);	    
		wv.loadUrl(url);	
		}	
	
	private class SwAWebClient extends WebViewClient {		
		
		@Override		
		public boolean shouldOverrideUrlLoading(WebView view, String url) {			
			return false;		
			}	
		}
	} 
