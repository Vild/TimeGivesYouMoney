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

import java.util.Map.Entry;

public class MoneyGiver implements Runnable {

  private final TimeGivesYouMoney tgym;

  public MoneyGiver(TimeGivesYouMoney tgym) {
    this.tgym = tgym;
  }

  @Override
  public void run() {
    for (final Entry<String, Long> player : tgym.afkTimer.entrySet()) {
      if (tgym.getServer().getPlayer(player.getKey()) == null)
        continue;

      final String group = tgym.Vault.GetGroup(tgym.getServer().getPlayer(
          player.getKey()));

      Object val = tgym.Settings._("Group." + group + ".AFKTimeout",
          (double) -1);

      double timeout = -1;
      if (val instanceof Integer)
        timeout = ((Integer) val).doubleValue();
      else
        timeout = (Double) val;

      val = tgym.Settings._("Group." + group + ".MoneyPerMinute", (double) -1);

      double money = -1;
      if (val instanceof Integer)
        money = ((Integer) val).doubleValue();
      else
        money = (Double) val;

      if ((timeout != -1 || player.getValue() - System.currentTimeMillis() > timeout * 1000 * 60))
        tgym.Bank.Add(player.getKey(), money, false);
    }
  }

}
