title @a times 20 200 20
title @a subtitle {"text":"Bemachtig een Diamond Helmet met Protection II... maar geen steen breken!","color":"green"}
title @a title {"text":"Real Caving","bold":true,"color":"yellow"}
tag @e[type=armor_stand,tag=challenge,name=RNG] add challenge_realcaving
tellraw @a ["",{"text":"Real Caving","bold":true,"color":"yellow"},{"text":"\n"},{"text":"Zorg dat je een Diamond Helmet met Protection II krijgt. Maar... als je steen breekt GA JE DOOD!","color":"green"}]
scoreboard objectives add breakstone minecraft.mined:minecraft.stone