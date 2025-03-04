execute unless score #spawngen uhclc matches 1 run function uhclifechallenge:spawngen

execute unless score #challenge_started uhclc matches 1 run function uhclifechallenge:lives/check
execute unless score #challenge_started uhclc matches 1 run function uhclifechallenge:info
execute unless score #challenge_started uhclc matches 1 run function uhclifechallenge:spawn
execute if score #challenge_started uhclc matches 1 run function uhclifechallenge:challenges/ongoing with storage uhclifechallenge:active data
execute if score #timershow uhclc matches 1 run function uhclifechallenge:timer/showtimer
execute if score #intro uhclc matches 1.. run function uhclifechallenge:intro with storage uhclifechallenge:data
execute if score #timer_hours uhclc matches 3.. run function uhclifechallenge:end