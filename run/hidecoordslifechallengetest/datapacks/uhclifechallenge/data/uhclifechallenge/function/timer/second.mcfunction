scoreboard players set #timer_ticks uhclc 0
scoreboard players add #timer_seconds uhclc 1
execute if score #timer_seconds uhclc matches 60.. run function uhclifechallenge:timer/minute