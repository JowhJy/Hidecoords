tag @s add success
execute at @s run playsound minecraft:entity.firework_rocket.twinkle ambient @a ~ ~ ~ 25
execute at @s run playsound minecraft:block.end_portal_frame.fill ambient @a ~ ~ ~ 25 1.5
tellraw @s {"text":"Well done! You'll be granted your life after the time is up :D","color":"green"}