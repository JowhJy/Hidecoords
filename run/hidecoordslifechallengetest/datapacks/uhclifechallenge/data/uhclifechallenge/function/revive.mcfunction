tag @s remove failure
gamemode survival @s
effect give @s resistance 10 10 true
tag @s add inchallenge
tp @s 0 250 0

function uhclifechallenge:broadcast_challenge_info with storage uhclifechallenge:active data
tellraw @s {"text":"Use command '/trigger info' to get this message again","color":"gray"}

tellraw @a ["",{"selector":"@s","color":"yellow"},{"text":" has been revived!","color":"yellow"}]
