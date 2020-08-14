# RoadMap

## v0.1 - Getting started
**Done**
- [x] Building & Git
- [x] Plan for game (GGD)
- [x] Terrain gen w/ all natural resources
- [x] Some basic code architecture



## v0.2 - Factory Logic 

#### New features

- [x] Functional chests
  * [x] Storing inventories
  * [x] New subscreen
  * [x] Transfering / taking out

- [ ] Mining drills
  * [ ] Basic mining drill 
   * *Maybe it just picks up what's below it?*
  * [ ] Auto mining drill

- [ ] Copper workbench created

- [ ] Smelting works
  * Smelt class (vs CraftingRecipe)?
  * [ ] Kiln smelting
  * [ ] Forge smelting
- [ ] Smelters work with funnels

- [ ] Funnels likely will need to be reworked
  * [ ] Directionality is hard to draw (& implement)
  * [ ] Maybe instead create networks
  * [ ] Wooden has a limit, but of speed or something else?

> 
> Instead of funnels, it'll be tubing
> Moves items from 1 inventory to another and only one other
> 
> **Implementation:**
> Every group of funnels acts as an entire being, and it's not
> clear how they'd be keyed.
> 
> These guys might not be keyed (hashmap), and instead a
> recursive search can be used every time?
> 
> The alternative would require storing information in
> the actual tiles. This could actually be done pretty
> realistically for just the factory layer.
>

- [ ] All tiers of funnels functional w/ chests
- [ ] All tiers of floats work too

- Don't care about crafting rn


#### Cleanup
- [x] Switch factory tiles to class-based
  * Still hold tiles?
  * Factory class would hold the entire thing

- [ ] Inheritence / interface for subscreen
  * Maybe also for handling input?

- [x] Turn all screens into statemachines
  * Refactor input handling in the craft screen

- [x] Move tile methods into the tile enum
  * Static
  * Namely collisions

- [ ] Move player out of the World class
  * Will require a good amount of rewriting because
    the player cords are considered the center of the
    world and referenced a /lot/




## v0.3 - Don't Know Yet

- [ ] Switch to a constant fps game clock
  * Update functions for movement
  * Input Buffer class
    * Maybe use modifier keys (shift, ctrl, etc) for larger stack transfers (chest)

- [ ] Refactor GUI to be more GUI and less state
  - May be unnecessary when I get here

- [ ] Place controls into globals
- [ ] Place colors into globals


## v0.5 - Visuals

#### New features

- Inventory displays 1-time crafts / player improvements
- Drawing is now done onto 3x3 cells (maybe 2x2)
  * Try to bring an artist on board now
- Create start screen



## v0.6 - Dont Know Yet



## v0.7 - Balance

#### New features
- Spawning logic looks at multiple islands &
    the resources there
- Crafting recipes get a rework
