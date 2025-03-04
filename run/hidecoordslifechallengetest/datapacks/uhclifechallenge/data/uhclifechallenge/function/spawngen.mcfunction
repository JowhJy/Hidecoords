execute positioned 0 230 0 run kill @e[type=text_display,distance=..50]
execute positioned 0 230 0 run kill @e[type=item_display,distance=..50]
execute positioned 0 230 0 run kill @e[type=interaction,distance=..50]
setblock 0 226 -20 structure_block{metadata:"",mirror:"NONE",ignoreEntities:0b,powered:0b,seed:0L,rotation:"NONE",posX:0,mode:"LOAD",posY:-6,sizeX:19,posZ:1,integrity:1.0f,showair:0b,x:0,name:"uhclifechallenge:spawnne",y:226,z:-20,id:"minecraft:structure_block",sizeY:32,sizeZ:20,showboundingbox:1b}
setblock 0 227 -20 redstone_block
setblock -20 226 0 structure_block{metadata:"",mirror:"NONE",ignoreEntities:0b,powered:0b,seed:0L,rotation:"NONE",posX:1,mode:"LOAD",posY:-6,sizeX:19,posZ:-19,integrity:1.0f,showair:0b,x:-20,name:"uhclifechallenge:spawnnw",y:226,z:0,id:"minecraft:structure_block",sizeY:32,sizeZ:20,showboundingbox:1b}
setblock -20 227 0 redstone_block
setblock 0 226 20 structure_block{metadata:"",mirror:"NONE",ignoreEntities:0b,powered:0b,seed:0L,rotation:"NONE",posX:-19,mode:"LOAD",posY:-6,sizeX:19,posZ:-19,integrity:1.0f,showair:0b,x:0,name:"uhclifechallenge:spawnsw",y:226,z:20,id:"minecraft:structure_block",sizeY:32,sizeZ:19,showboundingbox:1b}
setblock 0 227 20 redstone_block
setblock 19 226 0 structure_block{metadata:"",mirror:"NONE",ignoreEntities:0b,powered:0b,seed:0L,rotation:"NONE",posX:-19,mode:"LOAD",posY:-6,sizeX:19,posZ:1,integrity:1.0f,showair:0b,x:19,name:"uhclifechallenge:spawnse",y:226,z:0,id:"minecraft:structure_block",sizeY:32,sizeZ:19,showboundingbox:1b}
setblock 19 227 0 redstone_block
fill 0 226 -20 0 227 -20 air
fill -20 226 0 -20 227 0 air
fill 0 226 20 0 227 20 air
fill 19 226 0 19 227 0 air
fill 1 250 1 10 250 10 barrier
fill 13 227 0 13 228 0 glass_pane[north=true,south=true]
setworldspawn 0 231 0
gamerule doDaylightCycle false
gamerule doWeatherCycle false
gamerule doMobSpawning false
time set 13500
tp @a[gamemode=!creative] 0 231 0
execute positioned 0 228 12 run team join color_green @e[type=armor_stand,distance=..4]
execute positioned 0 228 -16 run team join color_green @e[type=armor_stand,distance=..4]
execute positioned 0 228 12 run tag @e[type=armor_stand,distance=..4] add text
execute positioned 0 228 -16 run tag @e[type=armor_stand,distance=..4] add text

scoreboard players reset #spawngen_success uhclc
execute if block 0 229 0 beacon if block -1 229 1 green_terracotta run scoreboard players set #spawngen_success uhclc 1
scoreboard players set #spawngen uhclc 1

