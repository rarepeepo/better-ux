package rare.peepo.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public final class InventoryUtils {
    /**
     * Sorts the specified inventory by combining mergeable items into stacks and
     * then sorting them alphabetically. 
     * 
     * @param inventory
     *  The inventory to sort.
     * @param start
     *  The starting slot index to start sorting at.
     * @param num
     *  The number of slots to sort.
     */
    public static void sortInventory(Inventory inventory, int start, int num) {
        List<ItemStack> stacks = new ArrayList<>();
        // Add everything into a list, combining stackable items in the process.
        for (var i = 0; i < num; i++) {
            var s = inventory.getStack(start + i);
            if (s.isEmpty() || mergeWithExisting(s, stacks))
                continue;
            stacks.add(s);
        }
        if (stacks.size() == 0)
            return;
        // Sort stacks alphabetically by item name.
        Collections.sort(stacks, (a, b) -> {
            return a.getName().getString().compareTo(b.getName().getString());
        });
        for (var i = 0; i < num; i++) {
            inventory.setStack(start + i, i < stacks.size() ?
                    stacks.get(i) : ItemStack.EMPTY);
        }
        inventory.markDirty();
    }
    
    /**
     * Transfers items between the two given inventories.
     * 
     * @param dst
     *  The inventory instance to receive items.
     * @param startDst
     *  The index of the first slot of the destination inventory to receive
     *  items into.
     * @param numDst
     *  The number of slots to receive items into.
     * @param src
     *  The inventory instance to take items from.
     * @param startSrc
     *  The index of the first slot of the source inventory to take items
     *  from.
     * @param numSrc
     *  The number of slots to take items from.
     */
    public static void transferInventory(Inventory dst, int startDst, int numDst,
            Inventory src, int startSrc, int numSrc) {
        for (var i = 0; i < numSrc; i++) {
            var s = src.getStack(startSrc + i);
            if (s.isEmpty())
                continue;
            if (!mergeIntoInventory (s, dst, startDst, numDst)) {
                var n = getEmptySlot(dst, startDst, numDst);
                // Stop if there's no more empty slots in the destination.
                if (n < 0)
                    break;
                dst.setStack(n, s);
            }
            src.removeStack(startSrc + i);
        }
        src.markDirty();
        dst.markDirty();
    }
    
    /**
     * Tries to merge the specified stack into the given list of existing ones.
     * 
     * @param s
     *  The stack to merge.
     * @param stacks
     *  The list of stacks to try to merge the stack into.
     * @return
     *  true if all items of the specified stack were merged into existing stacks, i.e.
     *  the stack is now empty; otherwise false.
     */
    static boolean mergeWithExisting(ItemStack s, List<ItemStack> stacks) {
        if(!s.isStackable())
            return false;
        for(var i = 0; i < stacks.size(); i++) {
            var cur = stacks.get(i);
            if (!canMergeInto(cur, s))
                continue;
            // If the stack has been emptied, we're done.
            if(mergeStacks(cur, s))
                return true;
        }
        return false;
    }
    
    /**
     * Gets whether items of the specified source stack can be merged into the specified
     * destination stack.
     * 
     * @param dst
     *  The destination stack.
     * @param src
     *  The source stack.
     * @return
     *  true if items from the specified source stack can be merged into the specified
     *  destination stack; otherwise false.
     */
    static boolean canMergeInto(ItemStack dst, ItemStack src) {
        if (dst.getCount() == dst.getMaxCount())
            return false;
        if (!ItemStack.canCombine(dst, src))
            return false;
        if (dst.getDamage() != src.getDamage())
            return false;
        return true;
    }
    
    /**
     * Merges two item stacks by adding items from one stack to the other.
     * 
     * @param dst
     *  The item stack that will be added to.
     * @param src
     *  The item stack that will be taken from.
     * @return
     *  true if all items of the source stack were added to the destination stack, i.e.
     *  the source stack is now empty; otherwise false.
     */
    static boolean mergeStacks(ItemStack dst, ItemStack src) {
        var num = Math.min(dst.getMaxCount() - dst.getCount(), src.getCount());
        dst.increment(num);
        src.decrement(num);
        return src.isEmpty();
    }
    
    /**
     * Tries to merge the specified stack into the specified inventory.
     * 
     * @param s
     *  The stack to merge.
     * @param inventory
     *  The inventory to try to merge the stack into.
     * @param start
     *  The starting index of the inventory slots to try to merge the stack into.
     * @param num
     *  The number of inventory slots to try to merge the stack into.
     * @return
     *  true if all items of the specified stack were merged into existing stacks, i.e.
     *  the stack is now empty; otherwise false.
     */
    static boolean mergeIntoInventory(ItemStack s, Inventory inventory, int start, int num) {
        for(var i = 0; i < num; i++) {
            var cur = inventory.getStack(start + i);
            if(cur.isEmpty())
                continue;
            if (!canMergeInto(cur, s))
                continue;
            // If the stack has been emptied, we're done.
            if(mergeStacks(cur, s))
                return true;
        }
        return false;
    }
    
    /**
     * Gets the next empty inventory slot.
     * 
     * @param inventory
     *  The inventory to get a free slot for.
     * @param start
     *  The slot index to start at.
     * @param num
     *  The number of slots to check.
     * @return
     *  The index of the first empty slot or -1 if no empty slot was found.
     */
    static int getEmptySlot(Inventory inventory, int start, int num) {
        for (var i = 0; i < num; i++) {
            var s = inventory.getStack(start + i);
            if (s.isEmpty())
                return start + i;
        }
        return -1;
    }
}
