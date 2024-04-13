package com.bwt.items;

import com.bwt.tags.BwtBlockTags;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

public class MattockItem extends MiningToolItem {
    public MattockItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, BwtBlockTags.MATTOCK_MINEABLE, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // Only shovels have a right click action, so we inherit from that
        return Items.NETHERITE_SHOVEL.useOnBlock(context);
    }
}
