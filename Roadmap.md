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

- [x] Item Tubes
  * [x] Glass pulls from inventories
  * [x] Stone just moves things along
    * [x] Prefer moving items in the same direction they came from
  * [x] Steel tube for sorting
  * [x] All tiers of funnels functional w/ chests
  * [x] Rework movement logic
    * Should move into adjacent inventories if possible
    * Otherwise try for steel tubes
    * Then try to follow direction
    * Then look for other stone tubes

- [x] Landfill works too
  * [x] Replace float with landfill

- ~~[ ] Refactor GUI to be more GUI and less state~~
  - May be unnecessary when I get here

- [x] Place controls into globals
- ~~[ ] Place colors into globals~~

- [x] Allow for picking up of factory parts
  * Holding down modifier key 

- [ ] Add Submarine 
  * [x] Placeable
  * [ ] You win by entering it
  * [ ] Multi-character image


#### Cleanup
- [x] Switch factory tiles to class-based
  * Still hold tiles?
  * Factory class would hold the entire thing

- [x] Allow for faster item transfer w/ holding shift
- [x] Allow for better factory building by staying
      on the placement screen if there are more items
      to be placed

- [ ] ~~Inheritence / interface for subscreen~~
  * ~~Maybe also for handling input?~~

- [ ] ~~Inheritence for Factory classes~~

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
- [x] Rework FacData priority in World.getAdjacentFactoryData();






## v0.3 - Final Stretch

#### New features
- [ ] Create start screen
  * [x] Maybe remappable controls
  * [ ] Perhaps options

- [x] Actual crafting recipes
- [ ] Rework input buffer to work on a hashmap instead
  - Allows for multiple keys -> 1 action (ex: both arrow keys & wasd used for movement)

- [x] Spawning Island logic
  * [x] Figure out what requirements there are for the starter island
  * [x] Character placement

- [ ] Save state?
- [ ] Draw all resources
  * (Bars, Alloys, Glass)

- [ ] Create a guide
- [ ] Game win screen


#### Cleanup
- [x] Playtest 

- [ ] Credit screen
- [ ] Nicer readme
- [ ] Remove unused imports

- [ ] Better organized crafting
- [ ] Names for all UI panels
- [ ] Rebindable keys

- [ ] Standardize colors
  * Especially with UI

Bro benchmarking is wack, I made some changes that seem
like they'd reduce computation but benchmarking made the changes
seem neglible.

It is however true that most of the processing is done in the
factory.update() call, and that that's what would lead to the
greatest increase in max fps. Right now though, the max fps 
is still 10x higher than the cap (max fps ~= 300, and game cap is 30).

- [x] Optimize Rendering
  - [ ] ~~Maybe try to batch similar colored sprite together?~~
    * terminal.write("****") vs for (int i=0;i<8;i=++) termianl.write("*", i);
  - [x] Rework World's buffer system
  - [x] Merge tiles & glyphs
  - [x] Benchmark fps




## V0.4 - Balance
- [ ] Creating Mining Drills & Auto Mining drills seperate
- [ ] Auto Mining drills should auto-spew onto item tubes

- [ ] Only allow for auto-crafting
- [ ] Crafting should take time

- [ ] Focus on instances of spamming
- [ ] Make mining a tick-based activity

- [ ] Generally add feedback (sound? visual?)
  - [ ] When is something mined
  - [ ] What subscreen is active





## V0.5 - Stretch Goals
- [ ] Sounds 
- [ ] Rebindable keys
- [ ] Seeded Worlds

- [ ] Safely Resizable Window
- [ ] Full Screen Mode

- [ ] Startup Screen
- [ ] Loading Screen for terrain gen & world loading

- [ ] Music that plays based on system time