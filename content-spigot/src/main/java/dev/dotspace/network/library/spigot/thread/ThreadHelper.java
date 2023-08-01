package dev.dotspace.network.library.spigot.thread;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Implementation for {@link IThreadHelper}
 */
public final class ThreadHelper implements IThreadHelper {
  /**
   * Reference plugin to synchronize tasks for.
   */
  private final @NotNull Plugin plugin;

  /**
   * Create instance-
   *
   * @param plugin to use for {@link ThreadHelper#plugin}
   * @throws NullPointerException if plugin is null.
   */
  public ThreadHelper(@Nullable final Plugin plugin) {
    this.plugin = Objects.requireNonNull(plugin);
  }

  /**
   * See {@link IThreadHelper#synchronizeIfAsync(Runnable)}
   */
  @Override
  public void synchronizeIfAsync(@Nullable final Runnable runnable) {
    //Return if runnable is not present.
    if (runnable == null) {
      return;
    }

    //Run synchron -> already on main thread
    if (Bukkit.isPrimaryThread()) {
      runnable.run();
    } else {
      this.synchronize(runnable);
    }
  }

  /**
   * See {@link IThreadHelper#synchronize(Runnable)}
   */
  @Override
  public void synchronize(@Nullable final Runnable runnable) {
    //Return if runnable is not present.
    if (runnable == null) {
      return;
    }

    //Execute.
    Bukkit.getScheduler().runTask(this.plugin, runnable);
  }

  /**
   * See {@link IThreadHelper#asyncIfPrimaryThreaded(Runnable)}
   */
  @Override
  public void asyncIfPrimaryThreaded(@Nullable final Runnable runnable) {
    //Return if runnable is not present.
    if (runnable == null) {
      return;
    }

    //Run async -> already of main thread
    if (!Bukkit.isPrimaryThread()) {
      runnable.run();
    } else {
      this.async(runnable);
    }
  }

  /**
   * See {@link IThreadHelper#async(Runnable)}
   */
  @Override
  public void async(@Nullable final Runnable runnable) {
    //Return if runnable is not present.
    if (runnable == null) {
      return;
    }

    //Execute async.
    Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
  }
}