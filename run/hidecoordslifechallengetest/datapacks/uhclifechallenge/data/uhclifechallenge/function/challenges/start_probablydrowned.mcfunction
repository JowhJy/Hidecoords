title @a times 20 200 20
title @a subtitle {"text":"Blijf 3 minuten onder water zonder Water Breathing potion.","color":"green"}
title @a title {"text":"Probably Drowned","bold":true,"color":"yellow"}
tag @e[type=armor_stand,tag=challenge,name=RNG] add challenge_probablydrowned
tellraw @a ["",{"text":"Probably Drowned","bold":true,"color":"yellow"},{"text":"\n"},{"text":"Zorg dat je 3 minuten onder water blijft zonder een Water Breathing potion te maken, hoe moeilijk het ook is... Gebruik gewoon water - geen waterlogged blok!","color":"green"}]
scoreboard objectives add watertime dummy "Water Time (Ticks)"
scoreboard objectives setdisplay sidebar watertime