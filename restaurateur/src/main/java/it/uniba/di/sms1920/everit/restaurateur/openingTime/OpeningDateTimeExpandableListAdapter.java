package it.uniba.di.sms1920.everit.restaurateur.openingTime;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;

public class OpeningDateTimeExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<OpeningDay> expandableListDetail;
    OpeningDateTimeFragment fragment;

    LocalTime openingTime;
    LocalTime closingTime;
    private Map<Integer, TextInputLayout> editTextOpeningTimeContainer = new HashMap<>();
    private Map<Integer, TextInputEditText> editTextOpeningTime = new HashMap<>();
    private Map<Integer, TextInputLayout> editTextClosingTimeContainer = new HashMap<>();
    private Map<Integer, TextInputEditText> editTextClosingTime = new HashMap<>();


    OpeningDateTimeExpandableListAdapter(Context context, List<OpeningDay> expandableListDetail, OpeningDateTimeFragment fragment){
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.fragment = fragment;
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
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<OpeningTime> values = new ArrayList<>(expandableListDetail.get(listPosition).getOpeningTimes());

        if(isLastChild){
            convertView = layoutInflater.inflate(R.layout.list_last_item_opening_time, null);
            editTextOpeningTimeContainer.put(listPosition, convertView.findViewById(R.id.editTextOpeningTimeContainer));
            editTextOpeningTime.put(listPosition, convertView.findViewById(R.id.editTextOpeningTime));
            editTextClosingTimeContainer.put(listPosition, convertView.findViewById(R.id.editTextClosingTimeContainer));
            editTextClosingTime.put(listPosition, convertView.findViewById(R.id.editTextClosingTime));

            int mHourOfDay = Calendar.HOUR_OF_DAY;
            int mMinute = Calendar.MINUTE;
            editTextOpeningTime.get(listPosition).setOnClickListener(v -> {
                TimePickerDialog timePickerDialogOpeningTime = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
                    openingTime = LocalTime.of(hourOfDay, minute);
                    editTextOpeningTime.get(listPosition).setText(openingTime.toString());
                }, mHourOfDay, mMinute, true);

                timePickerDialogOpeningTime.setTitle(R.string.title_opening_time_selection);
                timePickerDialogOpeningTime.show();
            });
            editTextClosingTime.get(listPosition).setOnClickListener(v -> {
                TimePickerDialog timePickerDialogClosingTime = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
                    closingTime = LocalTime.of(hourOfDay, minute);
                    editTextClosingTime.get(listPosition).setText(closingTime.toString());
                }, mHourOfDay, mMinute, true);
                timePickerDialogClosingTime.setTitle(R.string.title_closing_time_selection);
                timePickerDialogClosingTime.show();
            });

            MaterialButton btnAdd = convertView.findViewById(R.id.btnAddOpeningTime);
            btnAdd.setOnClickListener(v -> {

                boolean flag = true;

                if(editTextOpeningTime.get(listPosition).getText().toString().equals("")){
                    flag = false;
                    editTextOpeningTimeContainer.get(listPosition).setError(context.getString(R.string.error_opening_time));
                }
                else{
                    editTextOpeningTimeContainer.get(listPosition).setError(null);
                }

                if(editTextClosingTime.get(listPosition).getText().toString().equals("")){
                    flag = false;
                    editTextClosingTimeContainer.get(listPosition).setError(context.getString(R.string.error_closing_time));
                }
                else{
                    editTextClosingTimeContainer.get(listPosition).setError(null);
                }

                if(flag){
                    if(!openingTime.equals(closingTime)){
                        editTextOpeningTimeContainer.get(listPosition).setError(null);
                        editTextClosingTimeContainer.get(listPosition).setError(null);
                        fragment.createOpeningTime(listPosition, new OpeningTime(openingTime, closingTime));
                    }
                    else{
                        editTextOpeningTimeContainer.get(listPosition).setError(context.getString(R.string.error_time_equal));
                        editTextClosingTimeContainer.get(listPosition).setError(context.getString(R.string.error_time_equal));
                    }

                }

            });
        }
        else {
            convertView = layoutInflater.inflate(R.layout.list_item_opening_time, null);
            TextView textViewOpeningTime = convertView.findViewById(R.id.textViewOpeningTime);
            textViewOpeningTime.setText(values.get(expandedListPosition).toString());

            Button btnDeleteItem = convertView.findViewById(R.id.btnDeleteOpeningTime);
            btnDeleteItem.setOnClickListener(v -> {
                fragment.deleteOpeningTime(listPosition, expandedListPosition);
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
        convertView.setTag(group);

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

}

