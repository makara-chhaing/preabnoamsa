package com.example.bakseynoamsa.Activities.LoginAndRegistry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakseynoamsa.Activities.Messaging.LogChatActivity;
import com.example.bakseynoamsa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "0000" ;
    EditText etLogEmail, etLogPassword;
    TextView tvSignUp;
    Button btnLogin;
    String emailLogin;
    String passwordLogin;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
        btnLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

        callOutSharedPref();

    }
    public void callOutSharedPref(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedEditor = sharedPreferences.edit();
        sharedEditor.apply();

    }

    public void init(){
        etLogEmail = findViewById(R.id.etLoginEmail);
        etLogPassword = findViewById(R.id.etLoginPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSignUp:
                Intent toReg = new Intent(LoginActivity.this, RegistryActivity.class);
                startActivity(toReg);
                break;

            case R.id.btnLogin:
                emailLogin = etLogEmail.getText().toString();
                passwordLogin = etLogPassword.getText().toString();
                Log.d(TAG, emailLogin);
                Log.d(TAG, passwordLogin);

                if(emailLogin.isEmpty() || passwordLogin.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Empty Email/Password.Please fill the text in!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin, passwordLogin)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //updateUI(user);
                                Intent intent = new Intent(getApplicationContext(), LogChatActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        });
                break;

        }

    }
}
