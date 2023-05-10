package org.bulat5280.airdrop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static org.bulat5280.airdrop.AirDrop.items;

public class clearCommand implements CommandExecutor {
    FileConfiguration config;

    public clearCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (((Player) sender).getItemInHand() != null) {
                items.clear();
                sender.sendMessage(config.getString("messages.clearSuccess"));
            } else {
                sender.sendMessage(config.getString("messages.clearFailed"));
            }
        } catch (Exception ex) {
            sender.sendMessage(config.getString("messages.clearFailed"));
        }
        return true;
    }
}
