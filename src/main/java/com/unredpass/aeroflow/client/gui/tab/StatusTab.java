package com.unredpass.aeroflow.client.gui.tab;

import com.unredpass.aeroflow.client.gui.AeroFlowScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class StatusTab implements AeroFlowScreen.TabContent {
    @Override
    public void init(AeroFlowScreen screen) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta,
                       int panelX, int panelY, int panelWidth, int panelHeight) {
        var tr = MinecraftClient.getInstance().textRenderer;
        int y = panelY + 20;

        context.drawTextWithShadow(tr, "Обнаруженные оптимизаторы:", panelX + 20, y, 0xFFFFFF);
        y += 20;

        String[] mods = {"sodium", "lithium", "krypton", "ferritecore", "immediatelyfast"};
        for (String modId : mods) {
            boolean installed = FabricLoader.getInstance().isModLoaded(modId);
            String status = installed ? "✓ Установлен" : "✗ Не найден";
            int color = installed ? 0x44FF44 : 0xFF4444;
            context.drawTextWithShadow(tr, "  " + modId + ": " + status, panelX + 30, y, color);
            y += 16;
        }

        y += 10;
        context.drawTextWithShadow(tr, "Если мод уже установлен,", panelX + 20, y, 0xAAAAAA);
        y += 14;
        context.drawTextWithShadow(tr, "отключите дублирующую функцию", panelX + 20, y, 0xAAAAAA);
        y += 14;
        context.drawTextWithShadow(tr, "во вкладке «Оптимизации» вручную.", panelX + 20, y, 0xAAAAAA);

        y += 24;
        context.drawTextWithShadow(tr, "Целевой FPS: 60", panelX + 20, y, 0x8888FF);
        y += 16;
        context.drawTextWithShadow(tr, "Текущий FPS: " + MinecraftClient.getInstance().getCurrentFps(),
                panelX + 20, y, 0x8888FF);
    }

    @Override
    public String getTooltipAt(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}
