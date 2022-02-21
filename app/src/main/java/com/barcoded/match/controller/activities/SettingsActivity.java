package com.barcoded.match.controller.activities;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.barcoded.match.R;

/**
 * This activity handles the settings of the game. It is designed in an activity, so it can be
 * called from the Settings of the phone.
 *
 * @version 1.0
 * @since 2021-02-02
 * @author Kai Dauberschmidt
 */
public class SettingsActivity extends BaseActivity {

  /** {@inheritDoc} */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // Create this activity.
    super.onCreate(savedInstanceState);

    // Set the Content view to the respective layout.
    setContentView(R.layout.activity_settings);

    // Replace the settings dynamically with what's present in the preferences graph.
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.settings, new SettingsFragment())
        .commit();
  }

  /** This inner fragment is used to dynamically build the settings. */
  public static class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * Creates the preferences dependent on the pref graph and App theme.
     *
     * <p>{@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
  }
}
