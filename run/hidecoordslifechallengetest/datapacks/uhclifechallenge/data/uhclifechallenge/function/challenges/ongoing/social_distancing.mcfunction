execute as @a[tag=inchallenge,nbt=!{active_effects:[{id:"minecraft:poison"}]}] at @s if entity @e[type=!#uhclifechallenge:social_distancing_disregard,type=!#uhclifechallenge:social_distancing_disregard_juhc,distance=.1..3.00] run effect give @s minecraft:poison 2 1

tag @a[tag=!success,tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:potion",components:{"minecraft:potion_contents":{potion:"minecraft:fire_resistance"}}},{id:"minecraft:potion",components:{"minecraft:potion_contents":{potion:"minecraft:water_breathing"}}},{id:"minecraft:potion",components:{"minecraft:potion_contents":{potion:"minecraft:oozing"}}}]}] add firepotion
clear @a[tag=firepotion] potion[minecraft:potion_contents="minecraft:fire_resistance"]
execute as @a[tag=firepotion,tag=!success] run function uhclifechallenge:challenges/complete
tag @a[tag=firepotion] remove firepotion