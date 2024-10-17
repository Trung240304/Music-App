package com.example.musicapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.Admin.AminActivity;
import com.example.musicapp.Data.AccountTable;
import com.example.musicapp.Data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername, editPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    // Kiểm tra tài khoản admin
                    if (username.equals("trung") && password.equals("1")) {
                        // Điều hướng đến AdminActivity nếu là admin
                        Toast.makeText(LoginActivity.this, "Đăng nhập với quyền Admin!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(LoginActivity.this, AminActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                    // Kiểm tra tài khoản bình thường
                    else if (AccountTable.checkAccount(db, username, password)) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Điều hướng đến màn hình đăng ký khi nhấn nút "Đăng ký"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
