package dev.dotspace.network.node.message.web;

import dev.dotspace.network.node.message.IStringMessage;
import dev.dotspace.network.node.message.text.ITextMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/message")
public final class MessageController {

  /**
   * Get a formatted message.
   */
  @PostMapping("/")
  @ResponseBody
  public ResponseEntity<ITextMessage> getFormatted(@RequestBody @NotNull final IStringMessage iStringMessage) throws InterruptedException {

    return null;
  }

  /**
   * Get an profile from unique id.
   */
  @PutMapping("/key")
  @ResponseBody
  public ResponseEntity<ITextMessage> putKey(@PathVariable @NotNull final String IStringMessage) throws InterruptedException {

    return null;
  }


  /**
   * Get an profile from unique id.
   */
  @GetMapping("/key/{key}")
  @ResponseBody
  public ResponseEntity<ITextMessage> getKey(@PathVariable @NotNull final String IStringMessage) throws InterruptedException {

    return null;
  }


}
