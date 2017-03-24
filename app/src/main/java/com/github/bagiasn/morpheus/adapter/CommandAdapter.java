package com.github.bagiasn.morpheus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.CircularProgressButton;
import com.github.bagiasn.morpheus.R;
import com.github.bagiasn.morpheus.task.SendCommand;
import com.github.bagiasn.morpheus.model.Command;

import java.util.List;

/**
 * Adapter of the recycler view.
 */

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircularProgressButton commandButton;
        private ViewHolder(View itemView) {
            super(itemView);
            commandButton = (CircularProgressButton) itemView.findViewById(R.id.btnCommand);
            commandButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // If the progress state is idle(0), send the corresponding command.
            // If the progress state is completed(100) or there was an error(-1), restart the progress.
            // Otherwise, do nothing.
            int progress = commandButton.getProgress();
            switch (progress) {
                case 0:
                    new SendCommand(v.getContext(), commandButton).execute();
                    break;
                case -1:
                case 100:
                    commandButton.setProgress(0);
                    break;
                default:
                    break;
            }
        }
    }

    private List<Command> commandsList;
    private Context context;

    public CommandAdapter(Context context, List<Command> commandsList) {
        this.context = context;
        this.commandsList = commandsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the row layout
        View commandView = inflater.inflate(R.layout.recycler_row, parent, false);
        // Return a new holder instance
        return new ViewHolder(commandView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the corresponding command
        Command command = commandsList.get(position);
        // Set the button's title
        CircularProgressButton button = holder.commandButton;
        // We need to set the current text too, otherwise it is empty the first time it appears.
        button.setText(command.name);
        button.setIdleText(command.name);
    }

    @Override
    public int getItemCount() {
        return commandsList.size();
    }
}
