package com.barcoded.match.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.barcoded.match.R;
import com.barcoded.match.controller.activities.GameActivity;
import com.barcoded.match.controller.activities.SettingsActivity;

/**
 * This is used as the entry point of the Match application. It implements the functionality of
 * navigating to the other Fragments and activities used in this Application.
 *
 * @version 1.0
 * @since 2021-02-01
 * @author Kai Dauberschmidt
 */
public class MainFragment extends BaseFragment {

  /** {@inheritDoc} */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /** {@inheritDoc} */
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_main, container, false);

    // Setup settings navigation.
    View settingsBtn = view.findViewById(R.id.btn_settings);
    settingsBtn.setOnClickListener(navigate(SettingsActivity.class));

    // Setup about navigation.
    View aboutBtn = view.findViewById(R.id.btn_about);
    aboutBtn.setOnClickListener(navigate(R.id.navigateToAbout));

    // Setup new game navigation.
    View newGameBtn = view.findViewById(R.id.btn_newGame);
    newGameBtn.setOnClickListener(navigate(GameActivity.class));

    return view;
  }
}
