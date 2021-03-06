package com.example.localizationserdar.rewards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localizationserdar.R;
import com.example.localizationserdar.databinding.LocalizationItemBinding;
import com.example.localizationserdar.datamodels.Beacon;

import java.util.List;

import static com.example.localizationserdar.utils.Constants.BEACON_IN_AI_LAB;
import static com.example.localizationserdar.utils.Constants.BEACON_IN_CANTINA;
import static com.example.localizationserdar.utils.Constants.BEACON_IN_LIBRARY;
import static com.example.localizationserdar.utils.Constants.BEACON_IN_TEACHERS_ROOM;

public class MainRewardAdapter extends RecyclerView.Adapter<MainRewardAdapter.MainRewardViewHolder> {

    private Context context;
    private List<Beacon> beaconList;
    private LocalizationItemBinding binding;

    public MainRewardAdapter(Context context, List<Beacon> beaconList) {
        this.context = context;
        this.beaconList = beaconList;
    }

    @NonNull
    @Override
    public MainRewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LocalizationItemBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();
        return new MainRewardViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainRewardAdapter.MainRewardViewHolder holder, int position) {
        // Initialize the Object
        Beacon beacon = beaconList.get(position);
        holder.tvBeaconName.setText(beacon.beaconName);
        holder.tvBeaconDesc.setText(beacon.beaconDesc);
        holder.tvScannedNumber.setVisibility(View.VISIBLE);
        holder.tvScannedNumber.setText("x"+beacon.beaconCount);

        switch (beacon.beaconName) {
            case BEACON_IN_TEACHERS_ROOM:
                holder.ivBeacon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_meeting));
                break;
            case BEACON_IN_AI_LAB:
                holder.ivBeacon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_lab));
                break;
            case BEACON_IN_CANTINA:
                holder.ivBeacon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_cantina));
                break;
            case BEACON_IN_LIBRARY:
                holder.ivBeacon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_lib));
                break;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        binding = null;
    }

    @Override
    public int getItemCount() {
        return beaconList.size();
    }

    public class MainRewardViewHolder extends RecyclerView.ViewHolder {
        TextView tvBeaconName, tvBeaconDesc, tvScannedNumber;
        ImageView ivBeacon;

        public MainRewardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBeaconName = binding.tvTitleRv;
            tvBeaconDesc = binding.tvSubtitle;
            tvScannedNumber = binding.tvScannedTimes;
            ivBeacon = binding.ivBeacon;
        }
    }
}
