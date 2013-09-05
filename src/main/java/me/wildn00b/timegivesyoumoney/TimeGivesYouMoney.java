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
package me.wildn00b.timegivesyoumoney;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.wildn00b.timegivesyoumoney.command.TGYMCommand;
import me.wildn00b.timegivesyoumoney.io.Bank;
import me.wildn00b.timegivesyoumoney.io.Language;
import me.wildn00b.timegivesyoumoney.io.Settings;
import me.wildn00b.timegivesyoumoney.io.Vault;
import me.wildn00b.timegivesyoumoney.listener.BlockListener;
import me.wildn00b.timegivesyoumoney.listener.PlayerListener;

import org.bukkit.plugin.java.JavaPlugin;

public class TimeGivesYouMoney extends JavaPlugin {

  public static final int ONE_DAY_IN_TICKS = 20 * 60 * 60 * 24;
  public static final int ONE_MINUTE_IN_TICKS = 20 * 60;

  public HashMap<String, Long> afkTimer = new HashMap<String, Long>();

  public Bank Bank = null;

  public Language Lang = null;
  public Logger Log = Logger.getLogger("Minecraft");
  public Settings Settings = null;
  public Vault Vault = null;

  public String Version;

  @Override
  public void onDisable() {
    if ((Boolean) Settings._("SaveProgressOnShutdown", true))
      Bank.Save();
    Log.log(Level.INFO, Lang._("TimeGivesYouMoney.Disable"));
  }

  @Override
  public void onEnable() {
    Settings = new Settings(this);
    Lang = new Language(this);
    Vault = new Vault(this);

    if (Bank == null)
      Bank = new Bank(this);

    getCommand("tgym").setExecutor(new TGYMCommand(this));
    getServer().getPluginManager().registerEvents(new PlayerListener(this),
        this);
    getServer().getPluginManager()
        .registerEvents(new BlockListener(this), this);

    Version = getDescription().getVersion();

    getServer().getScheduler().scheduleSyncRepeatingTask(this,
        new MoneyGiver(this), 0, ONE_MINUTE_IN_TICKS);

    getServer().getScheduler().scheduleSyncRepeatingTask(this,
        new ClearDay(this), 0, ONE_DAY_IN_TICKS);

    try {
      final Metrics metrics = new Metrics(this);
      metrics.findCustomData();
      metrics.start();
    } catch (final Exception e) {
    }

    Log.log(Level.INFO, Lang._("TimeGivesYouMoney.Enable"));
  }

  public void Reload() {
    Settings = new Settings(this);
    Lang = new Language(this);
  }

}
