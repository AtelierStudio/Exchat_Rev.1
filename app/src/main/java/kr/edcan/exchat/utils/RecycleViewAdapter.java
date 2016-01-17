package kr.edcan.exchat.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.edcan.exchat.R;
import kr.edcan.exchat.data.HistoryData;

/**
 * Created by Junseok on 2016. 1. 11..
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    Context context;
    ArrayList<HistoryData> items;

    public RecycleViewAdapter(Context context, ArrayList<HistoryData> items) {
        this.context=context;
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview_content,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryData item = items.get(position);
        Log.e("sexonthebeach", "Setting View for "+ position +"th");
        holder.prevValue.setText(item.getPrevValue());
        holder.prevUnit.setText(item.getPrevUnit());
        holder.convertUnit.setText(item.getConvertUnit());
        holder.convertValue.setText(item.getConvertValue());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prevValue, convertValue, prevUnit, convertUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            prevValue = (TextView)itemView.findViewById(R.id.prevValue);
            prevUnit = (TextView)itemView.findViewById(R.id.prevUnit);
            convertValue = (TextView)itemView.findViewById(R.id.convertValue);
            convertUnit = (TextView)itemView.findViewById(R.id.convertUnit);
        }
    }
}