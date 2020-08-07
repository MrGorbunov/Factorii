# RoadMap

## v0.1 - Getting started
**Done**
- [x] Building & Git
- [x] Plan for game (GGD)
- [x] Terrain gen w/ all natural resources
- [x] Some basic code architecture

## v0.2 - Factory logic started
- [ ] Functional chests
  * [ ] Storing inventories
  * [ ] New subscreen
  * [ ] Transfering / taking out
- [ ] Funnels likely will need to be reworked
  * [ ] Directionality is hard to draw & implement
  * [ ] Maybe instead create networks
  * [ ] Wooden has a limit, but of speed or something else?
- [ ] All tiers of funnels functional w/ chests
- [ ] All tiers of floats work too

- Don't care about crafting rn


- [ ] Inheritence / interface for subscreen
  * Maybe also for handling input?
- [ ] Turn all screens into statemachines
- [ ] Move tile methods into the tile enum
  * Namely collisions


## v0.3 - Factory continued
- [ ] Mining drills
  * [ ] Basic mining drill 
   * *Maybe it just picks up what's below it?*
  * [ ] Auto mining drill
- [ ] Copper workbench created
- [ ] Smelting works
  * Smelt class
  * [ ] Kiln smelting
  * [ ] Forge smelting
- [ ] Smelters work with funnels

- [ ] Serializable gamestate

## v0.5 - Visuals
- Inventory displays 1-time crafts / player improvements
- Drawing is now done onto 3x3 cells (maybe 2x2)
  * Try to bring an artist on board now
- Create start screen

## v0.6 - Dont Know Yet

## v0.7 - Balance
- Spawning logic looks at multiple islands &
    the resources there
- Crafting recipes get a rework
