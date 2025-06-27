package dev.svrt.domiirl.morebannerfeatures.core.accessor;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public interface InventoryBannerable extends Bannerable {

	/**
	 * @return the slot where the banner is in.
	 * Used for the horse inventories
	 */
	default int getSlot() { return 2; }

	default int getTransferIndex() { return 38; }

}
