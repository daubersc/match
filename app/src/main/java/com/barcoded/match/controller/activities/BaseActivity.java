package com.barcoded.match.controller.activities;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is used as a utility class for methods shared among multiple activities.
 *
 * @version 1.0
 * @since 2021-02-02
 * @author Kai Dauberschmidt
 */
public class BaseActivity extends AppCompatActivity {

  /** OnClick Method to close this activity and to navigate back to the caller . */
  public void closeActivity(View view) {
    finish();
  }

  /** OnClick Method to close the current fragment and to navigate back to the caller. */
  public void closeFragment(View view) {
    onBackPressed();
  }
}
