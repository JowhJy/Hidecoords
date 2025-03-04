#perma night
execute store result score #daytime uhclc run time query daytime
execute if score #daytime uhclc matches 17999 run gamerule doDaylightCycle false
execute if score #daytime uhclc matches 17999 run time set 18000

#wheat limitation
clear @a[tag=inchallenge] minecraft:wheat

#completion
scoreboard players enable @a[tag=inchallenge] complete
execute as @a[tag=inchallenge,scores={complete=1..},tag=!success] at @s run function uhclifechallenge:challenges/colouring_darkness_completion_check