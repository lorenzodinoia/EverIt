package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.threeten.bp.LocalTime;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.OpeningTimeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class OpeningDateTimeFragment extends Fragment {

    private ExpandableListView expandableListView ;
    private OpeningDateTimeExpandibleListAdapter expandableListAdapter;
    private List<OpeningDay> expandableListDetail = new LinkedList<>();
    private Restaurateur restaurateur;

    public OpeningDateTimeFragment() {
        // Required empty public constructor
    }

    public OpeningDateTimeFragment(Restaurateur restaurateur) {
        this.restaurateur = restaurateur;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_opening_date_time, parent, false);

        fillListDetails(viewRoot);

        return viewRoot;
    }

    public static void createOpeningTime(OpeningTime openingDay, int opTimePosition){

        OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
        openingTimeRequest.create(new OpeningTime(null, null), new RequestListener<OpeningTime>() {
            @Override
            public void successResponse(OpeningTime response) {

            }

            @Override
            public void errorResponse(RequestException error) {

            }
        });
    }

    private void initComponent(View view) {
        expandableListView = view.findViewById(R.id.expandableMenuOpenung);
        if(restaurateur != null) {
            expandableListAdapter = new OpeningDateTimeExpandibleListAdapter(getActivity().getApplicationContext(), expandableListDetail, true);
        }
        else{
            expandableListAdapter = new OpeningDateTimeExpandibleListAdapter(getActivity().getApplicationContext(), expandableListDetail, false);
        }
        expandableListView.setAdapter(expandableListAdapter);

    }

    private void fillListDetails(View view){
        LocalTime fakeLocalTime = LocalTime.now();
        OpeningTime fakeOpeningTime = new OpeningTime(fakeLocalTime, fakeLocalTime);
        setExpandableListData();
        if(restaurateur != null){
            expandableListDetail.addAll(restaurateur.getOpeningDays());
        }

        for(OpeningDay day : expandableListDetail){
            day.getOpeningTimes().add(fakeOpeningTime);
        }

        initComponent(view);
    }

    private void setExpandableListData(){
        this.expandableListDetail.add(new OpeningDay(1, getString(R.string.monday)));
        this.expandableListDetail.add(new OpeningDay(2, getString(R.string.tuesday)));
        this.expandableListDetail.add(new OpeningDay(3, getString(R.string.wednesday)));
        this.expandableListDetail.add(new OpeningDay(4, getString(R.string.thursday)));
        this.expandableListDetail.add(new OpeningDay(5, getString(R.string.friday)));
        this.expandableListDetail.add(new OpeningDay(6, getString(R.string.saturday)));
        this.expandableListDetail.add(new OpeningDay(7, getString(R.string.sunday)));
    }


}