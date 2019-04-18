package com.example.myapplication.checkertext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckerLastName implements TextWatcher {
    private Context context;
    private EditText mLastnameView;
    private boolean[] errors;

    public CheckerLastName(Context context, EditText mLastnameView) {
        this.context = context;
        this.mLastnameView = mLastnameView;
        this.errors = new boolean[2];

        //this.errors[]
        //0 - Special charcater
        //1 - Space
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.checkLastName(s);
        String message = "";
        boolean hasError = false;
        if (this.errors[0]) {
            message += context.getString(R.string.lastname_special_char);
            hasError = true;
        }
        if (this.errors[1]) {
            message += (hasError ? '\n' : "") + context.getString(R.string.dont_have_space);
        }

        this.mLastnameView.setError(message != "" ? message : null);
    }

    public boolean checkLastName(CharSequence lastname) {

        //Accept ' or - only
        Pattern patternSpec = Pattern.compile("[]\\[!\"#\\$%&\\(\\)\\*\\+,\\./:;<=>\\?@\\\\\\^_`\\{\\|}~]");
        Pattern patternSpace = Pattern.compile("\\s");
        Matcher matcherSpeChar = patternSpec.matcher(lastname);
        Matcher matcherSpace = patternSpace.matcher(lastname);

        boolean hasError = false;
        int i = 0;

        errors[0] = matcherSpeChar.find();
        errors[1] = matcherSpace.find();

        while (i < errors.length && !hasError){
            if(errors[i]) {
                hasError = true;
            }
            i++;
        }

        return hasError;
    }

}
