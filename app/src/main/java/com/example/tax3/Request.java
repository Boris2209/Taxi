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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Request extends AppCompatActivity {

    private static final String TAG = "Response";

    static EditText editA, editB, editDateTime, editPhone, editMore;
    static CheckBox child;
    Button start_button, history_button;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // initialization object
        editA = (EditText) findViewById(R.id.addressA);
        editB = (EditText) findViewById(R.id.addressB);
        editDateTime = (EditText) findViewById(R.id.text_datetime);
        editPhone = (EditText) findViewById(R.id.editNumberPhone);
        child = (CheckBox) findViewById(R.id.check_child);
        editMore = (EditText) findViewById(R.id.text_more);
        start_button = (Button) findViewById(R.id.button_start_request);
        name = (TextView) findViewById(R.id.text_name);
        history_button = (Button) findViewById(R.id.button_history);

        name.setText("Здравствуйте, " + MainActivity.login + "!");

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postRequest();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Request.this, History_orders.class);
                startActivity(intent);
            }
        });
    }


    // метод для POST запроса создания нового заказа
    public void postRequest() throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://taxi-kostrovo.h1n.ru/newrequest.php";

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
                // вспылывающее уведомление на экране (выполняется только в потоке пользовательского интерфейса)
                runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast = Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                clear_form();

            }
        });

    }

    // собираем тут JSON для отправки нового заказа
    public static JSONObject creatJSON() {

        JSONObject postdata = new JSONObject();

        try {
            postdata.put("login", MainActivity.login);
            postdata.put("addressA", editA.getText().toString());
            postdata.put("addressB", editB.getText().toString());
            postdata.put("dateTime", editDateTime.getText().toString());
            postdata.put("child", childCheck());
            postdata.put("phone", editPhone.getText().toString());
            postdata.put("editMore", editMore.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return postdata;
    }


    // состояние чек бокса в строке
    private static String childCheck(){
        if (child.isChecked()){
            return "true";
        }
        return "false";
    }

    // очистка формы после успешного добавления заказа
    private void clear_form(){
        runOnUiThread(new Runnable() {
            public void run() {
                editA.setText("");
                editB.setText("");
                editDateTime.setText("");
                child.setChecked(false);
                editPhone.setText("");
                editMore.setText("");
            }
        });
    }

}