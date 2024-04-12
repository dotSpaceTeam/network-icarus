package dev.dotspace.network.library.game.mojang;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
public final class MojangUtils {
  /**
   * Mapper to convert string to jason and json to string.
   */
  private final static @NotNull ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Generate a json file similar to the one mojang sends with the client connection.
   *
   * @param uniqueId of profile given by mojang.
   * @param name     of the profile.
   * @param premium  type of profile.
   * @param textures to append to profile.
   * @return created profile json string.
   */
  public static @NotNull String buildTexture(@NotNull final String uniqueId,
                                             @NotNull final String name,
                                             final boolean premium,
                                             @NotNull final Map<String, String> textures) {
    final ObjectNode node = MAPPER.createObjectNode();

    node.put("timestamp", System.currentTimeMillis());
    node.put("profileId", uniqueId.replace("-", ""));
    node.put("profileName", name);
    node.put("signatureRequired", premium);

    //Create new node for textures.
    final ObjectNode textureNode = MAPPER.createObjectNode();
    textures.forEach((textureName, textureId) -> {
      textureNode.set(textureName, MAPPER.createObjectNode().put("url", buildTextureUrl(textureId)));
    });
    node.set("textures", textureNode);

    return node.toString();
  }

  /**
   * Get textures from {@link java.util.Base64} input profile.
   *
   * @param base64Input to get textures from.
   * @return map with present textures.
   */
  //Todo metadata.
  public static @NotNull Map<String, String> texturesIdMapFromBase64(@NotNull final String base64Input) {
    final Map<String, String> stringStringMap = new HashMap<>();
    final ObjectMapper mapper = new ObjectMapper();

    try {
      //Decode Base64 Input into byte[]
      final JsonNode jsonNode = mapper.readTree(new String(Base64.getDecoder().decode(base64Input), StandardCharsets.UTF_8));

      if (jsonNode.has("textures")) {
        jsonNode.get("textures").fields().forEachRemaining(stringJsonNodeEntry -> {
          final JsonNode valueNode = stringJsonNodeEntry.getValue();
          if (!valueNode.has("url")) { //Return if no ur was present
            return;
          }
          String url = valueNode.get("url").asText();
          //Remove static part of link -> reduce storage
          url = url.replace((url.startsWith("https") ? "https" : "http")+"://textures.minecraft.net/texture/", "");

          stringStringMap.put(stringJsonNodeEntry.getKey(), url);
        });
      }
    } catch (final Exception ignore) {
    }
    return stringStringMap;
  }

  /**
   * Build a valid texture url from id.
   * Pattern: <a href="javascript:void(0)">http://textures.minecraft.net/texture/{id}</a>
   *
   * @param id to generate url from.
   * @return generated texture url.
   */
  private static @NotNull String buildTextureUrl(final String id) {
    return "http://textures.minecraft.net/texture/"+id;
  }

}
