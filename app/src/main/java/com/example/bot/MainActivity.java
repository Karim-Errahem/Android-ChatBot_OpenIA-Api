package com.example.bot;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    ImageButton retour;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des éléments de l'interface utilisateur
        recyclerView = findViewById(R.id.recycler_view);
        welcomTextView = findViewById(R.id.welcom_text);
        sendButton = findViewById(R.id.send_btn);
        messageEditText = findViewById(R.id.message_edit_text);
        retour = findViewById(R.id.btnre);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        retour.setOnClickListener(this::returntoselection);

        // Action lorsque le bouton d'envoi est pressé
        sendButton.setOnClickListener(view -> {
            String question = messageEditText.getText().toString().trim();
            Toast.makeText(this, question, Toast.LENGTH_LONG).show();
            addtochat(question, Message.send_by_me);
            messageEditText.setText("");
            callAPI(question);
            welcomTextView.setVisibility(View.GONE);
        });
    }

    // Ajouter une réponse dans le chat
    void addResponse(String response) {
        addtochat(response, Message.send_by_bot);
    }

    // Ajouter un message dans le chat (envoyé ou reçu)
    void addtochat(String message, String sendby) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sendby));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }
    void returntoselection(View v) {
        Intent i = new Intent(MainActivity.this, selection.class);
        startActivity(i);
    }
    void callAPI(String question){
        //okhttp
        messageList.add(new Message("Typing... ",Message.send_by_bot));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",question);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    addResponse("Failed to load response due to "+response.code());
                }
            }
        });



}}
