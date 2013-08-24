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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import me.wildn00b.timegivesyoumoney.TimeGivesYouMoney;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class Bank {

  public static final int CURRENT_VERSION = 1;
  private HashMap<String, Double> db = new HashMap<String, Double>();
  private final File file;
  private final TimeGivesYouMoney tgym;

  public Bank(TimeGivesYouMoney tgym) {
    this.tgym = tgym;

    file = new File(tgym.getDataFolder().getAbsolutePath() + File.separator
        + "money.db");

    Load();
  }

  public void Add(String player, double value) {
    final String group = tgym.Vault
        .GetGroup(tgym.getServer().getPlayer(player));
    db.put(player, GetMoney(player) + value);
    if ((Boolean) tgym.Settings._("Group." + group + ".InstantPayout"))
      CashOut(player);
  }

  public double CashOut(String player) {
    if (!db.containsKey(player))
      return 0;

    if (!tgym.Vault.GetEconomy().hasAccount(player))
      if (!tgym.Vault.GetEconomy().createPlayerAccount(player))
        return -1;

    final EconomyResponse er = tgym.Vault.GetEconomy().depositPlayer(player,
        GetMoney(player));
    if (er.type == ResponseType.FAILURE)
      return -1;

    Remove(player, er.amount);
    return er.amount;
  }

  public double GetMoney(String player) {
    try {
      return db.get(player);
    } catch (final Exception e) {
      return 0;
    }
  }

  public void Load() {
    String player;
    double value;
    try {
      final ObjectInputStream in = new ObjectInputStream(new FileInputStream(
          file));
      final int version = in.readInt();
      if (version != CURRENT_VERSION)
        throw new Exception("");

      final int size = in.readInt();
      db = new HashMap<String, Double>(size);

      for (int i = 0; i < size; i++) {
        player = in.readUTF();
        value = in.readDouble();
        db.put(player, value);
      }

      in.close();
    } catch (final Exception e) {
    }
  }

  public boolean Remove(String player, double value) {
    if (GetMoney(player) >= value) {
      db.put(player, GetMoney(player) - value);
      return true;
    } else
      return false;
  }

  public void Save() {
    tgym.Log.log(Level.INFO, tgym.Lang._("TimeGivesYouMoney.Saving"));

    try {
      final ObjectOutputStream out = new ObjectOutputStream(
          new FileOutputStream(file));

      out.writeInt(CURRENT_VERSION);
      out.writeInt(db.size());
      for (final Entry<String, Double> item : db.entrySet())
        if (item.getValue() != 0) {
          out.writeUTF(item.getKey());
          out.writeDouble(item.getValue());
        }

      out.flush();
      out.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
