execute if entity @a[x=0,y=231,z=0,distance=..50] unless score #spawngen_success uhclc matches 1 run function uhclifechallenge:spawngen

gamemode adventure @a[gamemode=survival]
#gamemode adventure @a[gamemode=spectator]
effect give @a resistance 1 10 true
effect give @a regeneration 1 10 true
effect give @a saturation 1 10 true
execute in minecraft:overworld run tp @a[x=0,y=231,z=0,distance=50..,gamemode=!creative,nbt={Dimension:"minecraft:overworld"}] 0 231 0
execute in minecraft:overworld run tp @a[gamemode=!creative,nbt=!{Dimension:"minecraft:overworld"}] 0 231 0
tag @a[tag=!success,tag=inchallenge] add failure
clear @a[tag=inchallenge]
xp add @a[tag=inchallenge] -666 levels
tag @a[tag=inchallenge] remove inchallenge
scoreboard players add @a[tag=success] uhc_lives 1
tellraw @a[tag=success] {"text":"CONGRATULATIONS! You succeeded at the challenge, and earned an extra life on the UHC server!","color":"green"}
execute as @a[tag=success] run function uhclifechallenge:juhc_addlife
tag @a[tag=success] remove success
tellraw @a[tag=failure] {"text":"Aw... Sorry, you didn't succeed at the challenge. Better luck next time :-(","color":"red"}
tag @a[tag=failure] remove failure