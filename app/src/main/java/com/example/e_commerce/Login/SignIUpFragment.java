package com.example.e_commerce.Login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.MainActivity;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignIUpFragment extends Fragment {
    private TextView haveaccount;
    FrameLayout parentFrameLayout;
    private EditText email, fullName, password, confirmPassword;
    private ImageView closeBtn;
    private Button SignUpBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressBar progressBar;


    public SignIUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        haveaccount = view.findViewById(R.id.tv_have);
        email = view.findViewById(R.id.email_sign_up_ET);
        fullName = view.findViewById(R.id.full_name_sign_up_ET);
        password = view.findViewById(R.id.password_sign_up_ET);
        confirmPassword = view.findViewById(R.id.passwordConf_sign_up_ET);
        closeBtn = view.findViewById(R.id.sign_up_close_btn);
        SignUpBtn = view.findViewById(R.id.btnSign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar= view.findViewById(R.id.progressBar2);
        parentFrameLayout = getActivity().findViewById(R.id.frame_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInput();


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
                CheckInput();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //&& confirmPassword.getText().toString().equals(pass)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();

            }
        });
    }

    private void CheckEmailAndPassword() {
        Drawable customErroricon =getResources().getDrawable(R.drawable.ic_error_black_24dp);
        customErroricon.setBounds(-100,10,customErroricon.getIntrinsicWidth(),customErroricon.getIntrinsicHeight());
        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                SignUpBtn.setEnabled(false);
                SignUpBtn.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map  <Object,String> userdata= new HashMap<>();
                                    userdata.put("fullname",fullName.getText().toString());
                                    firestore.collection("USERS")
                                            .add(userdata).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                mainIntent();
                                            }else{
                                                SignUpBtn.setEnabled(true);
                                                SignUpBtn.setTextColor(Color.rgb(255, 255, 255));
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                } else {
                                    SignUpBtn.setEnabled(true);
                                    SignUpBtn.setTextColor(Color.rgb(255, 255, 255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            } else {
                confirmPassword.setError("Password doesn't matched!",customErroricon);
            }
        } else {
            email.setError("Invalid Email!",customErroricon);
        }
    }
    private void CheckInput() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(fullName.getText())) {
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                    if (!TextUtils.isEmpty(confirmPassword.getText())) {
                        SignUpBtn.setEnabled(true);
                        SignUpBtn.setTextColor(Color.rgb(255, 255, 255));
                    }
                } else {
                    SignUpBtn.setEnabled(false);
                    SignUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                SignUpBtn.setEnabled(false);
                SignUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            SignUpBtn.setEnabled(false);
            SignUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
    private void mainIntent(){
        Intent Main = new Intent(getActivity(), MainActivity.class);
        startActivity(Main);
        getActivity().finish();
    }
}
