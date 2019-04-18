package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.checkertext.CheckerFirstName;
import com.example.myapplication.checkertext.CheckerLastName;
import com.example.myapplication.checkertext.CheckerPassword;
import com.example.myapplication.checkertext.CheckerPasswordConfirm;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mFirstnameView;
    private AutoCompleteTextView mLastnameView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mButtonSexView;
    private Button mButtonBirthView;
    private Button mButtonSignup;
    private JsonObjectRequest jsonObjectRequest;
    private DatePickerDialogFragment datePickerDialogFragment;
    private SexChoiceDialogFragment sexChoiceDialogFragment;
    private PopupWindow popupWindowBirthday;
    private PopupWindow popupWindowSex;
    private RadioGroup radioGroup;
    private RadioButton radioButtonFemale;
    private RadioButton radioButtonMale;
    private CheckerFirstName checkerFirstName;
    private CheckerLastName checkerLastName;
    private CheckerPassword checkerPassword;
    private CheckerPasswordConfirm checkerPasswordConfirm;

    //@TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordConfirmView = (EditText) findViewById(R.id.password_confirm);
        mButtonSignup = (Button) findViewById(R.id.buttonSignup);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mFirstnameView = findViewById(R.id.firstname);
        mLastnameView = findViewById(R.id.lastname);
        mButtonSexView = findViewById(R.id.sex);
        mButtonBirthView = findViewById(R.id.birthday);


        checkerFirstName = new CheckerFirstName(this, this.mFirstnameView);
        checkerLastName = new CheckerLastName(this, this.mLastnameView);
        checkerPassword = new CheckerPassword(this, this.mPasswordView);
        checkerPasswordConfirm = new CheckerPasswordConfirm(this, this.mPasswordView, this.mPasswordConfirmView);

        mFirstnameView.addTextChangedListener(checkerFirstName);
        mLastnameView.addTextChangedListener(checkerLastName);
        mPasswordView.addTextChangedListener(checkerPassword);
        mPasswordConfirmView.addTextChangedListener(checkerPasswordConfirm);

        mButtonBirthView.setOnClickListener(this);
        mButtonSexView.setOnClickListener(this);
        mButtonSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = this.getFragmentManager();
        switch (v.getId()) {
            case R.id.birthday:
                datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.show(fragmentManager, "birthday");
                break;
            case R.id.sex:
                sexChoiceDialogFragment = new SexChoiceDialogFragment();
                sexChoiceDialogFragment.show(fragmentManager, "sexChoice");
                break;
            case R.id.buttonSignup:
                if (checkerFirstName.checkFirstName(mFirstnameView.getText().toString())) {
                    mFirstnameView.requestFocus();
                } else if (checkerLastName.checkLastName(mLastnameView.getText().toString())) {
                    mLastnameView.requestFocus();
                } else if (checkerPassword.checkPassword(mPasswordView.getText().toString())) {
                    mPasswordView.requestFocus();
                } else if (checkerPasswordConfirm.confirmPassword()){
                    mPasswordConfirmView.requestFocus();
                }

        }
    }

    @Override
    public void onResponse(JSONObject response) {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mPasswordView.setError(this.getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();

        this.showProgress(false);
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignup() throws JSONException {
        if (jsonObjectRequest != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String firstname = mFirstnameView.getText().toString();
        String lastname = mLastnameView.getText().toString();
        String birthday = mButtonBirthView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        Pattern patternName = Pattern.compile("\\s");
        

        /*if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {*/
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);

        String url = "http://10.0.2.2:8000/signin";
        JSONObject object = new JSONObject();
        object.put("email", email);
        object.put("password", password);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, this, this);
        SingletonRequest.getInstance(this).add(jsonObjectRequest);
        //}
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }





    /*@Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("MyApp", dayOfMonth+"/"+month+"/"+year);
    }*/

    private void checksFields() {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class DatePickerDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        private DatePicker datePicker;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Activity activity = getActivity();
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            this.datePicker = new DatePicker(activity);

            builder.setView(this.datePicker)
                    .setPositiveButton(R.string.validate, this)
                    .setNegativeButton(R.string.cancel, this);
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                String days = "";
                String months = "";

                days = this.datePicker.getDayOfMonth() < 10 ? "0" + this.datePicker.getDayOfMonth() : String.valueOf(this.datePicker.getDayOfMonth());
                months = this.datePicker.getMonth() < 10 ? "0" + this.datePicker.getMonth() : String.valueOf(this.datePicker.getMonth());

                mButtonBirthView.setText(days + "/" + months + "/" + this.datePicker.getYear());
            }
        }
    }

    public class SexChoiceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.prompt_gender)
                    .setItems(R.array.sex, this);
            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            mButtonSexView.setText(SignupActivity.this.getResources().getStringArray(R.array.sex)[which]);
        }
    }


}

