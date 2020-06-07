package it.uniba.di.sms1920.everit.restaurateur.activities.openingTime;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TimePicker;

import org.threeten.bp.LocalTime;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OpeningTimeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class OpeningDateTimeFragment extends Fragment {

    
    private ExpandableListView expandableListView;
    private OpeningDateTimeExpandibleListAdapter expandableListAdapter;
    private List<OpeningDay> expandableListDetail = new LinkedList<>();
    private Restaurateur.Builder restaurateur;

    public OpeningDateTimeFragment() {
        // Required empty public constructor
    }

    public OpeningDateTimeFragment(Restaurateur.Builder restaurateur) {
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

        initComponent(viewRoot);

        return viewRoot;
    }

    private void initComponent(View view) {
        expandableListView = view.findViewById(R.id.expandableMenuOpening);
        expandableListAdapter = new OpeningDateTimeExpandibleListAdapter(getActivity(), expandableListDetail, this);
        expandableListView.setAdapter(expandableListAdapter);
    }


    //TODO verificare dati del restaurateur
    //TODO verificare efficacia richieste

    public void createOpeningTime(int listPosition, OpeningTime openingTime){

        if(restaurateur == null){
            OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
            openingTimeRequest.create(openingTime, new RequestListener<OpeningTime>() {
                @Override
                public void successResponse(OpeningTime response) {
                    expandableListDetail.get(listPosition).getOpeningTimes().add(response);

                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errorResponse OpeningDateTime
                }
            });
        }
        else{
            restaurateur.getOpeningDays().get(listPosition).getOpeningTimes().add(openingTime);
            //expandableListDetail.get(listPosition).getOpeningTimes().add(openingTime);
        }

    }

    public void deleteOpeningTime(int listPosition, int expandedListPosition){

        if(restaurateur == null){
            OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
            openingTimeRequest.delete(expandableListDetail.get(listPosition).getOpeningTimes().get(expandedListPosition).getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    expandableListDetail.get(listPosition).getOpeningTimes().remove(expandedListPosition);
                    expandableListAdapter.notifyDataSetChanged();
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errorResponse OpeningDateTime
                }
            });
        }
        else{
            restaurateur.getOpeningDays().get(listPosition).getOpeningTimes().remove(expandedListPosition);
            //expandableListDetail.get(listPosition).getOpeningTimes().remove(expandedListPosition);
        }
    }

    public List<OpeningDay> getExpandableListDetail() {
        return expandableListDetail;
    }

    public OpeningDateTimeExpandibleListAdapter getExpandableListAdapter() {
        return expandableListAdapter;
    }

    public void notifyDataSetChanged(){
        expandableListAdapter.notifyDataSetChanged();
    }


}