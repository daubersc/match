package com.barcoded.match.controller.activities;

import android.os.Bundle;
import com.barcoded.match.R;

/**
 * This is the entry point for the Match game application. It defines the
 * home screen of the application.
 *
 * @version 1.0
 * @since 2021-02-01
 * @author Kai Dauberschmidt
 */
public class MainActivity extends BaseActivity {

  /** {@inheritDoc} */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
