package me.ender.highlands.exploration.conditions;

import org.bukkit.entity.Player;

public interface IUnlockable {
    IUnlockCondition getCondition();

    boolean isUnlocked(Player player);
}
