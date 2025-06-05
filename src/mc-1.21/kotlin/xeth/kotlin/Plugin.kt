package com.argi.plugin

import org.bukkit.plugin.java.JavaPlugin

class ArgiKotlinPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("✅ Argi-Kotlin habilitado correctamente.")
    }

    override fun onDisable() {
        logger.info("❌ Argi-Kotlin desactivado.")
    }
}
