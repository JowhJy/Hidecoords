execute if entity @a[x=0,y=227,z=11,distance=..1,scores={uhc_lives=-1},tag=!lives] run function uhclifechallenge:lives/dead
execute if entity @a[x=0,y=227,z=11,distance=..1,scores={uhc_lives=0},tag=!lives] run function uhclifechallenge:lives/0
execute if entity @a[x=0,y=227,z=11,distance=..1,scores={uhc_lives=1},tag=!lives] run function uhclifechallenge:lives/1
execute if entity @a[x=0,y=227,z=11,distance=..1,scores={uhc_lives=2},tag=!lives] run function uhclifechallenge:lives/2
execute if entity @a[x=0,y=227,z=11,distance=..1,scores={uhc_lives=3},tag=!lives] run function uhclifechallenge:lives/3
execute unless entity @a[x=0,y=227,z=11,distance=..1,scores={uhc_lives=-1..3}] unless block 0 227 15 cave_air run function uhclifechallenge:lives/reset
tag @a remove lives
tag @a[x=0,y=227,z=11,distance=..1] add lives