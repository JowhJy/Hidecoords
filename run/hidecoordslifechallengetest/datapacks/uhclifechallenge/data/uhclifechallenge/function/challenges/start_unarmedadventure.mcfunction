title @a times 20 200 20
title @a subtitle {"text":"Maak een Potion of Regeneration... Maar zwaarden en bogen zijn verboden!","color":"green"}
title @a title {"text":"Unarmed Adventure","bold":true,"color":"yellow"}
tag @e[type=armor_stand,tag=challenge,name=RNG] add challenge_unarmedadventure
tellraw @a ["",{"text":"Unarmed Adventure","bold":true,"color":"yellow"},{"text":"\n"},{"text":"Maak een Potion of Regeneration, maar LET OP: Als je een zwaard of boog in je inventory hebt GA JE DOOD.","color":"green"}]