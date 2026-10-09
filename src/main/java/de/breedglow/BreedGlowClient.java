package de.breedglow;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import org.lwjgl.glfw.GLFW;

public class BreedGlowClient implements ClientModInitializer {

    private static final int GREEN = 0x00FF00;
    private static final int RED = 0xFF0000;
    private static final double MAX_DISTANCE_SQ = 48.0 * 48.0;

    private static boolean enabled = true;
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        // Seit 1.21.9 braucht ein KeyMapping eine Kategorie.
        KeyMapping.Category category = KeyMapping.Category.register(
                ResourceLocation.fromNamespaceAndPath("breedglow", "main"));

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.breedglow.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.displayClientMessage(
                            Component.literal("BreedGlow: " + (enabled ? "AN" : "AUS")), true);
                }
            }
        });
    }

    public static boolean shouldHighlight(Entity entity) {
        if (!enabled || !(entity instanceof Animal)) return false;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        return mc.player.distanceToSqr(entity) <= MAX_DISTANCE_SQ;
    }

    public static int colorFor(Entity entity) {
        return isBreedable((Animal) entity) ? GREEN : RED;
    }

    /**
     * Zuechtbar = erwachsen, keine Abklingzeit und nicht schon im Liebesmodus.
     *
     * Der Client kennt Abklingzeit und Liebesmodus nicht. Im Einzelspielermodus lesen wir
     * deshalb das echte Tier vom integrierten Server. Auf fremden Servern bleibt nur
     * "erwachsen = gruen, Baby = rot".
     */
    private static boolean isBreedable(Animal clientAnimal) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSingleplayerServer() != null && clientAnimal.level() != null) {
            ServerLevel serverLevel = mc.getSingleplayerServer().getLevel(clientAnimal.level().dimension());
            if (serverLevel != null) {
                Entity real = serverLevel.getEntity(clientAnimal.getUUID());
                if (real instanceof Animal a) {
                    return a.getAge() == 0 && a.canFallInLove();
                }
            }
        }
        return !clientAnimal.isBaby();
    }
}
