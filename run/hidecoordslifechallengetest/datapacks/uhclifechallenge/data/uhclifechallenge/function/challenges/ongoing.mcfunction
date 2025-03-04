$function uhclifechallenge:challenges/ongoing/$(id)
execute if score #timer_running uhclc matches 1 run function uhclifechallenge:timer/tick
gamemode spectator @a[gamemode=adventure,tag=!inchallenge]
gamemode spectator @a[gamemode=survival,tag=!inchallenge]
clear @a[gamemode=!creative,tag=!inchallenge]
xp add @a[gamemode=!creative,tag=!inchallenge] -666 levels
tag @a[scores={dead=1..},tag=inchallenge,tag=!success] add failure
tag @a[scores={dead=1..},tag=inchallenge] remove inchallenge
scoreboard players set @a[scores={dead=1..}] dead 0

execute as @a[scores={info=1..}] run function uhclifechallenge:broadcast_challenge_info with storage uhclifechallenge:active data