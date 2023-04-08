package com.example.ajutt_mycarbonfootprint;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements Details.AddDetailsDialogListener{

    private ArrayList<Travel> travelDataList;
    private TextView totalFootprint;
    private TextView totalFuelCost;
    private FootprintAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        travelDataList = new ArrayList<>();

        ListView travelList = (ListView) findViewById(R.id.travelList);
        adapter = new FootprintAdapter(this, travelDataList);
        travelList.setAdapter(adapter);
        FloatingActionButton update = findViewById(R.id.updateList);
        totalFootprint = (TextView)findViewById(R.id.totalFootprint);
        totalFuelCost = (TextView)findViewById(R.id.totalFuelCost);

        //set a click listener that opens the fragment for the button
        update.setOnClickListener(v -> {
            new Details().show(getSupportFragmentManager(), "EDIT_TEXT");

        });

        //Clicking on the list item to edit it (make sure the user wants to edit it)
        travelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long l) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Edit this item?")
                        .setMessage("Would you like to edit  this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateTotalFootprint(travelDataList.get(index).getFootprint(), false);
                                Details.newInstance(travelDataList.get(index)).show(getSupportFragmentManager(), "EDIT_TEXT") ;
                                updateTotalFootprint(travelDataList.get(index).getFootprint(), true);
                            }
                        })
                        .setNegativeButton("No",  null)
                        .show();



            }
        });

        //on long press we delete the item that the user clicked on (make sure to verify)
        travelList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long l) {


                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("Delete this item?")
                        .setMessage("Would you like to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            //we can update here and remove the values as well
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateTotalFootprint(travelDataList.get(index).getFootprint(), false);
                                updateTotalFuelCost(travelDataList.get(index).getFuelCost(), false);
                                travelDataList.remove(index);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",  null)
                        .show();

                return true;
            }
        });


    }

    //we update the total footprint by taking the textview and storing it into a variable
    // , then adding/subtracting based on the boolean provided
    //after that we simply set the text as the updated integer
    public void updateTotalFootprint(Integer updatedFootprint, Boolean add) {
        int total = Integer.parseInt(totalFootprint.getText().toString());
        if (add) {
            total += updatedFootprint;
            totalFootprint.setText(String.format("%d", total));
        } else {
            total -= updatedFootprint;
            if (total < 0){
                total = 0;
            }
            totalFootprint.setText(String.format("%d", total));
        }
    }

    //Same as updateTotalFootprint but this time with a Double variable
    public void updateTotalFuelCost(Double updatedFuelcost, Boolean add) {
        double total = Double.parseDouble(totalFuelCost.getText().toString());
        if (add) {
            total += updatedFuelcost;
            totalFuelCost.setText(String.format("%.2f", total));
        } else {
            total -= updatedFuelcost;
            if (total < 0){
                total = 0;
            }
            totalFuelCost.setText(String.format("%.2f", total));
        }
    }


    //putting the interface methods,
    @Override
    public void addDetails(Travel travel) {
        adapter.add(travel);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateDetails(Travel travel, String nameStr, String fuelTypeStr, LocalDate dateLD,  Double amountD, Double PPLD, Double fuelCost, Integer footprint) {
        travel.setName(nameStr);
        travel.setFuelType(fuelTypeStr);
        travel.setDate(dateLD);
        travel.setPPL(PPLD);
        travel.setAmount(amountD);
        travel.setFootprint(footprint);
        travel.setFuelCost(fuelCost);
        adapter.notifyDataSetChanged();
    }
}