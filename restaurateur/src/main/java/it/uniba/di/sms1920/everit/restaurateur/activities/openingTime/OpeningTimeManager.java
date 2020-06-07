package it.uniba.di.sms1920.everit.restaurateur.activities.openingTime;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import org.threeten.bp.LocalTime;

import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.OpeningTimeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OpeningTimeManager implements  OpeningTimeCRUDInterface{

    private OpeningDateTimeFragment openingDateTimeFragment;
    private Restaurateur.Builder restaurateurBuilder;
    private Restaurateur restaurateur;
    private FragmentActivity parent;
    private int containerId;
    private OpeningDateTimeExpandibleListAdapter adapter;

    public OpeningTimeManager(FragmentActivity parent, int containerId, Restaurateur restaurateur){
        this.parent = parent;
        this.containerId = containerId;
        this.restaurateur = restaurateur;
        initFragment();
        setOpeningTimeData();
        adapter = openingDateTimeFragment.getExpandableListAdapter();
        adapter.setInterface(this);
    }

    public OpeningTimeManager(FragmentActivity parent, int containerId, Restaurateur.Builder restaurateurBuilder){
        this.parent = parent;
        this.containerId = containerId;
        this.restaurateurBuilder = restaurateurBuilder;
        initFragment();
        setOpeningTimeData();
        adapter = openingDateTimeFragment.getExpandableListAdapter();
        adapter.setInterface(this);
    }

    public void initFragment(){
        openingDateTimeFragment = new OpeningDateTimeFragment();
        FragmentTransaction fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerId, openingDateTimeFragment).addToBackStack(null).commit();
    }

    public void setOpeningTimeData(){
        List<OpeningDay> expandableListDetail = openingDateTimeFragment.getExpandableListDetail();
        setExpandableListData(expandableListDetail);
        if(restaurateurBuilder != null){
            restaurateurBuilder.setOpeningDays(expandableListDetail);
        }
        else{
            for(OpeningDay day : restaurateur.getOpeningDays()){
                Long index = day.getId()-1;
                expandableListDetail.get(index.intValue()).getOpeningTimes().addAll(day.getOpeningTimes());
            }
        }

        LocalTime fakeLocalTime = LocalTime.now();
        OpeningTime fakeOpeningTime = new OpeningTime(fakeLocalTime, fakeLocalTime);
        for(OpeningDay day : expandableListDetail){
            day.getOpeningTimes().add(fakeOpeningTime);
        }

        openingDateTimeFragment.notifyDataSetChanged();
    }

    private void setExpandableListData(List<OpeningDay> expandableListDetail){
        expandableListDetail.add(new OpeningDay(1, parent.getString(R.string.monday)));
        expandableListDetail.add(new OpeningDay(2, parent.getString(R.string.tuesday)));
        expandableListDetail.add(new OpeningDay(3, parent.getString(R.string.wednesday)));
        expandableListDetail.add(new OpeningDay(4, parent.getString(R.string.thursday)));
        expandableListDetail.add(new OpeningDay(5, parent.getString(R.string.friday)));
        expandableListDetail.add(new OpeningDay(6, parent.getString(R.string.saturday)));
        expandableListDetail.add(new OpeningDay(7, parent.getString(R.string.sunday)));
    }


    @Override
    public void create(int listPosition, OpeningTime openingTime) {
        if(restaurateurBuilder == null){
            OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
            openingTimeRequest.create(openingTime, new RequestListener<OpeningTime>() {
                @Override
                public void successResponse(OpeningTime response) {
                    openingDateTimeFragment.getExpandableListDetail().get(listPosition).getOpeningTimes().add(response);
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errorResponse OpeningDateTime
                }
            });
        }
        else{
            restaurateurBuilder.getOpeningDays().get(listPosition).getOpeningTimes().add(openingTime);
            openingDateTimeFragment.getExpandableListDetail().get(listPosition).getOpeningTimes().add(openingTime);
        }
    }

    @Override
    public void delete(int listPosition, int expandedListPosition) {

        if(restaurateurBuilder == null){
            OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
            openingTimeRequest.delete(openingDateTimeFragment.getExpandableListDetail().get(listPosition).getOpeningTimes().get(expandedListPosition).getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    openingDateTimeFragment.getExpandableListDetail().get(listPosition).getOpeningTimes().remove(expandedListPosition);
                    openingDateTimeFragment.notifyDataSetChanged();
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errorResponse OpeningDateTime
                }
            });
        }
        else{
            restaurateurBuilder.getOpeningDays().get(listPosition).getOpeningTimes().remove(expandedListPosition);
            openingDateTimeFragment.getExpandableListDetail().get(listPosition).getOpeningTimes().remove(expandedListPosition);
        }
    }
}
