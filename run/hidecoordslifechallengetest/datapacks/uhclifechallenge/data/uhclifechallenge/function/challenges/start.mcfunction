gamerule doDaylightCycle true
gamerule doWeatherCycle true
gamerule doMobSpawning true
time set 0
gamemode survival @a[tag=preinchallenge]
scoreboard players set #challenge_started uhclc 1
fill 20 232 20 -20 242 -20 air
fill 20 220 20 -20 231 -20 air
kill @e[type=item,x=0,y=230,z=0,distance=..50]
kill @e[type=item_display,x=0,y=230,z=0,distance=..50]
kill @e[type=text_display,x=0,y=230,z=0,distance=..50]
tp @a[tag=preinchallenge] 0 230 0
tag @a[tag=preinchallenge] add inchallenge
tag @a[tag=preinchallenge] remove preinchallenge
function uhclifechallenge:timer/reset
function uhclifechallenge:timer/start
effect give @a[tag=inchallenge] resistance 120 10 true
function uhclifechallenge:challenges/juhc_start

#roll
execute store result score #rng uhclc run random value 1..5
execute if score #rng uhclc matches 1 run data modify storage uhclifechallenge:active data set from storage uhclifechallenge:challenges/colouring_darkness {}
execute if score #rng uhclc matches 2 run data modify storage uhclifechallenge:active data set from storage uhclifechallenge:challenges/risky_robbery {}
execute if score #rng uhclc matches 3 run data modify storage uhclifechallenge:active data set from storage uhclifechallenge:challenges/pesky_bats {}
execute if score #rng uhclc matches 4 run data modify storage uhclifechallenge:active data set from storage uhclifechallenge:challenges/the_hunt {}
execute if score #rng uhclc matches 5 run data modify storage uhclifechallenge:active data set from storage uhclifechallenge:challenges/social_distancing {}

function uhclifechallenge:challenges/startchallenge with storage uhclifechallenge:active data
