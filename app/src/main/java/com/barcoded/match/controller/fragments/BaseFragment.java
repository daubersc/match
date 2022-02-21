package com.barcoded.match.controller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.barcoded.match.R;

import java.util.Objects;

/**
 * This class is used as a utility class for methods shared among multiple fragments.
 *
 * @version 1.0
 * @since 2021-02-01
 * @author Kai Dauberschmidt
 */
public class BaseFragment extends Fragment {

  /**
   * Creates and returns an on click listener that executes the navigation and animation of button
   * clicks.
   *
   * @param navActionId The id of the navigation action.
   * @return an instance of the {@link View.OnClickListener} that implements navigation.
   */
  public View.OnClickListener navigate(final int navActionId) {
    return new View.OnClickListener() {

      // On click: Slide the button and navigate.
      @Override
      public void onClick(View view) {
        slide(view);
        Navigation.findNavController(view).navigate(navActionId);
      }
    };
  }

  /**
   * Creates and returns an on click listener that executes the navigation and animation of button
   * clicks.
   *
   * @param activity The class of that activity.
   * @return an instance of the {@link View.OnClickListener} that implements navigation.
   */
  public <T> View.OnClickListener navigate(final Class<T> activity) {
    return new View.OnClickListener() {

      // On click: Slide the button and navigate.
      @Override
      public void onClick(View view) {
        slide(view);
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
      }
    };
  }

  /**
   * Animation method that slides the view from bottom up.
   *
   * @param view The component that animation is called upon.
   */
  private void slide(View view) {
    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide);
    view.startAnimation(animation);
  }
}
