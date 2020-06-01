package com.example.e_commerce.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.MainActivity;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private TextView donthaveAccount;
    private FrameLayout parentFrameLayout;
    private EditText email, password;
    private ImageButton closeBtn;
    private Button SignIn;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressBar progressBar;


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        donthaveAccount = view.findViewById(R.id.tv_have);
        email = view.findViewById(R.id.email_ET);
        password = view.findViewById(R.id.Pass_ED);
        closeBtn = view.findViewById(R.id.sign_in_close_btn);
        SignIn = view.findViewById(R.id.btnSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        parentFrameLayout = getActivity().findViewById(R.id.frame_layout);
        progressBar = view .findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignIUpFragment());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });
    }

    private void CheckEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)){
            if (password.length()>=8){
                progressBar.setVisibility(View.VISIBLE);
             //   SignIn.setEnabled(false);
               // SignIn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent Main = new Intent(getActivity(), MainActivity.class);
                                    startActivity(Main);
                                    getActivity().finish();

                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    SignIn.setEnabled(true);
                                    SignIn.setTextColor(Color.rgb(255,255,255));
                                    String err= task.getException().getMessage();
                                    Toast.makeText(getActivity(),err,Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }else{
                Toast.makeText(getActivity(),"Incorrect email or password!",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity(),"Incorrect email or password!",Toast.LENGTH_LONG).show();
        }

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(password.getText())){
                SignIn.setEnabled(true);
                SignIn.setTextColor(Color.rgb(255,255,255));
            }else {
              //  SignIn.setEnabled(false);
               // SignIn.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
         //   SignIn.setEnabled(false);
          //  SignIn.setTextColor(Color.argb(50,255,255,255));
        }

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();


    }
}
