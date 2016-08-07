package com.example.lvd.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lvd on 19/07/2016.
 */
public class LoginActivity extends Activity {
    private EditText password;
    private Button btnSubmit;
    private static int count = 0;
    private final static String PASSWORD = "nguoiyeuha";
    private final static String PASSWORD_01 = "hjiu";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        password = (EditText) findViewById(R.id.txtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PASSWORD.equalsIgnoreCase(password.getText().toString()) &&
                        !PASSWORD_01.equalsIgnoreCase(password.getText().toString())){
                    count++;
                    if (count < 3){
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("You do not permission to see the \"Sweet Memories^^\"")
                                .setMessage("Please contact @haha or @hjhj for more information")
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        password.setText("");
                                    }
                                }).create().show();
                    } else{
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Goodbye!")
                                .setMessage("You tried and failed the maximum of permitted times!")
                                .setCancelable(false)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        count = 0;
                                        finish();
                                    }
                                }).create().show();
                    }
                }else{
                    count = 0;
                    password.setText("");
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
