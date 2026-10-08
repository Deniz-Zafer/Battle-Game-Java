package proje.ui.screens;

import proje.model.character.Player;
import proje.model.item.Amulet;
import proje.model.item.EquipableItem;
import proje.model.item.Item;
import proje.model.item.UsableItem;
import proje.ui.GameContext;
import proje.ui.GameFrame;
import proje.ui.components.ItemSlotView;
import proje.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryPanel extends JPanel {

    private final GameFrame frame;
    private final GameContext context;

    private static final int INV_ROWS = 8;
    private static final int INV_COLS = 10;

    // slot boyutu (kutuları küçültür)
    private static final Dimension SLOT_SIZE = new Dimension(120, 120); // 110-130 arası oynatabilirsin

    private final ItemSlotView[] invSlots = new ItemSlotView[INV_ROWS * INV_COLS];

    private ItemSlotView equip1;
    private ItemSlotView equip2;
    private ItemSlotView amulet1;
    private ItemSlotView amulet2;

    public InventoryPanel(GameFrame frame, GameContext context) {
        this.frame = frame;
        this.context = context;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(12, 0)); // gap küçültüldü
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding küçültüldü

        // -------------------- Envanter (sol) --------------------
        JPanel invPanel = new JPanel(new BorderLayout());
        invPanel.setOpaque(false);

        TitledBorder invBorder = BorderFactory.createTitledBorder("Envanter");
        invBorder.setTitleFont(UiTheme.TITLE_FONT);
        invBorder.setTitleColor(UiTheme.TEXT_BLUE);
        invPanel.setBorder(invBorder);

        // gap küçültüldü
        JPanel grid = new JPanel(new GridLayout(INV_ROWS, INV_COLS, 8, 8));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // iç boşluk

        for (int i = 0; i < invSlots.length; i++) {
            final int idx = i;
            invSlots[i] = new ItemSlotView(
                    ItemSlotView.Role.INVENTORY,
                    idx,
                    this::handleDrop,
                    this::onDoubleClick
            );
            applySlotSize(invSlots[i]);
            grid.add(invSlots[i]);
        }

        invPanel.add(grid, BorderLayout.CENTER);
        root.add(invPanel, BorderLayout.CENTER);

        // -------------------- Sağ panel (Ekipman + Buton bandı) --------------------
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        JPanel equipPanel = createEquipPanel();
        right.add(equipPanel, BorderLayout.NORTH);

        // araya boşluk koy (buton banda yapışmasın)
        right.add(Box.createVerticalStrut(18), BorderLayout.CENTER);

        // buton ayrı bir bantta, sağ alta
        JButton back = new JButton("Savaşa dön");
        back.addActionListener(e -> frame.backToBattle());

        JPanel bottomBand = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomBand.setOpaque(false);
        bottomBand.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        bottomBand.add(back);

        right.add(bottomBand, BorderLayout.SOUTH);

        root.add(right, BorderLayout.EAST);

        add(root, BorderLayout.CENTER);
    }

    private JPanel createEquipPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        TitledBorder border = BorderFactory.createTitledBorder("Ekipman");
        border.setTitleFont(UiTheme.TITLE_FONT);
        border.setTitleColor(UiTheme.TEXT_BLUE);
        p.setBorder(border);

        // gap küçültüldü + padding eklendi
        JPanel slots = new JPanel(new GridLayout(2, 2, 8, 8));
        slots.setOpaque(false);
        slots.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        equip1 = new ItemSlotView(ItemSlotView.Role.EQUIP, 0, this::handleDrop, this::onDoubleClick);
        equip2 = new ItemSlotView(ItemSlotView.Role.EQUIP, 1, this::handleDrop, this::onDoubleClick);
        amulet1 = new ItemSlotView(ItemSlotView.Role.AMULET, 0, this::handleDrop, this::onDoubleClick);
        amulet2 = new ItemSlotView(ItemSlotView.Role.AMULET, 1, this::handleDrop, this::onDoubleClick);

        applySlotSize(equip1);
        applySlotSize(equip2);
        applySlotSize(amulet1);
        applySlotSize(amulet2);

        slots.add(equip1);
        slots.add(equip2);
        slots.add(amulet1);
        slots.add(amulet2);

        p.add(slots, BorderLayout.CENTER);
        return p;
    }

    // slotların tek yerden küçülmesi
    private static void applySlotSize(JComponent slot) {
        if (slot == null) return;
        slot.setPreferredSize(SLOT_SIZE);
        slot.setMinimumSize(SLOT_SIZE);
        slot.setMaximumSize(SLOT_SIZE);
    }

    public void refresh() {
        Player p = context.getPlayer();
        if (p == null) return;

        List<Item> items = p.getInventory().getItems();
        for (int i = 0; i < invSlots.length; i++) {
            Item it = (i < items.size()) ? items.get(i) : null;
            invSlots[i].setItem(it);
        }

        equip1.setItem(p.getEquipSlot(0));
        equip2.setItem(p.getEquipSlot(1));
        amulet1.setItem(p.getAmuletSlot(0));
        amulet2.setItem(p.getAmuletSlot(1));
    }

    private void onDoubleClick(ItemSlotView slot) {
        if (slot == null) return;
        if (slot.getRole() != ItemSlotView.Role.INVENTORY) return;

        Player p = context.getPlayer();
        if (p == null) return;

        Item it = slot.getItem();
        if (!(it instanceof UsableItem usable)) return;

        usable.use(p);
        p.getInventory().getItems().remove(it);

        if (frame.getBattlePanel() != null) {
            frame.getBattlePanel().refreshUI();
        }

        refresh();
    }

    private boolean handleDrop(ItemSlotView.ItemDragPayload payload, ItemSlotView target) {
        if (payload == null || payload.item == null || target == null) return false;

        Player p = context.getPlayer();
        if (p == null) return false;

        List<Item> inv = p.getInventory().getItems();
        Item dragged = payload.item;

        if (target.getRole() == ItemSlotView.Role.INVENTORY) {
            int to = target.getIndex();

            if (payload.sourceRole == ItemSlotView.Role.INVENTORY) {
                int from = payload.sourceIndex;
                if (from == to) return false;

                ensureSize(inv, Math.max(from, to) + 1);
                Item tmp = inv.get(to);
                inv.set(to, dragged);
                inv.set(from, tmp);
                refresh();
                return true;
            }

            if (payload.sourceRole == ItemSlotView.Role.EQUIP) {
                EquipableItem uneq = p.clearEquipSlot(payload.sourceIndex);
                if (uneq == null) return false;
                placeIntoInventory(inv, uneq, to);
                refresh();
                frame.getBattlePanel().refreshUI();
                return true;
            }

            if (payload.sourceRole == ItemSlotView.Role.AMULET) {
                Amulet uneq = p.clearAmuletSlot(payload.sourceIndex);
                if (uneq == null) return false;
                placeIntoInventory(inv, uneq, to);
                refresh();
                frame.getBattlePanel().refreshUI();
                return true;
            }
        }

        if (target.getRole() == ItemSlotView.Role.EQUIP) {
            if (!(dragged instanceof EquipableItem eq) || (dragged instanceof Amulet)) {
                Toolkit.getDefaultToolkit().beep();
                return false;
            }

            if (payload.sourceRole == ItemSlotView.Role.INVENTORY) {
                int from = payload.sourceIndex;
                if (from >= 0 && from < inv.size()) inv.set(from, null);
            }

            EquipableItem prev = p.setEquipSlot(target.getIndex(), eq);
            if (prev != null) p.getInventory().addItem(prev);

            cleanupNulls(inv);
            refresh();
            frame.getBattlePanel().refreshUI();
            return true;
        }

        if (target.getRole() == ItemSlotView.Role.AMULET) {
            if (!(dragged instanceof Amulet am)) {
                Toolkit.getDefaultToolkit().beep();
                return false;
            }

            if (payload.sourceRole == ItemSlotView.Role.INVENTORY) {
                int from = payload.sourceIndex;
                if (from >= 0 && from < inv.size()) inv.set(from, null);
            }

            Amulet prev = p.setAmuletSlot(target.getIndex(), am);
            if (prev != null) p.getInventory().addItem(prev);

            cleanupNulls(inv);
            refresh();
            frame.getBattlePanel().refreshUI();
            return true;
        }

        return false;
    }

    private static void ensureSize(List<Item> inv, int size) {
        while (inv.size() < size) inv.add(null);
    }

    private static void placeIntoInventory(List<Item> inv, Item item, int index) {
        if (item == null) return;
        ensureSize(inv, index + 1);
        Item prev = inv.get(index);
        inv.set(index, item);
        if (prev != null) inv.add(prev);
        cleanupNulls(inv);
    }

    private static void cleanupNulls(List<Item> inv) {
        List<Item> compact = new ArrayList<>();
        for (Item i : inv) if (i != null) compact.add(i);
        inv.clear();
        inv.addAll(compact);
    }
}
