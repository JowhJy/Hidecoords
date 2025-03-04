advancement revoke @s only uhclifechallenge:info_past

#legacy
tellraw @s ["",{"text":"\n"},{"text":"This the eighth UHC Life Challenge. Past challenges were:","color":"dark_green"},{"text":"\n"},{"text":"I: ","color":"green"},{"text":"Fire Suit","bold":true,"color":"green"},{"text":"\nII: ","color":"green"},{"text":"Well Prepared","bold":true,"color":"green"},{"text":"\nIII: ","color":"green"},{"text":"Unarmed Adventure","bold":true,"color":"green"},{"text":"\nIV: ","color":"green"},{"text":"High Level","bold":true,"color":"green"},{"text":"\nV: ","color":"green"},{"text":"Probably Drowned","bold":true,"color":"green"},{"text":"\nVI: ","color":"green"},{"text":"Harmless Adventure","bold":true,"color":"green"},{"text":"\nVII: ","color":"green"},{"text":"Real Caving","bold":true,"color":"green"}]

function uhclifechallenge:print_challenge_info with storage uhclifechallenge:challenges/voyage

tellraw @s {"text":"(order might not be correct for the first few; hover info coming soon!)","color":"gray"}
