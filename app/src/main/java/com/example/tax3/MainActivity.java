package com.example.tax3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button button_aut, button_reg, button_inform, button_number;
    public static String login="root";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_aut = (Button) findViewById(R.id.main_button_authorization);
        button_reg = (Button) findViewById(R.id.main_button_registration);
        button_inform = (Button) findViewById(R.id.button_info);
        button_number = (Button) findViewById(R.id.button_number);

        //переход в телефон для вызова номера
        button_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "tel:+79164001500";
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
                startActivity(callIntent);
            }
        });

        // авторизация
        button_aut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Authorization.class);
                startActivity(intent);
            }
        });

        // регистрация
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        //о программе
        button_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Inform.class);
                startActivity(intent);
            }
        });
    }


}