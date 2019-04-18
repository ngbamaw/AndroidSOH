package com.example.myapplication.checkertext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckerFirstName implements TextWatcher{
    private Context context;
    private EditText mFirstnameView;
    private boolean[] errors;

    public CheckerFirstName(Context context, EditText mFirstnameView) {
        this.context = context;
        this.mFirstnameView = mFirstnameView;
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
        this.checkFirstName(s);
        String message = "";
        boolean hasError = false;
        if (this.errors[0]) {
            message += context.getString(R.string.dont_have_spec_char);
            hasError = true;
        }
        if (this.errors[1]) {
            message += (hasError ? '\n' : "") + context.getString(R.string.dont_have_space);
        }

        this.mFirstnameView.setError(message != "" ? message : null);
    }

    public boolean checkFirstName(CharSequence firstname) {

        Matcher matcherSpeChar = Pattern.compile("\\p{Punct}").matcher(firstname);
        Matcher matcherSpace = Pattern.compile("\\s").matcher(firstname);

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
