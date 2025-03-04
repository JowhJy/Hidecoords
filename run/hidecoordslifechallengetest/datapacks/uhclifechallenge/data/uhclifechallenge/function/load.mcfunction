scoreboard objectives add uhc_lives dummy "UHC Lives"
scoreboard objectives add timer_ticks dummy
scoreboard objectives add timer_seconds dummy
scoreboard objectives add timer_minutes dummy
scoreboard objectives add timer_hours dummy
scoreboard objectives add timer_hours dummy
scoreboard objectives add dead deathCount
scoreboard objectives add uhclc dummy
scoreboard objectives add info trigger
team add color_green
team modify color_green color green
gamerule spectatorsGenerateChunks false
gamerule naturalRegeneration false
gamerule spawnChunkRadius 0
gamerule spawnRadius 0
#this legacy list gets overwritten by juhc_load if all goes well
function uhclifechallenge:lives/list
#changed my mind!
#function uhclifechallenge:hidecoords_load
function uhclifechallenge:juhc_load
difficulty hard

function uhclifechallenge:load_challenges