package com.movecode.movecalendar;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.movecode.movecalendar.content.CalendarContent;
import com.movecode.movecalendar.content.CalendarDao;
import com.movecode.movecalendar.content.CalendarItem;
import com.movecode.movecalendar.databinding.FragmentItemDetailBinding;

import java.text.SimpleDateFormat;
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
    public static final String APPOINTMENTS = "APPOINTMENTS";
    private static final String[] locations = {"San Diego", "St. George", "Park City", "Dallas", "Memphis", "Orlando"};

    /**
     * The placeholder content this fragment is presenting.
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
            //mItem = CalendarContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
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
        mDateView = (EditText) binding.itemTime;
        mUpdateButton = (MaterialButton) binding.addButton;
        mDeleteButton = (MaterialButton) binding.deleteButton;

        mUpdateButton.setOnClickListener(v -> {
            CalendarDao calendDao = ItemListFragment.appointmentDatabase.calendarDao();
            if (mItem == null) {
                // create new
                CalendarItem calendarItem = new CalendarItem(
                        null,
                        locations[mLocationView.getSelectedItemPosition()],
                        mDetailsView.getText().toString(),
                        new Date()
                );
                calendDao.addAppointment(calendarItem);
            } else {
                // update
                CalendarItem calendarItem = mItem;
                mItem.location = locations[mLocationView.getSelectedItemPosition()];
                mItem.details = mDetailsView.getText().toString();
                calendDao.updateAppointment(calendarItem);
            }

        });

        // spinner setup
        ArrayAdapter ad = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                locations);

        ad.setDropDownViewResource( android.R.layout .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the spinner which binds data to spinner
        mLocationView.setAdapter(ad);

        // Show the details as text in a TextView & in the toolbar if available.
        updateContent();
        rootView.setOnDragListener(dragListener);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (mItem != null) {
            mDetailsView.setText(mItem.details);
            //mLocationView.setText(mItem.location);

            mLocationView.setSelection(getPosition(mItem.location));

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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