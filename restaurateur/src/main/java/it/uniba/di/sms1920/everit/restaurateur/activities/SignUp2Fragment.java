package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.restaurateur.R;


public class SignUp2Fragment extends Fragment {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextDescription;
    private ImageButton imgButtonProfileImg;
    private MaterialButton btnContinue;

    private static int SELECT_PICTURE = 1;

    public SignUp2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up2, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){
        editTextEmail = viewRoot.findViewById(R.id.editTextMail);
        editTextPassword = viewRoot.findViewById(R.id.editTextPassword);
        editTextDescription = viewRoot.findViewById(R.id.editTextDescription);
        imgButtonProfileImg = viewRoot.findViewById(R.id.imageButton);

        btnContinue = viewRoot.findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String descr = editTextDescription.getText().toString();
        });
    }

    void fetchImageFromGallery(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            if(requestCode == SELECT_PICTURE){
                Uri selectedImageURI = data.getData();
            }
        }
    }
}
