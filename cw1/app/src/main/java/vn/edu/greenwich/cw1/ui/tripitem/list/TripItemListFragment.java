package vn.edu.greenwich.cw1.ui.tripitem.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import vn.edu.greenwich.cw1.R;
import vn.edu.greenwich.cw1.database.TripDAO;
import vn.edu.greenwich.cw1.models.TripItem;

public class TripItemListFragment extends Fragment {
    public static final String ARG_PARAM_TRIP_ID = "trip_id";

    protected ArrayList<TripItem> _tripItemList = new ArrayList<>();

    protected TripDAO _db;
    protected TextView fmTripListListEmptyNotice;
    protected RecyclerView fmTripListListRecylerView;

    public TripItemListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new TripDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tripitem_list, container, false);

        if (getArguments() != null) {
            TripItem tripItem = new TripItem();
            tripItem.setTripId(getArguments().getLong(ARG_PARAM_TRIP_ID));

            _tripItemList = _db.getTripListList(tripItem, null, false);
        }

        fmTripListListRecylerView = view.findViewById(R.id.fmTripListListRecylerView);
        fmTripListListEmptyNotice = view.findViewById(R.id.fmTripListListEmptyNotice);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        fmTripListListRecylerView.addItemDecoration(dividerItemDecoration);
        fmTripListListRecylerView.setAdapter(new TripItemAdapter(_tripItemList));
        fmTripListListRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show "No TripItem." message.
        fmTripListListEmptyNotice.setVisibility(_tripItemList.isEmpty() ? View.VISIBLE : View.GONE);

        return view;
    }
}