kill @a[scores={zombiekill=1..}]
kill @a[scores={skeletonkill=1..}]
kill @a[scores={creeperkill=1..}]
kill @a[scores={endermankill=1..}]
kill @a[scores={spiderkill=1..}]
kill @a[scores={blazekill=1..}]
kill @a[scores={ghastkill=1..}]
kill @a[scores={slimekill=1..}]
kill @a[scores={magmacubekill=1..}]
kill @a[scores={wskeletonkill=1..}]
kill @a[scores={silverfishkill=1..}]
kill @a[scores={cavespiderkill=1..}]
kill @a[scores={witchkill=1..}]
kill @a[scores={zombiepigmankill=1..}]
scoreboard players set @a[scores={zombiekill=1..}] zombiekill 0
scoreboard players set @a[scores={skeletonkill=1..}] skeletonkill 0
scoreboard players set @a[scores={creeperkill=1..}] creeperkill 0
scoreboard players set @a[scores={spiderkill=1..}] spiderkill 0
scoreboard players set @a[scores={endermankill=1..}] endermankill 0
scoreboard players set @a[scores={blazekill=1..}] blazekill 0
scoreboard players set @a[scores={ghastkill=1..}] ghastkill 0
scoreboard players set @a[scores={slimekill=1..}] slimekill 0
scoreboard players set @a[scores={magmacubekill=1..}] magmacubekill 0
scoreboard players set @a[scores={wskeletonkill=1..}] wskeletonkill 0
scoreboard players set @a[scores={silverfishkill=1..}] silverfishkill 0
scoreboard players set @a[scores={cavespiderkill=1..}] cavespiderkill 0
scoreboard players set @a[scores={witchkill=1..}] witchkill 0
scoreboard players set @a[scores={zombiepigmankill=1..}] zombiepigmankill 0
tag @a[tag=monsterkill] remove monsterkill
tag @a[tag=!success,tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:blaze_rod"}]}] add blazerod
clear @a[tag=blazerod] blaze_rod
execute as @a[tag=blazerod,tag=!success] run function uhclifechallenge:challenges/complete
tag @a[tag=blazerod] remove blazerod