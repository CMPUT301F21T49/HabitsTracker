package com.cmput301f21t49.habitstracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;

public class EditEventOnHistoryFragment extends DialogFragment {

    private EditText name;
    private EditText comment;
    private Event event;
    private int index;
    private int hI;
    private int eI;
    private EditEventOnHistoryFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onEdit(Event e, int hI, int eI, int index);
        void onDelete(Event e, int hI, int eI, int index);
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof EditEventOnHistoryFragment.OnFragmentInteractionListener){
            listener = (EditEventOnHistoryFragment.OnFragmentInteractionListener) context;
        } else{
            if (getParentFragment() != null && getParentFragment() instanceof EditEventOnHistoryFragment.OnFragmentInteractionListener) {
                listener = (EditEventOnHistoryFragment.OnFragmentInteractionListener) getParentFragment();
            }
            else {
                throw new RuntimeException(context.toString()
                        + "must implement OnFragmentListener");
            }

        }
    }




    static EditEventOnHistoryFragment newInstance(Event e, int hI, int eI, int index){
        Bundle args = new Bundle();
        args.putSerializable("event",e);
        args.putSerializable("hPosition", hI);
        args.putSerializable("ePosition", eI);
        args.putSerializable("Position", index);
        EditEventOnHistoryFragment fragment = new EditEventOnHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_events,null);
        name = view.findViewById(R.id.edit_name);
        comment = view.findViewById(R.id.edit_comment);

        Bundle arg = getArguments();

        if (arg!=null){
            event = (Event) arg.getSerializable("event");
            hI = (int) arg.getSerializable("hPosition");
            eI = (int) arg.getSerializable("ePosition");
            index = (int) arg.getSerializable("Position");
            name.setText(event.getName());
            if (event.getComment()!=""){
                comment.setText(event.getComment());
            }

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit/Complete Event")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (event!=null) {
                            listener.onDelete(event, hI, eI, index);
                        }
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String eName = name.getText().toString();
                        String eComment = comment.getText().toString();
                        if (event!=null){
                            event.setName(eName);
                            event.setComment(eComment);
                            listener.onEdit(event, hI, eI, index);
                        }

                    }
                }).create();
    }

    //To resize the Dialog Fragment
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
