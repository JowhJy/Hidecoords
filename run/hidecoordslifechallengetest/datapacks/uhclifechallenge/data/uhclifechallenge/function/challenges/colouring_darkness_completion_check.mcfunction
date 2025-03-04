scoreboard players reset @s complete

execute unless entity @s[nbt={Dimension:"minecraft:the_nether"}] run return run tellraw @s {"text":"You have to be in the Nether!","color":"red"}

execute unless entity @e[type=sheep,distance=..16,nbt={Color:0b}] run return run tellraw @s {"text":"Missing white sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:1b}] run return run tellraw @s {"text":"Missing orange sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:2b}] run return run tellraw @s {"text":"Missing magenta sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:3b}] run return run tellraw @s {"text":"Missing light blue sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:4b}] run return run tellraw @s {"text":"Missing yellow sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:5b}] run return run tellraw @s {"text":"Missing lime sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:6b}] run return run tellraw @s {"text":"Missing pink sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:7b}] run return run tellraw @s {"text":"Missing dark gray sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:8b}] run return run tellraw @s {"text":"Missing light gray sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:9b}] run return run tellraw @s {"text":"Missing cyan sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:10b}] run return run tellraw @s {"text":"Missing purple shep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:11b}] run return run tellraw @s {"text":"Missing blue sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:12b}] run return run tellraw @s {"text":"Missing brown sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:13b}] run return run tellraw @s {"text":"Missing green sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:14b}] run return run tellraw @s {"text":"Missing red sheep!","color":"red"}
execute unless entity @e[type=sheep,distance=..16,nbt={Color:15b}] run return run tellraw @s {"text":"Missing the black sheep in this family!","color":"red"}

execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:0b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:1b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:2b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:3b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:4b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:5b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:6b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:7b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:8b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:9b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:10b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:11b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:12b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:13b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:14b}] at @s run tp @s ~ -666 ~
execute as @e[limit=1,type=sheep,distance=..16,nbt={Color:15b}] at @s run tp @s ~ -666 ~

function uhclifechallenge:challenges/complete



