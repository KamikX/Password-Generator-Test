package com.sarriel.pass;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    /**
     * Attached activity. Iy has to implement listener, so fragment can interact with it.
     */
    private OnFragmentInteractionListener activity;
    /**
     * Password string, that is displayed to the user, or copied to clipboard.
     */
    private String password = "";

    public ResultFragment() {}

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
        View view = inflater.inflate(R.layout.activity_pass_part_result, container, false);

        // setup event listeners
        view.findViewById(R.id.button_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultFragment.this.activity.onResultDismissed();
            }
        });
        view.findViewById(R.id.button_clipboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardHelper.copyToClipboard(getContext(), ResultFragment.this.password);
                ResultFragment.this.activity.makeToast(R.string.toast_clipboard);
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
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
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
     * Set password string to show to user.
     * @param password
     */
    public void setPassword(String password) {
        ((TextView)getView().findViewById(R.id.password_result)).setText(password);
        this.password = password;
    }

    /**
     * Interface for attached activity.
     */
    public interface OnFragmentInteractionListener {
        /**
         * User dismissed result.
         */
        void onResultDismissed();
        /**
         * Show user a toast message.
         * @param stringId
         */
        void makeToast(int stringId);
    }
}
