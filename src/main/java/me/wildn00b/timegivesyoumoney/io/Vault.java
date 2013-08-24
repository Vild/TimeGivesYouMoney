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

import java.util.ArrayList;
import java.util.logging.Level;

import me.wildn00b.timegivesyoumoney.TimeGivesYouMoney;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {

  private Economy economy;
  private Permission permissions;
  private TimeGivesYouMoney tgym;

  public Vault(TimeGivesYouMoney tgym) {
    this.tgym = tgym;
    if (tgym.getServer().getPluginManager().getPlugin("Vault") == null) {
      tgym.Log.log(Level.SEVERE, "[PreKick] " + tgym.Lang._("Vault.NotFound"));
      tgym.getServer().getPluginManager().disablePlugin(tgym);
    } else {
      final RegisteredServiceProvider<Permission> perm = tgym.getServer()
          .getServicesManager().getRegistration(Permission.class);
      if (perm == null) {
        tgym.Log.log(Level.SEVERE,
            "[TimeGivesYouMoney] " + tgym.Lang._("Vault.PermissionNotFound"));
        tgym.getServer().getPluginManager().disablePlugin(tgym);
        return;
      }
      permissions = perm.getProvider();

      final RegisteredServiceProvider<Economy> econ = tgym.getServer()
          .getServicesManager().getRegistration(Economy.class);
      if (econ == null) {
        tgym.Log.log(Level.SEVERE,
            "[TimeGivesYouMoney] " + tgym.Lang._("Vault.EconomyNotFound"));
        tgym.getServer().getPluginManager().disablePlugin(tgym);
        return;
      }
      economy = econ.getProvider();
    }
  }

  public Economy GetEconomy() {
    return economy;
  }

  public String GetGroup(Player player) {
    final ArrayList<String> groups = tgym.Settings.GetAvailableGroups();
    for (final String group : groups)
      if (!group.equalsIgnoreCase("default")
          && HasPermissions(player, "tgym.group." + group))
        return group;
    return "Default";
  }

  public boolean HasPermissions(Player player, String permission) {
    return permissions.has(player, permission);
  }

}
