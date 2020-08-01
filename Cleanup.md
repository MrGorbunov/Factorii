# Cleanup

## Problems

**Many enums with wack overlap** 3 different enums to represent 2 sets 
of data. Tile & Glyph enum are just 2 uses for the same enum-use
(world gen vs world rendering). ItemIndex is off to the side, as a 
weird inventory manager with order-restrictions.

**Entanglement** (Kind of the next bullet) but it's not obvious what role 
each class has.

**It's not clear what logic goes into Screen, World, and World 
Renderer.** Looking at Inventory, InventoryRenderer is used by
CraftScreen & WorldScreen, which both handle formatting & drawing,
although instinctively I would look for that in renderer.

**Both the CraftScreen & WorldScreen have simlar tab-through menus**
but the code is not recycled (unless ctrl + c & v count). Creating SubScreens 
would be good, and then Screens would just need to coordinate SubScreens & pull
in the render info (Sounds like renderer).

**Control scheme is definitely all over the place.**

GameState is not serializable (minor) & makes for long method calls
(ok pay attention). This is mostly from the wack inventory system.

