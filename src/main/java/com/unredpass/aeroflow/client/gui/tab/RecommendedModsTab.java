package com.unredpass.aeroflow.client.gui.tab;

import com.unredpass.aeroflow.client.gui.AeroFlowScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class RecommendedModsTab implements AeroFlowScreen.TabContent {
    private final List<ModEntry> mods = new ArrayList<>();
    private int panelX, panelY, panelWidth, panelHeight;

    private record ModEntry(String name, String tooltip, String badge) {
    }

    public RecommendedModsTab() {
        mods.add(new ModEntry("Sodium",
                "Полностью переписывает движок рендеринга.\nЭффект: самый сильный прирост FPS — 2–5x.\nАбсолютный маст-хэв.", "+200–400%"));
        mods.add(new ModEntry("Lithium",
                "Оптимизирует физику, AI мобов и тики блоков.\nЭффект: стабилизация фреймтайма.\nНе меняет ванильные механики.", "CPU"));
        mods.add(new ModEntry("Krypton",
                "Оптимизирует сетевой стек.\nЭффект: меньше задержка и трафик.\nПолезен в мультиплеере.", "NET"));
        mods.add(new ModEntry("EntityCulling",
                "Асинхронная проверка видимости сущностей.\nЭффект: не рендерит сущности за стенами.\nРаботает с Sodium.", "FPS"));
        mods.add(new ModEntry("FerriteCore",
                "Оптимизирует структуры данных в памяти.\nЭффект: −30–50% потребления RAM.\nКритически важен для тяжёлых сборок.", "RAM"));
        mods.add(new ModEntry("ImmediatelyFast",
                "Ускоряет отрисовку сущностей, частиц и HUD.\nЭффект: прирост в динамичных сценах.\nОсобенно заметен в бою.", "FPS"));
        mods.add(new ModEntry("C2ME",
                "Многопоточная генерация чанков.\nЭффект: быстрее загрузка мира.\nМеньше лагов при исследовании.", "CHUNK"));
        mods.add(new ModEntry("More Culling",
                "Расширяет отсечение на листву и посевы.\nЭффект: дополнительный прирост FPS.\nНа природных ландшафтах.", "FPS"));
    }

    @Override
    public void init(AeroFlowScreen screen) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta,
                       int panelX, int panelY, int panelWidth, int panelHeight) {
        this.panelX = panelX;
        this.panelY = panelY;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        int cardHeight = 48;
        int startY = panelY + 10;

        for (int i = 0; i < mods.size(); i++) {
            ModEntry mod = mods.get(i);
            int y = startY + i * (cardHeight + 4);
            if (y + cardHeight > panelY + panelHeight) break;

            boolean hovered = mouseX >= panelX + 10 && mouseX < panelX + panelWidth - 10
                    && mouseY >= y && mouseY < y + cardHeight;

            int bg = hovered ? 0xFF2A2A4A : 0xFF151525;
            context.fill(panelX + 10, y, panelX + panelWidth - 10, y + cardHeight, bg);
            context.drawBorder(panelX + 10, y, panelWidth - 20, cardHeight, 0xFF333355);

            // Название
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                    mod.name, panelX + 18, y + 8, 0xFFFFFF);

            // Бейдж
            context.fill(panelX + panelWidth - 80, y + 6, panelX + panelWidth - 18, y + 20, 0xFF1A3A1A);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,
                    mod.badge, panelX + panelWidth - 49, y + 9, 0x44FF44);

            // Короткая строка описания (первая строка тултипа)
            String firstLine = mod.tooltip.split("\n")[0];
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                    firstLine, panelX + 18, y + 26, 0xAAAAAA);
        }
    }

    @Override
    public String getTooltipAt(int mouseX, int mouseY) {
        int cardHeight = 48;
        int startY = panelY + 10;

        for (int i = 0; i < mods.size(); i++) {
            int y = startY + i * (cardHeight + 4);
            if (mouseX >= panelX + 10 && mouseX < panelX + panelWidth - 10
                    && mouseY >= y && mouseY < y + cardHeight) {
                return mods.get(i).tooltip;
            }
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}
