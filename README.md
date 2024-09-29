Hide Coordinates
================
Server-side Fabric mod to prevent sending real coordinates to the client. Based on and partly copied from the Spigot version CoordinateOffset by [Joshua Prince](https://github.com/joshuaprince).
The gamerule `reducedDebugInfo` hides coordinates from the debug screen, but this can be easily circumvented using a client-side mod. This mod hides the coordinates for real by offsetting all coordinates sent to the client.


Features
---
* Players' coordinates are offset so that the location they entered the current world (join, teleport, portal or respawn) is shown to them as 0, 0.
* Admins can use /coordoffset get to find their own offset.



Disclaimer
---
I'm a new modder. I needed this for a server I'm working on and made it as a learning opportunity. Therefore the mod doesn't have all the features and the code is quite messy. But I still figured it might be useful to some others. Feel free to use the code as you please, and improvement suggestions are welcome!
The main feature missing here from CoordinateOffset are the vast configuration options. Offsets are always set so you spawn in chunk 0,0 and this resets on death and dimension change.
