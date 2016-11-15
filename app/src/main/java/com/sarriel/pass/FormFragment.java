package com.sarriel.pass;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sarriel.pass.notification.PasswordGeneratorNotification;
import com.sarriel.pass.passwordgenerator.PasswordGenException;
import com.sarriel.pass.passwordgenerator.PasswordGenerator;

public class FormFragment extends Fragment {
    private static final String TAG = FormFragment.class.getSimpleName();

    /**
     * Alias, used for creating password
     */
    private TextView inputAlias;
    /**
     * Secret, used for creating password
     */
    private TextView inputSecret;
    /**
     * Checkbox whether user wants app to remember current secret for future use.
     */
    private CheckBox checkboxRemember;
    /**
     * Checkbox whether user wants to copy generated password to clipboard automatically.
     */
    private CheckBox checkboxClipboard;

    /**
     * Attached activity. It has to implement listener, so fragment can interact with it.
     */
    private OnFragmentInteractionListener activity;

    public FormFragment() {}

    /**
     * Create fragment view, prepare event listeners for buttons and inputs.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_pass_part_form, container, false);

        //find all input views
        this.inputAlias = (TextView) view.findViewById(R.id.edit_alias);
        this.inputSecret = (TextView) view.findViewById(R.id.edit_secret);
        this.checkboxRemember = (CheckBox) view.findViewById(R.id.checkbox_remember);
        this.checkboxClipboard = (CheckBox) view.findViewById(R.id.checkbox_clipboard);

        //setup button event listeners
        (view.findViewById(R.id.button_generate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
            }
        });
        (view.findViewById(R.id.button_clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });
        this.inputAlias.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (inputSecret.getText().toString().length() > 0) {
                        generatePassword();
                    }
                }
                return false;
            }
        });
        this.inputSecret.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    generatePassword();
                }
                return false;
            }
        });


        return view;
    }

    /**
     * When attached to activity, verify that activity implements required interface
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            activity = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * When detached from activity, clear its reference.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    /**
     * Fragment is paused. Store settings and required data.
     */
    @Override
    public void onPause() {
        super.onPause();
        if(this.checkboxRemember.isChecked()) {
            String secret = this.inputSecret.getText().toString();
            Storage.setSecret(getContext(), secret);
            PasswordGeneratorNotification.showNotification(getContext());
        } else {
            Storage.setSecret(getContext(), "");
            PasswordGeneratorNotification.removeNotification(getContext());
        }
        Storage.setRememberSecret(getContext(), this.checkboxRemember.isChecked());
        Storage.setClipboard(getContext(), this.checkboxClipboard.isChecked());
    }

    /**
     * Fragment resumed, load saved settings.
     */
    @Override
    public void onResume() {
        super.onResume();
        boolean remember = Storage.getRememberSecret(getContext());
        boolean clipboard = Storage.getClipboard(getContext());
        String secret = Storage.getSecret(getContext());
        this.checkboxClipboard.setChecked(clipboard);
        this.checkboxRemember.setChecked(remember);
        if (remember) {
            this.inputSecret.setText(secret);
        }

    }

    /**************************************** BUTTONS *********************************************/

    /**
     * Generate password from inputs
     */
    private void generatePassword() {
        Log.d(TAG, "generating password");
        String alias = this.inputAlias.getText().toString();
        String secret = this.inputSecret.getText().toString();
        //test form not filled
        if(alias.isEmpty() || secret.isEmpty()) {
            this.activity.makeToast(R.string.toast_err_empty_fields);
            return;
        }

        try {
            //generatePassword password
            String password = PasswordGenerator.generatePassword(alias, secret);
            //tell activity password was generated
            this.activity.passwordGenerated(password);
            //if user requested clipboard copy, do it
            if(this.checkboxClipboard.isChecked()) {
                ClipboardHelper.copyToClipboard(getContext(), password);
                this.activity.makeToast(R.string.toast_clipboard);
            }
            hideKeyboard();
            this.activity.passwordGenerated(password);
        } catch (PasswordGenException e) {
            this.activity.makeToast(R.string.toast_err_pass_generator);
        }
    }

    /**
     * Reset form to default state, cleanin all inputs and reseting checkboxes.
     */
    private void resetForm() {
        View view = getView();
        ((TextView)view.findViewById(R.id.edit_alias)).setText("");
        ((TextView)view.findViewById(R.id.edit_secret)).setText("");
        ((CheckBox)view.findViewById(R.id.checkbox_remember)).setChecked(false);
        ((CheckBox)view.findViewById(R.id.checkbox_clipboard)).setChecked(false);
    }


    /****************************************** Other *********************************************/

    /**
     * Hide keyboard
     */
    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /**
     * Focus alias input
     */
    public void focusAlias() {
        this.inputAlias.requestFocus();
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.inputAlias, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Reset input values for alias, and for secret (it rememebering secret is not enabled).
     */
    public void cleanInputs() {
        this.inputAlias.setText(null);
        if (!Storage.getRememberSecret(getContext())) {
            this.inputSecret.setText(null);
        }
    }

    /**
     * Interface for attached activity.
     */
    public interface OnFragmentInteractionListener {

        /**
         * Password was generated.
         * @param password
         */
        void passwordGenerated(String password);

        /**
         * Show user a toast message.
         * @param stringId
         */
        void makeToast(int stringId);
    }
}
