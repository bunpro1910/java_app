package vn.edu.greenwich.cw1.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import vn.edu.greenwich.cw1.R;
import vn.edu.greenwich.cw1.database.TripDAO;
import vn.edu.greenwich.cw1.models.Trip;
import vn.edu.greenwich.cw1.ui.dialog.CalendarFragment;

public class TripRegisterFragment extends Fragment
        implements TripRegisterConfirmFragment.FragmentListener, CalendarFragment.FragmentListener {
    public static final String ARG_PARAM_Trip = "trip";

    protected EditText fmTripRegisterName, fmTripRegisterStartDate;
    protected LinearLayout fmTripRegisterLinearLayout;
    protected SwitchMaterial fmTripRegisterOwner;
    protected TextView fmTripRegisterError;
    protected Button fmTripRegisterButton;
    protected TextView description;
    protected TextView destination;
    protected TripDAO _db;

    public TripRegisterFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trip_register, container, false);

        fmTripRegisterError = view.findViewById(R.id.fmTripRegisterError);
        fmTripRegisterName = view.findViewById(R.id.fmTripRegisterName);
        fmTripRegisterStartDate = view.findViewById(R.id.fmTripRegisterStartDate);
        fmTripRegisterOwner = view.findViewById(R.id.fmTripRegisterOwner);
        description = view.findViewById(R.id.description);
        destination = view.findViewById(R.id.destination);
        fmTripRegisterButton = view.findViewById(R.id.fmTripRegisterButton);
        fmTripRegisterLinearLayout = view.findViewById(R.id.fmTripRegisterLinearLayout);

        // Show Calendar for choosing a date.
        fmTripRegisterStartDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        // Update current trip.
        if (getArguments() != null) {
            Trip trip = (Trip) getArguments().getSerializable(ARG_PARAM_Trip);

            fmTripRegisterName.setText(trip.getName());
            fmTripRegisterStartDate.setText(trip.getStartDate());
            fmTripRegisterOwner.setChecked(trip.getOwner() == 1 ? true : false);
            destination.setText(trip.getDestination());
            description.setText(trip.getDescription());
            fmTripRegisterButton.setText(R.string.label_update);
            fmTripRegisterButton.setOnClickListener(v -> update(trip.getId()));

            return view;
        }

        // Create new trip.
        fmTripRegisterButton.setOnClickListener(v -> register());

        return view;
    }

    protected void register() {
        if (isValidForm()) {
            Trip trip = getTripFromInput(-1);

            new TripRegisterConfirmFragment(trip).show(getChildFragmentManager(), null);

            return;
        }

        moveButton();
    }

    protected void update(long id) {
        if (isValidForm()) {
            Trip trip = getTripFromInput(id);

            long status = _db.updateTrip(trip);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromTripRegisterFragment(status);

            return;
        }

        moveButton();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected Trip getTripFromInput(long id) {
        String name = fmTripRegisterName.getText().toString();
        String startDate = fmTripRegisterStartDate.getText().toString();
        int owner = fmTripRegisterOwner.isChecked() ? 1 : 0;
        String Description = description.getText().toString();
        String Destination = destination.getText().toString();
        return new Trip(id, name, startDate, owner,Destination,Description);
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        String name = fmTripRegisterName.getText().toString();
        String startDate = fmTripRegisterStartDate.getText().toString();
        String Destination = destination.getText().toString();
        String Description = description.getText().toString();
        if (name == null || name.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }
        if (Destination == null || Destination.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_destination) + "\n";
            isValid = false;
        }if (Description == null || Description.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_description) + "\n";
            isValid = false;
        }
        if (startDate == null || startDate.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_start_date) + "\n";
            isValid = false;
        }

        fmTripRegisterError.setText(error);

        return isValid;
    }

    protected void moveButton() {
        LinearLayout.LayoutParams btnParams = (LinearLayout.LayoutParams) fmTripRegisterButton.getLayoutParams();

        int linearLayoutPaddingLeft = fmTripRegisterLinearLayout.getPaddingLeft();
        int linearLayoutPaddingRight = fmTripRegisterLinearLayout.getPaddingRight();
        int linearLayoutWidth = fmTripRegisterLinearLayout.getWidth() - linearLayoutPaddingLeft - linearLayoutPaddingRight;

        btnParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnParams.topMargin += fmTripRegisterButton.getHeight();
        btnParams.leftMargin = btnParams.leftMargin == 0 ? linearLayoutWidth - fmTripRegisterButton.getWidth() : 0;

        fmTripRegisterButton.setLayoutParams(btnParams);
    }

    @Override
    public void sendFromTripRegisterConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmTripRegisterName.setText("");
                fmTripRegisterStartDate.setText("");
                destination.setText("");
                description.setText("");
                fmTripRegisterName.requestFocus();
        }
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmTripRegisterStartDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromTripRegisterFragment(long status);
    }
}