package com.jf2mc1.a015004yuberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    enum State {
        SIGNUP, LOGIN
    }

    private EditText amEtUsername;
    private EditText amEtPassword;
    private EditText amEtPassengerDriver;
    String signUpAs = "";
    private RadioGroup radioGroupSignUp, radioGroupOneTime;
    private Button amBtnSignup, amBtnOnetimelogin;
    private State state;


    private AutoCompleteTextView autoCompletePassengerDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser.getCurrentUser().logOut();

        amEtUsername = findViewById(R.id.am_et_username);
        amEtPassword = findViewById(R.id.am_et_password);

        radioGroupSignUp = findViewById(R.id.am_radiogroup_signup);


        amBtnSignup = findViewById(R.id.am_btn_signup);
        amBtnOnetimelogin = findViewById(R.id.am_btn_onetimelogin);

        amBtnSignup.setOnClickListener(this::onClick);
        amBtnOnetimelogin.setOnClickListener(this::onClick);
        state = State.SIGNUP;


    }


    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.am_btn_signup) {
            // if the text o
            if (state == State.SIGNUP) {
                signUpUser();
            } else {
                loginUser();
            }

        } else if (view.getId() == R.id.am_btn_onetimelogin) {
            oneTimeLogin();

        } else {
            return;
        }

    }

    private void loginUser() {
        String username = amEtUsername.getText().toString();
        String password = amEtPassword.getText().toString();

        String toastMessage = someInputsMissing(username, password);
        if (!toastMessage.equals("")) {
            FancyToast.makeText(MainActivity.this,
                    toastMessage + " required :(",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR,
                    true).show();

        } else {

            ParseUser.logInInBackground(username, password,
                    new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                if (e == null) {
                                    FancyToast.makeText(MainActivity.this,
                                            user.getUsername() + " is logged in :)",
                                            FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                    // goToUsersActivity();

                                } else {
                                    FancyToast.makeText(MainActivity.this,
                                            "Exception :(" + e.getMessage(),
                                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }

                            } else {
                                FancyToast.makeText(MainActivity.this,
                                        "User not recognised :(" + e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    });
        }

    }

    private void oneTimeLogin() {


    }

    private void signUpUser() {

        String username = amEtUsername.getText().toString();
        String password = amEtPassword.getText().toString();

        String toastMessage = someInputsMissing(username, password);
        if (!toastMessage.equals("")) {
            FancyToast.makeText(MainActivity.this,
                    toastMessage + " required :(",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR,
                    true).show();

        } else {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Working...");
            progressDialog.show();

            ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.put("as", signUpAs);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(MainActivity.this,
                                newUser.getUsername() + " Signed up :)",
                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                true).show();

                        //goToUsersActivity();

                    } else {
                        FancyToast.makeText(MainActivity.this,
                                ":(\n" + e.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR,
                                true).show();
                        Log.e("SIGNUPERROR", e.getMessage());
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void logoutUser() {
        FancyToast.makeText(MainActivity.this,
                ParseUser.getCurrentUser().getUsername() +
                        " thanks, bye :)",
                FancyToast.LENGTH_LONG, FancyToast.DEFAULT, true).show();

        if (ParseUser.getCurrentUser().getUsername() != null) {

            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        MainActivity.this.finishAffinity();
                    }
                }
            });
        }

    }



    private String someInputsMissing(String username, String password) {
        String toastMessage = "";

        if (username.equals(""))
            toastMessage += "username, ";
        if (password.equals(""))
            toastMessage += " password, ";

        if (radioGroupSignUp.getCheckedRadioButtonId() == R.id.am_radiobutton_pass_signup) {
            signUpAs = "Passenger";
        } else if (radioGroupSignUp.getCheckedRadioButtonId() == R.id.am_radiobutton_driver_signup) {
            signUpAs = "Driver";
        } else {
            toastMessage += " passenger or driver";
        }
        return toastMessage;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_login_user:
                if (state == State.LOGIN) {
                    state = State.SIGNUP;
                    amBtnSignup.setText("sign up");
                    item.setTitle("Login");
                } else {
                    state = State.LOGIN;
                    amBtnSignup.setText("login");
                    item.setTitle("Sign up");
                }
                return true;

            case R.id.menu_item_logout:
                logoutUser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }



}