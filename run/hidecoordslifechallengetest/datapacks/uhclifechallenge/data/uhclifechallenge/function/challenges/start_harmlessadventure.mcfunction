title @a times 20 200 20
title @a subtitle {"text":"Bemachtig een Blaze Rod... Maar geen monsters vermoorden!","color":"green"}
title @a title {"text":"Harmless Adventure","bold":true,"color":"yellow"}
tag @e[type=armor_stand,tag=challenge,name=RNG] add challenge_harmlessadventure
tellraw @a ["",{"text":"Harmless Adventure","bold":true,"color":"yellow"},{"text":"\n"},{"text":"Je moet zonder ooit (direct) monsters af te maken een Blaze Rod krijgen, maar LET OP: Maak je toch een monster af, dan GA JE DOOD.","color":"green"}]
scoreboard objectives add zombiekill minecraft.killed:minecraft.zombie
scoreboard objectives add skeletonkill minecraft.killed:minecraft.skeleton
scoreboard objectives add creeperkill minecraft.killed:minecraft.creeper
scoreboard objectives add spiderkill minecraft.killed:minecraft.spider
scoreboard objectives add cavespiderkill minecraft.killed:minecraft.cave_spider
scoreboard objectives add wskeletonkill minecraft.killed:minecraft.wither_skeleton
scoreboard objectives add endermankill minecraft.killed:minecraft.enderman
scoreboard objectives add blazekill minecraft.killed:minecraft.blaze
scoreboard objectives add zombiepigmankill minecraft.killed:minecraft.zombie_pigman
scoreboard objectives add witchkill minecraft.killed:minecraft.witch
scoreboard objectives add ghastkill minecraft.killed:minecraft.ghast
scoreboard objectives add silverfishkill minecraft.killed:minecraft.silverfish
scoreboard objectives add slimekill minecraft.killed:minecraft.slime
scoreboard objectives add magmacubekill minecraft.killed:minecraft.magma_cube