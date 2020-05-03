package it.uniba.di.sms1920.everit.customer.activities.results;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.LoginActivity;


public class ResultDetailFragment extends Fragment {

    private Button btnInfo;
    private Button btnReviews;
    private Button btnMaps;

    public static final String ARG_ITEM_ID = "item_id";

    public ResultDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.result_detail, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot) {
        btnInfo = viewRoot.findViewById(R.id.btnInfo);
        btnReviews = viewRoot.findViewById(R.id.btnReview);
        btnMaps = viewRoot.findViewById(R.id.btnMaps);

        btnMaps.setOnClickListener(v ->{
            Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

            String title = getResources().getString(R.string.ChooseApp);

            Intent chooser = Intent.createChooser(mapIntent, title);

            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(chooser);
            }

        });
    }
}
