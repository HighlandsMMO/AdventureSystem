package me.ender.highlands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ender.highlands.exploration.book.*;
import me.ender.highlands.exploration.conditions.CitizensUnlock;
import me.ender.highlands.exploration.ExplorationHandler;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.conditions.LocationUnlock;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Core extends JavaPlugin {

    private ExplorationHandler explorationHandler;
    public ExplorationHandler getExplorationHandler() {
        return explorationHandler;
    }

    @Override
    public void onEnable() {
        explorationHandler = new ExplorationHandler(this);
        if(!getDataFolder().exists()) {
            try {
                Files.createDirectory(getDataFolder().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
