package com.example.fanyangsz.oschina.view.LoginView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.Result;
import com.example.fanyangsz.oschina.R;

public class LoginActivity extends ActionBarActivity implements HttpSDK.onLoginCallBack {

    EditText editAccount;
    EditText editPassWord;
    Button btnLogin;
    ProgressDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editAccount = (EditText)findViewById(R.id.et_username);
        editPassWord = (EditText)findViewById(R.id.et_password);
        btnLogin = (Button)findViewById(R.id.btn_login);

        btnLogin.setOnClickListener( new onLoginListener());
        creatDialog();
    }

    public class onLoginListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String username = editAccount.getText().toString();
            String password = editPassWord.getText().toString();
            new HttpSDK().login(getBaseContext(),username, password, LoginActivity.this);
            myDialog.show();
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(LoginUserBean loginUserBean) {
        if(loginUserBean != null){
            Result result = loginUserBean.getResult();
            myDialog.hide();
            Toast.makeText(getBaseContext(), result.getErrorMessage(),Toast.LENGTH_LONG).show();
            if(result.OK()){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("loginUserBean",loginUserBean);
                intent.putExtras(bundle);
                setResult(0, intent);
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*Intent intent = new Intent();
        setResult(1, intent);
        finish();*/
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        switch (id){
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void creatDialog(){
        myDialog = new ProgressDialog(this);
        myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        myDialog.setMessage("正在登陆中...");
    }

}
