package my.com.iwebs.iwebspush;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import my.com.iwebs.iwebspush.classes.RequestHandler;
import my.com.iwebs.iwebspush.classes.SharedPrefManager;
import my.com.iwebs.iwebspush.classes.URLs;
import my.com.iwebs.iwebspush.classes.User;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button getStartedBtn;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        getStartedBtn = findViewById(R.id.button);
        loading = findViewById(R.id.loading);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(email)) {
            emailInput.setError("Please enter your email");
            emailInput.requestFocus();
        }

        if(TextUtils.isEmpty(password)) {
            passwordInput.setError("Please enter your password");
            passwordInput.requestFocus();
        }

        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.setVisibility(View.VISIBLE);
                getStartedBtn.setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.setVisibility(View.GONE);
                getStartedBtn.setVisibility(View.VISIBLE);

                try {
                    JSONObject obj = new JSONObject(s);

                    if(!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("email"),
                                userJson.getString("domain"),
                                userJson.getString("secret_key"),
                                userJson.getString("logo_url"));

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        Toast.makeText(getApplicationContext(), "Name: " + user.getName() + "\nEmail: " + user.getEmail() + "\nDomain: " + user.getDomain() + "\nSecret Key: " + user.getSecretKey() + "\nLogo URL: " + user.getLogoURL(), Toast.LENGTH_SHORT).show();

                        Intent goToHome = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(goToHome);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}