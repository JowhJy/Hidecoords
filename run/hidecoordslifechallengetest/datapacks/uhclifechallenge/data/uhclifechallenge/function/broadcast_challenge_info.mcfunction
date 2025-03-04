

$tellraw @s {"text":$(title),"bold":true,"color":"green"}
$tellraw @s $(description)

#changed my mind!
#function uhclifechallenge:broadcast_seed with storage uhclifechallenge:data

scoreboard players reset @s info
scoreboard players enable @s info