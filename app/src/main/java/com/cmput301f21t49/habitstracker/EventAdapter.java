import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class EventAdapter extends ArrayList<Event> {

    ArrayList<Event> tempList;


    public EventAdapter(ArrayList<Event> tempList, Context context) {
        super(context, 0, tempList);
        this.tempList = tempList;
    }

    @Override
    @NonNull
    public View getView(int position, @NonNull ViewGroup parent, @Nullable View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todays_events, parent, false);
        }

        return v;
    }


}



