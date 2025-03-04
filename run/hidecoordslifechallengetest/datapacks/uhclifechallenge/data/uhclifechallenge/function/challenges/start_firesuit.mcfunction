title @a times 20 200 20
title @a subtitle {"text":"Enchant een setje iron armor met Fire Protection IV op alles.","color":"green"}
title @a title {"text":"Fire Suit","bold":true,"color":"yellow"}
tag @e[type=armor_stand,tag=challenge,name=RNG] add challenge_firesuit
tellraw @a ["",{"text":"Fire Suit","bold":true,"color":"yellow"},{"text":"\n"},{"text":"Maak een setje iron armor, met Fire Protection op alle stukken! Je krijgt 42 XP levels cadeau!","color":"green"}]
xp add @a[tag=inchallenge] 42 levels