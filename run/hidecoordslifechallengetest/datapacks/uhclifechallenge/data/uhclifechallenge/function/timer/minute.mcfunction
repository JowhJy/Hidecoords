scoreboard players set #timer_seconds uhclc 0
scoreboard players add #timer_minutes uhclc 1
execute if score #timer_minutes uhclc matches 60.. run function uhclifechallenge:timer/hour