setblock 0 227 16 structure_block{metadata:"",mirror:"NONE",ignoreEntities:1b,powered:0b,seed:0L,author:"jo12345ji",rotation:"NONE",posX:-2,mode:"LOAD",posY:0,sizeX:5,posZ:-2,integrity:1.0f,showair:0b,x:0,name:"uhclifechallenge:lives_reset",y:227,z:16,id:"minecraft:structure_block",sizeY:7,sizeZ:5,showboundingbox:1b}
setblock 0 228 16 redstone_block

execute positioned 0 228 16 run kill @e[type=item,distance=..10,nbt={Item:{id:"minecraft:torch"}}]
