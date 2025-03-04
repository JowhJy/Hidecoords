execute as @a[scores={breakstone=1}] at @s run playsound minecraft:item.totem.use player @s ~ ~ ~ 1 .7
tellraw @a[scores={breakstone=1}] {"color":"red","text":"STEEN BREKEN MAG NIET. Dit is je enige waarschuwing. De volgende keer ga je DOOD!"}
scoreboard players set @a[scores={breakstone=1}] breakstone 2
kill @a[scores={breakstone=3..}]
scoreboard players reset @a[scores={breakstone=3..}] breakstone
tag @a[tag=!success,tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:diamond_helmet",tag:{Enchantments:[{id:"minecraft:protection",lvl:2s}]}}]}] add requirement
clear @a[tag=requirement] diamond_helmet{Enchantments:[{id:"minecraft:protection",lvl:2}]}
execute as @a[tag=requirement,tag=!success] run function uhclifechallenge:challenges/complete
tag @a[tag=requirement] remove requirement