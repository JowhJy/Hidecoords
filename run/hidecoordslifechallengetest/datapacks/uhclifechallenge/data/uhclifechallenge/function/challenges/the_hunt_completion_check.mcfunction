scoreboard players reset @s complete

execute store result score #add rottenflesh run clear @s minecraft:rotten_flesh
execute store result score #add bone run clear @s minecraft:bone
execute store result score #add string run clear @s minecraft:string
execute store result score #add enderpearl run clear @s minecraft:ender_pearl
execute store result score #add gunpowder run clear @s minecraft:gunpowder

scoreboard players operation @s rottenflesh += #add rottenflesh
scoreboard players operation @s bone += #add bone
scoreboard players operation @s string += #add string
scoreboard players operation @s enderpearl += #add enderpearl
scoreboard players operation @s gunpowder += #add gunpowder

execute unless score @s rottenflesh matches 128.. run tellraw @s {"color":"red","text":"Need more Rotten Flesh!"}
execute unless score @s gunpowder matches 64.. run tellraw @s {"color":"red","text":"Need more Gunpowder!"}
execute unless score @s bone matches 128.. run tellraw @s {"color":"red","text":"Need more Bones!"}
execute unless score @s string matches 128.. run tellraw @s {"color":"red","text":"Need more String!"}
execute unless score @s enderpearl matches 8.. run tellraw @s {"color":"red","text":"Need more Ender Pearls!"}

execute if score @s rottenflesh matches 128.. if score @s gunpowder matches 64.. if score @s bone matches 128.. if score @s string matches 128.. if score @s enderpearl matches 8.. run function uhclifechallenge:challenges/complete