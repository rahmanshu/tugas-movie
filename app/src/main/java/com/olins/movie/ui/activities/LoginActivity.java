package com.olins.movie.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.olins.movie.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private static final String AUTO_LOGOUT = "autoLogout";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_email)
    EditText etEmail;
    @BindView(R.id.tv_password)
    EditText etPassword;
    String username = "", password = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        title.setText("Masuk");
    }

    public static void startActivityFromAutoLogout(BaseActivity sourceActivity) {
        Intent intent = new Intent(sourceActivity, LoginActivity.class);
        intent.putExtra(AUTO_LOGOUT, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sourceActivity.startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.login_btn)
    void btnLogin(){
        username = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if(!"".equalsIgnoreCase(username) && !"".equalsIgnoreCase(password)){
            HomeActivity.startActivity(LoginActivity.this);
        }else{
            Toast.makeText(this, "Masukan username dan password !", Toast.LENGTH_SHORT).show();
        }
    }
}

