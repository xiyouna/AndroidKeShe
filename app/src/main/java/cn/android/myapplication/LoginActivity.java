package cn.android.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.android.myapplication.db.UserDao;


public class LoginActivity extends AppCompatActivity {

    private EditText userName, passWord;
    private TextView userReg;
    private ImageView iv_icon;
    private Button login;
    private CheckBox mToggleShowPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();//初始化组件
        ViewClick();//注册组件点击事件

    }

    private void init() {
        userName = (EditText) findViewById(R.id.uname);
        passWord = (EditText) findViewById(R.id.pword);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        userReg = (TextView) findViewById(R.id.tv_register);
        login = (Button) findViewById(R.id.submit);
        mToggleShowPwd = (CheckBox) findViewById(R.id.toggle_show_pwd);

        mToggleShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passWord.setSelection(passWord.getText().toString().length());
            }
        });

    }

    private void login() {
        final String username = userName.getText().toString();
        final String password = passWord.getText().toString();
        //判断用户名密码是否为空
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "帐号不能为空", Toast.LENGTH_LONG).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        Cursor cursor = new UserDao(LoginActivity.this).query(username.trim(), password.trim());
        if (cursor.moveToNext()) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            intent.putExtra("login_user", username);

            cursor.close();


            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "密码验证失败，请重新验证登录", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 跳转到注册页面
     */
    private void goReg() {
        userReg.setTextColor(Color.rgb(0, 0, 0));
        Intent intent = new Intent(getApplicationContext(), RegActivity.class);
        startActivity(intent);
        finish();
    }

    private void ViewClick() {

        userReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goReg();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    }


}
