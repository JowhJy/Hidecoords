kill @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:potion",tag:{Potion:"minecraft:water_breathing"}}]}]
kill @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:potion",tag:{Potion:"minecraft:long_water_breathing"}}]}]
execute as @a[tag=inchallenge,gamemode=!creative,tag=!success] at @s unless block ~ ~1.75 ~ water unless score @s watertime matches 3600 run scoreboard players set @s watertime 3600
execute as @a[tag=inchallenge,gamemode=!creative] at @s if block ~ ~1.75 ~ water if score @s watertime matches 1..3600 run scoreboard players remove @s watertime 1
execute as @a[tag=inchallenge,gamemode=!creative,scores={watertime=0},tag=!success] run function uhclifechallenge:challenges/complete
scoreboard players reset @e[scores={watertime=0},gamemode=!creative] watertime