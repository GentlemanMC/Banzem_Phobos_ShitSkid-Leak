/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.mohalk.banzem.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

public class FriendManager
extends Feature {
    private final Map<String, UUID> friends = new HashMap<String, UUID>();

    public FriendManager() {
        super("Friends");
    }

    public boolean isFriend(String name) {
        return this.friends.get(name) != null;
    }

    public boolean isFriend(EntityPlayer player) {
        return this.isFriend(player.func_70005_c_());
    }

    public void addFriend(String name) {
        Friend friend = this.getFriendByName(name);
        if (friend != null) {
            this.friends.put(friend.getUsername(), friend.getUuid());
        }
    }

    public void removeFriend(String name) {
        this.friends.remove(name);
    }

    public void onLoad() {
        this.friends.clear();
        this.clearSettings();
    }

    public void saveFriends() {
        this.clearSettings();
        for (Map.Entry<String, UUID> entry : this.friends.entrySet()) {
            this.register(new Setting<String>(entry.getValue().toString(), entry.getKey()));
        }
    }

    public Map<String, UUID> getFriends() {
        return this.friends;
    }

    public Friend getFriendByName(String input) {
        UUID uuid = PlayerUtil.getUUIDFromName(input);
        if (uuid != null) {
            return new Friend(input, uuid);
        }
        return null;
    }

    public void addFriend(Friend friend) {
        this.friends.put(friend.getUsername(), friend.getUuid());
    }

    public static class Friend {
        private final String username;
        private final UUID uuid;

        public Friend(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        public String getUsername() {
            return this.username;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public boolean equals(Object other) {
            return other instanceof Friend && ((Friend)other).getUsername().equals(this.username) && ((Friend)other).getUuid().equals(this.uuid);
        }

        public int hashCode() {
            return this.username.hashCode() + this.uuid.hashCode();
        }
    }
}

