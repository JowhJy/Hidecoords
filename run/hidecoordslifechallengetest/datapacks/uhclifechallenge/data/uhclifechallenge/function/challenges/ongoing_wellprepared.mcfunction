tag @a[tag=inchallenge,gamemode=!creative,nbt={Dimension:-1}] add nether
kill @a[nbt={Dimension:0},gamemode=!creative,tag=nether]
tag @a[tag=!inchallenge] remove nether
execute as @a[scores={blazekill=15..},tag=!success] run function uhclifechallenge:challenges/complete
