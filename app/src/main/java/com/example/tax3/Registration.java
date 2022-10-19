package com.example.tax3;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Registration extends AppCompatActivity {

    private static final String TAG = "Response";


    static TextView login, password, name, phone;
    Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        login = (TextView) findViewById(R.id.text_login_reg);
        password = (TextView) findViewById(R.id.text_password_reg);
        name = (TextView) findViewById(R.id.text_name_reg);
        phone = (TextView) findViewById(R.id.text_phone_reg);

        registration = (Button) findViewById(R.id.start_reg);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postRegistration();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    // метод для POST запроса регистрации нового пользователя
    public void postRegistration() throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://taxi-kostrovo.h1n.ru/registration.php";

        OkHttpClient client = new OkHttpClient();

        // json который передается на сервер
        JSONObject postdata;

        postdata = creatJSON();

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();

                Log.w("failure Response", mMessage);

                String error = "Ошибка подключения к серверу! Проверьте соединение с сетью Интернет";

                // вспылывающее уведомление на экране (выполняется только в потоке пользовательского интерфейса)
                runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // сообщение
                String mMessage = response.body().string();
                // вывод сообщения в терминале
                Log.e(TAG, mMessage);
                // вспылывающее уведомление на экране (выполняется только в потоке пользовательского интерфейса)
                runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast = Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                MainActivity.login = login.getText().toString();
                finish();
            }
        });
    }

    public static JSONObject creatJSON() {

        JSONObject postdata = new JSONObject();

        try {
            postdata.put("login", login.getText().toString());
            postdata.put("password", password.getText().toString());
            postdata.put("name", name.getText().toString());
            postdata.put("phone", phone.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return postdata;
    }
}