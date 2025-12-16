package com.jowhjy.hidecoords;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInterpolateSizeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderSizeChangedS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    private static EnumSet<Wall> visibleBorders(BlockPos location, ServerWorld world) {
        double viewDistanceBlocks = world.getServer().getPlayerManager().getViewDistance() * 16;

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

    public static Packet<?> translate(@NotNull Packet<?> packet, Offset offset, ServerPlayerEntity player) {

        ServerWorld world = player.getEntityWorld();

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

        EnumSet<Wall> seenWalls = visibleBorders(player.getBlockPos(), world);
        if ((seenWalls.contains(Wall.X_POSITIVE) && seenWalls.contains(Wall.X_NEGATIVE)) ||
                (seenWalls.contains(Wall.Z_POSITIVE) && seenWalls.contains(Wall.Z_NEGATIVE))) {
            // If the player can see opposing walls, or obfuscation is disabled, we should just send the complete
            // offsetted border. No diameter change.
            if (packet.getPacketType().equals(PlayPackets.INITIALIZE_BORDER)) {
                WorldBorderInitializeS2CPacket typedPacket = (WorldBorderInitializeS2CPacket)(packet);
                long l = typedPacket.getSizeLerpTime();
                if (l > 0L) {
                    dummyWorldBorder.interpolateSize(typedPacket.getSize(), typedPacket.getSizeLerpTarget(), l, world.getTime());
                } else {
                    dummyWorldBorder.setSize(typedPacket.getSizeLerpTarget());
                }
                dummyWorldBorder.setMaxRadius(typedPacket.getMaxRadius());
                dummyWorldBorder.setWarningBlocks(typedPacket.getWarningBlocks());
                dummyWorldBorder.setWarningTime(typedPacket.getWarningTime());
                dummyWorldBorder.setCenter((typedPacket.getCenterX() + offset.getX()) * scaleFactor, (typedPacket.getCenterZ() + offset.getZ()) * scaleFactor);
                return new WorldBorderInitializeS2CPacket(dummyWorldBorder);
            } else if (packet.getPacketType().equals(PlayPackets.SET_BORDER_CENTER)) {
                WorldBorderCenterChangedS2CPacket typedPacket = (WorldBorderCenterChangedS2CPacket)(packet);
                dummyWorldBorder.setCenter((typedPacket.getCenterX() + offset.getX()) * scaleFactor, (typedPacket.getCenterZ() + offset.getZ()) * scaleFactor);
                return new WorldBorderCenterChangedS2CPacket(dummyWorldBorder);
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

        if (packet.getPacketType().equals(PlayPackets.INITIALIZE_BORDER)) {
            var typedPacket = (WorldBorderInitializeS2CPacket)(packet);
            long l = typedPacket.getSizeLerpTime();
            if (l > 0L) {
                dummyWorldBorder.interpolateSize(diameter, diameter, l, world.getTime());
            } else {
                dummyWorldBorder.setSize(diameter);
            }
            dummyWorldBorder.setMaxRadius(typedPacket.getMaxRadius());
            dummyWorldBorder.setWarningBlocks(typedPacket.getWarningBlocks());
            dummyWorldBorder.setWarningTime(typedPacket.getWarningTime());
            dummyWorldBorder.setCenter(centerX * scaleFactor,  centerZ * scaleFactor);
            return new WorldBorderInitializeS2CPacket(dummyWorldBorder);
        }
        if (packet.getPacketType().equals(PlayPackets.SET_BORDER_CENTER)) {
            dummyWorldBorder.setCenter(centerX * scaleFactor,  centerZ * scaleFactor);
            return new WorldBorderCenterChangedS2CPacket(dummyWorldBorder);
        }
        if (packet.getPacketType().equals(PlayPackets.SET_BORDER_LERP_SIZE)) {
            var typedPacket = (WorldBorderInterpolateSizeS2CPacket)(packet);
            long l = typedPacket.getSizeLerpTime();
            if (l > 0L) {
                dummyWorldBorder.interpolateSize(diameter, diameter, l, world.getTime());
            } else {
                dummyWorldBorder.setSize(diameter);
            }
            return new WorldBorderInterpolateSizeS2CPacket(dummyWorldBorder);
        }
        if (packet.getPacketType().equals(PlayPackets.SET_BORDER_SIZE)) {
            dummyWorldBorder.setSize(diameter);
            return new WorldBorderSizeChangedS2CPacket(dummyWorldBorder);
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
