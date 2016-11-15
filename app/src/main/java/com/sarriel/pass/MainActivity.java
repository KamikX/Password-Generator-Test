package com.sarriel.pass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class MainActivity extends AppCompatActivity implements FormFragment.OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TIMEOUT = 60000; //1 min
    private boolean passVisible = false;
    private long lastVisited = 0;
    private FormFragment form;
    private ResultFragment result;

    /**
     * Prepare activity layout and get reference to form and result fragments.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_generator);
        Log.d(TAG, "activity created");
        FragmentManager fm = getSupportFragmentManager();
        this.form = (FormFragment) fm.findFragmentById(R.id.fragment_form);
        this.result = (ResultFragment) fm.findFragmentById(R.id.fragment_result);
    }

    /**
     * Create overflow menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pass_generator, menu);
        return true;
    }

    /**
     * Handle overflow menu item selection event.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(getBaseContext(), AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        if(passVisible) {
            animateShowForm();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.lastVisited = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //clear input if app in background too long
        if (System.currentTimeMillis() > this.lastVisited + TIMEOUT && this.lastVisited != 0) {
            makeToast(R.string.toast_timeout);
            showForm();
            this.form.cleanInputs();
        }
    }

    private void hideForm() {
        findViewById(R.id.main_formwrapper).setVisibility(View.GONE);
        findViewById(R.id.main_resultwrapper).setVisibility(View.VISIBLE);
        this.passVisible = true;
    }

    private void animateHideForm() {
        Animation fadeout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.form_fadeout);
        Animation fadein = AnimationUtils.loadAnimation(getBaseContext(), R.anim.result_fadein);
        findViewById(R.id.main_formwrapper).startAnimation(fadeout);
        findViewById(R.id.main_resultwrapper).startAnimation(fadein);
        hideForm();
    }

    private void animateShowForm() {
        Animation fadeout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.result_fadeout);
        Animation fadein = AnimationUtils.loadAnimation(getBaseContext(), R.anim.form_fadein);
        findViewById(R.id.main_resultwrapper).startAnimation(fadeout);
        findViewById(R.id.main_formwrapper).startAnimation(fadein);
        showForm();
    }

    private void showForm() {
        this.passVisible = false;
        findViewById(R.id.main_resultwrapper).setVisibility(View.GONE);
        findViewById(R.id.main_formwrapper).setVisibility(View.VISIBLE);
        this.form.focusAlias();
    }


    @Override
    public void passwordGenerated(String password) {
        this.result.setPassword(password);
        animateHideForm();
    }

    @Override
    public void makeToast(int stringId) {
        String str = getString(stringId);
        Snackbar.make(findViewById(R.id.wrapper), Html.fromHtml("<font color=\"#cccccc\">" + str +"</font>"), Snackbar.LENGTH_SHORT)
                .setAction(R.string.dismiss, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                }).show();
    }

    @Override
    public void onResultDismissed() {
        animateShowForm();
    }
}
