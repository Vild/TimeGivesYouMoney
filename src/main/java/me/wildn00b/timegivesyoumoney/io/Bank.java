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
  private final HashMap<String, Double> day = new HashMap<String, Double>();
  private HashMap<String, Double> db = new HashMap<String, Double>();
  private final File file;
  private final HashMap<String, Double> session = new HashMap<String, Double>();
  private final TimeGivesYouMoney tgym;

  public Bank(TimeGivesYouMoney tgym) {
    this.tgym = tgym;

    file = new File(tgym.getDataFolder().getAbsolutePath() + File.separator
        + "money.db");

    Load();
  }

  public void Add(String player, double value, boolean force) {
    final String group = tgym.Vault
        .GetGroup(tgym.getServer().getPlayer(player));
    double money = value;
    Object tmpobj;
    double tmpval;

    if (!force) {
      tmpobj = tgym.Settings._(
          "Group." + tgym.Vault.GetGroup(tgym.getServer().getPlayer(player))
              + ".MaxMoneyEarnPerDay", (double) -1);

      if (tmpobj instanceof Integer)
        tmpval = ((Integer) tmpobj).doubleValue();
      else
        tmpval = (Double) tmpobj;

      if (day.containsKey(player)) {
        if (tmpval != -1 && day.get(player) + money > tmpval)
          money = day.get(player) + money - tmpval;
      } else if (tmpval != -1 && money > tmpval)
        money = money - tmpval;

      tmpobj = tgym.Settings._(
          "Group." + tgym.Vault.GetGroup(tgym.getServer().getPlayer(player))
              + ".MaxMoneyEarnPerSession", (double) -1);

      if (tmpobj instanceof Integer)
        tmpval = ((Integer) tmpobj).doubleValue();
      else
        tmpval = (Double) tmpobj;

      if (session.containsKey(player)) {
        if (tmpval != -1 && session.get(player) + money > tmpval)
          money = session.get(player) + money - tmpval;
      } else if (tmpval != -1 && money > tmpval)
        money = money - tmpval;
    }

    db.put(player, GetMoney(player) + money);
    if (day.containsKey(player))
      day.put(player, day.get(player) + money);
    if (session.containsKey(player))
      day.put(player, session.get(player) + money);

    if ((Boolean) tgym.Settings._("Group." + group + ".InstantPayout", false))
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

  public void ClearDay() {
    day.clear();
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
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(new FileInputStream(file));
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

    } catch (final Exception e) {
    } finally {
      try {
        in.close();
      } catch (final Exception e) {
      }
    }
  }

  public void PlayerDisconnected(String player) {
    if (session.containsKey(player))
      session.remove(player);
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
