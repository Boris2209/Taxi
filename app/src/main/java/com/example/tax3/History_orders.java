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
import android.text.Layout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;
import org.json.JSONObject;


import java.io.IOException;

public class History_orders extends AppCompatActivity {

    private static final String TAG = "Response";

    TextView text_history;

    JSONObject postdata = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_orders);

        text_history = (TextView) findViewById(R.id.text_history);


        try {
            postHistory();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void postHistory() throws IOException {

        //System.out.println("Start search history");

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://taxi-kostrovo.h1n.ru/getorders.php";

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(MEDIA_TYPE, MainActivity.login);

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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // сообщение
                String mMessage = response.body().string();
                // вывод сообщения в терминале
                Log.e(TAG, mMessage);
                // получаем json
                JSONObject history_orders_json = string_to_json(mMessage);
                // отпрака на печать
                try {
                    history_print(history_orders_json);
                } catch (JSONException e) {
                    System.out.println("Ошибка в result");
                    e.printStackTrace();
                }
            }
        });
    }

    private JSONObject string_to_json(String json_string){
        JSONObject json;
        try {
            json = new JSONObject(json_string);

        } catch (JSONException e) {
            e.printStackTrace();
            json = new JSONObject();
        }
        return json;
    }

    private void history_print(JSONObject json) throws JSONException {

        if (json.length() < 1)
            return;

        String result = "";

        for (int i = 0; i < json.length(); i++) {
            String is = Integer.toString(i);
            result += ("Откуда: " + json.getJSONObject(is).getString("addressA") + "\n");
            result += ("Куда: " + json.getJSONObject(is).getString("addressB") + "\n");
            result += ("Когда: " + json.getJSONObject(is).getString("dateTime") + "\n");
            result += (chek_child(json.getJSONObject(is).getString("child")) + "\n");
            result += ("Телефон для связи: " + json.getJSONObject(is).getString("phone") + "\n");
            result += ("Дополнительно: " + json.getJSONObject(is).getString("editMore") + "\n" + "\n");
        }

        String finalResult = result;
        runOnUiThread(new Runnable() {
            public void run() {
                text_history.setText(finalResult);
            }
        });
    }

    private String chek_child(String child){
        if (child.equals("true"))
            return "С детским креслом";
        return "Без детского кресла";
    }

}