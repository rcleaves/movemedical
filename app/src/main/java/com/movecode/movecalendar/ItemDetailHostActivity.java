package com.movecode.movecalendar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.movecode.movecalendar.content.AppointmentDatabase;
import com.movecode.movecalendar.content.CalendarContent;
import com.movecode.movecalendar.content.CalendarDao;
import com.movecode.movecalendar.content.CalendarItem;
import com.movecode.movecalendar.databinding.ActivityItemDetailBinding;

import java.util.List;

public class ItemDetailHostActivity extends AppCompatActivity {

    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = getApplicationContext();

        ActivityItemDetailBinding binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_item_detail);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.
                Builder(navController.getGraph())
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        AppointmentDatabase db = AppointmentDatabase.getInstance(this);
        CalendarDao calendarDao = db.calendarDao();

        calendarDao.getAppointments().observe(this, items -> {
            // reload items to prevent duplicates
            if (CalendarContent.ITEMS.size() > 0) {
                CalendarContent.removeAll();
            }
            for(CalendarItem item : items) {
                CalendarContent.addItem(item);
            }
            ItemListFragment.mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_item_detail);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}