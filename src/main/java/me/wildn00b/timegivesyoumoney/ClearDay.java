package me.wildn00b.timegivesyoumoney;

public class ClearDay implements Runnable {

  TimeGivesYouMoney tgym;

  public ClearDay(TimeGivesYouMoney tgym) {
    this.tgym = tgym;
  }

  @Override
  public void run() {
    tgym.Bank.ClearDay();
  }

}
