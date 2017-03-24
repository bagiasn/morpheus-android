package com.github.bagiasn.morpheus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.bagiasn.morpheus.R;
import com.github.bagiasn.morpheus.adapter.CommandAdapter;
import com.github.bagiasn.morpheus.model.Command;

import java.util.ArrayList;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the recycler view.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCommands);
        // Get a static list of commands
        ArrayList<Command> commands = Command.getCommands();
        // Create the adapter with the static list.
        CommandAdapter commandAdapter = new CommandAdapter(this, commands);
        // Set the adapter.
        recyclerView.setAdapter(commandAdapter);
        // Set the layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
