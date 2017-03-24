package com.github.bagiasn.morpheus.model;

import java.util.ArrayList;

/**
 * This class represents the command.
 * It could be easily replaced by an array of Strings, but this way
 * is more expandable.
 */

public class Command {
    public String name;

    private Command(String name) {
        this.name = name;
    }

    public static ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new Command("SLEEP"));
        commands.add(new Command("RESTART"));
        commands.add(new Command("SHUTDOWN"));

        return commands;
    }
}
