package it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.LoginActivity;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AccountDetailFragment extends Fragment {

    private LinearLayout linearLayoutAccountInfo;
    private LinearLayout linearLayoutChangePassword;
    private TextView textViewShopName;
    private TextView textViewEmail;
    private Button buttonDeleteAccount;

    private ImageView imageProfile;
    private String imagePath;

    private AccountDetailActivity mParent;
    private Restaurateur restaurateur;

    private static int PICK_IMAGE = 1;

    public AccountDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);

        //TODO ridimensionare imageView
        imageProfile = view.findViewById(R.id.imageViewProfile);
        imageProfile.setOnClickListener(v -> {
            fetchImageFromGallery(view);
        });

        if(restaurateur.getImagePath() != null){
            String url = String.format("%s/images/%s", Constants.SERVER_HOST, restaurateur.getImagePath());
            Picasso.get().
                    load(url)
                    .placeholder(R.drawable.ic_baseline_add_a_photo_36)
                    .transform(new CropCircleTransformation())
                    .fit()
                    .into(imageProfile);
        }

        textViewShopName = view.findViewById(R.id.textViewShopNameProfile);
        textViewEmail = view.findViewById(R.id.textViewEmailProfile);
        textViewShopName.setText(restaurateur.getShopName());
        textViewShopName.setOnClickListener(v -> {
            Dialog dialogNewShopName = new Dialog(mParent);
            dialogNewShopName.setContentView(R.layout.dialog_new_shop_name);
            dialogNewShopName.setTitle(R.string.new_shop_name);

            TextInputLayout newNameContainer = dialogNewShopName.findViewById(R.id.editTextNewShopNameContainer);
            TextInputEditText newName = dialogNewShopName.findViewById(R.id.editTextNewShopName);
            MaterialButton confirm = dialogNewShopName.findViewById(R.id.BtnConfirm);
            MaterialButton cancel = dialogNewShopName.findViewById(R.id.BtnCancel);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shopName = newName.getText().toString();
                    if(!shopName.equals("") || Utility.isShopNameValid(shopName, newNameContainer, mParent)){
                        restaurateur.setShopName(shopName);
                        RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                        restaurateurRequest.setShopName(restaurateur, new RequestListener<Restaurateur>() {
                            @Override
                            public void successResponse(Restaurateur response) {
                                dialogNewShopName.dismiss();
                                textViewShopName.setText(response.getShopName());
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                Log.d("test", error.toString());
                                //TODO gestire error response
                            }
                        });
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogNewShopName.dismiss();
                }
            });

            dialogNewShopName.show();
        });

        textViewEmail.setText(restaurateur.getEmail());
        textViewEmail.setOnClickListener(v -> {
            Dialog dialogNewEmail = new Dialog(mParent);
            dialogNewEmail.setContentView(R.layout.dialog_new_email);
            dialogNewEmail.setTitle(R.string.new_email);

            TextInputLayout newEmailContainer = dialogNewEmail.findViewById(R.id.editTextNewEmailContainer);
            TextInputEditText newEmail = dialogNewEmail.findViewById(R.id.editTextNewEmail);
            MaterialButton confirm = dialogNewEmail.findViewById(R.id.BtnConfirm);
            MaterialButton cancel = dialogNewEmail.findViewById(R.id.BtnCancel);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = newEmail.getText().toString();
                    if(!email.equals("") || Utility.isEmailValid(email)){
                        restaurateur.setEmail(email);
                        RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                        restaurateurRequest.setEmail(restaurateur, new RequestListener<Restaurateur>() {
                            @Override
                            public void successResponse(Restaurateur response) {
                                dialogNewEmail.dismiss();
                                textViewEmail.setText(response.getEmail());
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                Log.d("test", error.toString());
                                //TODO gestire error response
                            }
                        });
                    }
                    else{
                        newEmailContainer.setError(getString(R.string.error_email));
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogNewEmail.dismiss();
                }
            });

            dialogNewEmail.show();
        });

        linearLayoutAccountInfo = view.findViewById(R.id.linearLayoutAccountInfo);
        linearLayoutAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentManager fragmentManager = mParent.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSettings, profileFragment).addToBackStack(null).commit();
            }
        });
        linearLayoutChangePassword = view.findViewById(R.id.linearLayoutChangePassword);
        linearLayoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                FragmentManager fragmentManager = mParent.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSettings, changePasswordFragment).addToBackStack(null).commit();
            }
        });

        buttonDeleteAccount = view.findViewById(R.id.buttonDeleteAccount);
        buttonDeleteAccount.setOnClickListener(v -> {
            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
            restaurateurRequest.delete(restaurateur.getId(), new RequestListener<Boolean>() {
                @Override
                public void successResponse(Boolean response) {
                    //TODO aggiungere messaggio di feedback
                    Intent intent = new Intent(mParent, LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire error response
                }
            });
        });
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
            restaurateur = mParent.getRestaurateur();
        }
    }

    void fetchImageFromGallery(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            //TODO aggiungere controllo immagine solo per jpg
            Uri selectedImageURI = data.getData();
            imagePath = selectedImageURI.getPath();

            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
            restaurateurRequest.saveImage(selectedImageURI, mParent, new RequestListener<String>() {
                    @Override
                    public void successResponse(String response) {
                        Picasso.get().
                                load(selectedImageURI)
                                .placeholder(R.mipmap.icon)
                                .transform(new CropCircleTransformation())
                                .fit()
                                .into(imageProfile);
                    }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire errore
                }
            });

        }
    }
}