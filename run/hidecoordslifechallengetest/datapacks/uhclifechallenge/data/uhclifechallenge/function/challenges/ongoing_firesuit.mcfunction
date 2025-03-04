tag @a[tag=!success,tag=inchallenge,gamemode=!creative,nbt={Inventory:[{id:"minecraft:iron_helmet",Count:1b,tag:{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]}},{id:"minecraft:iron_chestplate",Count:1b,tag:{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]}},{id:"minecraft:iron_leggings",Count:1b,tag:{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]}},{id:"minecraft:iron_boots",Count:1b,tag:{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]}}]}] add firearmor
clear @a[tag=firearmor] iron_helmet{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]} 1
clear @a[tag=firearmor] iron_chestplate{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]} 1
clear @a[tag=firearmor] iron_leggings{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]} 1
clear @a[tag=firearmor] iron_boots{Enchantments:[{id:"minecraft:fire_protection",lvl:4}]} 1
execute as @a[tag=firearmor,tag=!success] run function uhclifechallenge:challenges/complete
tag @a[tag=firearmor] remove firearmor