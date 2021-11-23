package com.cmput301f21t49.habitstracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Fragment class to add/edit/view habits
 * @author  Purvi S.
 */
public class AddHabitFragment extends DialogFragment {
    private EditText habit_title;
    private EditText habit_reason;
    DatePicker picker;
    CheckBox privateC;
    //Day buttons
    private ToggleButton tSun;
    private ToggleButton tMon;
    private ToggleButton tTue;
    private ToggleButton tWed;
    private ToggleButton tThu;
    private ToggleButton tFri;
    private ToggleButton tSat;
    Habit EditHabit;
    private OnFragmentInteractionListener listener;
    int index;

    public interface OnFragmentInteractionListener {
        void onNewPressed(Habit newHabit);
        void onEditPressed(Habit editMedicine, int i);
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentListener");
        }
    }

    static AddHabitFragment newInstance(Habit habit, int i){
        //To get the Medicine object to be edited using Bundle
        Bundle args = new Bundle();
        args.putSerializable("habit",habit);
        args.putSerializable("position",i);

        AddHabitFragment fragment = new AddHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habits,null);
        habit_title = view.findViewById(R.id.title_edittext);
        habit_reason = view.findViewById(R.id.reason_edittext);
        privateC = view.findViewById(R.id.checkBox);
        picker = view.findViewById(R.id.datePicker1);
        tSun = view.findViewById(R.id.tSun);
        tMon = view.findViewById(R.id.tMon);
        tTue = view.findViewById(R.id.tTue);
        tWed = view.findViewById(R.id.tWed);
        tThu = view.findViewById(R.id.tThur);
        tFri = view.findViewById(R.id.tFri);
        tSat = view.findViewById(R.id.tSat);

        Bundle arg = getArguments();

        privateC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (arg!=null){
            EditHabit = (Habit) arg.getSerializable("habit");
            index = (int) arg.getSerializable("position");
            habit_title.setText(EditHabit.getName());
            if (EditHabit.getReason()!=""){
                habit_reason.setText(EditHabit.getReason());
            }

            if (EditHabit.getPrivateHabit() == Boolean.TRUE){
                privateC.setChecked(true);
            }
            if (EditHabit.getDays().contains("Mon")){
                tMon.setChecked(true);
            }
            if (EditHabit.getDays().contains("Tue")){
                tTue.setChecked(true);
            }
            if (EditHabit.getDays().contains("Wed")){
                tWed.setChecked(true);
            }
            if (EditHabit.getDays().contains("Thur")){
                tThu.setChecked(true);
            }
            if (EditHabit.getDays().contains("Fri")){
                tFri.setChecked(true);
            }
            if (EditHabit.getDays().contains("Sat")){
                tSat.setChecked(true);
            }
            if (EditHabit.getDays().contains("Sun")){
                tSun.setChecked(true);
            }
            if (EditHabit.getStartDate()!=null){
                Calendar calendar = Calendar.getInstance();
                System.out.println(EditHabit.getStartDate());
                calendar.setTime(EditHabit.getStartDate());
                picker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

            }



        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit/View Habit")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = habit_title.getText().toString();
                        String reason = habit_reason.getText().toString();
                        Boolean privateHabit = Boolean.FALSE;
                        if (privateC.isChecked()){
                             privateHabit = Boolean.TRUE;
                        }
                        ArrayList<String> days = new ArrayList<>();
                        if(tSun.isChecked()){
                            days.add("Sun");
                        }
                        if(tMon.isChecked()){
                            days.add("Mon");
                        }
                        if(tTue.isChecked()){
                            days.add("Tue");
                        }
                        if(tWed.isChecked()){
                            days.add("Wed");
                        }
                        if(tThu.isChecked()){
                            days.add("Thur");
                        }
                        if(tFri.isChecked()){
                            days.add("Fri");
                        }
                        if(tSat.isChecked()){
                            days.add("Sat");
                        }
                        Date date = new Date();
                        String sDate = picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (EditHabit!=null){
                            EditHabit.setName(title);
                            EditHabit.setReason(reason);
                            EditHabit.setPrivateHabit(privateHabit);
                            EditHabit.setDays(days);
                            EditHabit.setStartDate(date);
                            listener.onEditPressed(EditHabit, index);
                        }else {
                            listener.onNewPressed(new Habit(title,privateHabit,date,days,reason));
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
