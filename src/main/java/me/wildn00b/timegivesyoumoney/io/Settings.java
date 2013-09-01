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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import me.wildn00b.timegivesyoumoney.TimeGivesYouMoney;

import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {

  private final YamlConfiguration file;
  private File path;
  private final TimeGivesYouMoney tgym;

  public Settings(TimeGivesYouMoney tgym) {
    this.tgym = tgym;
    file = new YamlConfiguration();

    try {
      path = new File(tgym.getDataFolder().getAbsolutePath() + File.separator
          + "config.yml");
      if (path.exists())
        file.load(path);

      addDefaults();
      file.save(path);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public Object _(String path, Object value) {
    if (!file.contains(path))
      file.set(path, value);
    return file.get(path);
  }

  public ArrayList<String> GetAvailableGroups() {
    final ArrayList<String> output = new ArrayList<String>();

    for (final String group : file.getConfigurationSection("Group").getKeys(
        false))
      output.add(group);

    return output;
  }

  public void Set(String path, Object value) {
    file.set(path, value);
    try {
      file.save(path);
    } catch (final IOException e) {
    }
  }

  private void addDefaults() {
    final HashMap<String, Object> list = new HashMap<String, Object>();

    list.put("SettingsVersion", 2);
    list.put("Language", "en-US");

    list.put("SaveProgressOnLogout", true);
    list.put("SaveProgressOnShutdown", true);

    list.put("Group.Default.AFKTimeout", (double) 5);
    list.put("Group.Default.MoneyPerMinute", (double) 2);
    list.put("Group.Default.InstantPayout", false);
    list.put("Group.Default.MaxMoneyEarnPerDay", (double) 20000);
    list.put("Group.Default.MaxMoneyEarnPerSession", (double) 10000);

    for (final Entry<String, Object> entry : list.entrySet())
      if (!file.contains(entry.getKey()) || file.equals("SettingsVersion"))
        file.set(entry.getKey(), entry.getValue());

  }
}
