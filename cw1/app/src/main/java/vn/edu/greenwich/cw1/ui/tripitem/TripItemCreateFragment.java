package vn.edu.greenwich.cw1.ui.tripitem;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.fragment.app.FragmentResultOwner;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import vn.edu.greenwich.cw1.R;
import vn.edu.greenwich.cw1.database.TripEntry;
import vn.edu.greenwich.cw1.database.TripDAO;
import vn.edu.greenwich.cw1.models.TripItem;
import vn.edu.greenwich.cw1.models.Trip;
import vn.edu.greenwich.cw1.ui.dialog.DatePickerFragment;
import vn.edu.greenwich.cw1.ui.dialog.TimePickerFragment;
import vn.edu.greenwich.cw1.ui.trip.TripDetailFragment;


public class TripItemCreateFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener{
    protected long _TripId;
    protected TripDAO _db;
    FragmentManager fragmentManager;
    TripDetailFragment frg = new TripDetailFragment();

    protected EditText fmTripListCreateDate, fmTripListCreateTime, fmTripListCreateContent,amount;
    protected Button fmTripListCreateButtonCancel, fmTripListCreateButtonAdd;
    protected Spinner fmTripListCreateType;
    protected TextView current_amount;

    public TripItemCreateFragment() {
        _TripId = -1;
    }

    public TripItemCreateFragment(long TripId) {
        _TripId = TripId;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        _db = new TripDAO(getContext());
        fragmentManager =getActivity().getSupportFragmentManager();

    }
    @Override
    public void sendFromDatePickerFragment(String date) {
        fmTripListCreateDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) {
        fmTripListCreateTime.setText(time);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tripitem_create, container, false);


        fmTripListCreateDate = view.findViewById(R.id.fmTripListCreateDate);
        fmTripListCreateTime = view.findViewById(R.id.fmTripListCreateTime);
        fmTripListCreateContent = view.findViewById(R.id.fmTripListCreateContent);
        fmTripListCreateButtonCancel = view.findViewById(R.id.fmTripListCreateButtonCancel);
        fmTripListCreateButtonAdd = view.findViewById(R.id.fmTripListCreateButtonAdd);
        fmTripListCreateType = view.findViewById(R.id.fmTripListCreateType);
        amount = view.findViewById(R.id.amount);
        fmTripListCreateButtonCancel.setOnClickListener(v -> dismiss());
        fmTripListCreateButtonAdd.setOnClickListener(v -> createTripList());
        current_amount = view.findViewById(R.id.amount_label);
        fmTripListCreateDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        fmTripListCreateTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        setTypeSpinner();

        return view;
    }

    protected void setTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.TripList_type,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmTripListCreateType.setAdapter(adapter);
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected void createTripList() {
        TripItem tripItem = new TripItem();
        Trip trip = new Trip();
        trip.setCurrentAmout(Long.parseLong(amount.getText().toString()));

        String query = "UPDATE "+ TripEntry.TABLE_NAME +" SET "+ TripEntry.COL_Current_Amount+ " = "+ TripEntry.COL_Current_Amount+"+ "+String.valueOf(amount.getText().toString())+" WHERE id= "+_TripId+";";
        _db.updatedb(query);
        tripItem.setType(fmTripListCreateType.getSelectedItem().toString());
        tripItem.setTime(fmTripListCreateTime.getText().toString());
        tripItem.setamount(Long.parseLong(amount.getText().toString()));
        tripItem.setDate(fmTripListCreateDate.getText().toString());
        tripItem.setContent(fmTripListCreateContent.getText().toString());
        Bundle args = new Bundle();
        args.putLong("trip",_TripId);
        frg.setArguments(args);
        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripListCreateFragment(tripItem);
        fragmentManager.findFragmentById(R.id.frg_trip_detail);

        fragmentManager.beginTransaction().replace(R.id.frg_trip_detail,frg).detach(frg).attach(frg).commit();


        dismiss();
    }

    public interface FragmentListener {
        void sendFromTripListCreateFragment(TripItem tripItem);
    }
}