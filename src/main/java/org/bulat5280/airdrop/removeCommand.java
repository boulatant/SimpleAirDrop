package org.bulat5280.airdrop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static org.bulat5280.airdrop.AirDrop.items;

public class removeCommand implements CommandExecutor {
    FileConfiguration config;

    public removeCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (((Player) sender).getItemInHand() != null) {
                items.remove(((Player) sender).getItemInHand());
                sender.sendMessage(config.getString("messages.removeSuccess"));
            } else {
                sender.sendMessage(config.getString("messages.removeFailed"));
            }
        } catch (Exception ex) {
            sender.sendMessage(config.getString("messages.removeFailed"));
        }
        return true;
    }
}
