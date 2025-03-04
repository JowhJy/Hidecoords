title @a times 20 200 20
title @a subtitle {"text":"Dood 15 blazes, maar je mag de Nether niet verlaten.","color":"green"}
title @a title {"text":"Well Prepared","bold":true,"color":"yellow"}
tag @e[type=armor_stand,tag=challenge,name=RNG] add challenge_wellprepared
tellraw @a ["",{"text":"Well Prepared","bold":true,"color":"yellow"},{"text":"\n"},{"text":"Dood 15 blazes, maar bereid je heel goed voor voordat je de Nether binnengaat; je mag deze namelijk niet meer verlaten...","color":"green"}]
scoreboard objectives add bats minecraft.killed:minecraft.blaze "Blazes"
scoreboard objectives setdisplay sidebar bats