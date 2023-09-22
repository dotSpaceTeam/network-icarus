package dev.dotspace.network.library.game.message.context;

public enum ContextType {
  //Message will only be handled offline.
  OFFLINE,

  //Message will be sent to node to be parsed and content will be replaced if present.
  ONLINE,

  //Message is a stored key.
  KEY
}
