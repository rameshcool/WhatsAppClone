package com.rameshcodeworks.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnsignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                event.getAction()== KeyEvent.ACTION_DOWN) {

                    onClick(btnsignUp);
                }
                return false;
            }
        });

        edtUsername = findViewById(R.id.edtUsername);
        btnsignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        btnsignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser().logOut();

            transitionToSocialMediaActivity();
        }
    }

    private void transitionToSocialMediaActivity() {

        Intent intent = new Intent(MainActivity.this, WhatsAppUsersActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSignUp:

                if (edtEmail.getText().toString().equals("") ||
                edtUsername.getText().toString().equals("") ||
                edtPassword.getText().toString().equals("")) {

                    FancyToast.makeText(MainActivity.this, "Email, Username, Password is required!!!",
                            Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

                } else {

                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up " + edtUsername.getText().toString());
                    progressDialog.show();
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                FancyToast.makeText(MainActivity.this, appUser.getUsername() + " is signed up",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                                transitionToSocialMediaActivity();

                            } else {

                                FancyToast.makeText(MainActivity.this, "There was an error: " + e.getMessage(),
                                        Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                            }

                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnLogin:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void rootLayoutTapped(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
