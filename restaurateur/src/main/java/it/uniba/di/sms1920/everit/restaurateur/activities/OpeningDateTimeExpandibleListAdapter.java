package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;

public class OpeningDateTimeExpandibleListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<OpeningDay> expandableListDetail;

    private boolean logged;

    private Integer openingHoursSelected;
    private Integer openingMinutesSelected;
    private Integer closingHoursSelected;
    private Integer closingMinutesSelected;
    private List<Integer> hours = new ArrayList<>();
    private List<Integer> minutes = new ArrayList<>();

    OpeningDateTimeExpandibleListAdapter(Context context, List<OpeningDay> expandableListDetail, boolean logged){
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.logged = logged;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        List<OpeningTime> values = new ArrayList<>(expandableListDetail.get(listPosition).getOpeningTimes());
        return values.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) { return expandedListPosition; }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        OpeningTime child = (OpeningTime) getChild(listPosition, expandedListPosition);
        final String expandedListText = child.toString();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<OpeningTime> values = new ArrayList<>(expandableListDetail.get(listPosition).getOpeningTimes());

        if(isLastChild){
            initTime();
            convertView = layoutInflater.inflate(R.layout.list_last_item_opening_time, null);

            initSpinners(convertView);

            Button btnAdd = convertView.findViewById(R.id.btnDeleteOpeningTime);
            btnAdd.setOnClickListener(v -> {
                if(logged){
                   // OpeningTimeRequest openingTimeRequest = new OpeningTimeRequest();
                }
                //TODO capire come aggiugere alla lista
                //expandableListDetail.get(listPosition).getOpeningTimes().add(new OpeningTime(new LocalTime(openingHoursSelected, openingMinutesSelected, null, null), ))
            });
        }
        else {
            convertView = layoutInflater.inflate(R.layout.list_item_opening_time, null);

            TextView textViewOpeningTime = convertView.findViewById(R.id.textViewOpeningTime);
            textViewOpeningTime.setText(values.get(expandedListPosition).toString());

            Button btnDeleteItem = convertView.findViewById(R.id.btnDeleteOpeningTime);
            btnDeleteItem.setOnClickListener(v -> {
                //TODO capire come rimuovere dalla lista
            });

        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) { return this.expandableListDetail.get(listPosition).getOpeningTimes().size(); }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListDetail.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListDetail.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        OpeningDay group = (OpeningDay) getGroup(listPosition);
        String listTitle = group.getName();

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_group_opening_day, null);

        TextView listTitleTextView = convertView.findViewById(R.id.textViewDayName);
        listTitleTextView.setFocusable(false);


        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }


    public void updateAdapter(){
        notifyDataSetChanged();
    }

    private void initTime(){
        for(int i=1; i<=24; i++){
            hours.add(i);
        }
        for(int i=1; i<30; i++){
            minutes.add(i);
        }
    }

    private  void initSpinners(View convertView){
        Spinner spinnerOpeningHours = convertView.findViewById(R.id.spinnerOpeningHours);
        ArrayAdapter<Integer> adapterHours = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, hours);
        spinnerOpeningHours.setAdapter(adapterHours);
        spinnerOpeningHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    openingHoursSelected = adapterHours.getItem(position);
                }
                else{
                    openingHoursSelected = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerOpeningMinutes = convertView.findViewById(R.id.spinnerOpeningMinutes);
        ArrayAdapter<Integer> adapterMinutes = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, minutes);
        spinnerOpeningMinutes.setAdapter(adapterMinutes);
        spinnerOpeningMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    openingMinutesSelected = adapterHours.getItem(position);
                }
                else{
                    openingMinutesSelected = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerClosingHours = convertView.findViewById(R.id.spinnerClosingHours);
        spinnerClosingHours.setAdapter(adapterHours);
        spinnerClosingHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    closingHoursSelected = adapterHours.getItem(position);
                }
                else{
                    closingHoursSelected = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerClosingMinutes = convertView.findViewById(R.id.spinnerClosingMinutes);
        spinnerClosingMinutes.setAdapter(adapterMinutes);
        spinnerClosingMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    closingMinutesSelected = adapterMinutes.getItem(position);
                }
                else{
                    closingMinutesSelected = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
