package it.uniba.di.sms1920.everit.customer.activities.results;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.util.Log;
import android.view.MenuItem;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class ResultDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        long resultId = getIntent().getLongExtra(ResultDetailFragment.ARG_ITEM_ID, 0);
        Restaurateur restaurateur = ResultListActivity.getResultById(resultId);


        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        if (restaurateur != null) {
            toolbar.setTitle(restaurateur.getShopName());
        }
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(ResultDetailFragment.ARG_ITEM_ID, resultId);
            ResultDetailFragment fragment = new ResultDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.result_detail_container, fragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ResultListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
