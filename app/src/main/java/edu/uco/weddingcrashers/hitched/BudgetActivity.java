package edu.uco.weddingcrashers.hitched;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class BudgetActivity extends Activity {

    private ArrayList<BudgetItem> budgetItems;

    private double budget;
    private double used;

    TextView budgetView;
    TextView budgetUsedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budgetItems = new ArrayList<>();

        //replaced with database population
        budget = 10000;
        used = 2000;

        budgetView =  (TextView) findViewById(R.id.budgetValueText);
        budgetUsedView =  (TextView) findViewById(R.id.budgetUsedText);

        budgetView.setText(NumberFormat.getCurrencyInstance().format(budget));
        budgetUsedView.setText(NumberFormat.getCurrencyInstance().format(used));

        //will be replaced with database population
        BudgetItem budgetItem = new BudgetItem("Dress", 5000, 0);
        BudgetItem budgetItem2 = new BudgetItem("Cake", 1000, 0);

        budgetItems.add(budgetItem);
        budgetItems.add(budgetItem2);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
