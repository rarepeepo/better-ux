package rare.peepo.network;

import dev.architectury.networking.NetworkManager.PacketContext;
import java.util.function.Supplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import rare.peepo.Log;
import rare.peepo.inventory.InventoryUtils;

public class TransferPacket {
    boolean storeItems;
    
    /**
     * Called by network api to decode a new instance from the serialized
     * network data.
     * 
     * @param buf
     *  The serialized network data to initialize the instance with.
     */
    public TransferPacket(PacketByteBuf buf) {
        storeItems = buf.readBoolean();
    }
 
    /**
     * Called by the user to construct a new transfer command packet that can
     * be sent over the network.
     * 
     * @param storeItems
     *  true to store the player's items into the container that he is interacting
     *  with; false to transfer the container's items into the player's inventory.
     */
    public TransferPacket(boolean storeItems) {
        this.storeItems = storeItems;
    }
 
    /**
     * Called by network api to serialize instance data into a buffer.
     * 
     * @param buf
     *  The buffer to serialize the instance data to.
     */
    public void encode(PacketByteBuf buf) {
        buf.writeBoolean(storeItems);
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
        c.queue(() -> doTransfer(p));
    }
    
    /**
     * Performs inventory transfer.
     * 
     * @param player
     *  The player whose inventory to transfer.
     */
    void doTransfer(PlayerEntity player) {
        Log.debug("Performing server-side transfer of inventory for {}",
                player.getName().getString());
        var h = player.currentScreenHandler;
        if (h == null || !h.canUse(player) || h.slots.size() == 0) {
            Log.debug("currentScreenHandler is null or it can't be used it or it has no slots for {}",
                    player.getName().toString());
            return;
        }
        var ci = h.slots.get(0).inventory;
        var pi = player.getInventory();
        if (storeItems)
            InventoryUtils.transferInventory (ci, 0, ci.size(), pi, 9, 27);
        else
            InventoryUtils.transferInventory (pi, 9, 27, ci, 0, ci.size());
    }
}
