package edu.uco.weddingcrashers.hitched;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends Activity {

    private Button loginbutton,testbutton;
    private EditText user,password;
    private TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_login);
        user = (EditText)findViewById(R.id.userID);
        password = (EditText)findViewById(R.id.password);
        loginbutton = (Button)findViewById(R.id.loginbutton);
        signUpTextView = (TextView)findViewById(R.id.signupButton);
        testbutton = (Button)findViewById(R.id.testbutton);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Login.this, SignUpActivity.class);
                startActivity(intent3);
            }
        });
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setText("hitched");
                password.setText("pass");
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//hey
                        String useraddress = user.getText().toString();
                        String pass = password.getText().toString();

                        useraddress = useraddress.trim();
                        pass= pass.trim();

                        if (useraddress.isEmpty() || pass.isEmpty()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setMessage(R.string.login_error_message)
                                    .setTitle(R.string.login_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else {
                            setProgressBarIndeterminateVisibility(true);

                            ParseUser.logInInBackground(useraddress, pass, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    setProgressBarIndeterminateVisibility(false);

                                    if (e == null) {
                                        // Success!
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        // Fail
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                        builder.setMessage(e.getMessage())
                                                .setTitle(R.string.login_error_title)
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });

                        }

                    }
                });


    }

}