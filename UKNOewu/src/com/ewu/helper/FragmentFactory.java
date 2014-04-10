package com.ewu.helper;

import android.support.v4.app.Fragment;

import com.ewu.fragments.ClubSportsListFragment;
import com.ewu.fragments.IntramuralSportsListFragment;
import com.ewu.fragments.SchoolSportsListFragment;

public class FragmentFactory {

	public FragmentFactory()
	{
		
	}
	
	public Fragment CreateFragment(String identifier)
	{
		if(identifier.equalsIgnoreCase("SclSports"))
			return new SchoolSportsListFragment();
		if(identifier.equalsIgnoreCase("ClbSports"))
			return new ClubSportsListFragment();
		if(identifier.equalsIgnoreCase("IntSports"))
			return new IntramuralSportsListFragment();
		else
			return null;
	}
}
