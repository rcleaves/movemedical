package com.movecode.movecalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.movecode.movecalendar.content.CalendarContent;
import com.movecode.movecalendar.content.CalendarDao;
import com.movecode.movecalendar.content.CalendarItem;
import com.movecode.movecalendar.databinding.FragmentItemDetailBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment represents.
     */
    public static final String ARG_ITEM_ID = "calendar_id";
    private static final String[] locations = {"San Diego", "St. George", "Park City", "Dallas", "Memphis", "Orlando"};
    private int mYear, mMonth, mDay, mHour, mMinute;

    /**
     * The Calendar content this fragment is presenting.
     */
    private CalendarItem mItem;
    private CollapsingToolbarLayout mToolbarLayout;
    private EditText mDetailsView;
    private AppCompatSpinner mLocationView;
    private EditText mDateView;
    private MaterialButton mUpdateButton;
    private MaterialButton mDeleteButton;

    private final View.OnDragListener dragListener = (v, event) -> {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ClipData.Item clipDataItem = event.getClipData().getItemAt(0);
            mItem = CalendarContent.ITEM_MAP.get(clipDataItem.getText().toString());
            updateContent();
        }
        return true;
    };
    private FragmentItemDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalendarDao calendarDao = ItemListFragment.appointmentDatabase.calendarDao();

        if (getArguments().containsKey(ARG_ITEM_ID) && calendarDao != null) {
            // load from db
            mItem = calendarDao.getAppointment(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentItemDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        mToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        mDetailsView = (EditText) binding.itemDetail;
        mLocationView = (AppCompatSpinner) binding.itemLocation;
        mDateView = (EditText) binding.itemDate;
        mUpdateButton = (MaterialButton) binding.addButton;
        mDeleteButton = (MaterialButton) binding.deleteButton;

        // buttonize
        if (mItem == null) {
            mUpdateButton.setText("ADD");
            mDeleteButton.setEnabled(false);
        } else {
            mUpdateButton.setText("UPDATE");
            mDeleteButton.setEnabled(true);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        CalendarDao calendarDao = ItemListFragment.appointmentDatabase.calendarDao();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_item_detail);

        mDateView.setOnClickListener(v -> {
            // date
            final Calendar c = Calendar.getInstance();
            if (mDateView.getText().length() > 0) {
                // use date already set
                Date date = null;
                try {
                    date = sdf.parse(mDateView.getText().toString());
                    c.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mYear = year;
                        // bug in callback
                        mMonth = ++monthOfYear;
                        mDay = dayOfMonth;
                        showTimePicker(c);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        mDeleteButton.setOnClickListener(v -> {
            if (mItem != null) {
                CalendarItem calendarItem = mItem;
                calendarDao.deleteAppointment(calendarItem);
                navController.navigateUp();
            }
        });

        mUpdateButton.setOnClickListener(v -> {
            // do not allow empty date/time, details
            if (mDateView.getText().length() < 1) {
                mDateView.setHintTextColor(ContextCompat.getColor(getContext(), R.color.red));
                return;
            }
            if (mDetailsView.getText().length() < 1) {
                mDetailsView.setHintTextColor(ContextCompat.getColor(getContext(), R.color.red));
                return;
            }
            if (mItem == null) {
                // create new
                CalendarItem calendarItem = null;
                try {
                    calendarItem = new CalendarItem(
                            null,
                            locations[mLocationView.getSelectedItemPosition()],
                            mDetailsView.getText().toString(),
                            sdf.parse(mDateView.getText().toString())
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendarDao.addAppointment(calendarItem);
            } else {
                // update
                CalendarItem calendarItem = mItem;
                mItem.location = locations[mLocationView.getSelectedItemPosition()];
                mItem.details = mDetailsView.getText().toString();
                try {
                    calendarItem = new CalendarItem(
                            mItem.id,
                            locations[mLocationView.getSelectedItemPosition()],
                            mDetailsView.getText().toString(),
                            sdf.parse(mDateView.getText().toString())
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendarDao.updateAppointment(calendarItem);
            }

            // navigate back
            navController.navigateUp();

        });

        // spinner setup
        ArrayAdapter ad = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                locations);

        ad.setDropDownViewResource( android.R.layout .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the spinner which binds data to spinner
        mLocationView.setAdapter(ad);

        // set selected text size
        mLocationView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                ((TextView) parent.getChildAt(0)).setTextSize(20);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Show the details as text in a TextView & in the toolbar if available.
        updateContent();
        rootView.setOnDragListener(dragListener);

        return rootView;
    }

    private void showTimePicker(Calendar c) {
        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        mDateView.setText(mMonth + "/" + mDay + "/" + mYear + " " + mHour + ":" + mMinute);
                        //txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (mItem != null) {
            mDetailsView.setText(mItem.details);
            mLocationView.setSelection(getPosition(mItem.location));

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            mDateView.setText(sdf.format(mItem.date));
            if (mToolbarLayout != null) {
                mToolbarLayout.setTitle(mItem.details);
            }
        }
    }

    private int getPosition(String location) {
        int result = -1;
        for(int i=0; i<locations.length; i++) {
            if (location.equals(locations[i])) {
                result = i;
            }
        }
        return result;
    }
}