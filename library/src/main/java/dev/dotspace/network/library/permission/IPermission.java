package dev.dotspace.network.library.permission;

import jakarta.validation.constraints.NotNull;


public interface IPermission {

  @NotNull String permission();
}
