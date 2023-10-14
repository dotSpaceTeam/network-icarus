package dev.dotspace.network.library.game.profile;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


/**
 * Implementation of skin
 */
@Getter
@Accessors(fluent=true)
public abstract class AbstractGameSkin<PROFILE> implements GameSkin<PROFILE> {
  /**
   * See {@link GameSkin#value()}
   */
  private final @NotNull String value;
  /**
   * See {@link GameSkin#signature()}
   */
  private final @Nullable String signature;
  /**
   * See {@link GameSkin#origin()}
   */
  private final @NotNull SkinOrigin origin;

  /**
   * Construct instance.
   *
   * @param value     of skin information.
   * @param signature of skin.
   * @param origin    get value from name or url of texture (base64 encoded)
   */
  public AbstractGameSkin(@Nullable final String value,
                          @Nullable final String signature,
                          @Nullable final SkinOrigin origin) {
    this.value = Objects.requireNonNull(value);
    this.signature = signature;
    this.origin = Objects.requireNonNull(origin);
  }

  @Override
  public @NotNull Optional<String> signature() {
    return Optional.ofNullable(this.signature);
  }

  /**
   * Get random name.
   */
  protected @NotNull String randomName() {
    return UUID.randomUUID().toString().split("-")[0] /*Gen rmd.*/;
  }
}
