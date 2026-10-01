package dev.tactical;

import dev.tactical.loot.SafeBlock;
import dev.tactical.loot.SafeBlockEntity;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod("tactical_inventory")
public final class Tactical {
    public static final DeferredRegister.Items ITEMS=DeferredRegister.createItems("tactical_inventory");
    public static final DeferredRegister.Blocks BLOCKS=DeferredRegister.createBlocks("tactical_inventory");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES=DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,"tactical_inventory");
    public static final DeferredBlock<SafeBlock> SAFE=BLOCKS.registerBlock("safe",SafeBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F,6.0F));
    public static final DeferredItem<BlockItem> SAFE_ITEM=ITEMS.registerSimpleBlockItem(SAFE);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<SafeBlockEntity>> SAFE_ENTITY=BLOCK_ENTITIES.register("safe",
            ()->BlockEntityType.Builder.of(SafeBlockEntity::new,SAFE.get()).build(null));
    public static final DeferredItem<Item> BACKPACK=ITEMS.registerSimpleItem("field_backpack",new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> RIG=ITEMS.registerSimpleItem("tactical_rig",new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> HEADSET=ITEMS.registerSimpleItem("headset",new Item.Properties().stacksTo(1));
    public static final DeferredRegister<MenuType<?>> MENUS=DeferredRegister.create(Registries.MENU,"tactical_inventory");
    public static final DeferredHolder<MenuType<?>,MenuType<BagMenu>> MENU=MENUS.register("inventory",()->new MenuType<>(BagMenu::new,FeatureFlags.DEFAULT_FLAGS));
    public Tactical(IEventBus bus) {
        BLOCKS.register(bus); BLOCK_ENTITIES.register(bus); ITEMS.register(bus); MENUS.register(bus); bus.addListener(Packets::register);
        bus.addListener((BuildCreativeModeTabContentsEvent event)-> {
            if(event.getTabKey()==CreativeModeTabs.TOOLS_AND_UTILITIES) { event.accept(BACKPACK); event.accept(RIG); event.accept(HEADSET); event.accept(SAFE_ITEM); }
        });
    }
}
