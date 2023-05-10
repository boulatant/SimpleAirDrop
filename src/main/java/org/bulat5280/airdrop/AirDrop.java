package org.bulat5280.airdrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public final class AirDrop extends JavaPlugin {
    public static List<SerializableItemStack> items = new ArrayList<>();
    public static ItemStack empty;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        empty = new ItemStack(Material.PAPER);
        ItemMeta empty_meta = empty.getItemMeta();
        empty_meta.setDisplayName(getConfig().getString("empty.name"));
        empty_meta.setLore(getConfig().getStringList("empty.description"));
        empty.setItemMeta(empty_meta);
        getCommand("add").setExecutor(new addCommand(getConfig()));
        getCommand("remove").setExecutor(new removeCommand(getConfig()));
        getCommand("clear").setExecutor(new clearCommand(getConfig()));
        try {
            File data = new File("airdrop.mmap");
            FileInputStream fis = new FileInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (List<SerializableItemStack>) ois.readObject();
            fis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            getLogger().log(Level.SEVERE, e.getMessage());

        }
        // Plugin startup logic
        getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
            @Override
            public void run() {
                World world = getServer().getWorld("world");
                Location pos = new Location(world, Math.random() * getConfig().getDouble("distance") * 2.0 - getConfig().getDouble("distance"), 255.0, Math.random() * getConfig().getDouble("distance") * 2.0 - getConfig().getDouble("distance"), 0.0f, 0.0f);
                for (int i = 255; i > 0; i--) {
                    if (!world.getBlockAt(pos).getType().equals(Material.AIR)) {
                        pos = pos.add(0, 1, 0);
                        break;
                    } else {
                        pos = pos.add(0, -1, 0);
                    }
                }
                pos.getBlock().setType(Material.CHEST);
                Chest chest = (Chest) pos.getBlock().getState();
                chest.getInventory().setItem(13, empty);
                if (items.isEmpty()) {
                    chest.getInventory().setItem(13, empty);
                } else {
                    for (int i = 0; i < 27; i++) {
                        if (Math.random() < getConfig().getDouble("chance")) {
                            chest.getInventory().setItem(i, items.get((int) (Math.random() * items.size())).toItemStack());
                        }
                    }
                }
                chest.setCustomName(getConfig().getString("name"));
                for (Player p : getServer().getOnlinePlayers()) {
                    p.sendMessage(getConfig().getString("messages.spawned") + " " + pos.getBlockX() + " " + pos.getBlockY() + " " + pos.getBlockZ());
                }
            }
        }, getConfig().getLong("interval"), getConfig().getLong("interval"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            File data = new File("airdrop.mmap");
            FileOutputStream fos = new FileOutputStream(data);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // Запись объектов в файл
            oos.writeObject(items);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    // Save Object Into A File
    //
    public void saveObject(Object obj, String path) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    //
    // Load Object From A File
    //
    public Object loadObject(String path) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Object result = ois.readObject();
        ois.close();
        return result;
    }
}
