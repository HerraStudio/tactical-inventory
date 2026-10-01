package dev.tactical.client;

import dev.tactical.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="tactical_inventory",value=Dist.CLIENT)
public final class ClientEvents {
    private static boolean openCreative;
    @SubscribeEvent public static void commands(net.neoforged.neoforge.client.event.RegisterClientCommandsEvent event) {
        event.getDispatcher().register(net.minecraft.commands.Commands.literal("creativeinv").executes(context->{
            var mc=Minecraft.getInstance();
            if(mc.player==null || !mc.player.isCreative()) {
                context.getSource().sendFailure(net.minecraft.network.chat.Component.literal("请先切换创造模式，再使用 /creativeinv。"));
                return 0;
            }
            openCreative=true;
            return 1;
        }));
    }
    @SubscribeEvent public static void tick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        var mc=Minecraft.getInstance();
        if(openCreative) {
            openCreative=false;
            if(mc.player!=null && mc.player.isCreative()) {
                mc.player.closeContainer();
                mc.setScreen(new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(
                        mc.player,mc.level.enabledFeatures(),mc.options.operatorItemsTab().get()));
            }
        }
    }
    @SubscribeEvent public static void opening(ScreenEvent.Opening event) {
        if(event.getNewScreen() instanceof InventoryScreen && Minecraft.getInstance().player!=null) {
            event.setCanceled(true);
            PacketDistributor.sendToServer(new Packets.Action(0,0,0,0,0,0,0,false,false));
        }
    }
    @EventBusSubscriber(modid="tactical_inventory",value=Dist.CLIENT,bus=EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void renderers(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(Tactical.SAFE_ENTITY.get(),context->new SafeRenderer());
        }
        @SubscribeEvent public static void screens(RegisterMenuScreensEvent event) {
            event.register(Tactical.MENU.get(),BagScreen::new);
            Packets.stateReceiver=state->{
                if(Minecraft.getInstance().screen instanceof BagScreen screen) screen.accept(state);
            };
        }
    }
}
