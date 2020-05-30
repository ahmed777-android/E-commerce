package com.example.e_commerce.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.e_commerce.MainActivity;
import com.example.e_commerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
 private FrameLayout frameLayout;
 private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null){
          setFragment(new SignInFragment());

        }else{
           Intent Main = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(Main);
            finish();

       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        frameLayout=findViewById(R.id.frame_layout);
        firebaseAuth=FirebaseAuth.getInstance();
        setFragment(new SignInFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();


    }
}
