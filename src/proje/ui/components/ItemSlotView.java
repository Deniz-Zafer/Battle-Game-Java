package proje.ui.components;

import proje.model.item.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * SlotView + Item gösterimi + drag&drop.
 */
public class ItemSlotView extends SlotView {

    public enum Role {
        INVENTORY,
        EQUIP,
        AMULET
    }

    public interface DropHandler {
        boolean onDrop(ItemDragPayload payload, ItemSlotView target);
    }

    public interface ClickHandler {
        void onDoubleClick(ItemSlotView slot);
    }

    public static final class ItemDragPayload {
        public final Item item;
        public final Role sourceRole;
        public final int sourceIndex;

        public ItemDragPayload(Item item, Role sourceRole, int sourceIndex) {
            this.item = item;
            this.sourceRole = sourceRole;
            this.sourceIndex = sourceIndex;
        }
    }

    private static final DataFlavor PAYLOAD_FLAVOR;
    static {
        try {
            PAYLOAD_FLAVOR = new DataFlavor(
                    DataFlavor.javaJVMLocalObjectMimeType + ";class=" + ItemDragPayload.class.getName()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final Role role;
    private final int index;
    private final DropHandler dropHandler;
    private final ClickHandler clickHandler;
    private Item item;

    public ItemSlotView(Role role, int index, DropHandler dropHandler, ClickHandler clickHandler) {
        super();
        this.role = role;
        this.index = index;
        this.dropHandler = dropHandler;
        this.clickHandler = clickHandler;

        setTransferHandler(new SlotTransferHandler());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && clickHandler != null) {
                    clickHandler.onDoubleClick(ItemSlotView.this);
                }
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (item == null) return;
                getTransferHandler().exportAsDrag(ItemSlotView.this, e, TransferHandler.MOVE);
            }
        });
    }

    public Role getRole() {
        return role;
    }

    public int getIndex() {
        return index;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (item == null) {
            setToolTipText("Empty");
            clear();
            return;
        }

        setToolTipText(ItemTooltip.build(item));

        String path = ItemIconResolver.iconPath(item);
        if (path != null) {
            java.net.URL url = getClass().getResource(path);
            if (url != null) {
                setIconImage(new ImageIcon(url).getImage());
            } else {
                clear();
            }
        } else {
            clear();
        }
    }

    private final class SlotTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            ItemDragPayload payload = new ItemDragPayload(item, role, index);
            return new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{PAYLOAD_FLAVOR};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return PAYLOAD_FLAVOR.equals(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                    if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
                    return payload;
                }
            };
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(PAYLOAD_FLAVOR);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) return false;
            try {
                ItemDragPayload payload = (ItemDragPayload) support.getTransferable().getTransferData(PAYLOAD_FLAVOR);
                if (dropHandler == null) return false;
                return dropHandler.onDrop(payload, ItemSlotView.this);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
