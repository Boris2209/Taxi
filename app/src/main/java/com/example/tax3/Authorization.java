package com.example.tax3;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Authorization extends AppCompatActivity {

    private static final String TAG = "Response";

    static TextView login, password;
    Button start_aut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        login = (TextView) findViewById(R.id.login_aut);
        password = (TextView) findViewById(R.id.password_aut);

        start_aut = (Button) findViewById(R.id.aut_start_button);

        start_aut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    postAuthorization();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void postAuthorization() throws IOException{
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://taxi-kostrovo.h1n.ru/authorization.php";

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
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // сообщение
                String mMessage = response.body().string();
                // вывод сообщения в терминале
                Log.e(TAG, mMessage);

                if (mMessage.equals("true")){
                    // авторизация логином
                    MainActivity.login = login.getText().toString();
                    // сообщение на экран
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(getApplicationContext(), "Успешная авторизация!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    // переход в меню добвления заказа
                    Intent intent = new Intent(Authorization.this, Request.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(getApplicationContext(), "Неверный логин и/или пароль", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }

            }
        });
    }

    public static JSONObject creatJSON() {

        JSONObject postdata = new JSONObject();

        try {
            postdata.put("login", login.getText().toString());
            postdata.put("password", password.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return postdata;
    }
}