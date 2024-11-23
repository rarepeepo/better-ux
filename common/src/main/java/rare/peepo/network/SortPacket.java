package rare.peepo.network;

import dev.architectury.networking.NetworkManager.PacketContext;
import java.util.function.Supplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import rare.peepo.Log;
import rare.peepo.inventory.InventoryUtils;

public class SortPacket {
    boolean sortPlayerInventory;
    
    /**
     * Called by network api to decode a new instance from the serialized
     * network data.
     * 
     * @param buf
     *  The serialized network data to initialize the instance with.
     */
    public SortPacket(PacketByteBuf buf) {
        sortPlayerInventory = buf.readBoolean();
    }
 
    /**
     * Called by the user to construct a new sort command packet that can
     * be sent over the network.
     * 
     * @param sortPlayerInventory
     *  true to sort the player's inventory; false to sort the container's inventory
     *  that the player is interacting with.
     */
    public SortPacket(boolean sortPlayerInventory) {
        this.sortPlayerInventory = sortPlayerInventory;
    }
 
    /**
     * Called by network api to serialize instance data into a buffer.
     * 
     * @param buf
     *  The buffer to serialize the instance data to.
     */
    public void encode(PacketByteBuf buf) {
        buf.writeBoolean(sortPlayerInventory);
    }
 
    /**
     * Called by network api to process a received packet.
     * 
     * @param context
     *  The packet context providing access to the sender as well as the logical
     *  side the packet is being received on.
     *  
     *  @apiNote
     *  This method executes on the network thread. Any work to be done must be
     *  scheduled to run on the main thread.
     */
    public void handle(Supplier<PacketContext> context) {
        var c = context.get();
        var p = c.getPlayer();
        // Schedule to run on server thread.
        c.queue(() -> doSort(p));
    }
    
    /**
     * Performs inventory sorting.
     * 
     * @param player
     *  The player whose inventory to sort.
     */
    void doSort(PlayerEntity player) {
        Log.debug("Performing server-side sort of inventory for {}",
                player.getName().getString());
        if (sortPlayerInventory) {
            // The player's inventory consists of 27 slots + 9 hotbar slots:
            // The hotbar slots go left to right from 0 (leftmost) to 8 (rightmost).
            // The other 27 slots go left to right, top to bottom from 9 (top row, leftmost)
            // to 35 (third row, rightmost).
            InventoryUtils.sortInventory (player.getInventory(), 9, 27);
        } else {
            var h = player.currentScreenHandler;
            if (h == null || !h.canUse(player) || h.slots.size() == 0) {
                Log.warn("currentScreenHandler is null or it can't be used it or it has no slots.");
                return;
            }
            var containerInventory = h.slots.get(0).inventory;
            // Container slots go from 0 to size() with 0 being the top-left slot and
            // size() - 1 being the bottom-right slot.
            InventoryUtils.sortInventory(containerInventory, 0, containerInventory.size());
        }
    }
}
