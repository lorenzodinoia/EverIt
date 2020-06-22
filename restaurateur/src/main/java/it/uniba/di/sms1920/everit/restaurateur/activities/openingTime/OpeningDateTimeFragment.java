package it.uniba.di.sms1920.everit.restaurateur.activities.openingTime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.signup.OpeningTimeSelectionFragment;
import it.uniba.di.sms1920.everit.restaurateur.activities.signup.SignUpActivity;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.OpeningTimeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class OpeningDateTimeFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private SignUpActivity signUpActivity;
    private ExpandableListView expandableListView;
    private OpeningDateTimeExpandibleListAdapter expandableListAdapter;
    private List<OpeningDay> expandableListDetail = new LinkedList<>();
    private Restaurateur.Builder restaurateurBuilder;
    private Restaurateur restaurateur;
    private OpeningTime lastItem = new OpeningTime(LocalTime.now(), LocalTime.now());
    private List<OpeningDay> days = new ArrayList<>();

    public OpeningDateTimeFragment() {
        // Required empty public constructor
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
        if((restaurateurBuilder != null) && (restaurateurBuilder.getOpeningDays() != null)){
            for(OpeningDay day : restaurateurBuilder.getOpeningDays()){
                day.getOpeningTimes().remove(day.getOpeningTimes().size()-1);
            }
        }
        expandableListView = view.findViewById(R.id.expandableMenuOpening);
        //add data to expandible list detail
        fillListDetail();
        expandableListAdapter = new OpeningDateTimeExpandibleListAdapter(getActivity(), expandableListDetail, this);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void setExpandableListData(){
        expandableListDetail.clear();
        days.clear();
        days.add(new OpeningDay(1, getString(R.string.monday)));
        days.add(new OpeningDay(2, getString(R.string.tuesday)));
        days.add(new OpeningDay(3, getString(R.string.wednesday)));
        days.add(new OpeningDay(4, getString(R.string.thursday)));
        days.add(new OpeningDay(5, getString(R.string.friday)));
        days.add(new OpeningDay(6, getString(R.string.saturday)));
        days.add(new OpeningDay(7, getString(R.string.sunday)));
        expandableListDetail.addAll(days);
    }

    private void fillListDetail(){
        if(restaurateurBuilder == null){
            restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();
            expandableListDetail = restaurateur.getOpeningDays();
        }
        else{
            if(restaurateurBuilder.getOpeningDays() == null) {
                setExpandableListData();
                List<OpeningDay> temp_day = new ArrayList<>(days);
                restaurateurBuilder.setOpeningDays(temp_day);
            } else{
                expandableListDetail = null;
                expandableListDetail = restaurateurBuilder.getOpeningDays();
            }
        }
        for(OpeningDay item : expandableListDetail){
            item.getOpeningTimes().add(lastItem);
        }
    }


    //TODO verificare dati del restaurateur
    //TODO verificare efficacia richieste

    public void createOpeningTime(int listPosition, OpeningTime openingTime){

        if(restaurateurBuilder == null){
            OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
            openingTimeRequest.create(openingTime, new RequestListener<OpeningTime>() {
                @Override
                public void successResponse(OpeningTime response) {
                    expandableListDetail.get(listPosition).getOpeningTimes().add(response);
                    notifyDataSetChanged();
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errorResponse OpeningDateTime
                }
            });
        }
        else{
            expandableListDetail.get(listPosition).getOpeningTimes().remove(expandableListDetail.get(listPosition).getOpeningTimes().size()-1);
            expandableListDetail.get(listPosition).getOpeningTimes().add(openingTime);
            expandableListDetail.get(listPosition).getOpeningTimes().add(lastItem);
            notifyDataSetChanged();
        }

    }

    public void deleteOpeningTime(int listPosition, int expandedListPosition){

        if(restaurateurBuilder == null){
            OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
            openingTimeRequest.delete(expandableListDetail.get(listPosition).getOpeningTimes().get(expandedListPosition).getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    expandableListDetail.get(listPosition).getOpeningTimes().remove(expandedListPosition);
                    notifyDataSetChanged();
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errorResponse OpeningDateTime
                }
            });
        }
        else{
            expandableListDetail.get(listPosition).getOpeningTimes().remove(expandedListPosition);
            notifyDataSetChanged();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  SignUpActivity){
            signUpActivity = (SignUpActivity) context;
            restaurateurBuilder = null;
            restaurateurBuilder = signUpActivity.getRestaurateurBuilder();
        }

        if(context instanceof OpeningTimeSelectionFragment.OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }

    public void updateEditText(LocalTime time){

    }
}