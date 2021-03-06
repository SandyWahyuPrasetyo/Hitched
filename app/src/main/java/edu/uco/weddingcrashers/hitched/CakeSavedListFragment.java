package edu.uco.weddingcrashers.hitched;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by PC User on 2/19/2016.
 */
public class CakeSavedListFragment extends Fragment {
    private RecyclerView mVendorRecycleView;
    private VendorAdapter mAdapter;
    private ParseObject mFavoriteVendor;
    private List<CakeSaved> mVendors;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_cake,container,false);
        mVendorRecycleView = (RecyclerView) view.findViewById(R.id.detail_vendor_recycle_view);
        mVendorRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CakeSavedList vendorList = CakeSavedList.get(getActivity());
        List<CakeSaved> vendors = vendorList.getSavedVendors();
        if(mAdapter == null) {
            mAdapter = new VendorAdapter(vendors);
            mVendorRecycleView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private class VendorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CakeSaved mVendor;
        public TextView mTextView;
        public ImageView mImageView;
        public Button mButton;
        public VendorHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView)itemView.findViewById(R.id.list_item_vendor_name_text_view);
            mImageView = (ImageView)itemView.findViewById(R.id.vendorListImageView);
        }
        public void bindVendor(CakeSaved vendor){
            mVendor = vendor;
            mTextView.setText("Name: " + mVendor.getName()
                    + "\n Address" + mVendor.getAddress()
                    + "\n Phone: " + mVendor.getPhone()
                    +"\n Website: "+ mVendor.getWebsite());
            // mImageView.setImageResource(R.drawable.a);
        }

        @Override
        public void onClick(View view) {
//            Intent intent = CakeDetailsPagerActivity.newIntent(getActivity(),mVendor.getVendorID());
//            startActivity(intent);
        }
    }

    private class VendorAdapter extends RecyclerView.Adapter<VendorHolder>{


        public VendorAdapter(List<CakeSaved> vendors){
            mVendors = vendors;
        }

        @Override
        public VendorHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_cake_details,viewGroup,false);
            return new VendorHolder(view);
        }

        @Override
        public void onBindViewHolder(VendorHolder vendorHolder, int i) {
            CakeSaved mSaveVendor = mVendors.get(i);
            vendorHolder.bindVendor(mSaveVendor);
        }

        @Override
        public int getItemCount() {
            return mVendors.size();
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        CakeSavedList.get(getActivity()).deleteDataFromDatabase("FavoriteVendors");
        for(int i = 0;i<mVendors.size();i++){
            mFavoriteVendor = new ParseObject("FavoriteVendors");
            CakeSaved mSaveVendor = mVendors.get(i);
            mFavoriteVendor.put("vendorID",mSaveVendor.getId());
            mFavoriteVendor.put("name",mSaveVendor.getName());
            mFavoriteVendor.put("address",mSaveVendor.getAddress());
            mFavoriteVendor.put("phone",mSaveVendor.getPhone());
            mFavoriteVendor.put("rating",String.valueOf(mSaveVendor.getRating()));
            mFavoriteVendor.put("website",mSaveVendor.getWebsite());
            mFavoriteVendor.put("imgURL",mSaveVendor.getImgURL());
            mFavoriteVendor.saveInBackground();
        }

    }


}
