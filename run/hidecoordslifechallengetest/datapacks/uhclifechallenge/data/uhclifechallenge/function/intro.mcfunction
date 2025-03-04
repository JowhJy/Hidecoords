scoreboard players add #intro uhclc 1

execute if score #intro uhclc matches 5 run tellraw @a[tag=!preinchallenge] {"text":"You weren't in the teleportation room when the challenge started. Hope that was intentional...","color":"gold"}
execute if score #intro uhclc matches 300 run title @a times 10 100 10
$execute if score #intro uhclc matches 300 run title @a[tag=preinchallenge] subtitle {"text":"$(date)","color":"green"}
$execute if score #intro uhclc matches 300 run title @a[tag=preinchallenge] title {"text":"UHC Life Challenge $(number)","color":"yellow"}
execute if score #intro uhclc matches 300 run playsound entity.lightning.thunder ambient @a 15 227 0
execute if score #intro uhclc matches 500 run title @a[tag=preinchallenge] title {"text":"Get ready!","color":"dark_green"}
execute if score #intro uhclc matches 700 run title @a times 0 20 2
execute if score #intro uhclc matches 700 run title @a[tag=preinchallenge] title {"text":"10","color":"green"}
execute if score #intro uhclc matches 700 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 720 run title @a[tag=preinchallenge] title {"text":"9","color":"green"}
execute if score #intro uhclc matches 720 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 740 run title @a[tag=preinchallenge] title {"text":"8","color":"green"}
execute if score #intro uhclc matches 740 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 760 run title @a[tag=preinchallenge] title {"text":"7","color":"green"}
execute if score #intro uhclc matches 760 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 780 run title @a[tag=preinchallenge] title {"text":"6","color":"green"}
execute if score #intro uhclc matches 780 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 800 run title @a[tag=preinchallenge] title {"text":"5","color":"green"}
execute if score #intro uhclc matches 800 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 820 run title @a[tag=preinchallenge] title {"text":"4","color":"green"}
execute if score #intro uhclc matches 820 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 840 run title @a[tag=preinchallenge] title {"text":"3","color":"green"}
execute if score #intro uhclc matches 840 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 860 run title @a[tag=preinchallenge] title {"text":"2","color":"green"}
execute if score #intro uhclc matches 860 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 880 run title @a[tag=preinchallenge] title {"text":"1","color":"green"}
execute if score #intro uhclc matches 880 run playsound block.note_block.pling ambient @a[tag=preinchallenge] 15 227 0
execute if score #intro uhclc matches 900 run title @a[tag=preinchallenge] title {"text":"BEST OF LUCK!!","color":"green"}
execute if score #intro uhclc matches 920 run function uhclifechallenge:challenges/start
execute if score #intro uhclc matches 1270 run function uhclifechallenge:timer/show

execute if score #intro uhclc matches 1270 run scoreboard players reset #intro uhclc
