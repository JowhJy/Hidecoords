package com.jowhjy.hidecoords;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.WorldBorder;

/**
 * Adapted from jt_prince
 * World border packets require special handling, since applying a plain offset would run into two problems:
 * <ul>
 *     <li>The player can intercept the packet and use it to derive their offset.</li>
 *     <li>World border coordinates apply world scaling for some reason (the only packets that seem to...)</li>
 * </ul>
 * <a href="https://github.com/joshuaprince/CoordinateOffset/wiki/Implications-and-Limitations#world-border">Wiki</a>
 */
public class WorldBorderObfuscator {
    private static final double BASELINE_SIZE = 60_000_000;

    private static EnumSet<Wall> visibleBorders(BlockPos location, ServerLevel world) {
        double viewDistanceBlocks = world.getServer().getPlayerList().getViewDistance() * 16;

        WorldBorder realBorder = world.getWorldBorder();
        double xMax = realBorder.getCenterX() + realBorder.getSize() / 2;
        double xMin = realBorder.getCenterX() - realBorder.getSize() / 2;
        double zMax = realBorder.getCenterZ() + realBorder.getSize() / 2;
        double zMin = realBorder.getCenterZ() - realBorder.getSize() / 2;

        EnumSet<Wall> seen = EnumSet.noneOf(Wall.class);
        if (xMax - location.getX() < viewDistanceBlocks) {
            seen.add(Wall.X_POSITIVE);
        }
        if (location.getX() - xMin < viewDistanceBlocks) {
            seen.add(Wall.X_NEGATIVE);
        }
        if (zMax - location.getZ() < viewDistanceBlocks) {
            seen.add(Wall.Z_POSITIVE);
        }
        if (location.getZ() - zMin < viewDistanceBlocks) {
            seen.add(Wall.Z_NEGATIVE);
        }

        return seen;
    }

    public static Packet<?> translate(@NotNull Packet<?> packet, Offset offset, ServerPlayer player) {

        ServerLevel world = player.level();

        /*
         * For reasons I cannot fathom, the Minecraft protocol applies the world's coordinate scaling to the world
         * border center location. (e.g. if I wanted to center a border at Nether coordinates (100,100), I would need to
         * send a packet containing (800, 800) as the center.)
         *
         * This could cause problems if the server is running a custom world with a different coordinateScale (which is
         * only accessible through NMS as DimensionType::coordinateScale). For now, just checking environment should be
         * enough.
         */
        double scaleFactor = 1; //JowhJy update 1.21.11: seems that this is no longer the case now, set it to 1.

        //dummy world border used for packet creation
        WorldBorder dummyWorldBorder = new WorldBorder();

        EnumSet<Wall> seenWalls = visibleBorders(player.blockPosition(), world);
        if ((seenWalls.contains(Wall.X_POSITIVE) && seenWalls.contains(Wall.X_NEGATIVE)) ||
                (seenWalls.contains(Wall.Z_POSITIVE) && seenWalls.contains(Wall.Z_NEGATIVE))) {
            // If the player can see opposing walls, or obfuscation is disabled, we should just send the complete
            // offsetted border. No diameter change.
            if (packet.type().equals(GamePacketTypes.CLIENTBOUND_INITIALIZE_BORDER)) {
                ClientboundInitializeBorderPacket typedPacket = (ClientboundInitializeBorderPacket)(packet);
                long l = typedPacket.getLerpTime();
                if (l > 0L) {
                    dummyWorldBorder.lerpSizeBetween(typedPacket.getOldSize(), typedPacket.getNewSize(), l, world.getGameTime());
                } else {
                    dummyWorldBorder.setSize(typedPacket.getNewSize());
                }
                dummyWorldBorder.setAbsoluteMaxSize(typedPacket.getNewAbsoluteMaxSize());
                dummyWorldBorder.setWarningBlocks(typedPacket.getWarningBlocks());
                dummyWorldBorder.setWarningTime(typedPacket.getWarningTime());
                dummyWorldBorder.setCenter((typedPacket.getNewCenterX() + offset.getX()) * scaleFactor, (typedPacket.getNewCenterZ() + offset.getZ()) * scaleFactor);
                return new ClientboundInitializeBorderPacket(dummyWorldBorder);
            } else if (packet.type().equals(GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER)) {
                ClientboundSetBorderCenterPacket typedPacket = (ClientboundSetBorderCenterPacket)(packet);
                dummyWorldBorder.setCenter((typedPacket.getNewCenterX() + offset.getX()) * scaleFactor, (typedPacket.getNewCenterZ() + offset.getZ()) * scaleFactor);
                return new ClientboundSetBorderCenterPacket(dummyWorldBorder);
            }
            return packet;
        }

        //real border
        final WorldBorder border = world.getWorldBorder();

        double centerX = 0.0, centerZ = 0.0;
        final double diameter = BASELINE_SIZE;

        if (!seenWalls.isEmpty()) {
            // The player can see one wall, or two walls that are on different axes, adjust the border such that the
            // walls they can't see are a constant and large distance away.
            if (seenWalls.contains(Wall.X_POSITIVE)) {
                double realXMax = border.getCenterX() + border.getSize() / 2;
                centerX = realXMax - (BASELINE_SIZE / 2);
                centerX += offset.getX();
            }
            if (seenWalls.contains(Wall.X_NEGATIVE)) {
                double realXMin = border.getCenterX() - border.getSize() / 2;
                centerX = realXMin + (BASELINE_SIZE / 2);
                centerX += offset.getX();
            }
            if (seenWalls.contains(Wall.Z_POSITIVE)) {
                double realZMax = border.getCenterZ() + border.getSize() / 2;
                centerZ = realZMax - (BASELINE_SIZE / 2);
                centerZ += offset.getZ();
            }
            if (seenWalls.contains(Wall.Z_NEGATIVE)) {
                double realZMin = border.getCenterZ() - border.getSize() / 2;
                centerZ = realZMin + (BASELINE_SIZE / 2);
                centerZ += offset.getZ();
            }
        } else {
            // The player cannot see any walls. Fully obfuscate the worldborder.
            centerX = centerZ = 0.0;
        }

        if (packet.type().equals(GamePacketTypes.CLIENTBOUND_INITIALIZE_BORDER)) {
            var typedPacket = (ClientboundInitializeBorderPacket)(packet);
            long l = typedPacket.getLerpTime();
            if (l > 0L) {
                dummyWorldBorder.lerpSizeBetween(diameter, diameter, l, world.getGameTime());
            } else {
                dummyWorldBorder.setSize(diameter);
            }
            dummyWorldBorder.setAbsoluteMaxSize(typedPacket.getNewAbsoluteMaxSize());
            dummyWorldBorder.setWarningBlocks(typedPacket.getWarningBlocks());
            dummyWorldBorder.setWarningTime(typedPacket.getWarningTime());
            dummyWorldBorder.setCenter(centerX * scaleFactor,  centerZ * scaleFactor);
            return new ClientboundInitializeBorderPacket(dummyWorldBorder);
        }
        if (packet.type().equals(GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER)) {
            dummyWorldBorder.setCenter(centerX * scaleFactor,  centerZ * scaleFactor);
            return new ClientboundSetBorderCenterPacket(dummyWorldBorder);
        }
        if (packet.type().equals(GamePacketTypes.CLIENTBOUND_SET_BORDER_LERP_SIZE)) {
            var typedPacket = (ClientboundSetBorderLerpSizePacket)(packet);
            long l = typedPacket.getLerpTime();
            if (l > 0L) {
                dummyWorldBorder.lerpSizeBetween(diameter, diameter, l, world.getGameTime());
            } else {
                dummyWorldBorder.setSize(diameter);
            }
            return new ClientboundSetBorderLerpSizePacket(dummyWorldBorder);
        }
        if (packet.type().equals(GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE)) {
            dummyWorldBorder.setSize(diameter);
            return new ClientboundSetBorderSizePacket(dummyWorldBorder);
        }
        return packet;
    }

    enum Wall {
        X_POSITIVE,
        X_NEGATIVE,
        Z_POSITIVE,
        Z_NEGATIVE
    }
}
