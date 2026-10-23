package com.unredpass.aeroflow.client;

import com.unredpass.aeroflow.AeroFlow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AeroFlowClient implements ClientModInitializer {
    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aeroflow.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.aeroflow"
        ));
        AeroFlow.LOGGER.info("AeroFlow client initialized.");
    }
}
