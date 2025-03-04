#$function uhclifechallenge:challenges/start/$(id)
$function uhclifechallenge:challenges/start/$(id)

execute as @a run function uhclifechallenge:broadcast_challenge_info with storage uhclifechallenge:active data
tellraw @a {"text":"Use command '/trigger info' to get this message again","color":"gray"}

title @a times 20 160 20
$title @a title {"text":$(title),"bold":true,"color":"green"}