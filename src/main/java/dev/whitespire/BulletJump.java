package dev.whitespire;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import dev.whitespire.component.BulletJumpComponent;
import dev.whitespire.config.BulletJumpConfig;
import dev.whitespire.systems.BulletJumpSystem;
import javax.annotation.Nonnull;

public class BulletJump extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static BulletJump instance;
    private final Config<BulletJumpConfig> config;
    public ComponentType<
        EntityStore,
        BulletJumpComponent
    > bulletJumpComponentType;

    public BulletJump(@Nonnull JavaPluginInit init) {
        super(init);
        config = this.withConfig("bulletJump", BulletJumpConfig.CODEC);
        instance = this;
    }

    @Override
    protected void setup() {
        this.config.save();
        this.bulletJumpComponentType =
            this.getEntityStoreRegistry().registerComponent(
                BulletJumpComponent.class,
                BulletJumpComponent::new
            );
        this.getEntityStoreRegistry().registerSystem(
            new BulletJumpSystem(this.config)
        );
        this.getEventRegistry().registerGlobal(
            PlayerReadyEvent.class,
            BulletJump::onPlayerReady
        );
    }

    public static BulletJump get() {
        return instance;
    }

    public static void onPlayerReady(@Nonnull PlayerReadyEvent event) {
        Player player = event.getPlayer();
        Store<EntityStore> store = player
            .getWorld()
            .getEntityStore()
            .getStore();
        store.addComponent(
            player.getReference(),
            BulletJumpComponent.getComponentType(),
            new BulletJumpComponent()
        );
    }
}
