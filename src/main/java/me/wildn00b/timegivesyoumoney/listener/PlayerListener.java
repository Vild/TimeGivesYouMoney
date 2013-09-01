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
package me.wildn00b.timegivesyoumoney.listener;

import me.wildn00b.timegivesyoumoney.TimeGivesYouMoney;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

  private final TimeGivesYouMoney tgym;

  public PlayerListener(TimeGivesYouMoney tgym) {
    this.tgym = tgym;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockBreak(BlockBreakEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockIgnite(BlockIgniteEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getEntity() != null && event.getDamager() instanceof Player
        && !event.isCancelled())
      tgym.afkTimer.put(((Player) event.getDamager()).getName(),
          System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLogin(PlayerLoginEvent event) {
    if (event.getResult() == Result.ALLOWED && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerMove(PlayerMoveEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event) {
    if (event.getPlayer() != null) {
      if (!tgym.Vault.HasPermissions(event.getPlayer(), "SaveProgressOnLogout"))
        tgym.afkTimer.remove(event.getPlayer().getName());
      tgym.Bank.PlayerDisconnected(event.getPlayer().getName());
    }

  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    if (!event.isCancelled() && event.getPlayer() != null)
      tgym.afkTimer
          .put(event.getPlayer().getName(), System.currentTimeMillis());
  }
}
