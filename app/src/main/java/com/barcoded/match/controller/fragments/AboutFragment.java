package com.barcoded.match.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.barcoded.match.R;

/**
 * This {@link AboutFragment} controls the About fragment view, as in a MVC pattern. The About view
 * has just the functionality to navigate back to the main view. The view itself is defined within
 * the fragment_about.xml.
 *
 * @version 1.0
 * @author Kai Dauberschmidt
 * @since 2021-02-02
 */
public class AboutFragment extends BaseFragment {

  /** Constructs an empty about fragment. Android requires an empty public constructor. */
  public AboutFragment() {}

  /** {@inheritDoc} */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /** {@inheritDoc} */
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_about, container, false);
  }
}
