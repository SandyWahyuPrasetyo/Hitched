package edu.uco.weddingcrashers.hitched;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tung Nguyen on 2/4/2016.
 */
public class DetailsFragment extends Fragment {

    private RecyclerView mVendorRecycleView;
    private Vendor mVendor;
    private VendorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVendor = new Vendor();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        mVendorRecycleView = (RecyclerView) view.findViewById(R.id.detail_vendor_recycle_view);
        mVendorRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI(){
        VendorList vendorList = VendorList.get(getActivity());
        List<Vendor> vendors = vendorList.getVendors();

        mAdapter = new VendorAdapter(vendors);
        mVendorRecycleView.setAdapter(mAdapter);
    }

    private class VendorHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public VendorHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView;
        }
    }

    private class VendorAdapter extends RecyclerView.Adapter<VendorHolder>{
        private List<Vendor> mVendors;
        public VendorAdapter(List<Vendor> vendors){
            mVendors = vendors;
        }

        @Override
        public VendorHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,viewGroup,false);
            return new VendorHolder(view);
        }

        @Override
        public void onBindViewHolder(VendorHolder vendorHolder, int i) {
            Vendor vendor = mVendors.get(i);
            vendorHolder.mTextView.setText(vendor.getVendorName());
        }

        @Override
        public int getItemCount() {
            return mVendors.size();
        }
    }
}
