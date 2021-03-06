package edu.uco.weddingcrashers.hitched;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by PC User on 2/18/2016.
 */
public class CakeSaveFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static final String ARG_URL = "url";
    private  String userState;
    private Spinner mSpinner;

    public static CakeSaveFragment newInstance(String url){
        Bundle args = new Bundle();
        args.putString(ARG_URL,url);

        CakeSaveFragment fragment = new CakeSaveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_save_cake,null);
        mSpinner = (Spinner)view.findViewById(R.id.state_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.StateList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Choose your state")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(),"Marked " + userState+" as default location" ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),CakeDetailActivityCake.class);
                        intent.putExtra("STATE",userState);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Discard",null)
                .create();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        userState = adapterView.getItemAtPosition(pos).toString();
//        ((ParseDatabase)this.getActivity().getApplication()).setUserState(userState);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        userState="";
    }
}
