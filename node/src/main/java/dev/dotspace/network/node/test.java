package dev.dotspace.network.node;

import dev.dotspace.network.node.economy.db.EconomyDatabase;
import org.springframework.beans.factory.annotation.Autowired;

public class test {

  @Autowired
  private EconomyDatabase economyDatabase;

  public void init() {
    economyDatabase.createCurrency("C", "Coin", "Coins").ifPresent(iCurrency -> {
        System.out.println("Created " + iCurrency);
      })
      .ifExceptionally(throwable -> {
        System.out.println(throwable);
      });
  }
}
