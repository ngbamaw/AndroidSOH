package com.example.myapplication.checkertext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckerPassword implements TextWatcher {
    private Context context;
    private EditText mPasswordView;
    private boolean[] errors;

    public CheckerPassword(Context context, EditText mPasswordView) {
        this.context = context;
        this.mPasswordView = mPasswordView;
        this.errors = new boolean[4];

        //this.errors[]
        //0 - Special character
        //1 - Upper
        //2 - Lower
        //3 - Length
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.checkPassword(s);
        String message = "";
        boolean hasError = false;
        if (this.errors[0]) {
            message += context.getString(R.string.pwd_special_char);
            hasError = true;
        }
        if (this.errors[1]) {
            message += (hasError ? '\n' : "") + context.getString(R.string.pwd_upper_char);
            hasError = true;
        }
        if (this.errors[2]) {
            message += (hasError ? '\n' : "") + context.getString(R.string.pwd_lower_char);
            hasError = true;
        }
        if (this.errors[3]) {
            message += (hasError ? '\n' : "") + context.getString(R.string.pwd_length);
        }

        this.mPasswordView.setError(message != "" ? message : null);
    }

    public boolean checkPassword(@NotNull CharSequence pass) {
        String password = pass.toString();

        Matcher matcherSpeChar = Pattern.compile("\\p{Punct}").matcher(password);
        Matcher matcherUpper = Pattern.compile("\\p{Upper}").matcher(password);
        Matcher matcherLower = Pattern.compile("\\p{Lower}").matcher(password);

        boolean hasError = false;
        int i = 0;

        errors[0] = !matcherSpeChar.find();
        errors[1] = !matcherUpper.find();
        errors[2] = !matcherLower.find();
        errors[3] = password.length() < 8;

        while (i < errors.length && !hasError){
            if(errors[i]) {
                hasError = true;
            }
            i++;
        }
        
        return hasError;
    }
}
