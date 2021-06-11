package com.example.bakseynoamsa.Activities.LoginAndRegistry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "debugingpns";
    private static final int PICK_IMAGE = 1;
    EditText etRegUser, etRegPassword, etRegEmail;
    TextView tvAlready;
    Button btnRegister, btn_select_photo;
    String userReg;
    String emailReg;
    String passwordReg;
    Bitmap bitmap;
    Uri selectPhotoUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        init();
        btnRegister.setOnClickListener(this);
        tvAlready.setOnClickListener(this);
        btn_select_photo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d(TAG, "Photo was selected");

                    selectPhotoUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectPhotoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    CircleImageView civ_profile_image = findViewById(R.id.civ_profile_image);
                    civ_profile_image.setImageBitmap(bitmap);
                    btn_select_photo.setAlpha(0f);
                    btn_select_photo.setText("");
                }
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init() {
        etRegUser = findViewById(R.id.etRegUser);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegEmail = findViewById(R.id.etRegEmail);
        tvAlready = findViewById(R.id.tvAlready);
        btnRegister = findViewById(R.id.btn_Register);
        btn_select_photo = findViewById(R.id.btn_select_photo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                userReg = etRegUser.getText().toString();
                emailReg = etRegEmail.getText().toString();
                passwordReg = etRegPassword.getText().toString();

                if(emailReg.isEmpty() || passwordReg.isEmpty()){
                    Toast.makeText(RegistryActivity.this, "Empty Email/Password! Please fill the text in!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailReg, passwordReg)
                        .addOnCompleteListener(RegistryActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                Toast.makeText(RegistryActivity.this, "Registered Successfully.",
                                        Toast.LENGTH_SHORT).show();
                                uploadPhotoToFirebaseStroage();
                                finish();
                            } else {
                                Toast.makeText(RegistryActivity.this, "Authentication failed." + task.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }); break;

            case R.id.tvAlready:
                Intent intent = new Intent(RegistryActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_select_photo:
                Toast.makeText(this, "Clicked on select photo", Toast.LENGTH_SHORT).show();
                if(isStoragePermissionGranted()) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE);

                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }


    }

    private void uploadPhotoToFirebaseStroage() {
        if(selectPhotoUri == null) return;

        String fileName = UUID.randomUUID().toString();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("/images/" + fileName);
        storageReference.putFile(selectPhotoUri)
                .addOnSuccessListener(taskSnapshot -> {Log.d(TAG, "upload successfully");
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Log.d(TAG, "File location " + uri.toString());
                            saveUserToFirebaseDatabase(uri.toString());
                        });
                })
                .addOnFailureListener(e -> {

                });

    }

    private void saveUserToFirebaseDatabase(String profileImageUrl) {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + uid);
        Users users = new Users(uid, userReg,profileImageUrl );

        databaseReference.setValue(users)
                .addOnSuccessListener( aVoid ->{
                        Log.d(TAG, "Saved user to database stroage");
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Permission is granted");
                return true;
            } else {

                Log.d(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}