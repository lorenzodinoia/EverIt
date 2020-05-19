package it.uniba.di.sms1920.everit.restaurateur.activities;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<String>> expandableListDetail;
    //TODO passa da String a Product


    CustomExpandableListAdapter(Context context, List<String> expandableListTitle, LinkedHashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(isLastChild){
            convertView = layoutInflater.inflate(R.layout.list_last_item, null);
            ImageButton btnAdd = convertView.findViewById(R.id.btnAddItem);
            btnAdd.setOnClickListener(v -> {

                AlertDialog.Builder dialogNewItem = new AlertDialog.Builder(context);
                dialogNewItem.setTitle("NEW ITEM NAME");
                dialogNewItem.setMessage("Enter new name");

                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                dialogNewItem.setView(input);

                dialogNewItem.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                    List<String> newItem = getValueOfPair(listPosition);
                    newItem.remove(""); //TODO mado che porcata
                    newItem.add(input.getText().toString());
                    newItem.add("");

                    expandableListDetail.put(getValueOfKey(listPosition), newItem);

                    updateAdapter(expandableListTitle, expandableListDetail);
                });

                dialogNewItem.setNegativeButton(android.R.string.no, null);

                dialogNewItem.show();
            });
            TextView textHint = convertView.findViewById(R.id.textHint);
        }
        else {

            convertView = layoutInflater.inflate(R.layout.list_item, null);

            TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
            expandedListTextView.setText(expandedListText);

            ImageButton btnModItem = convertView.findViewById(R.id.btnModItem);
            btnModItem.setOnClickListener(v -> {
                AlertDialog.Builder dialogModName = new AlertDialog.Builder(context);
                dialogModName.setTitle("MOD NAME");
                dialogModName.setMessage("Enter new name");

                final EditText input = new EditText(context);
                input.setHint(expandedListTextView.getText().toString());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                dialogModName.setView(input);

                dialogModName.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                });

                dialogModName.show();

            });

            ImageButton btnDelItem = convertView.findViewById(R.id.btnDelItem);
            btnDelItem.setOnClickListener(v -> {

                AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(context);
                dialogConfirm.setTitle("DELETE ITEM");
                dialogConfirm.setMessage("You are going to delete " + expandedListText + ". Are you sure ?");

                dialogConfirm.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                    Log.d("test", expandableListDetail.toString());

                    List<String> newItemList = getValueOfPair(listPosition);
                    newItemList.remove(expandedListPosition);

                    expandableListDetail.put(getValueOfKey(listPosition), newItemList);

                    Log.d("test", expandableListDetail.toString());

                    updateAdapter(expandableListTitle, expandableListDetail);
                });

                dialogConfirm.setNegativeButton(android.R.string.no, null);

                dialogConfirm.show();

            });

        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_group, null);

        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setFocusable(false);

        ImageButton btnModGroup = convertView.findViewById(R.id.btnModGroup);
        btnModGroup.setFocusable(false);
        btnModGroup.setOnClickListener(v -> {
            AlertDialog.Builder dialogNewName = new AlertDialog.Builder(context);
            dialogNewName.setTitle("MODIFY GROUP NAME");
            dialogNewName.setMessage("Enter new name");

            final EditText input = new EditText(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            dialogNewName.setView(input);

            dialogNewName.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                expandableListDetail.put(input.getText().toString(), expandableListDetail.remove(getValueOfKey(listPosition)));
                expandableListTitle.set(listPosition, input.getText().toString());
                updateAdapter(expandableListTitle, expandableListDetail);
            });

            dialogNewName.setNegativeButton(android.R.string.no, null);

            dialogNewName.show();
        });

        ImageButton btnDelGroup = convertView.findViewById(R.id.btnDelGroup);
        btnDelGroup.setFocusable(false);
        btnDelGroup.setOnClickListener(v -> {
            AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(context);
            dialogConfirm.setTitle("DELETE GROUP");
            dialogConfirm.setMessage("You are going to delete " + listTitle + ". Are you sure ?");

            dialogConfirm.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                Log.d("test", expandableListDetail.toString());

                expandableListDetail.remove(getValueOfKey(listPosition));
                expandableListTitle.remove(listPosition);
                updateAdapter(expandableListTitle, expandableListDetail);

                Log.d("test", expandableListDetail.toString());
            });

            dialogConfirm.setNegativeButton(android.R.string.no, null);

            dialogConfirm.show();
        });


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


    public void updateAdapter(List<String> expandableListTitle, LinkedHashMap<String, List<String>> expandableListDetail){
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        notifyDataSetChanged();
    }


    private String getValueOfKey(int indexParent){
        String key;
        key = expandableListTitle.get(indexParent);

        return key;
    }

    private List<String> getValueOfPair(int indexParent){
        List<String> values;
        values = expandableListDetail.get(getValueOfKey(indexParent));

        return values;
    }


}
