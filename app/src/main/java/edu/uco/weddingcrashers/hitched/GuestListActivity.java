package edu.uco.weddingcrashers.hitched;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class GuestListActivity extends ActionBarActivity implements GuestListNewItem.NoticeDialogListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<GuestListItem> theList;
    private GuestListRecViewAdapter guestListAdapter;
    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);
        guestListAdapter = new GuestListRecViewAdapter();

       // addButton = (Button) findViewById(R.id.addGuestButton);

        recyclerView = (RecyclerView) findViewById(R.id.guestListRecView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(30));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        /*
        GuestListItem item = new GuestListItem("Derek Renfro", "6816 Bear Canyon", "Oklahoma City",
                "OK", "73162", "Groom", "405-517-7375", true, "hitchedtestemail@gmail.com");
        item.saveInBackground();



        item = new GuestListItem("Hannah Helper", "1234 Somewhere Ln", "Oklahoma City", "OK",
                "73209", "Bridesmaid", "505-222-3333", true, "hitchedtestemail@gmail.com");
        item.saveInBackground();

        */


        recyclerView.setAdapter(guestListAdapter);


//        addButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                DialogFragment newFragment = new GuestListNewItem();
//                newFragment.show(getFragmentManager(), "New Entry");
//            }
//        });


    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText name = (EditText) dialogView.findViewById(R.id.GuestListNameInputEdit);
        EditText phone = (EditText) dialogView.findViewById(R.id.GuestListPhoneNumberInputEdit);
        EditText address = (EditText) dialogView.findViewById(R.id.GuestListAddressInputEdit);
        EditText state = (EditText) dialogView.findViewById(R.id.GuestListStateInputEdit);
        EditText zip = (EditText) dialogView.findViewById(R.id.GuestListZipcodeInputEdit);
        EditText city = (EditText) dialogView.findViewById(R.id.GuestListCityInputEdit);
        EditText role = (EditText) dialogView.findViewById(R.id.GuestListRoleInputEdit);
        CheckBox WP = (CheckBox) dialogView.findViewById(R.id.GuestListWeddingPartuInputCheckBox);


        addTask(name.getText().toString(), address.getText().toString(), city.getText().toString(), state.getText().toString(), zip.getText().toString(), role.getText().toString(),phone.getText().toString(), WP.isChecked());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    private void addTask(String name, String address, String city, String state, String zip, String role,String phone, boolean wp)
    {
        GuestListItem item = new GuestListItem(name, address, city,state,zip,role,phone,wp, "");
        item.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    guestListAdapter.updateTheList();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guestlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_addguest){
            DialogFragment newFragment = new GuestListNewItem();
            newFragment.show(getFragmentManager(), "New Entry");

        }

        return super.onOptionsItemSelected(item);
    }

}
