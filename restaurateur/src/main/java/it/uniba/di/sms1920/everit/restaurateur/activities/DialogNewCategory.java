package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class DialogNewCategory extends DialogFragment {

    private EditText editTextNameCat;
    private Button btnConfirm;
    private ImageButton btnDismiss;
    private DialogNewCategoryListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_category, null);

        builder.setView(view);

        builder.setView(view).setNegativeButton("Close", (dialog, which) -> {

        }).setPositiveButton("Confirm", (dialog, which) -> {
                String newCatName = editTextNameCat.getText().toString();
                listener.getNewName(newCatName);
        });

        editTextNameCat = view.findViewById(R.id.editTextCatName);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogNewCategoryListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DialogNewCategoryListener!");
        }
    }

    public interface DialogNewCategoryListener{
        void getNewName(String name);
    }
}
