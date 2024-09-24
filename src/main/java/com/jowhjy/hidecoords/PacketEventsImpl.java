package com.jowhjy.hidecoords;

/*
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.entity.EntityData;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.Map;
import java.util.stream.Collectors;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.LodestoneTracker;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleItemStackData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleVibrationData;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
*/
public class PacketEventsImpl /*implements PacketListener*/ {

/*
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        ServerCommonNetworkHandler networkHandler = ((ServerPlayerEntity)(event.getPlayer())).networkHandler;
        Offset offset = ((HasCoordOffset)networkHandler).juhc$getCoordOffset();

        unoffset(event,offset);
    }

    private void unoffset(PacketReceiveEvent event, Offset offset) {
        PacketTypeCommon packetType = event.getPacketType();
        if (packetType == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);
            if (packet.getSlots().isPresent()) {
                Map<Integer, ItemStack> clientItems = packet.getSlots().get();
                Map<Integer, ItemStack> serverItems = clientItems.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, v -> unapplyOffset(v.getValue(), offset)));
                packet.setSlots(Optional.of(serverItems));
            }
            packet.setCarriedItemStack(unapplyOffset(packet.getCarriedItemStack(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION) {
            WrapperPlayClientCreativeInventoryAction packet = new WrapperPlayClientCreativeInventoryAction(event);
            packet.setItemStack(unapplyOffset(packet.getItemStack(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.GENERATE_STRUCTURE) {
            WrapperPlayClientGenerateStructure packet = new WrapperPlayClientGenerateStructure(event);
            packet.setBlockPosition(unapplyOffset(packet.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(event);
            packet.setBlockPosition(unapplyOffset(packet.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);
            packet.setBlockPosition(unapplyOffset(packet.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.PLAYER_POSITION) {
            WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(event);
            packet.setLocation(unapplyOffset(packet.getLocation(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.UPDATE_COMMAND_BLOCK) {
            WrapperPlayClientUpdateCommandBlock packet = new WrapperPlayClientUpdateCommandBlock(event);
            packet.setPosition(unapplyOffset(packet.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.UPDATE_JIGSAW_BLOCK) {
            WrapperPlayClientUpdateJigsawBlock packet = new WrapperPlayClientUpdateJigsawBlock(event);
            packet.setPosition(unapplyOffset(packet.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.UPDATE_SIGN) {
            WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event);
            packet.setBlockPosition(unapplyOffset(packet.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Client.VEHICLE_MOVE) {
            WrapperPlayClientVehicleMove packet = new WrapperPlayClientVehicleMove(event);
            packet.setPosition(unapplyOffset(packet.getPosition(), offset));
            return;
        }


    }

    private static Vector3d unapplyOffset(Vector3d position, Offset offset) {
        return position.add(offset.getX(), 0, offset.getZ());
    }

    //todo credit jt_prince!!
    private static Location unapplyOffset(Location loc, Offset offset) {
        loc.setPosition(new Vector3d(loc.getX() + offset.getX(), loc.getY(), loc.getZ() + offset.getZ()));
        return loc;
    }

    private static Vector3i unapplyOffset(Vector3i blockPosition, Offset offset) {
        return blockPosition.add(offset.getX(), 0, offset.getZ());
    }

    private static ItemStack unapplyOffset(ItemStack itemStack, Offset offset) {
        return applyOffset(itemStack, new Offset(offset.blockPos.multiply(-1)));
    }

    @Override
    public void onPacketSend(PacketSendEvent event)
    {

        ServerCommonNetworkHandler networkHandler = ((ServerPlayerEntity)(event.getPlayer())).networkHandler;
        Offset offset = ((HasCoordOffset)networkHandler).juhc$getCoordOffset();

        offset(event,offset);

        // do other things

    }

    //TODO credit jtprince!!
    private void offset(PacketSendEvent event, Offset offset)  {
        PacketTypeCommon packetType = event.getPacketType();
        if (packetType == PacketType.Play.Server.BLOCK_ACTION) {
            WrapperPlayServerBlockAction wrapper = new WrapperPlayServerBlockAction(event);
            wrapper.setBlockPosition(applyOffset(wrapper.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.BLOCK_CHANGE) {
            WrapperPlayServerBlockChange wrapper = new WrapperPlayServerBlockChange(event);
            wrapper.setBlockPosition(applyOffset(wrapper.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.BLOCK_BREAK_ANIMATION) {
            WrapperPlayServerBlockBreakAnimation wrapper = new WrapperPlayServerBlockBreakAnimation(event);
            wrapper.setBlockPosition(applyOffset(wrapper.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.BLOCK_ENTITY_DATA) {
            WrapperPlayServerBlockEntityData wrapper = new WrapperPlayServerBlockEntityData(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.ACKNOWLEDGE_PLAYER_DIGGING) {
            WrapperPlayServerAcknowledgePlayerDigging wrapper = new WrapperPlayServerAcknowledgePlayerDigging(event);
            wrapper.setBlockPosition(applyOffset(wrapper.getBlockPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.CHUNK_DATA) {
            WrapperPlayServerChunkData wrapper = new WrapperPlayServerChunkData(event);
            wrapper.setColumn(applyOffset(wrapper.getColumn(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_EQUIPMENT) {
            WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(event);
            for (Equipment equipment : wrapper.getEquipment()) {
                equipment.setItem(applyOffset(equipment.getItem(), offset));
            }
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata(event);
            for (EntityData data : wrapper.getEntityMetadata()) {
                Object value = data.getValue();
                if (value == null) continue;
                if (value instanceof Optional<?> optional) {
                    if (optional.isPresent()) {
                        data.setValue(Optional.of(applyOffsetToEntityMeta(optional.get(), offset)));
                    }
                } else {
                    data.setValue(applyOffsetToEntityMeta(value, offset));
                }
            }
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_TELEPORT) {
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.EXPLOSION) {
            WrapperPlayServerExplosion wrapper = new WrapperPlayServerExplosion(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.FACE_PLAYER) {
            WrapperPlayServerFacePlayer wrapper = new WrapperPlayServerFacePlayer(event);
            wrapper.setTargetPosition(applyOffset(wrapper.getTargetPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.JOIN_GAME) {
            WrapperPlayServerJoinGame wrapper = new WrapperPlayServerJoinGame(event);
            if (wrapper.getLastDeathPosition() != null) wrapper.setLastDeathPosition(applyOffset(wrapper.getLastDeathPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.MULTI_BLOCK_CHANGE) {
            WrapperPlayServerMultiBlockChange wrapper = new WrapperPlayServerMultiBlockChange(event);
            wrapper.setChunkPosition(applyChunkOffset(wrapper.getChunkPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.OPEN_SIGN_EDITOR) {
            WrapperPlayServerOpenSignEditor wrapper = new WrapperPlayServerOpenSignEditor(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.PARTICLE) {
            WrapperPlayServerParticle wrapper = new WrapperPlayServerParticle(event);
            if (wrapper.getParticle().getData() instanceof ParticleVibrationData vibrationData) {
                if (vibrationData.getBlockPosition().isPresent()) {
                    vibrationData.setBlockPosition(applyOffset(vibrationData.getBlockPosition().get(), offset));
                }
            }

            if (wrapper.getParticle().getData() instanceof ParticleItemStackData itemStackData) {
                itemStackData.setItemStack(applyOffset(itemStackData.getItemStack(), offset));
            }
            return;
        }
        if (packetType == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            WrapperPlayServerPlayerPositionAndLook wrapper = new WrapperPlayServerPlayerPositionAndLook(event);
            if (!wrapper.isRelativeFlag(RelativeFlag.X)) {
                wrapper.setX(wrapper.getX() - offset.getX());
            }
            if (!wrapper.isRelativeFlag(RelativeFlag.Z)) {
                wrapper.setX(wrapper.getZ() - offset.getZ());
            }
            return;
        }
        if (packetType == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn wrapper = new WrapperPlayServerRespawn(event);
            if (wrapper.getLastDeathPosition() != null) wrapper.setLastDeathPosition(applyOffset(wrapper.getLastDeathPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.SET_SLOT) {
            WrapperPlayServerSetSlot wrapper = new WrapperPlayServerSetSlot(event);
            wrapper.setItem(applyOffset(wrapper.getItem(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.SOUND_EFFECT) {
            WrapperPlayServerSoundEffect wrapper = new WrapperPlayServerSoundEffect(event);
            wrapper.setEffectPosition(applyOffsetTimes8(wrapper.getEffectPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.SPAWN_ENTITY) {
            WrapperPlayServerSpawnEntity wrapper = new WrapperPlayServerSpawnEntity(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
            WrapperPlayServerSpawnLivingEntity wrapper = new WrapperPlayServerSpawnLivingEntity(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.SPAWN_EXPERIENCE_ORB) {
            WrapperPlayServerSpawnEntity wrapper = new WrapperPlayServerSpawnEntity(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.SPAWN_PAINTING) {
            WrapperPlayServerSpawnPainting wrapper = new WrapperPlayServerSpawnPainting(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        //in my mod this will be changed though. //todo figure out how mod/event priority works
        if (packetType == PacketType.Play.Server.SPAWN_POSITION) {
            WrapperPlayServerSpawnPosition wrapper = new WrapperPlayServerSpawnPosition(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.UNLOAD_CHUNK) {
            WrapperPlayServerUnloadChunk wrapper = new WrapperPlayServerUnloadChunk(event);
            wrapper.setChunkX(wrapper.getChunkX() - offset.getChunkX());
            wrapper.setChunkZ(wrapper.getChunkZ() - offset.getChunkZ());
            return;
        }
        //todo is this chunk thing right?
        if (packetType == PacketType.Play.Server.UPDATE_LIGHT) {
            WrapperPlayServerUpdateLight wrapper = new WrapperPlayServerUpdateLight(event);
            wrapper.setX(wrapper.getX() - offset.getChunkX());
            wrapper.setZ(wrapper.getZ() - offset.getChunkZ());
            return;
        }
        if (packetType == PacketType.Play.Server.UPDATE_VIEW_POSITION) {
            WrapperPlayServerUpdateViewPosition wrapper = new WrapperPlayServerUpdateViewPosition(event);
            wrapper.setChunkX(wrapper.getChunkX() - offset.getChunkX());
            wrapper.setChunkZ(wrapper.getChunkZ() - offset.getChunkZ());
            return;
        }
        if (packetType == PacketType.Play.Server.VEHICLE_MOVE) {
            WrapperPlayServerVehicleMove wrapper = new WrapperPlayServerVehicleMove(event);
            wrapper.setPosition(applyOffset(wrapper.getPosition(), offset));
            return;
        }
        if (packetType == PacketType.Play.Server.WINDOW_ITEMS) {
            WrapperPlayServerWindowItems wrapper = new WrapperPlayServerWindowItems(event);
            wrapper.setItems(wrapper.getItems().stream().map(it -> applyOffset(it, offset)).toList());
            if (wrapper.getCarriedItem().isPresent()) {
                wrapper.setCarriedItem(applyOffset(wrapper.getCarriedItem().get(), offset));
            }
        }


    }

    //TODO credit jtprince!!
    private static Vector3i applyOffsetTimes8(Vector3i effectPosition, Offset offset) {
        return effectPosition.add(-offset.getX() * 8, 0, -offset.getZ() * 8);
    }

    private static Vector3i applyChunkOffset(Vector3i chunkPosition, Offset offset) {
        return chunkPosition.add(-offset.getChunkX(), 0, -offset.getChunkZ());

    }

    private static Vector3d applyOffset(Vector3d position, Offset offset) {
        return position.add(-offset.getX(), 0, -offset.getZ());
    }

    //TODO credit jtprince!!
    private static Object applyOffsetToEntityMeta(Object o, Offset offset) {
        if (o instanceof Vector3i blockPosition) {
            // TBD: subtract instead of adding negative https://github.com/retrooper/packetevents/issues/646
            return blockPosition.add(-offset.getX(), 0, -offset.getZ());
        }
        if (o instanceof ItemStack) {
            return applyOffset((ItemStack) o, offset);
        }
        return o;
    }

    private static Column applyOffset(Column column, Offset offset) {
        return new Column(column.getX() - offset.getChunkX(), column.getZ() - offset.getChunkZ(), column.isFullChunk(), column.getChunks(), column.getTileEntities());
    }

    private static Vector3i applyOffset(Vector3i blockPosition, Offset offset) {
        return blockPosition.add(-offset.getX(), 0, -offset.getZ());
    }

    //TODO credit jtprince!!
    private static ItemStack applyOffset(ItemStack item, Offset offset) {
        if (item == null) return null;

        if (item.getType() == ItemTypes.COMPASS) {

            // 1.20.5+ only: Components
            Optional<?> lodestoneComponent = null;
            try {
                lodestoneComponent = item.getComponents().getPatches().get(ComponentTypes.LODESTONE_TRACKER);
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
            if (lodestoneComponent != null
                    && lodestoneComponent.isPresent()
                    && lodestoneComponent.get() instanceof LodestoneTracker lodestone
                    && lodestone.getTarget() != null) {
                lodestone.setTarget(applyOffset(lodestone.getTarget(), offset));
            }
        }

        return item;
    }
    private static WorldBlockPosition applyOffset(WorldBlockPosition blockPosition, Offset offset) {
        blockPosition.setBlockPosition(applyOffset(blockPosition.getBlockPosition(), offset));
        return blockPosition;
    }*/
}
