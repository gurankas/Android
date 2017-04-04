package com.khalsa.gurankassingh.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Gurankas Singh on 4/2/2017.
 */

public class CrimeListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}
