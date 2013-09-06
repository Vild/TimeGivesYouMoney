/*
 * TimeGivesYouMoney - Gives players money every time interval
 * Copyright (C) 2013 Dan Printzell
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package me.wildn00b.timegivesyoumoney.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import me.wildn00b.timegivesyoumoney.TimeGivesYouMoney;

import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

  private final YamlConfiguration file;
  private File path;

  public Language(TimeGivesYouMoney tgym) {
    file = new YamlConfiguration();
    final String partpath = tgym.getDataFolder().getAbsolutePath()
        + File.separator + "lang" + File.separator;

    try {
      path = new File(partpath + tgym.Settings._("Language", "en-US") + ".yml");
      if (path.exists())
        file.load(path);
      else {
        tgym.Log
            .log(Level.WARNING,
                "[TimeGivesYouMoney] Couldn't find language file, reverting to en-US");
        path = new File(partpath + "en-US.yml");
      }
      addDefaults();
      file.save(path);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public String _(String path) {
    if (file.contains(path))
      return file.getString(path);
    else
      return path;
  }

  private void addDefaults() {
    final HashMap<String, String> list = new HashMap<String, String>();

    list.put("TimeGivesYouMoney.Enable", "Enabled successfully.");
    list.put("TimeGivesYouMoney.Disable", "Disabled successfully.");
    list.put("Vault.NotFound", "Vault not found! Disable self!");
    list.put("Vault.PermissionsNotFound",
        "Failed to permissions from Vault! Have you got a permission plugin installed?");
    list.put("Vault.EconomyNotFound",
        "Failed to economy from Vault! Have you got a economy plugin installed?");

    list.put("Command.Title",
        "TimeGivesYouMoney V%VERSION% Page %PAGE%/%MAXPAGE% by %AUTHOR%");

    list.put("Command.Must",
        "<Option> - Means that this option is a must for the command.");
    list.put("Command.Optional",
        "[Option] - Means that this option is optional for the command.");

    list.put("Command.FindNoPlayer", "Couldn't find %PLAYER%");

    list.put("Command.Help.Help", "- Shows this help.");
    list.put("Command.Help.Reload", "- Reloads the config.");
    list.put("Command.Help.Stats.Self",
        "- Shows how much money you haven't cashout yet.");
    list.put("Command.Help.Stats.Other",
        "[Player] - Shows how much money [Player] haven't cashout yet.");
    list.put("Command.Help.Cashout.Self",
        "- Cash out your money to your bank account.");
    list.put("Command.Help.Cashout.Other",
        "[Player] - Cash out [Player]s money to their bank account.");
    list.put("Command.Help.Add",
        "<Player> <Money> - Add cash to <Player>s TGYM account.");
    list.put("Command.Help.Remove",
        "<Player> <Money> - Remove cash from <Player>s TGYM account.");

    list.put("Command.Reload.Success", "Reloaded successfully.");

    list.put(
        "Command.Cashout.Success.Self",
        "Cashed out '%MONEY%' to your account. You earn that for playing for %TIME% minutes.");
    list.put(
        "Command.Cashout.Success.Other",
        "Cashed out '%MONEY%' to %PLAYER%s account. %PLAYER% earn that for playing for %TIME% minutes.");

    list.put("Command.Stats.Self",
        "You got %MONEY% for playing for %TIME% minutes.");
    list.put("Command.Stats.Other",
        "%PLAYER% got %MONEY% for playing for %TIME% minutes.");

    list.put("Command.Cashout.Failed", "Failed to cashout the money.");

    list.put("Command.Add.Success", "Added '%MONEY%' to %PLAYERS% account.");

    list.put("Command.Remove.Success",
        "Removed '%MONEY%' from %PLAYERS% account.");
    for (final Entry<String, String> entry : list.entrySet())
      if (!file.contains(entry.getKey()))
        file.set(entry.getKey(), entry.getValue());
  }
}