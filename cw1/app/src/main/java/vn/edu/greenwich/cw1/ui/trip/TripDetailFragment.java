package vn.edu.greenwich.cw1.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomappbar.BottomAppBar;
import vn.edu.greenwich.cw1.R;
import vn.edu.greenwich.cw1.database.TripDAO;
import vn.edu.greenwich.cw1.models.TripItem;
import vn.edu.greenwich.cw1.models.Trip;
import vn.edu.greenwich.cw1.ui.dialog.DeleteConfirmFragment;
import vn.edu.greenwich.cw1.ui.tripitem.TripItemCreateFragment;
import vn.edu.greenwich.cw1.ui.tripitem.list.TripItemListFragment;

public class TripDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, TripItemCreateFragment.FragmentListener {
    public static final String ARG_PARAM_Trip = "trip";

    protected TripDAO _db;
    protected Trip _trip;
    protected Button fmTripDetailTripListButton,fmTripListCreateButtonAdd;
    protected BottomAppBar fmTripDetailBottomAppBar;
    protected FragmentContainerView fmTripDetailTripListList;
    protected TextView fmTripDetailName, fmTripDetailStartDate, fmTripDetailOwner,Destination,Description,current_amount;

    public TripDetailFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        fmTripDetailName = view.findViewById(R.id.fmTripDetailName);
        fmTripDetailStartDate = view.findViewById(R.id.fmTripDetailStartDate);
        fmTripDetailOwner = view.findViewById(R.id.fmTripDetailOwner);
        fmTripDetailBottomAppBar = view.findViewById(R.id.fmTripDetailBottomAppBar);
        fmTripDetailTripListButton = view.findViewById(R.id.fmTripDetailTripListButton);
        fmTripDetailTripListList = view.findViewById(R.id.fmTripDetailTripListList);
        Destination = view.findViewById(R.id.destination_label);
        Description = view.findViewById(R.id.description_label);
        current_amount = view.findViewById(R.id.amount_label);
        fmTripDetailBottomAppBar.setOnMenuItemClickListener(item -> menuItemSelected(item));
        fmTripDetailTripListButton.setOnClickListener(v -> showAddTripListFragment());



        showDetails();
        showTripListList();

        return view;
    }

    protected void showDetails() {
        String name = getString(R.string.error_not_found);
        String destination = getString(R.string.error_not_found);
        String description = getString(R.string.error_not_found);
        String startDate = getString(R.string.error_not_found);
        String current_ammout = getString(R.string.error_not_found);
        String owner = getString(R.string.error_not_found);


        if (getArguments() != null) {
            long tripid;
            System.out.println(getArguments().get("trip"));
            if(getArguments().getLong("trip") != 0){
                tripid = getArguments().getLong("trip");

            }else{
                _trip = (Trip) getArguments().getSerializable(ARG_PARAM_Trip);
               tripid = _trip.getId();

            }
           // Retrieve data from Database.
            _trip = _db.getTripById(tripid);
            name = _trip.getName();
            startDate = _trip.getStartDate();
            destination = _trip.getDestination()   ;
            description = _trip.getDescription();
            current_ammout = String.valueOf(_trip.getCurrentAmout());
            owner = _trip.getOwner() == 1 ? getString(R.string.label_owner) : getString(R.string.label_tenant);
        }
        Destination.setText(destination);
        Description.setText(description);
        fmTripDetailName.setText(name);
        current_amount.setText(current_ammout +" VND");
        fmTripDetailStartDate.setText(startDate);
        fmTripDetailOwner.setText(owner);
    }

    protected void showTripListList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(TripItemListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

            // Send arguments (trip id) to TripItemListFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.TripUpdateFragment:
                showUpdateFragment();
                return true;

            case R.id.TripDeleteFragment:
                showDeleteConfirmFragment();
                return true;
        }

        return true;
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_trip != null) {
            bundle = new Bundle();
            bundle.putSerializable(TripUpdateFragment.ARG_PARAM_TRIP, _trip);
        }

        Navigation.findNavController(getView()).navigate(R.id.TripUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddTripListFragment() {
        new TripItemCreateFragment(_trip.getId()).show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _trip != null) {
            long numOfDeletedRows = _db.deleteTrip(_trip.getId());

            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();

                return;
            }
        }

        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendFromTripListCreateFragment(TripItem tripItem) {
        if (tripItem != null) {
            tripItem.setTripId(_trip.getId());

            long id = _db.insertTripList(tripItem);

            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();

            reloadTripListList();
        }
    }

    public void reloadTripListList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TripItemListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fmTripDetailTripListList, TripItemListFragment.class, bundle)
                .commit();
    }

}