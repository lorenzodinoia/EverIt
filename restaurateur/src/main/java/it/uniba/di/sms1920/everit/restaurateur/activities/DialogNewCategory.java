package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class DialogNewCategory extends DialogFragment {

    private EditText editTextNameCat;
    private Button btnConfirm;
    private Button btnCancel;

    private DialogNewCategoryListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_category, null);

        builder.setView(view);

        editTextNameCat = view.findViewById(R.id.editTextCatName);
        btnConfirm = view.findViewById(R.id.BtnConfirm);
        btnConfirm.setOnClickListener(v -> {
            String newCatName = editTextNameCat.getText().toString();
            if(newCatName.equals("")){
                Toast.makeText(getContext(), "EMPTY FIELD", Toast.LENGTH_LONG).show();
            }else{
                listener.getNewCatName(newCatName);
                this.dismiss();
            }

        });
        btnCancel = view.findViewById(R.id.BtnCancel);
        btnCancel.setOnClickListener(v -> {
            this.dismiss();
        });

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
        void getNewCatName(String name);
    }
}
