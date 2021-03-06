package edu.uco.weddingcrashers.hitched;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by PC User on 2/18/2016.
 */
public class PhotographerDetailsPagerActivity extends AppCompatActivity {
    private static final String EXTRA_VENDOR_ID = "edu.uco.weddingcrashers.hitched.vendorID";
    private static final String EXTRA_STATE="edu.uco.weddingcrashers.hitched.state";

    private ViewPager mViewPager;
    private List<PhotographerV> mPhotographerVs;

    public static Intent newIntent(Context packageContext,UUID vendorID){
        Intent intent = new Intent(packageContext,PhotographerDetailsPagerActivity.class);
        intent.putExtra(EXTRA_VENDOR_ID,vendorID);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details_pager_photographer);

        UUID vendorID = (UUID)getIntent().getSerializableExtra(EXTRA_VENDOR_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_vendor_details_pager_view_pager);
        mPhotographerVs = PhotographerList.get(this).getVendors();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                PhotographerV photographerV = mPhotographerVs.get(position);
                return PhotographerDetailFragment.newInstance(photographerV.getVendorID());

            }

            @Override
            public int getCount() {
                return mPhotographerVs.size();
            }
        });
        for(int i = 0;i< mPhotographerVs.size();i++){
            if(mPhotographerVs.get(i).getVendorID().equals(vendorID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
