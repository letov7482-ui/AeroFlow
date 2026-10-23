package com.unredpass.aeroflow;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AeroFlow implements ModInitializer {
    public static final String MOD_ID = "aeroflow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("AeroFlow initialized.");
    }
}
