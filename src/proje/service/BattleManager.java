package proje.service;

import proje.model.character.Enemy;
import proje.model.character.Player;
import proje.model.combat.ElementType;
import proje.model.item.Item;

import java.util.Scanner;

public class BattleManager {

    private final Scanner scanner = new Scanner(System.in);

    public void startBattle(Player player, Enemy enemy) {

        System.out.println("====================================");
        System.out.println("Savaş başladı: " + player.getName() + " VS " + enemy.getName());
        System.out.println("====================================");

        player.printStatus();
        enemy.printStatus();

        boolean playerTurn = true;

        while (player.isAlive() && enemy.isAlive()) {

            System.out.println("\n--- Yeni Tur ---");

            if (playerTurn) {
                playerTurn(player, enemy);
            } else {
                enemyTurn(player, enemy);
            }

            playerTurn = !playerTurn;
        }

        if (!player.isAlive()) {
            System.out.println("\n>>> Oyuncu yenildi...");
        } else if (!enemy.isAlive()) {
            System.out.println("\n>>> Düşman yenildi!");
            System.out.println(enemy.getName() + " yenildi.");
            player.gainExperience(enemy.getRewardExp());
            System.out.println("Kazandığın altın: " + enemy.getRewardGold());

            giveBattleRewards(player);
        }
    }

    // ===================== PLAYER TURN =====================

    private void playerTurn(Player player, Enemy enemy) {
        boolean validChoice = false;

        while (!validChoice && player.isAlive() && enemy.isAlive()) {

            System.out.println("\nOyuncu Sırası:");
            player.printStatus();
            enemy.printStatus();

            System.out.println("1- Temel Saldırı");
            System.out.println("2- Özel Yetenek");
            System.out.println("3- Savun (kalkan defansı 1 tur 2x)");
            System.out.println("4- Hızlı iksir kullan (turn harcamaz)");
            System.out.println("5- Envanteri aç (turn harcamaz)");
            System.out.println("6- Crafting menüsü (turn harcamaz)");
            System.out.print("Seçim: ");

            int choice = readIntSafe();

            switch (choice) {
                case 1 -> {
                    // Fiziksel + elemental saldırı (weapon üzerinden)
                    player.attack(enemy);
                    validChoice = true;
                }
                case 2 -> {
                    player.useSkill(enemy);
                    validChoice = true;
                }
                case 3 -> {
                    player.setDefending(true);
                    System.out.println(player.getName() + " savunma pozisyonuna geçti!");
                    System.out.println("Bir sonraki düşman saldırısında kalkan defansının 2 katı uygulanacak.");
                    validChoice = true;
                }
                case 4 -> {
                    // Hızlı iksir: turn harcamaz
                    System.out.println("1- Can iksiri kullan");
                    System.out.println("2- Mana iksiri kullan");
                    System.out.println("3- Güç (strength) iksiri kullan");
                    System.out.print("Seçim: ");
                    int itemChoice = readIntSafe();
                    if (itemChoice == 1) {
                        player.useFirstHealthPotion();
                    } else if (itemChoice == 2) {
                        player.useFirstManaPotion();
                    } else if (itemChoice == 3) {
                        player.useFirstStrengthPotion();
                    } else {
                        System.out.println("Geçersiz seçim.");
                    }
                    // validChoice = false → tekrar ana menüye döner, düşman daha oynamadı
                }
                case 5 -> {
                    // Envanter menüsüne gir, çıkınca tekrar seçim yap
                    InventoryMenu.openInventoryMenu(player, scanner);
                    // validChoice = false;  // tur harcamaz
                }
                default -> System.out.println("Geçersiz seçim, tekrar deneyin.");
            }
        }
    }

    // ===================== ENEMY TURN =====================

    private void enemyTurn(Player player, Enemy enemy) {
        System.out.println("\nDüşman Sırası:");

        int baseDamage = enemy.getAttackPower();
        ElementType enemyElement = enemy.getElementType();

        System.out.println(enemy.getName() + " saldırıya hazırlanıyor! (Element: " + enemyElement + ")");
        player.receiveElementalAttack(baseDamage, enemyElement);
    }

    // ===================== SAVAŞ ÖDÜLLERİ =====================

    private void giveBattleRewards(Player player) {
        System.out.println("\n*** Savaş ödülleri ***");

        for (int i = 0; i < 3; i++) {
            Item randomItem = ItemFactory.getRandomItem();

            if (randomItem == null) {
                System.out.println("Ödül üretilemedi (null item geldi).");
                continue;
            }

            player.addItem(randomItem);
            System.out.println(" - " + randomItem.getName() + " envantere eklendi.");
        }
    }

    // ===================== YARDIMCI =====================

    private int readIntSafe() {
        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line.trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
