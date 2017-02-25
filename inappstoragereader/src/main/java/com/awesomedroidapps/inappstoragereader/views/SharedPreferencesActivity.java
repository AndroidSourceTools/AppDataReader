package com.awesomedroidapps.inappstoragereader.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.awesomedroidapps.inappstoragereader.AppStorageDataRecyclerView;
import com.awesomedroidapps.inappstoragereader.ErrorMessageHandler;
import com.awesomedroidapps.inappstoragereader.ErrorMessageInterface;
import com.awesomedroidapps.inappstoragereader.ErrorType;
import com.awesomedroidapps.inappstoragereader.R;
import com.awesomedroidapps.inappstoragereader.SharedPreferenceReader;
import com.awesomedroidapps.inappstoragereader.adapters.SharedPreferencesListAdapter;
import com.awesomedroidapps.inappstoragereader.entities.SharedPreferenceObject;

import java.util.List;

/**
 * Created by anshul on 11/2/17.
 */

public class SharedPreferencesActivity extends AppCompatActivity implements
    ErrorMessageInterface, PopupMenu.OnMenuItemClickListener {

  private AppStorageDataRecyclerView sharedPreferencesRecylerView;
  private RelativeLayout errorHandlerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shared_preferences_list);
    sharedPreferencesRecylerView =
        (AppStorageDataRecyclerView) findViewById(R.id.shared_preferences_recycler_view);
    int height = (int) (getResources().getDimension(R.dimen.sharedpreferences_type_width) +
        getResources
            ().getDimension(R.dimen.sharedpreferences_key_width) +
        getResources().getDimension(R.dimen
            .sharedpreferences_value_width));
    sharedPreferencesRecylerView.setRecyclerViewWidth(height);
    errorHandlerLayout = (RelativeLayout) findViewById(R.id.error_handler);
  }

  @Override
  public void onStart() {
    super.onStart();
    loadSharedPreferences();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.shared_preference_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    if (item.getItemId() == R.id.shared_preferences_filter) {
      View view = findViewById(R.id.shared_preferences_filter);
      showPopup(view);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showPopup(View v) {
    PopupMenu popup = new PopupMenu(this, v);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.shared_preferences_popup_menu, popup.getMenu());
    popup.setOnMenuItemClickListener(this);
    popup.show();
  }

  private void loadSharedPreferences() {
    List<SharedPreferenceObject> sharedPreferenceObjectArrayList =
        SharedPreferenceReader.getAllSharedPreferences
            (this);
    if (sharedPreferenceObjectArrayList == null) {
      handleError(ErrorType.NO_SHARED_PREFERENCES_FOUND);
      return;
    }
    sharedPreferencesRecylerView.setVisibility(View.VISIBLE);
    errorHandlerLayout.setVisibility(View.GONE);

    SharedPreferencesListAdapter
        adapter = new SharedPreferencesListAdapter(sharedPreferenceObjectArrayList, this);
    sharedPreferencesRecylerView.setLayoutManager(new LinearLayoutManager(this));
    sharedPreferencesRecylerView.setAdapter(adapter);
  }

  @Override
  public void handleError(ErrorType errorType) {
    sharedPreferencesRecylerView.setVisibility(View.GONE);
    errorHandlerLayout.setVisibility(View.VISIBLE);
    ErrorMessageHandler handler = new ErrorMessageHandler();
    handler.handleError(errorType, errorHandlerLayout);
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.shared_preferences_all) {
      item.setChecked(item.isChecked());
      return true;
    } else if (item.getItemId() == R.id.shared_preferences_file) {
      item.setChecked(item.isChecked());
      return true;
    }
    return onMenuItemClick(item);
  }
}
