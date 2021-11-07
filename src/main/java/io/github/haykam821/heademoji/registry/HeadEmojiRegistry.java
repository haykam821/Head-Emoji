package io.github.haykam821.heademoji.registry;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.client.MinecraftClient;

/**
 * A registry for head emojis.
 */
public interface HeadEmojiRegistry {
	/**
	 * Gets a head emoji by its key.
	 * @param key the key of the head emoji
	 * @return the profile of the head emoji, or {@code null} if no head emoji was found
	 */
	public GameProfile get(String key);

	/**
	 * Gets the size of the registry.
	 * @return the registry size
	 */
	public int size();

	/**
	 * Registers a head emoji by its profile, replacing any existing head emoji.
	 * @param key the key of the head emoji
	 * @param profile the profile of the head emoji
	 * @return whether the head emoji was successfully registered
	 */
	public boolean register(String key, GameProfile profile);

	/**
	 * Registers a head emoji by its texture, replacing any existing head emoji.
	 * @param key the key of the head emoji
	 * @param texture the texture of the head emoji
	 * @return whether the head emoji was successfully registered
	 */
	public default boolean register(String key, String texture) {
		GameProfile profile = new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000000"), null);
		profile.getProperties().put("textures", new Property("textures", texture));

		MinecraftClient.getInstance().getSkinProvider().loadSkin(profile, null, false);
		return this.register(key, profile);
	}

	/**
	 * Unregisters a head emoji.
	 * @param key the key of the head emoji
	 * @return whether the head emoji was successfully unregistered
	 */
	public boolean unregister(String key);

	/**
	 * Clears the registry.
	 * @return whether the registry was successfully cleared
	 */
	public boolean clear();
}
