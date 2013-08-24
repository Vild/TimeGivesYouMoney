package me.wildn00b.timegivesyoumoney;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

  private final TimeGivesYouMoney tgym;

  public BlockListener(TimeGivesYouMoney tgym) {
    this.tgym = tgym;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockBreak(BlockBreakEvent event) {
    Sign sign;

    if (!event.isCancelled()
        && event.getBlock() != null
        && (event.getBlock().getType() == Material.WALL_SIGN || event
            .getBlock().getType() == Material.SIGN_POST)) {
      sign = (Sign) event.getBlock().getState();

      if (sign.getLine(0).equals(
          "" + ChatColor.GRAY + ChatColor.BOLD + "[" + ChatColor.YELLOW + "ATM"
              + ChatColor.GRAY + "]")
          && sign.getLine(1).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && sign.getLine(2).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && sign.getLine(3).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && !tgym.Vault.HasPermissions(event.getPlayer(), "tgym.atm.create"))
        event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockDamage(BlockDamageEvent event) {
    Sign sign;
    if (!event.isCancelled()
        && event.getBlock() != null
        && (event.getBlock().getType() == Material.WALL_SIGN || event
            .getBlock().getType() == Material.SIGN_POST)) {
      sign = (Sign) event.getBlock().getState();

      if (sign.getLine(0).equals(
          "" + ChatColor.GRAY + ChatColor.BOLD + "[" + ChatColor.YELLOW + "ATM"
              + ChatColor.GRAY + "]")
          && sign.getLine(1).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && sign.getLine(2).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && sign.getLine(3).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && !tgym.Vault.HasPermissions(event.getPlayer(), "tgym.atm.create"))
        event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteract(PlayerInteractEvent event) {
    Sign sign = null;
    Block block = event.getClickedBlock();

    if (!event.isCancelled() && block != null) {
      if (event.getAction() == Action.LEFT_CLICK_BLOCK
          || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

        if (block.getType() == Material.STONE_BUTTON
            || block.getType() == Material.WOOD_BUTTON
            || block.getType() == Material.LEVER) {
          for (BlockFace bf : BlockFace.values())
            if (testSign(event.getPlayer(), block.getRelative(bf)))
              return;
        } else if (block.getType() == Material.WALL_SIGN
            || block.getType() == Material.SIGN_POST)
          testSign(event.getPlayer(), block);
      } else if (event.getAction() == Action.PHYSICAL) {
        if (block.getType() == Material.STONE_PLATE
            || block.getType() == Material.WOOD_PLATE) {
          for (BlockFace bf : BlockFace.values())
            if (testSign(event.getPlayer(), block.getRelative(bf)))
              return;
        }
      }
    }
  }

  private boolean testSign(Player player, Block block) {
    Sign sign = null;
    if (block.getType() == Material.WALL_SIGN
        || block.getType() == Material.SIGN_POST)
      sign = (Sign) block.getState();

    if (sign != null) {
      if (sign.getLine(0).equals(
          "" + ChatColor.GRAY + ChatColor.BOLD + "[" + ChatColor.YELLOW + "ATM"
              + ChatColor.GRAY + "]")
          && sign.getLine(1).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && sign.getLine(2).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)
          && sign.getLine(3).equals(
              "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD)) {
        if (tgym.Vault.HasPermissions(player, "tgym.atm.use")) {
          final double result = tgym.Bank.CashOut(player.getName());
          player.sendMessage(ChatColor.YELLOW
              + "[TimeGivesYouMoney] "
              + ChatColor.GOLD
              + tgym.Lang._("Command.Cashout.Success.Self").replaceAll(
                  "%MONEY%",
                  result + tgym.Vault.GetEconomy().currencyNamePlural()));
        }
        return true;
      }
    }
    return false;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onSignChange(SignChangeEvent event) {
    if (event.getLine(0).equalsIgnoreCase("[ATM]")
        && tgym.Vault.HasPermissions(event.getPlayer(), "tgym.atm.create")) {
      event.setLine(0, "" + ChatColor.GRAY + ChatColor.BOLD + "["
          + ChatColor.YELLOW + "ATM" + ChatColor.GRAY + "]");
      event.setLine(1, "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD);
      event.setLine(2, "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD);
      event.setLine(3, "" + ChatColor.STRIKETHROUGH + ChatColor.BOLD);
    }
  }
}
