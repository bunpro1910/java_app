package vn.edu.greenwich.cw1.ui.tripitem.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.greenwich.cw1.R;
import vn.edu.greenwich.cw1.models.TripItem;

public class TripItemAdapter extends RecyclerView.Adapter<TripItemAdapter.ViewHolder> implements Filterable {
    protected ArrayList<TripItem> _originalList;
    protected ArrayList<TripItem> _filteredList;
    protected TripItemAdapter.ItemFilter _itemFilter = new TripItemAdapter.ItemFilter();

    public TripItemAdapter(ArrayList<TripItem> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<TripItem> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_tripitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripItem tripItem = _filteredList.get(position);

        holder.listItemRequestDate.setText(tripItem.getDate());
        holder.listItemRequestTime.setText(tripItem.getTime());
        holder.listItemRequestType.setText(tripItem.getType());
        holder.listItemRequestContent.setText(tripItem.getContent());
        holder.listItemRequestAmount.setText(String.valueOf(tripItem.getamount())+" VND");
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView listItemRequestDate, listItemRequestTime, listItemRequestType, listItemRequestContent,listItemRequestAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemRequestDate = itemView.findViewById(R.id.listItemTripItemDate);
            listItemRequestTime = itemView.findViewById(R.id.listItemTripItemTime);
            listItemRequestType = itemView.findViewById(R.id.listItemITripItemType);
            listItemRequestContent = itemView.findViewById(R.id.listItemTripItemContent);
            listItemRequestAmount = itemView.findViewById(R.id.amount);
        }

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<TripItem> list = _originalList;
            final ArrayList<TripItem> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (TripItem tripItem : list) {
                String filterableString = tripItem.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(tripItem);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<TripItem>) results.values;
            notifyDataSetChanged();
        }
    }
}