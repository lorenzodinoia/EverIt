package it.uniba.di.sms1920.everit.rider.works.proposal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.models.Proposal;
import it.uniba.di.sms1920.everit.utils.request.ProposalRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ProposalDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(ARG_ITEM_ID)) && (savedInstanceState == null)) {
            long id = 0;
            Object idObject = getIntent().getExtras().get(ARG_ITEM_ID);
            if (idObject instanceof String) {
                id = Long.parseLong((String) idObject);
            }
            else if (idObject instanceof Long) {
                id = (long) idObject;
            }

            ProposalRequest proposalRequest = new ProposalRequest();
            proposalRequest.read(id, new RequestListener<Proposal>() {
                @Override
                public void successResponse(Proposal response) {
                    loadFragment(response);
                }

                @Override
                public void errorResponse(RequestException error) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProposalDetailActivity.this);
                    dialogBuilder.setTitle(R.string.message_generic_error)
                            .setMessage(error.getMessage())
                            .setPositiveButton(R.string.ok_default, (dialog, which) -> {
                                dialog.dismiss();
                                ProposalDetailActivity.this.finish();
                            })
                            .show();
                }
            });
        }
    }

    private void loadFragment(Proposal proposal) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ProposalDetailFragment.ARG_ITEM, proposal);
        ProposalDetailFragment fragment = new ProposalDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.proposal_detail_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}