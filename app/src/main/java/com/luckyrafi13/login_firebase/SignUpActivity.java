package com.luckyrafi13.login_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.luckyrafi13.login_firebase.Model.Pengguna;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    //Deklarasi Variable
    private EditText et_email, et_password;
    private Button bt_Login, bt_SignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String getEmail, getPassword;
    private ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        bt_Login = findViewById(R.id.bt_Login);
        bt_SignUp = findViewById(R.id.bt_SignUp);
        ib_back = findViewById(R.id.ib_back);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //Instance / Membuat Objek Firebase Authentication
        auth = FirebaseAuth.getInstance();

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekDataUser();
            }
        });


        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
    private void cekDataUser() {
        //Mendapatkan data yang diinputkan User
        getEmail = et_email.getText().toString();
        getPassword = et_password.getText().toString();

        //Mengecek apakah email dan sandi kosong atau tidak
        if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
            Toast.makeText(this, "Email atau Sandi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            //Mengecek panjang karakter password baru yang akan didaftarkan
            if (getPassword.length() < 6) {
                Toast.makeText(this, "Sandi Terlalu Pendek, Minimal 6 Karakter", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                createUserAccount();
            }
        }
    }

    //Method ini digunakan untuk membuat akun baru user
    private void createUserAccount() {
        auth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Pengguna user = new Pengguna(getEmail, getPassword);
                        FirebaseDatabase.getInstance().getReference("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                //Mengecek status keberhasilan saat medaftarkan email dan sandi baru
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Terjadi Kesalahan, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

                    }
                });


    }

}