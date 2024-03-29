package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.i18n.TranslatableContext;
import club.asyncraft.papercutter.util.Reference;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Log
@Getter
public class PaperCutter extends JavaPlugin {

    @Getter
    private static PaperCutter instance;

    private TranslatableContext translatableContext;

    @Override
    public void onEnable() {
        try {
            PaperCutter.instance = this;

            this.initConfig();

            CutterExecutor executor = new MainExecutor();
            Objects.requireNonNull(this.getServer().getPluginCommand("PaperCutter")).setExecutor(executor);
            Objects.requireNonNull(this.getServer().getPluginCommand("PaperCutter")).setTabCompleter(executor);
            this.getLogger().info(this.translatableContext.translate("debug.loaded"));
        } catch (Exception e) {
            PaperCutter.disable();
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info(this.translatableContext.translate("debug.disabled"));
    }

    public void initConfig() throws Exception {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        this.reloadConfig();
        String langConfig = this.getConfig().getString("lang");
        //初始化i18n
        this.translatableContext = new TranslatableContext(this, Reference.plugin_langs, langConfig);
    }

    public static void disable() {
        PaperCutter.instance.getServer().getPluginManager().disablePlugin(PaperCutter.instance);
    }

}
