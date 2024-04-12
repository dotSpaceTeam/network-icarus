package dev.dotspace.network.library.game.plugin;

/**
 * State of plugin load.
 */
public enum PluginState {
    /**
     * Before system has anything in load area.
     */
    PRE_LOAD,
    /**
     * If system is loaded -> next step pre_enable.
     */
    POST_LOAD,

    /**
     * Before system has anything in enabled area.
     */
    PRE_ENABLE,
    /**
     * If system is enabled -> next step pre_disable.
     */
    POST_ENABLE,

    /**
     * Before system has anything in disable area.
     */
    PRE_DISABLE,
    /**
     * If system is disabled -> plugin or server shutdown.
     */
    POST_DISABLE

}
