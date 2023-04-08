package com.example.ajutt_mycarbonfootprint;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

//most concepts here are taken from Lab3 Participation
public class Details extends DialogFragment {
    //create an interface
    interface AddDetailsDialogListener {
        void addDetails(Travel travel);
        void updateDetails(Travel travel,
                           String nameStr,
                           String fuelTypeStr,
                           LocalDate dateLD,
                           Double amountD,
                           Double PPL,
                           Double fuelPrice,
                           Integer footprint);

    }

    //add that interface into the fragment
    private AddDetailsDialogListener listener;

    //this helps us remember any value kept when we decide to edit an exiting list
    static Details newInstance(Travel travel){
        Bundle args = new Bundle();
        args.putSerializable("travel", travel);

        Details details = new Details();
        details.setArguments(args);
        return details;
    }


    //checkers, every input provided must be validated here
    private Boolean checkFuelType(String fuelType){
        if (fuelType.equalsIgnoreCase("g") || fuelType.equalsIgnoreCase("gasoline")) {
            return true;
        } else if (fuelType.equalsIgnoreCase("diesel") || fuelType.equalsIgnoreCase( "d")) {
            return true;
        }


        return false;
    }
    private Boolean checkPPLAmount(Double amount, Double PPL){
        if (PPL < 0.0){
            return false;
        } else if (amount < 0.0){
            return false;
        }
        return true;
    }
    private Boolean checkName(String name){
        return name.length() <= 30 && name.length() != 0;
    }

    private LocalDate checkDate(LocalDate date, String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            date = LocalDate.parse(dateStr, formatter);
        } catch(Exception e){
            return null;
        }

        return date;
    }

    //create a footprint by taking the amount and fuelType, a footprint is provided j to help convert the values in case
    private Integer createFootprint(Integer footprint, String fuelType, Double amount){
        double x = 0.0;
        if (fuelType.equalsIgnoreCase("g") || fuelType.equalsIgnoreCase("gasoline")) {
            x = 2.32 * amount;
        } else if (fuelType.equalsIgnoreCase("d") || fuelType.equalsIgnoreCase("diesel")) {
            x = 2.69 * amount;
        }
        footprint = (int) x;
        return footprint;
    }

    //toast error, if any checker fails we call this
    private void showToast(Toast error, String text){
        error = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        error.show();
    }

    //checks to see if the fragment exists
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddDetailsDialogListener){
            listener = (AddDetailsDialogListener) context;
        } else {
            throw new RuntimeException(context + " ,must implement AddDetailsDialogListener");

        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.list_indepth, null);
        EditText editName = view.findViewById(R.id.editName);
        EditText editDate = view.findViewById(R.id.editDate);
        EditText editFuelType = view.findViewById(R.id.editFuelType);
        EditText editPPL = view.findViewById(R.id.editPPL);
        EditText editAmount = view.findViewById(R.id.editAmount);
        TextView viewFuelCost = view.findViewById(R.id.fuelPrice);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Toast error = null;

        //if we are editing a existing list we get all the values provided with a 2 decimal double int
        if(getArguments() != null){
            Travel travel = (Travel) getArguments().getSerializable("travel");
            editName.setText(travel.getName());
            editDate.setText(travel.getDate().toString());
            editFuelType.setText(travel.getFuelType());
            editPPL.setText(String.format("%.2f", travel.getPPL()));
            editAmount.setText(String.format("%.2f",travel.getAmount()));
            viewFuelCost.setText(String.format("%.2f", travel.getFuelCost()));
        }
        return builder.setView(view)
                .setTitle("Please enter the fields")
                .setPositiveButton("Enter", (dialog, which) -> {
                    //if we are making a new list
                    if(getArguments() == null) {
                        //get texts
                        String name = editName.getText().toString().trim();
                        String dateStr = editDate.getText().toString().trim();
                        String fuelType = editFuelType.getText().toString().trim();
                        String PPLStr = editPPL.getText().toString();
                        String fuelCostStr = viewFuelCost.getText().toString().trim();
                        String editAmountStr = editAmount.getText().toString().trim();
                        Double PPL = Double.parseDouble(PPLStr);
                        Double amount = Double.parseDouble(editAmountStr);
                        Double pricePaid = Double.parseDouble(fuelCostStr);
                        //initialize variables to use
                        Integer footprint = 0;
                        Boolean checker;

                        //needed a way to get only a correct format of date as well as a Date class,
                        //Found this using
                        //https://www.baeldung.com/java-creating-localdate-with-values
                        LocalDate date = null;

                        //check the name to see if its 0 < x < 30 chars

                        if (!(checker = checkName(name))){
                            showToast(error, "Please enter a name with a description no more of 30 characters");
                            return;
                        }

                        //check the date to see if its a valid input
                        date = checkDate(date, dateStr);
                        if (date == null){
                            showToast(error, "Please enter the correct date format");
                            return;
                        }

                        //check fueltype to be gasoline, g, diesel or d

                        if (!(checker = checkFuelType(fuelType))){
                            showToast(error, "Please enter the correct fuel types");
                            return;
                        }

                        if(!(checker = checkPPLAmount(amount, PPL))){
                            showToast(error, "Please enter non-negative values.");
                            return;
                        }


                        //create footprint with the values provided
                        footprint = createFootprint(footprint, fuelType, amount);
                        pricePaid = amount * PPL;
                        //add those details into the list view and update the footprint
                        listener.addDetails(new Travel(name, fuelType, date, PPL, amount, pricePaid, footprint));
                        ((MainActivity)getActivity()).updateTotalFootprint(footprint, true);
                        ((MainActivity)getActivity()).updateTotalFuelCost(pricePaid, true);

                    } else {
                        //get texts
                        String name = editName.getText().toString().trim();
                        String dateStr = editDate.getText().toString().trim();
                        String fuelType = editFuelType.getText().toString().trim();
                        String PPLStr = editPPL.getText().toString();
                        String editAmountStr = editAmount.getText().toString().trim();
                        String fuelCostStr = viewFuelCost.getText().toString().trim();
                        Double PPL = Double.parseDouble(editPPL.getText().toString());
                        Double amount = Double.parseDouble(editAmount.getText().toString().trim());
                        double costOfFuel = Double.parseDouble(fuelCostStr);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        //initialize variables to use
                        Integer footprint = 0;
                        Boolean checker;
                        LocalDate date = null;
                        Travel travel = (Travel) getArguments().getSerializable("travel");

                        editName.setText(travel.getName());
                        editDate.setText(travel.getDate().format(formatter));
                        editFuelType.setText(travel.getFuelType());
                        editPPL.setText(String.format("%.2f", travel.getPPL()));
                        editAmount.setText(String.format("%.2f",travel.getAmount()));
                        viewFuelCost.setText(String.format("%.2f", travel.getFuelCost()));


                        //check the name to see if its 0 < x < 30 chars
                        if (!(checker = checkName(name))){
                            showToast(error, "Please enter a name with a description no more of 30 characters");
                            return;
                        }

                        //check the date to see if its a valid input
                        date = checkDate(date, dateStr);
                        if (date == null){
                            showToast(error, "Please enter the correct date format");
                            return;
                        }

                        //check fueltype to be gasoline, g, diesel or d

                        if (!(checker = checkFuelType(fuelType))){
                            showToast(error, "Please enter the correct fuel types");
                            return;
                        }

                        if(!(checker = checkPPLAmount(amount, PPL))){
                            showToast(error, "Please enter non-negative values.");
                            return;
                        }
                        //first subtract the initial footprint
                        ((MainActivity)getActivity()).updateTotalFootprint(travel.getFootprint(), false);
                        ((MainActivity)getActivity()).updateTotalFuelCost(travel.getFuelCost(), false);

                        //create footprint with the values provided
                        footprint = createFootprint(footprint, fuelType, amount);
                        //get cost of Fuel
                        costOfFuel = amount * PPL;
                        listener.updateDetails(travel,name,fuelType,date,amount, PPL, costOfFuel, footprint);
                        //then with the updated travel re-update the total footprint
                        ((MainActivity)getActivity()).updateTotalFootprint(travel.getFootprint(), true);
                        ((MainActivity)getActivity()).updateTotalFuelCost(travel.getFuelCost(), true);

                    }



                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}
