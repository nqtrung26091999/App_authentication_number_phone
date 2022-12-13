package com.example.myproject.fragment;


import static com.example.myproject.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myproject.MainActivity;
import com.example.myproject.R;

import com.example.myproject.SignInActivity;
import com.example.myproject.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;



public class MyProfileFragment extends Fragment{
    private View mView;
    private ImageView imgAvatarProfile;
    private EditText edtFullName, edtEmailProfile;
    private Button btnUpdateProfile, btnUpdateEmail, btnVerify;
    private Uri mUri;
    private MainActivity mMainActivity;
    private TextView tvEmailNotVerify;
    final private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        Init();
        mMainActivity = (MainActivity) getActivity();
        setUserInformation();
        Act();
        return mView;
    }


    private void onClickRequestPermission() {

        if (mMainActivity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mMainActivity.openGallery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mMainActivity.openGallery();
        } else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }


    private void setUserInformation() {

        FirebaseUser user = mAuth.getInstance().getCurrentUser();

        if (!user.isEmailVerified()) {
            tvEmailNotVerify.setVisibility(View.VISIBLE);
            btnVerify.setVisibility(View.VISIBLE);

            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(),"Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Verification Email has not been sent.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            tvEmailNotVerify.setVisibility(View.GONE);
            btnVerify.setVisibility(View.GONE);
        }

        if (user == null) {
            return;
        }

        edtFullName.setText(user.getDisplayName());
        edtEmailProfile.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.useravatardefaul).into(imgAvatarProfile);
    }

    private void Init() {
        btnVerify = mView.findViewById(R.id.btn_verify);
        tvEmailNotVerify = mView.findViewById(R.id.tv_email_not_verify);
        edtFullName = mView.findViewById(R.id.edt_fullname);
        edtEmailProfile = mView.findViewById(R.id.edt_email_profile);
        imgAvatarProfile = mView.findViewById(R.id.image_avatar_profile);
        btnUpdateProfile = mView.findViewById(R.id.btn_update_profile);
        btnUpdateEmail = mView.findViewById(R.id.btn_update_email);

    }
    private void Act() {
        imgAvatarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateEmail();
            }
        });
    }



    private void onClickUpdateProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return;
        }
        String strFullName = edtFullName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Update profile success", Toast.LENGTH_SHORT).show();
                            mMainActivity.showUserInformation();
                        }
                    }
                });
    }

    private void onClickUpdateEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String strNewEmail = edtEmailProfile.getText().toString().trim();

        user.updateEmail(strNewEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User email address updated",Toast.LENGTH_SHORT).show();
                            mMainActivity.showUserInformation();
                        }
                    }
                });
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        imgAvatarProfile.setImageBitmap(bitmapImageView);
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }
}
