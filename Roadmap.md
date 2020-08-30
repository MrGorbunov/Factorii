# RoadMap

## v0.1 - Getting started
**Done**
- [x] Building & Git
- [x] Plan for game (GGD)
- [x] Terrain gen w/ all natural resources
- [x] Some basic code architecture







## v0.2 - Game Base

#### New features
- [x] Functional chests
  * [x] Storing inventories
  * [x] New subscreen
  * [x] Transfering / taking out

- [x] Mining drills
  * [x] Basic mining drill 
   * *Maybe it just picks up what's below it?*
  * [x] Auto mining drill

- [x] Copper workbench created

- [x] Smelting works
  * Smelt class (vs CraftingRecipe)?
  * [x] Kiln smelting
  * [x] Forge smelting
- [x] Smelters work with funnels
  * [x] Auto form of kiln
  * [x] Auto form of forge

- [ ] Item Tubes
  * [x] Glass pulls from inventories
  * [x] Stone just moves things along
    * [x] Prefer moving items in the same direction they came from
  * [ ] Steel tube for sorting
  * [ ] Bronze tubes for directionality
  * [ ] All tiers of funnels functional w/ chests

- [x] Landfill works too
  * [x] Replace float with landfill

- ~~[ ] Refactor GUI to be more GUI and less state~~
  - May be unnecessary when I get here

- [x] Place controls into globals
- ~~[ ] Place colors into globals~~

- [ ] Allow for picking up of factory parts

- [ ] Add Submarine 
  * [ ] Placeable
  * [ ] You win by entering it
  * [ ] Multi-character image


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

- [x] Instead of crafting location, have workbench FacData classes to provide the available crafts.

- [x] Go to update mode w/ constant fps
- [x] Rename project to be factorii instead of minirpg
- [x] Create consistency in file names

- [x] Make sure Auto-Crafting cannot craft equipment






## v0.3 - Final Stretch

#### New features
- [ ] Create start screen
  * [ ] Maybe remappable controls

- [ ] Actual crafting recipes

- [ ] Spawning Island logic
  * [ ] Figure out what requirements there are for the starter island
  * [ ] Character placement

- [ ] Save state?

- [ ] Create a guide or tutorial


#### Cleanup
- [ ] Playtest 
