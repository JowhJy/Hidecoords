effect give @a resistance 1 100
tp @a 0 231 0
function uhclifechallenge:timer/reset
function uhclifechallenge:timer/hide
function uhclifechallenge:timer/stop
playsound minecraft:entity.ender_dragon.growl ambient @a ~ ~ ~ 42753
scoreboard players reset #spawngen uhclc
scoreboard players reset #challenge_started uhclc
scoreboard objectives remove rottenflesh
scoreboard objectives remove bone
scoreboard objectives remove string
scoreboard objectives remove gunpowder
scoreboard objectives remove enderpearl
scoreboard objectives remove blazerod
scoreboard objectives remove bats
scoreboard objectives remove zombiekill
scoreboard objectives remove skeletonkill
scoreboard objectives remove creeperkill
scoreboard objectives remove spiderkill
scoreboard objectives remove cavespiderkill
scoreboard objectives remove wskeletonkill
scoreboard objectives remove endermankill
scoreboard objectives remove blazekill
scoreboard objectives remove zombiepigmankill
scoreboard objectives remove witchkill
scoreboard objectives remove ghastkill
scoreboard objectives remove silverfishkill
scoreboard objectives remove slimekill
scoreboard objectives remove magmacubekill
scoreboard objectives remove watertime
scoreboard objectives remove breakstone
scoreboard objectives remove complete
scoreboard objectives remove vaults