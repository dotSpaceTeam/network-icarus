package dev.dotspace.network.library.spigot.thread;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class helps the handling with {@link Bukkit} threads.
 */
public interface IThreadHelper {
  /**
   * Synchronize {@link Runnable} so {@link Bukkit#isPrimaryThread()} returns ture.
   * Ignoring call if runnable is null.
   *
   * @param runnable to run synchronized.
   */
  void synchronizeIfAsync(@Nullable final Runnable runnable);

  /**
   * Execute {@link Runnable} synchronized.
   * Ignoring call if runnable is null.
   *
   * @param runnable to run synchronized.
   */
  void synchronize(@Nullable final Runnable runnable);

  /**
   * Execute {@link Runnable} so {@link Bukkit#isPrimaryThread()} returns false.
   * Ignoring call if runnable is null.
   *
   * @param runnable to run async if on main thread.
   */
  void asyncIfPrimaryThreaded(@Nullable final Runnable runnable);

  /**
   * Execute {@link Runnable} async.
   * Ignoring call if runnable is null.
   *
   * @param runnable to run async.
   */
  void async(@Nullable final Runnable runnable);
}
