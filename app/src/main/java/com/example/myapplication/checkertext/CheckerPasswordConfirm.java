package com.example.myapplication.checkertext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.myapplication.R;

public class CheckerPasswordConfirm implements TextWatcher {
    private Context context;
    private EditText password;
    private EditText passwordConfirm;

    public CheckerPasswordConfirm(Context context, EditText password, EditText passwordConfirm){
        this.context = context;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.confirmPassword();
    }

    public boolean confirmPassword(){
        boolean hasError = false;

        if(!passwordConfirm.getText().toString().equals(password.getText().toString())){
            passwordConfirm.setError(context.getString(R.string.wrong_confirm_pwd));
            hasError = true;
        }

        return hasError;
    }
}
