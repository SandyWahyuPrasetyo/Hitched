package edu.uco.weddingcrashers.hitched;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by vdpotvin on 2/18/16.
 *
 */
public class ItineraryItemAdapter extends ArrayAdapter<ItineraryItem> {
    public ItineraryItemAdapter(Context context, ArrayList<ItineraryItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ItineraryItem item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_itinerary,
                    parent, false);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.itinerary_item_title);
        TextView assignedText = (TextView) convertView.findViewById(R.id.itinerary_item_assigned);
        TextView timeText = (TextView) convertView.findViewById(R.id.itinerary_item_time);
        TextView tipText = (TextView) convertView.findViewById(R.id.itinerary_item_tip);

        titleText.setText(item.getTitle());
        assignedText.setText(item.getAssigned());

        tipText.setText(NumberFormat.getCurrencyInstance().format(item.getTip()));

        if(item.getTime() != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            timeText.setText(timeFormat.format(item.getTime()));
        } else timeText.setText("Any Time");

        return convertView;
    }


}
