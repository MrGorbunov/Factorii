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
  * [x] Basic mining drill 
   * *Maybe it just picks up what's below it?*
  * [ ] Auto mining drill

- [ ] Copper workbench created

- [x] Smelting works
  * Smelt class (vs CraftingRecipe)?
  * [x] Kiln smelting
  * [x] Forge smelting
- [ ] Smelters work with funnels

- [ ] Item Tubes
  * [x] Glass pulls from inventories
  * [x] Stone just moves things along
    * [ ] Prefer moving items in the same direction they came from
  * [ ] All tiers of funnels functional w/ chests

- [ ] All tiers of floats work too

#### Cleanup
- [x] Switch factory tiles to class-based
  * Still hold tiles?
  * Factory class would hold the entire thing

- [ ] Inheritence / interface for subscreen
  * ~~Maybe also for handling input?~~

- [x] Turn all screens into statemachines
  * Refactor input handling in the craft screen

- [x] Move tile methods into the tile enum
  * Static
  * Namely collisions

- [x] Move player out of the World class

- [x] Switch to a constant fps game clock
  * Update functions for movement
  * Input Buffer class
    * Maybe use modifier keys (shift, ctrl, etc) for larger stack transfers (chest)




## v0.3 - Don't know yet

- [ ] Refactor GUI to be more GUI and less state
  - May be unnecessary when I get here

- [x] Place controls into globals
- [ ] Place colors into globals
- [ ] Allow for picking up of factory parts

#### Cleanup

- [x] Go to update mode w/ constant fps
- [ ] Rename project to be factorii instead of minirpg
- [x] Create consistency in file names


## v0.5 - Visuals

#### New features
- Create start screen



## v0.6 - Dont Know Yet
- [ ] Add Submarine 
  - [ ] Placeable
  - [ ] You win by entering it
  - [ ] Multi-character image



## v0.7 - Balance

#### New features
- Spawning logic looks at multiple islands &
    the resources there
- Crafting recipes get a rework
