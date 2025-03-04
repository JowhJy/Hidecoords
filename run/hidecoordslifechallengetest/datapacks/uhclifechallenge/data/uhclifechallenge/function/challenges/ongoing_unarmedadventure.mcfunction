tag @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:wooden_sword"}]}] add weapon
tag @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:bow"}]}] add weapon
tag @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:stone_sword"}]}] add weapon
tag @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:iron_sword"}]}] add weapon
tag @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:golden_sword"}]}] add weapon
tag @a[tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:diamond_sword"}]}] add weapon
kill @a[tag=weapon]
tag @a[tag=weapon] remove weapon
tag @a[tag=!success,tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:potion",tag:{Potion:"minecraft:regeneration"}}]}] add regenpotion
clear @a[tag=regenpotion] potion{Potion:"minecraft:regeneration"}
execute as @a[tag=regenpotion,tag=!success] run function uhclifechallenge:challenges/complete
tag @a[tag=regenpotion] remove regenpotion