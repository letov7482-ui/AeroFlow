package com.unredpass.aeroflow.client.gui;

import com.unredpass.aeroflow.client.gui.tab.OptimizationsTab;
import com.unredpass.aeroflow.client.gui.tab.RecommendedModsTab;
import com.unredpass.aeroflow.client.gui.tab.StatusTab;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AeroFlowScreen extends Screen {
    private static final int TAB_HEIGHT = 28;
    private static final int TAB_WIDTH = 120;
    private static final int PANEL_MARGIN = 20;

    private final List<Tab> tabs = new ArrayList<>();
    private Tab activeTab;
    private String hoveredTooltip = null;

    public AeroFlowScreen() {
        super(Text.literal("AeroFlow"));
    }

    @Override
    protected void init() {
        super.init();

        // Три вкладки
        tabs.add(new Tab("Оптимизации", new OptimizationsTab()));
        tabs.add(new Tab("Рекомендуемые моды", new RecommendedModsTab()));
        tabs.add(new Tab("Статус", new StatusTab()));

        activeTab = tabs.get(0);
        activeTab.content.init(this);

        // Инициализируем содержимое всех вкладок, чтобы избежать повторной инициализации при переключении
        for (Tab tab : tabs) {
            if (tab != activeTab) {
                tab.content.init(this);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Полупрозрачный фон
        context.fill(0, 0, this.width, this.height, 0xCC000000);

        // Отрисовка панели
        int panelX = PANEL_MARGIN;
        int panelY = PANEL_MARGIN + TAB_HEIGHT + 10;
        int panelWidth = this.width - PANEL_MARGIN * 2;
        int panelHeight = this.height - panelY - PANEL_MARGIN;

        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xFF1A1A2E);
        context.drawBorder(panelX, panelY, panelWidth, panelHeight, 0xFF4A4A6A);

        // Отрисовка вкладок
        int tabX = panelX;
        for (Tab tab : tabs) {
            boolean isActive = tab == activeTab;
            boolean isHovered = mouseX >= tabX && mouseX < tabX + TAB_WIDTH
                    && mouseY >= PANEL_MARGIN && mouseY < PANEL_MARGIN + TAB_HEIGHT;

            int bgColor = isActive ? 0xFF2A2A4A : (isHovered ? 0xFF3A3A5A : 0xFF0F0F1F);
            context.fill(tabX, PANEL_MARGIN, tabX + TAB_WIDTH, PANEL_MARGIN + TAB_HEIGHT, bgColor);
            context.drawBorder(tabX, PANEL_MARGIN, TAB_WIDTH, TAB_HEIGHT, 0xFF4A4A6A);

            context.drawCenteredTextWithShadow(this.textRenderer, tab.title,
                    tabX + TAB_WIDTH / 2, PANEL_MARGIN + 10, isActive ? 0xFFFFFF : 0xAAAAAA);

            tabX += TAB_WIDTH + 4;
        }

        // Отрисовка содержимого активной вкладки
        activeTab.content.render(context, mouseX, mouseY, delta, panelX, panelY, panelWidth, panelHeight);

        // Всплывающая подсказка (если есть)
        if (hoveredTooltip != null && !hoveredTooltip.isEmpty()) {
            String[] lines = hoveredTooltip.split("\n");
            int tooltipWidth = 0;
            for (String line : lines) {
                tooltipWidth = Math.max(tooltipWidth, this.textRenderer.getWidth(line));
            }
            tooltipWidth += 12;
            int tooltipHeight = lines.length * 12 + 8;

            int tooltipX = mouseX + 12;
            int tooltipY = mouseY - 12;
            if (tooltipX + tooltipWidth > this.width) tooltipX = mouseX - tooltipWidth - 4;
            if (tooltipY + tooltipHeight > this.height) tooltipY = mouseY - tooltipHeight - 4;

            context.fill(tooltipX - 2, tooltipY - 2, tooltipX + tooltipWidth, tooltipY + tooltipHeight, 0xFF000000);
            context.drawBorder(tooltipX - 2, tooltipY - 2, tooltipWidth + 4, tooltipHeight + 4, 0xFF4A4A6A);

            int lineY = tooltipY + 4;
            for (String line : lines) {
                context.drawTextWithShadow(this.textRenderer, line, tooltipX + 4, lineY, 0xFFFFFF);
                lineY += 12;
            }
        }

        // Кнопка закрытия в правом нижнем углу
        int closeX = this.width - PANEL_MARGIN - 60;
        int closeY = this.height - PANEL_MARGIN - 24;
        boolean closeHovered = mouseX >= closeX && mouseX < closeX + 60
                && mouseY >= closeY && mouseY < closeY + 20;
        context.fill(closeX, closeY, closeX + 60, closeY + 20, closeHovered ? 0xFFAA3333 : 0xFF553333);
        context.drawBorder(closeX, closeY, 60, 20, 0xFFAA5555);
        context.drawCenteredTextWithShadow(this.textRenderer, "Закрыть", closeX + 30, closeY + 6, 0xFFFFFF);

        // Сохраняем ссылку на кнопку для обработки кликов
        this.closeButtonX = closeX;
        this.closeButtonY = closeY;

        super.render(context, mouseX, mouseY, delta);
    }

    private int closeButtonX, closeButtonY;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Клик по вкладкам
        int tabX = PANEL_MARGIN;
        for (Tab tab : tabs) {
            if (mouseX >= tabX && mouseX < tabX + TAB_WIDTH
                    && mouseY >= PANEL_MARGIN && mouseY < PANEL_MARGIN + TAB_HEIGHT) {
                activeTab = tab;
                return true;
            }
            tabX += TAB_WIDTH + 4;
        }

        // Клик по кнопке закрытия
        if (mouseX >= closeButtonX && mouseX < closeButtonX + 60
                && mouseY >= closeButtonY && mouseY < closeButtonY + 20) {
            this.close();
            return true;
        }

        // Делегируем клик содержимому активной вкладки
        if (activeTab.content.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        // Обновляем всплывающую подсказку при движении мыши
        hoveredTooltip = null;
        int mouseX = (int) this.client.mouse.getX();
        int mouseY = (int) this.client.mouse.getY();
        hoveredTooltip = activeTab.content.getTooltipAt(mouseX, mouseY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    /** Внутренний класс для вкладки */
    private static class Tab {
        final String title;
        final TabContent content;
        Tab(String title, TabContent content) {
            this.title = title;
            this.content = content;
        }
    }

    /** Интерфейс содержимого вкладки */
    public interface TabContent {
        void init(AeroFlowScreen screen);
        void render(DrawContext context, int mouseX, int mouseY, float delta,
                    int panelX, int panelY, int panelWidth, int panelHeight);
        String getTooltipAt(int mouseX, int mouseY);
        boolean mouseClicked(double mouseX, double mouseY, int button);
    }
            }
