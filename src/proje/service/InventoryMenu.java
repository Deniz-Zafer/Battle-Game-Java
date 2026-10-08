package proje.service;

import proje.model.character.Player;
import proje.model.inventory.Inventory;
import proje.model.item.*;

import java.util.List;
import java.util.Scanner;

public class InventoryMenu {

    public static void openInventoryMenu(Player player, Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            Inventory<Item> inv = player.getInventory();
            List<Item> items = inv.getItems();

            System.out.println("\n=== Envanter ===");
            if (items.isEmpty()) {
                System.out.println("Envanter boş.");
            } else {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    System.out.println(i + " - " + item.getName() + " (" + item.getClass().getSimpleName() + ")");
                }
            }

            System.out.println("\nSeçenekler:");
            System.out.println("1- Item kullan");
            System.out.println("2- Ekipman tak");
            System.out.println("0- Geri dön");
            System.out.print("Seçim: ");

            int choice = readIntSafe(scanner);

            switch (choice) {
                case 1 -> useItemMenu(player, scanner);
                case 2 -> equipItemMenu(player, scanner);
                case 0 -> exit = true;
                default -> System.out.println("Geçersiz seçim.");
            }
        }
    }

    private static void useItemMenu(Player player, Scanner scanner) {
        Inventory<Item> inv = player.getInventory();
        List<Item> items = inv.getItems();

        if (items.isEmpty()) {
            System.out.println("Envanter boş.");
            return;
        }

        System.out.println("\nKullanılabilir item seç (-1 = iptal):");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof UsableItem) {
                System.out.println(i + " - " + item.getName());
            }
        }

        System.out.print("Seçim: ");
        int index = readIntSafe(scanner);
        if (index < 0 || index >= items.size()) {
            System.out.println("İptal edildi.");
            return;
        }

        UsableItem item = (UsableItem) items.get(index);
        if (item instanceof UsableItem) {
            item.use(player);
            inv.removeItem(item);
        } else {
            System.out.println("Bu item kullanılamaz.");
        }
    }

    private static void equipItemMenu(Player player, Scanner scanner) {
        Inventory<Item> inv = player.getInventory();
        List<Item> items = inv.getItems();

        if (items.isEmpty()) {
            System.out.println("Envanter boş.");
            return;
        }

        System.out.println("\nTakılabilir item seç (-1 = iptal):");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof EquipableItem) {
                System.out.println(i + " - " + item.getName() + " (" + item.getClass().getSimpleName() + ")");
            }
        }

        System.out.print("Seçim: ");
        int index = readIntSafe(scanner);
        if (index < 0 || index >= items.size()) {
            System.out.println("İptal edildi.");
            return;
        }

        Item item = items.get(index);
        if (item instanceof EquipableItem equipable) {
            player.equipItem(equipable);
        } else {
            System.out.println("Bu item takılamaz.");
        }
    }

    private static int readIntSafe(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
