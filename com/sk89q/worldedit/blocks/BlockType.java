package com.sk89q.worldedit.blocks;

import com.google.common.base.Preconditions;
import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.PlayerDirection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public enum BlockType {
   AIR(0, "Air", "air"),
   STONE(1, "Stone", "stone", "rock"),
   GRASS(2, "Grass", "grass"),
   DIRT(3, "Dirt", "dirt"),
   COBBLESTONE(4, "Cobblestone", "cobblestone", "cobble"),
   WOOD(5, "Wood", "wood", "woodplank", "plank", "woodplanks", "planks"),
   SAPLING(6, "Sapling", "sapling", "seedling"),
   BEDROCK(7, "Bedrock", "adminium", "bedrock"),
   WATER(8, "Water", "watermoving", "movingwater", "flowingwater", "waterflowing"),
   STATIONARY_WATER(9, "Water (stationary)", "water", "waterstationary", "stationarywater", "stillwater"),
   LAVA(10, "Lava", "lavamoving", "movinglava", "flowinglava", "lavaflowing"),
   STATIONARY_LAVA(11, "Lava (stationary)", "lava", "lavastationary", "stationarylava", "stilllava"),
   SAND(12, "Sand", "sand"),
   GRAVEL(13, "Gravel", "gravel"),
   GOLD_ORE(14, "Gold ore", "goldore"),
   IRON_ORE(15, "Iron ore", "ironore"),
   COAL_ORE(16, "Coal ore", "coalore"),
   LOG(17, "Log", "log", "tree", "pine", "oak", "birch", "redwood"),
   LEAVES(18, "Leaves", "leaves", "leaf"),
   SPONGE(19, "Sponge", "sponge"),
   GLASS(20, "Glass", "glass"),
   LAPIS_LAZULI_ORE(21, "Lapis lazuli ore", "lapislazuliore", "blueore", "lapisore"),
   LAPIS_LAZULI(22, "Lapis lazuli", "lapislazuli", "lapislazuliblock", "bluerock"),
   DISPENSER(23, "Dispenser", "dispenser"),
   SANDSTONE(24, "Sandstone", "sandstone"),
   NOTE_BLOCK(25, "Note block", "musicblock", "noteblock", "note", "music", "instrument"),
   BED(26, "Bed", "bed"),
   POWERED_RAIL(27, "Powered Rail", "poweredrail", "boosterrail", "poweredtrack", "boostertrack", "booster"),
   DETECTOR_RAIL(28, "Detector Rail", "detectorrail", "detector"),
   PISTON_STICKY_BASE(29, "Sticky Piston", "stickypiston"),
   WEB(30, "Web", "web", "spiderweb"),
   LONG_GRASS(31, "Long grass", "longgrass", "tallgrass"),
   DEAD_BUSH(32, "Shrub", "deadbush", "shrub", "deadshrub", "tumbleweed"),
   PISTON_BASE(33, "Piston", "piston"),
   PISTON_EXTENSION(34, "Piston extension", "pistonextendsion", "pistonhead"),
   CLOTH(35, "Wool", "cloth", "wool"),
   PISTON_MOVING_PIECE(36, "Piston moving piece", "movingpiston"),
   YELLOW_FLOWER(37, "Yellow flower", "yellowflower", "flower"),
   RED_FLOWER(38, "Red rose", "redflower", "redrose", "rose"),
   BROWN_MUSHROOM(39, "Brown mushroom", "brownmushroom", "mushroom"),
   RED_MUSHROOM(40, "Red mushroom", "redmushroom"),
   GOLD_BLOCK(41, "Gold block", "gold", "goldblock"),
   IRON_BLOCK(42, "Iron block", "iron", "ironblock"),
   DOUBLE_STEP(43, "Double step", "doubleslab", "doublestoneslab", "doublestep"),
   STEP(44, "Step", "slab", "stoneslab", "step", "halfstep"),
   BRICK(45, "Brick", "brick", "brickblock"),
   TNT(46, "TNT", "tnt", "c4", "explosive"),
   BOOKCASE(47, "Bookcase", "bookshelf", "bookshelves", "bookcase", "bookcases"),
   MOSSY_COBBLESTONE(48, "Cobblestone (mossy)", "mossycobblestone", "mossstone", "mossystone", "mosscobble", "mossycobble", "moss", "mossy", "sossymobblecone"),
   OBSIDIAN(49, "Obsidian", "obsidian"),
   TORCH(50, "Torch", "torch", "light", "candle"),
   FIRE(51, "Fire", "fire", "flame", "flames"),
   MOB_SPAWNER(52, "Mob spawner", "mobspawner", "spawner"),
   WOODEN_STAIRS(53, "Wooden stairs", "woodstair", "woodstairs", "woodenstair", "woodenstairs"),
   CHEST(54, "Chest", "chest", "storage", "storagechest"),
   REDSTONE_WIRE(55, "Redstone wire", "redstone", "redstoneblock"),
   DIAMOND_ORE(56, "Diamond ore", "diamondore"),
   DIAMOND_BLOCK(57, "Diamond block", "diamond", "diamondblock"),
   WORKBENCH(58, "Workbench", "workbench", "table", "craftingtable", "crafting"),
   CROPS(59, "Crops", "crops", "crop", "plant", "plants"),
   SOIL(60, "Soil", "soil", "farmland"),
   FURNACE(61, "Furnace", "furnace"),
   BURNING_FURNACE(62, "Furnace (burning)", "burningfurnace", "litfurnace"),
   SIGN_POST(63, "Sign post", "sign", "signpost"),
   WOODEN_DOOR(64, "Wooden door", "wooddoor", "woodendoor", "door"),
   LADDER(65, "Ladder", "ladder"),
   MINECART_TRACKS(66, "Minecart tracks", "track", "tracks", "minecrattrack", "minecarttracks", "rails", "rail"),
   COBBLESTONE_STAIRS(67, "Cobblestone stairs", "cobblestonestair", "cobblestonestairs", "cobblestair", "cobblestairs"),
   WALL_SIGN(68, "Wall sign", "wallsign"),
   LEVER(69, "Lever", "lever", "switch", "stonelever", "stoneswitch"),
   STONE_PRESSURE_PLATE(70, "Stone pressure plate", "stonepressureplate", "stoneplate"),
   IRON_DOOR(71, "Iron Door", "irondoor"),
   WOODEN_PRESSURE_PLATE(72, "Wooden pressure plate", "woodpressureplate", "woodplate", "woodenpressureplate", "woodenplate", "plate", "pressureplate"),
   REDSTONE_ORE(73, "Redstone ore", "redstoneore"),
   GLOWING_REDSTONE_ORE(74, "Glowing redstone ore", "glowingredstoneore"),
   REDSTONE_TORCH_OFF(75, "Redstone torch (off)", "redstonetorchoff", "rstorchoff"),
   REDSTONE_TORCH_ON(76, "Redstone torch (on)", "redstonetorch", "redstonetorchon", "rstorchon", "redtorch"),
   STONE_BUTTON(77, "Stone Button", "stonebutton", "button"),
   SNOW(78, "Snow", "snow"),
   ICE(79, "Ice", "ice"),
   SNOW_BLOCK(80, "Snow block", "snowblock"),
   CACTUS(81, "Cactus", "cactus", "cacti"),
   CLAY(82, "Clay", "clay"),
   SUGAR_CANE(83, "Reed", "reed", "cane", "sugarcane", "sugarcanes", "vine", "vines"),
   JUKEBOX(84, "Jukebox", "jukebox", "stereo", "recordplayer"),
   FENCE(85, "Fence", "fence"),
   PUMPKIN(86, "Pumpkin", "pumpkin"),
   NETHERRACK(87, "Netherrack", "redmossycobblestone", "redcobblestone", "redmosstone", "redcobble", "netherstone", "netherrack", "nether", "hellstone"),
   SOUL_SAND(88, "Soul sand", "slowmud", "mud", "soulsand", "hellmud"),
   GLOWSTONE(89, "Glowstone", "brittlegold", "glowstone", "lightstone", "brimstone", "australium"),
   PORTAL(90, "Portal", "portal"),
   JACK_O_LANTERN(91, "Pumpkin (on)", "pumpkinlighted", "pumpkinon", "litpumpkin", "jackolantern"),
   CAKE(92, "Cake", "cake", "cakeblock"),
   REDSTONE_REPEATER_OFF(93, "Redstone repeater (off)", "diodeoff", "redstonerepeater", "repeateroff", "delayeroff"),
   REDSTONE_REPEATER_ON(94, "Redstone repeater (on)", "diodeon", "redstonerepeateron", "repeateron", "delayeron"),
   STAINED_GLASS(95, "Stained Glass", "stainedglass"),
   TRAP_DOOR(96, "Trap door", "trapdoor", "hatch", "floordoor"),
   SILVERFISH_BLOCK(97, "Silverfish block", "silverfish", "silver"),
   STONE_BRICK(98, "Stone brick", "stonebrick", "sbrick", "smoothstonebrick"),
   RED_MUSHROOM_CAP(100, "Red mushroom cap", "giantmushroomred", "redgiantmushroom", "redmushroomcap"),
   BROWN_MUSHROOM_CAP(99, "Brown mushroom cap", "giantmushroombrown", "browngiantmushoom", "brownmushroomcap"),
   IRON_BARS(101, "Iron bars", "ironbars", "ironfence"),
   GLASS_PANE(102, "Glass pane", "window", "glasspane", "glasswindow"),
   MELON_BLOCK(103, "Melon (block)", "melonblock"),
   PUMPKIN_STEM(104, "Pumpkin stem", "pumpkinstem"),
   MELON_STEM(105, "Melon stem", "melonstem"),
   VINE(106, "Vine", "vine", "vines", "creepers"),
   FENCE_GATE(107, "Fence gate", "fencegate", "gate"),
   BRICK_STAIRS(108, "Brick stairs", "brickstairs", "bricksteps"),
   STONE_BRICK_STAIRS(109, "Stone brick stairs", "stonebrickstairs", "smoothstonebrickstairs"),
   MYCELIUM(110, "Mycelium", "mycelium", "fungus", "mycel"),
   LILY_PAD(111, "Lily pad", "lilypad", "waterlily"),
   NETHER_BRICK(112, "Nether brick", "netherbrick"),
   NETHER_BRICK_FENCE(113, "Nether brick fence", "netherbrickfence", "netherfence"),
   NETHER_BRICK_STAIRS(114, "Nether brick stairs", "netherbrickstairs", "netherbricksteps", "netherstairs", "nethersteps"),
   NETHER_WART(115, "Nether wart", "netherwart", "netherstalk"),
   ENCHANTMENT_TABLE(116, "Enchantment table", "enchantmenttable", "enchanttable"),
   BREWING_STAND(117, "Brewing Stand", "brewingstand"),
   CAULDRON(118, "Cauldron"),
   END_PORTAL(119, "End Portal", "endportal", "blackstuff", "airportal", "weirdblackstuff"),
   END_PORTAL_FRAME(120, "End Portal Frame", "endportalframe", "airportalframe", "crystalblock"),
   END_STONE(121, "End Stone", "endstone", "enderstone", "endersand"),
   DRAGON_EGG(122, "Dragon Egg", "dragonegg", "dragons"),
   REDSTONE_LAMP_OFF(123, "Redstone lamp (off)", "redstonelamp", "redstonelampoff", "rslamp", "rslampoff", "rsglow", "rsglowoff"),
   REDSTONE_LAMP_ON(124, "Redstone lamp (on)", "redstonelampon", "rslampon", "rsglowon"),
   DOUBLE_WOODEN_STEP(125, "Double wood step", "doublewoodslab", "doublewoodstep"),
   WOODEN_STEP(126, "Wood step", "woodenslab", "woodslab", "woodstep", "woodhalfstep"),
   COCOA_PLANT(127, "Cocoa plant", "cocoplant", "cocoaplant"),
   SANDSTONE_STAIRS(128, "Sandstone stairs", "sandstairs", "sandstonestairs"),
   EMERALD_ORE(129, "Emerald ore", "emeraldore"),
   ENDER_CHEST(130, "Ender chest", "enderchest"),
   TRIPWIRE_HOOK(131, "Tripwire hook", "tripwirehook"),
   TRIPWIRE(132, "Tripwire", "tripwire", "string"),
   EMERALD_BLOCK(133, "Emerald block", "emeraldblock", "emerald"),
   SPRUCE_WOOD_STAIRS(134, "Spruce wood stairs", "sprucestairs", "sprucewoodstairs"),
   BIRCH_WOOD_STAIRS(135, "Birch wood stairs", "birchstairs", "birchwoodstairs"),
   JUNGLE_WOOD_STAIRS(136, "Jungle wood stairs", "junglestairs", "junglewoodstairs"),
   COMMAND_BLOCK(137, "Command block", "commandblock", "cmdblock", "command", "cmd"),
   BEACON(138, "Beacon", "beacon", "beaconblock"),
   COBBLESTONE_WALL(139, "Cobblestone wall", "cobblestonewall", "cobblewall"),
   FLOWER_POT(140, "Flower pot", "flowerpot", "plantpot", "pot"),
   CARROTS(141, "Carrots", "carrots", "carrotsplant", "carrotsblock"),
   POTATOES(142, "Potatoes", "potatoes", "potatoesblock"),
   WOODEN_BUTTON(143, "Wooden button", "woodbutton", "woodenbutton"),
   HEAD(144, "Head", "head", "skull"),
   ANVIL(145, "Anvil", "anvil", "blacksmith"),
   TRAPPED_CHEST(146, "Trapped Chest", "trappedchest", "redstonechest"),
   PRESSURE_PLATE_LIGHT(147, "Weighted Pressure Plate (Light)", "lightpressureplate"),
   PRESSURE_PLATE_HEAVY(148, "Weighted Pressure Plate (Heavy)", "heavypressureplate"),
   COMPARATOR_OFF(149, "Redstone Comparator (inactive)", "redstonecomparator", "comparator"),
   COMPARATOR_ON(150, "Redstone Comparator (active)", "redstonecomparatoron", "comparatoron"),
   DAYLIGHT_SENSOR(151, "Daylight Sensor", "daylightsensor", "lightsensor", "daylightdetector"),
   REDSTONE_BLOCK(152, "Block of Redstone", "redstoneblock", "blockofredstone"),
   QUARTZ_ORE(153, "Nether Quartz Ore", "quartzore", "netherquartzore"),
   HOPPER(154, "Hopper", "hopper"),
   QUARTZ_BLOCK(155, "Block of Quartz", "quartzblock", "quartz"),
   QUARTZ_STAIRS(156, "Quartz Stairs", "quartzstairs"),
   ACTIVATOR_RAIL(157, "Activator Rail", "activatorrail", "tntrail", "activatortrack"),
   DROPPER(158, "Dropper", "dropper"),
   STAINED_CLAY(159, "Stained Clay", "stainedclay", "stainedhardenedclay"),
   STAINED_GLASS_PANE(160, "Stained Glass Pane", "stainedglasspane"),
   LEAVES2(161, "Leaves", "leaves2", "acacialeaves", "darkoakleaves"),
   LOG2(162, "Log", "log2", "acacia", "darkoak"),
   ACACIA_STAIRS(163, "Acacia Wood Stairs", "acaciawoodstairs", "acaciastairs"),
   DARK_OAK_STAIRS(164, "Dark Oak Wood Stairs", "darkoakwoodstairs", "darkoakstairs"),
   SLIME(165, "SLime", "slimeblock"),
   BARRIER(166, "Barrier", "barrier", "wall", "worldborder", "edge"),
   IRON_TRAP_DOOR(167, "Iron Trap Door", "irontrapdoor"),
   PRISMARINE(168, "Prismarine", "prismarine"),
   SEA_LANTERN(169, "Sea Lantern", "sealantern"),
   HAY_BLOCK(170, "Hay Block", "hayblock", "haybale", "wheatbale"),
   CARPET(171, "Carpet", "carpet"),
   HARDENED_CLAY(172, "Hardened Clay", "hardenedclay", "hardclay"),
   COAL_BLOCK(173, "Block of Coal", "coalblock", "blockofcoal"),
   PACKED_ICE(174, "Packed Ice", "packedice", "hardice"),
   DOUBLE_PLANT(175, "Large Flowers", "largeflowers", "doubleflowers"),
   STANDING_BANNER(176, "Standing Banner", "standingbannear", "banner"),
   WALL_BANNER(177, "Wall Banner", "wallbanner"),
   DAYLIGHT_SENSOR_INVERTED(178, "Inverted Daylight Sensor", "inverteddaylight", "inverteddaylightsensor"),
   RED_SANDSTONE(179, "Red Sandstone", "redsandstone"),
   RED_SANDSTONE_STAIRS(180, "Red Sandstone Stairs", "redsandstonestairs"),
   DOUBLE_STEP2(181, "Double Step 2", "doublestep2", "doubleslab2", "doublestoneslab2", "doublestonestep2"),
   STEP2(182, "Step 2", "step2", "slab2", "stonestep2", "stoneslab2"),
   SPRUCE_FENCE_GATE(183, "Spruce Fence Gate", "spurcefencegate"),
   BIRCH_FENCE_GATE(184, "Birch Fence Gate", "birchfencegate"),
   JUNGLE_FENCE_GATE(185, "Jungle Fence Gate", "junglefencegate"),
   DARK_OAK_FENCE_GATE(186, "Dark Oak Fence Gate", "darkoakfencegate"),
   ACACIA_FENCE_GATE(187, "Acacia Fence Gate", "acaciafencegate"),
   SPRUCE_FENCE(188, "Spruce Fence", "sprucefence"),
   BIRCH_FENCE(189, "Birch Fence", "birchfence"),
   JUNGLE_FENCE(190, "Jungle Fence", "junglefence"),
   DARK_OAK_FENCE(191, "Dark Oak Fence", "darkoakfence"),
   ACACIA_FENCE(192, "Acacia Fence", "acaciafence"),
   SPRUCE_DOOR(193, "Spruce Door", "sprucedoor"),
   BIRCH_DOOR(194, "Birch Door", "birchdoor"),
   JUNGLE_DOOR(195, "Jungle Door", "jungledoor"),
   ACACIA_DOOR(196, "Acacia Door", "acaciadoor"),
   DARK_OAK_DOOR(197, "Dark Oak Door", "darkoakdoor"),
   END_ROD(198, "End Rod", "endrod", "endtorch"),
   CHORUS_PLANT(199, "Chorus Plant", "chorusplant", "chorusstem"),
   CHORUS_FLOWER(200, "Chorus Flower", "chorusflower"),
   PURPUR_BLOCK(201, "Purpur Block", "purpurblock", "blockpurpur"),
   PURPUR_PILLAR(202, "Purpur Pillar", "purpurpillar"),
   PURPUR_STAIRS(203, "Purpur Stairs", "purpurstairs"),
   PURPUR_DOUBLE_SLAB(204, "Purpur Double Slab", "purpurdoubleslab", "doubleslabpurpur", "doublepurpurslab"),
   PURPUR_SLAB(205, "Purpur Slab", "purpurslab", "slabpurpur"),
   END_BRICKS(206, "End Bricks", "endbricks"),
   BEETROOTS(207, "Beetroots", "beetroots", "beetroot_plant"),
   GRASS_PATH(208, "Grass Path", "grasspath", "dirtpath"),
   END_GATEWAY(209, "End Gateway", "endgateway"),
   REPEATING_COMMAND_BLOCK(210, "Repeating Command Block", "repeatingcommandblock", "commandblockrepeating"),
   CHAIN_COMMAND_BLOCK(211, "Chain Command Block", "chaincommandblock", "commandblockchain"),
   FROSTED_ICE(212, "Frosted Ice", "frostedice", "frostwalkerice"),
   MAGMA_BLOCK(213, "Magma Block", "magmablock", "magma"),
   NETHER_WART_BLOCK(214, "Nether Wart Block", "netherwartblock"),
   RED_NETHER_BRICK(215, "Red Nether Brick", "rednetherbrick", "netherbrickred"),
   BONE_BLOCK(216, "Bone Block", "boneblock", "blockbone", "fossil", "fossilblock", "blockfossil"),
   STRUCTURE_VOID(217, "Structure Void", "structurevoid", "structureair"),
   OBSERVER(218, "Observer", "observer", "blockupdatedetector"),
   SHULKER_BOX_WHITE(219, "White Shulker Box", "shulkerboxwhite"),
   SHULKER_BOX_ORANGE(220, "Orange Shulker Box", "shulkerboxorange"),
   SHULKER_BOX_MAGENTA(221, "Magenta Shulker Box", "shulkerboxmagenta"),
   SHULKER_BOX_LIGHT_BLUE(222, "Light Blue Shulker Box", "shulkerboxlightblue"),
   SHULKER_BOX_YELLOW(223, "Yellow Shulker Box", "shulkerboxyellow"),
   SHULKER_BOX_LIME(224, "Lime Shulker Box", "shulkerboxlime"),
   SHULKER_BOX_PINK(225, "Pink Shulker Box", "shulkerboxpink"),
   SHULKER_BOX_GRAY(226, "Gray Shulker Box", "shulkerboxgray"),
   SHULKER_BOX_LIGHT_GRAY(227, "Light Gray Shulker Box", "shulkerboxlightgray"),
   SHULKER_BOX_CYAN(228, "Cyan Shulker Box", "shulkerboxcyan"),
   SHULKER_BOX_PURPLE(229, "Purple Shulker Box", "shulkerboxpurple"),
   SHULKER_BOX_BLUE(230, "Blue Shulker Box", "shulkerboxblue"),
   SHULKER_BOX_BROWN(231, "Brown Shulker Box", "shulkerboxbrown"),
   SHULKER_BOX_GREEN(232, "Green Shulker Box", "shulkerboxgreen"),
   SHULKER_BOX_RED(233, "Red Shulker Box", "shulkerboxred"),
   SHULKER_BOX_BLACK(234, "Black Shulker Box", "shulkerboxblack"),
   TERRACOTTA_WHITE(235, "White Terracotta", "terracottawhite"),
   TERRACOTTA_ORANGE(236, "Orange Terracotta", "terracottaorange"),
   TERRACOTTA_MAGENTA(237, "Magenta Terracotta", "terracottamagenta"),
   TERRACOTTA_LIGHT_BLUE(238, "Light Blue Terracotta", "terracottalightblue"),
   TERRACOTTA_YELLOW(239, "Yellow Terracotta", "terracottayellow"),
   TERRACOTTA_LIME(240, "Lime Terracotta", "terracottalime"),
   TERRACOTTA_PINK(241, "Pink Terracotta", "terracottapink"),
   TERRACOTTA_GRAY(242, "Gray Terracotta", "terracottagray"),
   TERRACOTTA_LIGHT_GRAY(243, "Light Gray Terracotta", "terracottalightgray"),
   TERRACOTTA_CYAN(244, "Cyan Terracotta", "terracottacyan"),
   TERRACOTTA_PURPLE(245, "Purple Terracotta", "terracottapurple"),
   TERRACOTTA_BLUE(246, "Blue Terracotta", "terracottablue"),
   TERRACOTTA_BROWN(247, "Brown Terracotta", "terracottabrown"),
   TERRACOTTA_GREEN(248, "Green Terracotta", "terracottagreen"),
   TERRACOTTA_RED(249, "Red Terracotta", "terracottared"),
   TERRACOTTA_BLACK(250, "Black Terracotta", "terracottablack"),
   CONCRETE(251, "Concrete", "concrete"),
   CONCRETE_POWDER(252, "Concrete Powder", "concretepowder"),
   STRUCTURE_BLOCK(255, "Structure Block", "structureblock");

   private static final Map<Integer, BlockType> ids = new HashMap<>();
   private static final Map<String, BlockType> lookup = new HashMap<>();
   private final int id;
   private final String name;
   private final String[] lookupKeys;
   private static final Map<Integer, BaseBlock> itemBlockMapping;
   private static final Map<Integer, BaseBlock> dataItemBlockMapping;
   private static final Set<Integer> shouldPlaceLast;
   private static final Set<Integer> shouldPlaceFinal;
   private static final Set<Integer> canPassThrough;
   private static final Map<Integer, Double> centralTopLimit;
   private static final Set<Integer> usesData;
   private static final Set<Integer> isContainerBlock;
   private static final Set<Integer> isRedstoneBlock;
   private static final Set<Integer> canTransferRedstone;
   private static final Set<Integer> isRedstoneSource;
   private static final Set<Integer> isRailBlock;
   private static final Set<Integer> isNaturalTerrainBlock;
   private static final Set<Integer> emitsLight;
   private static final Set<Integer> isTranslucent;
   private static final Map<Integer, BaseItem> dataBlockBagItems;
   private static final Map<Integer, BaseItem> nonDataBlockBagItems;
   private static final BaseItem doNotDestroy;
   private static final Random random;
   private static final Map<Integer, PlayerDirection> dataAttachments;
   private static final Map<Integer, PlayerDirection> nonDataAttachments;

   private BlockType(int id, String name, String lookupKey) {
      this.id = id;
      this.name = name;
      this.lookupKeys = new String[]{lookupKey};
   }

   private BlockType(int id, String name, String... lookupKeys) {
      this.id = id;
      this.name = name;
      this.lookupKeys = lookupKeys;
   }

   @Nullable
   public static BlockType fromID(int id) {
      return ids.get(id);
   }

   @Nullable
   public static BlockType lookup(String name) {
      return lookup(name, true);
   }

   @Nullable
   public static BlockType lookup(String name, boolean fuzzy) {
      try {
         return fromID(Integer.parseInt(name));
      } catch (NumberFormatException var3) {
         return StringUtil.lookup(lookup, name, fuzzy);
      }
   }

   @Nullable
   public static BaseBlock getBlockForItem(int typeId, int data) {
      BaseBlock block = itemBlockMapping.get(typeId);
      return block != null ? block : dataItemBlockMapping.get(typeDataKey(typeId, data));
   }

   public int getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public static boolean shouldPlaceLast(int id) {
      return shouldPlaceLast.contains(id);
   }

   public boolean shouldPlaceLast() {
      return shouldPlaceLast.contains(this.id);
   }

   public static boolean shouldPlaceFinal(int id) {
      return shouldPlaceFinal.contains(id);
   }

   public static boolean canPassThrough(int id) {
      return canPassThrough.contains(id);
   }

   public static boolean canPassThrough(int id, int data) {
      return canPassThrough.contains(-16 * id - data) || canPassThrough.contains(id);
   }

   public static boolean canPassThrough(BaseBlock block) {
      Preconditions.checkNotNull(block);
      return canPassThrough(block.getId(), block.getData());
   }

   public boolean canPassThrough() {
      return canPassThrough.contains(this.id);
   }

   public static double centralTopLimit(int id, int data) {
      if (centralTopLimit.containsKey(-16 * id - data)) {
         return centralTopLimit.get(-16 * id - data);
      } else if (centralTopLimit.containsKey(id)) {
         return centralTopLimit.get(id);
      } else {
         return canPassThrough(id) ? 0.0 : 1.0;
      }
   }

   public static double centralTopLimit(BaseBlock block) {
      Preconditions.checkNotNull(block);
      return centralTopLimit(block.getId(), block.getData());
   }

   public double centralTopLimit() {
      if (centralTopLimit.containsKey(this.id)) {
         return centralTopLimit.get(this.id);
      } else {
         return canPassThrough(this.id) ? 0.0 : 1.0;
      }
   }

   public static boolean usesData(int id) {
      return usesData.contains(id);
   }

   public boolean usesData() {
      return usesData.contains(this.id);
   }

   public static boolean isContainerBlock(int id) {
      return isContainerBlock.contains(id);
   }

   public boolean isContainerBlock() {
      return isContainerBlock.contains(this.id);
   }

   public static boolean isRedstoneBlock(int id) {
      return isRedstoneBlock.contains(id);
   }

   public boolean isRedstoneBlock() {
      return isRedstoneBlock.contains(this.id);
   }

   public static boolean canTransferRedstone(int id) {
      return canTransferRedstone.contains(id);
   }

   public boolean canTransferRedstone() {
      return canTransferRedstone.contains(this.id);
   }

   public static boolean isRedstoneSource(int id) {
      return isRedstoneSource.contains(id);
   }

   public boolean isRedstoneSource() {
      return isRedstoneSource.contains(this.id);
   }

   public static boolean isRailBlock(int id) {
      return isRailBlock.contains(id);
   }

   public boolean isRailBlock() {
      return isRailBlock.contains(this.id);
   }

   @Deprecated
   public static boolean isNaturalTerrainBlock(int id) {
      return isNaturalTerrainBlock.contains(id);
   }

   public static boolean isNaturalTerrainBlock(int id, int data) {
      return isNaturalTerrainBlock.contains(-16 * id - data) || isNaturalTerrainBlock.contains(id);
   }

   public static boolean isNaturalTerrainBlock(BaseBlock block) {
      return isNaturalTerrainBlock(block.getId(), block.getData());
   }

   public boolean isNaturalTerrainBlock() {
      return isNaturalTerrainBlock.contains(this.id);
   }

   public static boolean emitsLight(int id) {
      return emitsLight.contains(id);
   }

   public static boolean isTranslucent(int id) {
      return isTranslucent.contains(id);
   }

   @Nullable
   public static BaseItem getBlockBagItem(int type, int data) {
      BaseItem dropped = nonDataBlockBagItems.get(type);
      if (dropped != null) {
         return dropped;
      } else {
         dropped = dataBlockBagItems.get(typeDataKey(type, data));
         if (dropped == null) {
            return new BaseItemStack(0, 0);
         } else {
            return dropped == doNotDestroy ? null : dropped;
         }
      }
   }

   private static void addIdentity(int type) {
      nonDataBlockBagItems.put(type, new BaseItem(type));
   }

   private static void addIdentities(int type, int maxData) {
      for (int data = 0; data < maxData; data++) {
         dataBlockBagItems.put(typeDataKey(type, data), new BaseItem(type, (short)data));
      }
   }

   @Deprecated
   public static int getDroppedBlock(int id) {
      BaseItem dropped = nonDataBlockBagItems.get(id);
      return dropped == null ? 0 : dropped.getType();
   }

   public BaseItemStack getBlockDrop(short data) {
      return getBlockDrop(this.id, data);
   }

   @Nullable
   public static BaseItemStack getBlockDrop(int id, short data) {
      switch (id) {
         case 0:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 20:
         case 34:
         case 47:
         case 51:
         case 52:
         case 78:
         case 79:
         case 90:
         case 97:
         case 106:
         case 119:
         case 120:
         case 144:
         case 160:
            return null;
         case 1:
            return new BaseItemStack(4);
         case 2:
            return new BaseItemStack(3);
         case 3:
         case 4:
         case 5:
         case 6:
         case 12:
         case 14:
         case 15:
         case 19:
         case 22:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 29:
         case 30:
         case 32:
         case 33:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 54:
         case 57:
         case 58:
         case 61:
         case 65:
         case 66:
         case 69:
         case 70:
         case 72:
         case 76:
         case 77:
         case 80:
         case 81:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 91:
         case 92:
         case 95:
         case 96:
         case 98:
         case 101:
         case 102:
         case 107:
         case 112:
         case 113:
         case 116:
         case 121:
         case 122:
         case 123:
         case 126:
         case 128:
         case 130:
         case 131:
         case 133:
         case 137:
         case 138:
         case 139:
         case 143:
         case 145:
         case 146:
         case 147:
         case 148:
         case 151:
         case 152:
         case 154:
         case 157:
         case 158:
         case 159:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 169:
         default:
            if (usesData(id)) {
               return new BaseItemStack(id, 1, data);
            }

            return new BaseItemStack(id);
         case 13:
            if (random.nextInt(10) == 0) {
               return new BaseItemStack(318);
            }

            return new BaseItemStack(13);
         case 16:
            return new BaseItemStack(263);
         case 17:
            return new BaseItemStack(17, 1, (short)(data & 3));
         case 18:
            if (random.nextDouble() > 0.95) {
               return new BaseItemStack(6, 1, data);
            }

            return null;
         case 21:
            return new BaseItemStack(351, random.nextInt(5) + 4, (short)4);
         case 26:
            return new BaseItemStack(355);
         case 31:
            if (random.nextInt(8) == 0) {
               return new BaseItemStack(295);
            }

            return null;
         case 43:
            return new BaseItemStack(44, 2, data);
         case 53:
         case 67:
         case 108:
         case 109:
         case 114:
         case 134:
         case 135:
         case 136:
         case 156:
            return new BaseItemStack(id);
         case 55:
            return new BaseItemStack(331);
         case 56:
            return new BaseItemStack(264);
         case 59:
            if (data == 7) {
               return new BaseItemStack(296);
            }

            return new BaseItemStack(295);
         case 60:
            return new BaseItemStack(3);
         case 62:
            return new BaseItemStack(61);
         case 63:
            return new BaseItemStack(323);
         case 64:
            return new BaseItemStack(324);
         case 68:
            return new BaseItemStack(323);
         case 71:
            return new BaseItemStack(330);
         case 73:
         case 74:
            return new BaseItemStack(331, random.nextInt(2) + 4);
         case 75:
            return new BaseItemStack(76);
         case 82:
            return new BaseItemStack(337, 4);
         case 83:
            return new BaseItemStack(338);
         case 89:
            return new BaseItemStack(348, random.nextInt(3) + 2);
         case 93:
         case 94:
            return new BaseItemStack(356);
         case 99:
            int storex = random.nextInt(10);
            if (storex == 0) {
               return new BaseItemStack(39, 2);
            } else {
               if (storex == 1) {
                  return new BaseItemStack(39);
               }

               return null;
            }
         case 100:
            int store = random.nextInt(10);
            if (store == 0) {
               return new BaseItemStack(40, 2);
            } else {
               if (store == 1) {
                  return new BaseItemStack(40);
               }

               return null;
            }
         case 103:
            return new BaseItemStack(360, random.nextInt(5) + 3);
         case 104:
            return new BaseItemStack(361);
         case 105:
            return new BaseItemStack(362);
         case 110:
            return new BaseItemStack(3);
         case 111:
            return new BaseItemStack(111);
         case 115:
            return new BaseItemStack(372, random.nextInt(3) + 1);
         case 117:
            return new BaseItemStack(379);
         case 118:
            return new BaseItemStack(380);
         case 124:
            return new BaseItemStack(123);
         case 125:
            return new BaseItemStack(126, 2, data);
         case 127:
            return new BaseItemStack(351, data >= 2 ? 3 : 1, (short)3);
         case 129:
            return new BaseItemStack(388);
         case 132:
            return new BaseItemStack(287);
         case 140:
            return new BaseItemStack(390);
         case 141:
            return new BaseItemStack(391, random.nextInt(3) + 1);
         case 142:
            return new BaseItemStack(392, random.nextInt(3) + 1);
         case 149:
         case 150:
            return new BaseItemStack(404);
         case 153:
            return new BaseItemStack(406);
         case 155:
            return new BaseItemStack(155, 1, data >= 2 ? 2 : data);
         case 170:
            return new BaseItemStack(170);
      }
   }

   public static PlayerDirection getAttachment(int type, int data) {
      PlayerDirection direction = nonDataAttachments.get(type);
      return direction != null ? direction : dataAttachments.get(typeDataKey(type, data));
   }

   private static int typeDataKey(int type, int data) {
      return type << 4 | data & 15;
   }

   private static void addCardinals(int type, int west, int north, int east, int south) {
      dataAttachments.put(typeDataKey(type, west), PlayerDirection.WEST);
      dataAttachments.put(typeDataKey(type, north), PlayerDirection.NORTH);
      dataAttachments.put(typeDataKey(type, east), PlayerDirection.EAST);
      dataAttachments.put(typeDataKey(type, south), PlayerDirection.SOUTH);
   }

   static {
      for (BlockType type : EnumSet.allOf(BlockType.class)) {
         ids.put(type.id, type);

         for (String key : type.lookupKeys) {
            lookup.put(key, type);
         }
      }

      itemBlockMapping = new HashMap<>();
      dataItemBlockMapping = new HashMap<>();

      for (int data = 0; data < 16; data++) {
         dataItemBlockMapping.put(typeDataKey(3, data), new BaseBlock(3, data));
         dataItemBlockMapping.put(typeDataKey(5, data), new BaseBlock(5, data));
         dataItemBlockMapping.put(typeDataKey(12, data), new BaseBlock(12, data));
         dataItemBlockMapping.put(typeDataKey(24, data), new BaseBlock(24, data));
         dataItemBlockMapping.put(typeDataKey(31, data), new BaseBlock(31, data));
         dataItemBlockMapping.put(typeDataKey(35, data), new BaseBlock(35, data));
         dataItemBlockMapping.put(typeDataKey(97, data), new BaseBlock(97, data));
         dataItemBlockMapping.put(typeDataKey(98, data), new BaseBlock(98, data));
         dataItemBlockMapping.put(typeDataKey(139, data), new BaseBlock(139, data));
         dataItemBlockMapping.put(typeDataKey(159, data), new BaseBlock(159, data));
         dataItemBlockMapping.put(typeDataKey(171, data), new BaseBlock(171, data));
         dataItemBlockMapping.put(typeDataKey(38, data), new BaseBlock(38, data));
         dataItemBlockMapping.put(typeDataKey(175, data), new BaseBlock(175, data));
         dataItemBlockMapping.put(typeDataKey(95, data), new BaseBlock(95, data));
      }

      itemBlockMapping.put(259, new BaseBlock(51, -1));
      itemBlockMapping.put(287, new BaseBlock(132, -1));
      itemBlockMapping.put(295, new BaseBlock(59, -1));
      itemBlockMapping.put(323, new BaseBlock(63, -1));
      itemBlockMapping.put(324, new BaseBlock(64, -1));
      itemBlockMapping.put(326, new BaseBlock(9, -1));
      itemBlockMapping.put(327, new BaseBlock(11, -1));
      itemBlockMapping.put(330, new BaseBlock(71, -1));
      itemBlockMapping.put(331, new BaseBlock(55, -1));
      itemBlockMapping.put(338, new BaseBlock(83, -1));
      itemBlockMapping.put(355, new BaseBlock(26, -1));
      itemBlockMapping.put(356, new BaseBlock(93, -1));
      itemBlockMapping.put(361, new BaseBlock(104, -1));
      itemBlockMapping.put(362, new BaseBlock(105, -1));
      itemBlockMapping.put(372, new BaseBlock(115, -1));
      itemBlockMapping.put(379, new BaseBlock(117, -1));
      itemBlockMapping.put(380, new BaseBlock(118, -1));
      itemBlockMapping.put(390, new BaseBlock(140, -1));
      itemBlockMapping.put(391, new BaseBlock(141, -1));
      itemBlockMapping.put(392, new BaseBlock(142, -1));
      itemBlockMapping.put(404, new BaseBlock(149, -1));
      itemBlockMapping.put(425, new BaseBlock(176, -1));
      itemBlockMapping.put(427, new BaseBlock(193, -1));
      itemBlockMapping.put(428, new BaseBlock(194, -1));
      itemBlockMapping.put(429, new BaseBlock(195, -1));
      itemBlockMapping.put(430, new BaseBlock(196, -1));
      itemBlockMapping.put(431, new BaseBlock(197, -1));
      shouldPlaceLast = new HashSet<>();
      shouldPlaceLast.add(6);
      shouldPlaceLast.add(26);
      shouldPlaceLast.add(27);
      shouldPlaceLast.add(28);
      shouldPlaceLast.add(31);
      shouldPlaceLast.add(32);
      shouldPlaceLast.add(34);
      shouldPlaceLast.add(37);
      shouldPlaceLast.add(38);
      shouldPlaceLast.add(39);
      shouldPlaceLast.add(40);
      shouldPlaceLast.add(50);
      shouldPlaceLast.add(51);
      shouldPlaceLast.add(55);
      shouldPlaceLast.add(59);
      shouldPlaceLast.add(65);
      shouldPlaceLast.add(66);
      shouldPlaceLast.add(69);
      shouldPlaceLast.add(70);
      shouldPlaceLast.add(72);
      shouldPlaceLast.add(75);
      shouldPlaceLast.add(76);
      shouldPlaceLast.add(77);
      shouldPlaceLast.add(78);
      shouldPlaceLast.add(90);
      shouldPlaceLast.add(93);
      shouldPlaceLast.add(94);
      shouldPlaceLast.add(96);
      shouldPlaceLast.add(106);
      shouldPlaceLast.add(111);
      shouldPlaceLast.add(115);
      shouldPlaceLast.add(33);
      shouldPlaceLast.add(29);
      shouldPlaceLast.add(34);
      shouldPlaceLast.add(36);
      shouldPlaceLast.add(127);
      shouldPlaceLast.add(131);
      shouldPlaceLast.add(132);
      shouldPlaceLast.add(140);
      shouldPlaceLast.add(141);
      shouldPlaceLast.add(142);
      shouldPlaceLast.add(143);
      shouldPlaceLast.add(145);
      shouldPlaceLast.add(147);
      shouldPlaceLast.add(148);
      shouldPlaceLast.add(149);
      shouldPlaceLast.add(150);
      shouldPlaceLast.add(157);
      shouldPlaceLast.add(167);
      shouldPlaceLast.add(171);
      shouldPlaceLast.add(175);
      shouldPlaceLast.add(178);
      shouldPlaceFinal = new HashSet<>();
      shouldPlaceFinal.add(63);
      shouldPlaceFinal.add(64);
      shouldPlaceFinal.add(196);
      shouldPlaceFinal.add(194);
      shouldPlaceFinal.add(195);
      shouldPlaceFinal.add(197);
      shouldPlaceFinal.add(193);
      shouldPlaceFinal.add(68);
      shouldPlaceFinal.add(71);
      shouldPlaceFinal.add(81);
      shouldPlaceFinal.add(83);
      shouldPlaceFinal.add(92);
      shouldPlaceFinal.add(34);
      shouldPlaceFinal.add(36);
      shouldPlaceFinal.add(176);
      shouldPlaceFinal.add(177);
      canPassThrough = new HashSet<>();
      canPassThrough.add(0);
      canPassThrough.add(8);
      canPassThrough.add(9);
      canPassThrough.add(6);
      canPassThrough.add(27);
      canPassThrough.add(28);
      canPassThrough.add(30);
      canPassThrough.add(31);
      canPassThrough.add(32);
      canPassThrough.add(37);
      canPassThrough.add(38);
      canPassThrough.add(39);
      canPassThrough.add(40);
      canPassThrough.add(50);
      canPassThrough.add(51);
      canPassThrough.add(55);
      canPassThrough.add(59);
      canPassThrough.add(63);
      canPassThrough.add(65);
      canPassThrough.add(66);
      canPassThrough.add(68);
      canPassThrough.add(69);
      canPassThrough.add(70);
      canPassThrough.add(72);
      canPassThrough.add(75);
      canPassThrough.add(76);
      canPassThrough.add(77);
      canPassThrough.add(-1248);
      canPassThrough.add(-1256);
      canPassThrough.add(83);
      canPassThrough.add(90);
      canPassThrough.add(93);
      canPassThrough.add(94);
      canPassThrough.add(104);
      canPassThrough.add(105);
      canPassThrough.add(106);
      canPassThrough.add(115);
      canPassThrough.add(119);
      canPassThrough.add(131);
      canPassThrough.add(132);
      canPassThrough.add(141);
      canPassThrough.add(142);
      canPassThrough.add(143);
      canPassThrough.add(147);
      canPassThrough.add(148);
      canPassThrough.add(149);
      canPassThrough.add(150);
      canPassThrough.add(157);
      canPassThrough.add(167);
      canPassThrough.add(171);
      canPassThrough.add(175);
      canPassThrough.add(176);
      canPassThrough.add(177);
      centralTopLimit = new HashMap<>();
      centralTopLimit.put(26, 0.5625);
      centralTopLimit.put(117, 0.875);
      centralTopLimit.put(92, 0.4375);

      for (int data = 6; data < 16; data++) {
         centralTopLimit.put(-1472 - data, 0.0);
      }

      centralTopLimit.put(118, 0.3125);
      centralTopLimit.put(127, 0.75);
      centralTopLimit.put(116, 0.75);

      for (int data = 0; data < 16; data++) {
         if ((data & 4) != 0) {
            centralTopLimit.put(-1920 - data, 1.0);
         } else {
            centralTopLimit.put(-1920 - data, 0.8125);
         }

         centralTopLimit.put(-2304 - data, 0.75);
      }

      centralTopLimit.put(-2305, 0.5);
      centralTopLimit.put(-2313, 0.5);
      centralTopLimit.put(85, 1.5);

      for (int data = 0; data < 8; data++) {
         centralTopLimit.put(-704 - data, 0.5);
         centralTopLimit.put(-2016 - data, 0.5);
         centralTopLimit.put(-2912 - data, 0.5);
         centralTopLimit.put(-1248 - data, 0.125 * data);
         centralTopLimit.put(-1248 - (data + 8), 0.125 * data);
      }

      centralTopLimit.put(111, 0.015625);
      centralTopLimit.put(94, 0.125);
      centralTopLimit.put(93, 0.125);

      for (int data = 0; data < 4; data++) {
         centralTopLimit.put(-1536 - (data + 0), 0.1875);
         centralTopLimit.put(-1536 - (data + 4), 0.0);
         centralTopLimit.put(-1536 - (data + 8), 1.0);
         centralTopLimit.put(-1536 - (data + 12), 0.0);
         centralTopLimit.put(-1712 - (data + 0), 1.5);
         centralTopLimit.put(-1712 - (data + 4), 0.0);
         centralTopLimit.put(-1712 - (data + 8), 1.5);
         centralTopLimit.put(-1712 - (data + 12), 0.0);
      }

      centralTopLimit.put(88, 0.875);
      centralTopLimit.put(139, 1.5);
      centralTopLimit.put(140, 0.375);
      centralTopLimit.put(149, 0.125);
      centralTopLimit.put(150, 0.125);
      centralTopLimit.put(151, 0.375);
      centralTopLimit.put(154, 0.625);
      centralTopLimit.put(144, 0.75);
      centralTopLimit.put(96, 1.0);
      centralTopLimit.put(107, 1.5);
      usesData = new HashSet<>();
      usesData.add(1);
      usesData.add(3);
      usesData.add(5);
      usesData.add(6);
      usesData.add(8);
      usesData.add(9);
      usesData.add(10);
      usesData.add(11);
      usesData.add(12);
      usesData.add(17);
      usesData.add(162);
      usesData.add(18);
      usesData.add(161);
      usesData.add(19);
      usesData.add(23);
      usesData.add(24);
      usesData.add(26);
      usesData.add(27);
      usesData.add(28);
      usesData.add(29);
      usesData.add(31);
      usesData.add(33);
      usesData.add(34);
      usesData.add(35);
      usesData.add(38);
      usesData.add(43);
      usesData.add(44);
      usesData.add(50);
      usesData.add(51);
      usesData.add(53);
      usesData.add(54);
      usesData.add(55);
      usesData.add(59);
      usesData.add(60);
      usesData.add(61);
      usesData.add(62);
      usesData.add(63);
      usesData.add(64);
      usesData.add(65);
      usesData.add(66);
      usesData.add(67);
      usesData.add(68);
      usesData.add(69);
      usesData.add(70);
      usesData.add(71);
      usesData.add(72);
      usesData.add(75);
      usesData.add(76);
      usesData.add(77);
      usesData.add(78);
      usesData.add(81);
      usesData.add(83);
      usesData.add(84);
      usesData.add(86);
      usesData.add(91);
      usesData.add(92);
      usesData.add(93);
      usesData.add(94);
      usesData.add(96);
      usesData.add(97);
      usesData.add(98);
      usesData.add(100);
      usesData.add(99);
      usesData.add(104);
      usesData.add(105);
      usesData.add(106);
      usesData.add(107);
      usesData.add(108);
      usesData.add(109);
      usesData.add(114);
      usesData.add(115);
      usesData.add(117);
      usesData.add(118);
      usesData.add(120);
      usesData.add(125);
      usesData.add(126);
      usesData.add(127);
      usesData.add(128);
      usesData.add(130);
      usesData.add(131);
      usesData.add(132);
      usesData.add(134);
      usesData.add(135);
      usesData.add(136);
      usesData.add(139);
      usesData.add(140);
      usesData.add(141);
      usesData.add(142);
      usesData.add(143);
      usesData.add(144);
      usesData.add(145);
      usesData.add(147);
      usesData.add(148);
      usesData.add(149);
      usesData.add(150);
      usesData.add(155);
      usesData.add(156);
      usesData.add(157);
      usesData.add(158);
      usesData.add(154);
      usesData.add(159);
      usesData.add(95);
      usesData.add(160);
      usesData.add(167);
      usesData.add(168);
      usesData.add(170);
      usesData.add(171);
      usesData.add(175);
      usesData.add(176);
      usesData.add(177);
      usesData.add(179);
      usesData.add(180);
      usesData.add(181);
      usesData.add(182);
      usesData.add(193);
      usesData.add(194);
      usesData.add(195);
      usesData.add(196);
      usesData.add(197);
      isContainerBlock = new HashSet<>();
      isContainerBlock.add(23);
      isContainerBlock.add(61);
      isContainerBlock.add(62);
      isContainerBlock.add(54);
      isContainerBlock.add(117);
      isContainerBlock.add(146);
      isContainerBlock.add(154);
      isContainerBlock.add(158);
      isRedstoneBlock = new HashSet<>();
      isRedstoneBlock.add(27);
      isRedstoneBlock.add(28);
      isRedstoneBlock.add(29);
      isRedstoneBlock.add(33);
      isRedstoneBlock.add(69);
      isRedstoneBlock.add(70);
      isRedstoneBlock.add(72);
      isRedstoneBlock.add(75);
      isRedstoneBlock.add(76);
      isRedstoneBlock.add(77);
      isRedstoneBlock.add(55);
      isRedstoneBlock.add(64);
      isRedstoneBlock.add(196);
      isRedstoneBlock.add(194);
      isRedstoneBlock.add(195);
      isRedstoneBlock.add(197);
      isRedstoneBlock.add(193);
      isRedstoneBlock.add(71);
      isRedstoneBlock.add(46);
      isRedstoneBlock.add(23);
      isRedstoneBlock.add(25);
      isRedstoneBlock.add(93);
      isRedstoneBlock.add(94);
      isRedstoneBlock.add(131);
      isRedstoneBlock.add(137);
      isRedstoneBlock.add(143);
      isRedstoneBlock.add(146);
      isRedstoneBlock.add(147);
      isRedstoneBlock.add(148);
      isRedstoneBlock.add(149);
      isRedstoneBlock.add(150);
      isRedstoneBlock.add(151);
      isRedstoneBlock.add(152);
      isRedstoneBlock.add(154);
      isRedstoneBlock.add(157);
      isRedstoneBlock.add(158);
      isRedstoneBlock.add(178);
      canTransferRedstone = new HashSet<>();
      canTransferRedstone.add(75);
      canTransferRedstone.add(76);
      canTransferRedstone.add(55);
      canTransferRedstone.add(93);
      canTransferRedstone.add(94);
      canTransferRedstone.add(149);
      canTransferRedstone.add(150);
      isRedstoneSource = new HashSet<>();
      isRedstoneSource.add(28);
      isRedstoneSource.add(75);
      isRedstoneSource.add(76);
      isRedstoneSource.add(69);
      isRedstoneSource.add(70);
      isRedstoneSource.add(72);
      isRedstoneSource.add(77);
      isRedstoneSource.add(131);
      isRedstoneSource.add(143);
      isRedstoneSource.add(147);
      isRedstoneSource.add(148);
      isRedstoneSource.add(151);
      isRedstoneSource.add(152);
      isRedstoneSource.add(178);
      isRailBlock = new HashSet<>();
      isRailBlock.add(27);
      isRailBlock.add(28);
      isRailBlock.add(66);
      isRailBlock.add(157);
      isNaturalTerrainBlock = new HashSet<>();
      isNaturalTerrainBlock.add(1);
      isNaturalTerrainBlock.add(2);
      isNaturalTerrainBlock.add(3);
      isNaturalTerrainBlock.add(7);
      isNaturalTerrainBlock.add(12);
      isNaturalTerrainBlock.add(13);
      isNaturalTerrainBlock.add(82);
      isNaturalTerrainBlock.add(110);
      isNaturalTerrainBlock.add(174);
      isNaturalTerrainBlock.add(159);
      isNaturalTerrainBlock.add(87);
      isNaturalTerrainBlock.add(88);
      isNaturalTerrainBlock.add(89);
      isNaturalTerrainBlock.add(153);
      isNaturalTerrainBlock.add(16);
      isNaturalTerrainBlock.add(15);
      isNaturalTerrainBlock.add(14);
      isNaturalTerrainBlock.add(21);
      isNaturalTerrainBlock.add(56);
      isNaturalTerrainBlock.add(73);
      isNaturalTerrainBlock.add(74);
      isNaturalTerrainBlock.add(129);
      emitsLight = new HashSet<>();
      emitsLight.add(10);
      emitsLight.add(11);
      emitsLight.add(39);
      emitsLight.add(40);
      emitsLight.add(50);
      emitsLight.add(51);
      emitsLight.add(62);
      emitsLight.add(74);
      emitsLight.add(76);
      emitsLight.add(89);
      emitsLight.add(90);
      emitsLight.add(91);
      emitsLight.add(94);
      emitsLight.add(99);
      emitsLight.add(100);
      emitsLight.add(119);
      emitsLight.add(124);
      emitsLight.add(130);
      emitsLight.add(138);
      emitsLight.add(152);
      emitsLight.add(169);
      isTranslucent = new HashSet<>();
      isTranslucent.add(0);
      isTranslucent.add(6);
      isTranslucent.add(8);
      isTranslucent.add(9);
      isTranslucent.add(18);
      isTranslucent.add(20);
      isTranslucent.add(26);
      isTranslucent.add(27);
      isTranslucent.add(28);
      isTranslucent.add(30);
      isTranslucent.add(31);
      isTranslucent.add(32);
      isTranslucent.add(34);
      isTranslucent.add(37);
      isTranslucent.add(38);
      isTranslucent.add(39);
      isTranslucent.add(40);
      isTranslucent.add(50);
      isTranslucent.add(51);
      isTranslucent.add(52);
      isTranslucent.add(53);
      isTranslucent.add(54);
      isTranslucent.add(55);
      isTranslucent.add(59);
      isTranslucent.add(63);
      isTranslucent.add(64);
      isTranslucent.add(65);
      isTranslucent.add(66);
      isTranslucent.add(67);
      isTranslucent.add(68);
      isTranslucent.add(69);
      isTranslucent.add(70);
      isTranslucent.add(71);
      isTranslucent.add(72);
      isTranslucent.add(75);
      isTranslucent.add(76);
      isTranslucent.add(77);
      isTranslucent.add(78);
      isTranslucent.add(79);
      isTranslucent.add(81);
      isTranslucent.add(83);
      isTranslucent.add(85);
      isTranslucent.add(90);
      isTranslucent.add(92);
      isTranslucent.add(93);
      isTranslucent.add(94);
      isTranslucent.add(96);
      isTranslucent.add(101);
      isTranslucent.add(102);
      isTranslucent.add(104);
      isTranslucent.add(105);
      isTranslucent.add(106);
      isTranslucent.add(107);
      isTranslucent.add(108);
      isTranslucent.add(109);
      isTranslucent.add(111);
      isTranslucent.add(113);
      isTranslucent.add(114);
      isTranslucent.add(115);
      isTranslucent.add(116);
      isTranslucent.add(117);
      isTranslucent.add(118);
      isTranslucent.add(126);
      isTranslucent.add(127);
      isTranslucent.add(128);
      isTranslucent.add(130);
      isTranslucent.add(131);
      isTranslucent.add(132);
      isTranslucent.add(134);
      isTranslucent.add(135);
      isTranslucent.add(136);
      isTranslucent.add(139);
      isTranslucent.add(140);
      isTranslucent.add(141);
      isTranslucent.add(142);
      isTranslucent.add(143);
      isTranslucent.add(144);
      isTranslucent.add(145);
      isTranslucent.add(146);
      isTranslucent.add(147);
      isTranslucent.add(148);
      isTranslucent.add(149);
      isTranslucent.add(150);
      isTranslucent.add(151);
      isTranslucent.add(154);
      isTranslucent.add(156);
      isTranslucent.add(157);
      isTranslucent.add(166);
      isTranslucent.add(167);
      isTranslucent.add(171);
      isTranslucent.add(160);
      isTranslucent.add(175);
      isTranslucent.add(176);
      isTranslucent.add(177);
      isTranslucent.add(178);
      isTranslucent.add(180);
      isTranslucent.add(182);
      isTranslucent.add(183);
      isTranslucent.add(184);
      isTranslucent.add(185);
      isTranslucent.add(186);
      isTranslucent.add(187);
      isTranslucent.add(188);
      isTranslucent.add(189);
      isTranslucent.add(190);
      isTranslucent.add(191);
      isTranslucent.add(192);
      isTranslucent.add(193);
      isTranslucent.add(194);
      isTranslucent.add(195);
      isTranslucent.add(196);
      isTranslucent.add(197);
      dataBlockBagItems = new HashMap<>();
      nonDataBlockBagItems = new HashMap<>();
      doNotDestroy = new BaseItemStack(0, 0);
      nonDataBlockBagItems.put(1, new BaseItem(4));
      nonDataBlockBagItems.put(2, new BaseItem(3));
      addIdentities(3, 3);
      addIdentity(4);
      addIdentities(5, 6);
      addIdentities(6, 6);
      nonDataBlockBagItems.put(7, doNotDestroy);
      addIdentity(12);
      addIdentity(13);
      addIdentity(14);
      addIdentity(15);
      nonDataBlockBagItems.put(16, new BaseItem(263));
      addIdentities(17, 4);
      addIdentities(18, 4);
      addIdentity(19);
      addIdentity(20);
      addIdentity(21);
      addIdentity(22);
      addIdentity(23);
      addIdentity(24);
      addIdentity(25);
      addIdentities(26, 8);
      addIdentity(27);
      addIdentity(28);
      addIdentity(29);
      nonDataBlockBagItems.put(30, new BaseItem(287));
      addIdentity(33);
      addIdentities(35, 16);
      addIdentity(37);
      addIdentity(38);
      addIdentity(39);
      addIdentity(40);
      addIdentity(41);
      addIdentity(42);
      addIdentities(43, 7);
      addIdentities(44, 7);
      addIdentity(45);
      addIdentity(46);
      addIdentity(47);
      addIdentity(48);
      addIdentity(49);
      addIdentity(50);
      addIdentity(53);
      addIdentity(54);
      nonDataBlockBagItems.put(55, new BaseItem(331));
      nonDataBlockBagItems.put(56, new BaseItem(264));
      addIdentity(57);
      addIdentity(58);
      nonDataBlockBagItems.put(59, new BaseItem(295));
      nonDataBlockBagItems.put(60, new BaseItem(3));
      addIdentity(61);
      nonDataBlockBagItems.put(62, new BaseItem(61));
      nonDataBlockBagItems.put(63, new BaseItem(323));
      addIdentities(64, 8);
      addIdentity(65);
      addIdentity(66);
      addIdentity(67);
      nonDataBlockBagItems.put(68, new BaseItem(323));
      addIdentity(69);
      addIdentity(70);
      addIdentities(71, 8);
      addIdentity(72);
      addIdentity(73);
      nonDataBlockBagItems.put(74, new BaseItem(73));
      nonDataBlockBagItems.put(75, new BaseItem(76));
      addIdentity(76);
      addIdentity(77);
      addIdentity(78);
      addIdentity(79);
      addIdentity(80);
      addIdentity(81);
      addIdentity(82);
      nonDataBlockBagItems.put(83, new BaseItem(338));
      addIdentity(84);
      addIdentity(85);
      addIdentity(86);
      addIdentity(87);
      addIdentity(88);
      addIdentity(89);
      addIdentity(91);
      nonDataBlockBagItems.put(92, new BaseItem(354));
      nonDataBlockBagItems.put(93, new BaseItem(356));
      nonDataBlockBagItems.put(94, new BaseItem(356));
      addIdentities(160, 16);
      addIdentity(96);
      nonDataBlockBagItems.put(97, doNotDestroy);
      addIdentity(98);
      addIdentity(99);
      addIdentity(100);
      addIdentity(101);
      addIdentity(102);
      addIdentity(103);
      nonDataBlockBagItems.put(104, new BaseItem(361));
      nonDataBlockBagItems.put(105, new BaseItem(362));
      nonDataBlockBagItems.put(106, doNotDestroy);
      addIdentity(107);
      addIdentity(108);
      addIdentity(109);
      nonDataBlockBagItems.put(110, new BaseItem(3));
      addIdentity(111);
      addIdentity(112);
      addIdentity(113);
      addIdentity(114);
      nonDataBlockBagItems.put(115, new BaseItem(372));
      addIdentity(116);
      nonDataBlockBagItems.put(117, new BaseItem(379));
      nonDataBlockBagItems.put(118, new BaseItem(380));
      nonDataBlockBagItems.put(119, doNotDestroy);
      nonDataBlockBagItems.put(120, doNotDestroy);
      addIdentity(121);
      addIdentity(123);
      nonDataBlockBagItems.put(124, new BaseItem(123));
      addIdentities(125, 7);
      addIdentities(126, 7);
      nonDataBlockBagItems.put(127, new BaseItem(351, (short)3));
      addIdentity(128);
      nonDataBlockBagItems.put(129, new BaseItem(388));
      addIdentity(130);
      addIdentity(131);
      nonDataBlockBagItems.put(132, new BaseItem(287));
      addIdentity(133);
      addIdentity(134);
      addIdentity(135);
      addIdentity(136);
      addIdentity(137);
      addIdentities(139, 1);
      nonDataBlockBagItems.put(140, new BaseItemStack(390));
      nonDataBlockBagItems.put(141, new BaseItemStack(391));
      nonDataBlockBagItems.put(142, new BaseItemStack(392));
      addIdentity(143);
      nonDataBlockBagItems.put(144, doNotDestroy);
      addIdentities(145, 2);
      addIdentity(146);
      addIdentity(147);
      addIdentity(148);
      nonDataBlockBagItems.put(149, new BaseItemStack(404));
      nonDataBlockBagItems.put(150, new BaseItemStack(404));
      addIdentity(151);
      addIdentity(152);
      nonDataBlockBagItems.put(153, new BaseItemStack(406));
      addIdentity(154);
      addIdentities(155, 1);

      for (int i = 2; i <= 4; i++) {
         dataBlockBagItems.put(typeDataKey(155, i), new BaseItem(155, (short)2));
      }

      addIdentity(156);
      addIdentity(157);
      addIdentity(158);
      addIdentities(159, 16);
      addIdentity(170);
      addIdentities(171, 16);
      addIdentity(172);
      addIdentity(173);
      addIdentities(162, 1);
      addIdentities(161, 1);
      addIdentity(163);
      addIdentity(164);
      addIdentity(174);
      addIdentities(160, 16);
      addIdentities(175, 6);
      addIdentities(196, 8);
      addIdentities(194, 8);
      addIdentities(195, 8);
      addIdentities(197, 8);
      addIdentities(193, 8);
      random = new Random();
      dataAttachments = new HashMap<>();
      nonDataAttachments = new HashMap<>();
      nonDataAttachments.put(6, PlayerDirection.DOWN);
      nonDataAttachments.put(31, PlayerDirection.DOWN);
      nonDataAttachments.put(32, PlayerDirection.DOWN);

      for (int offset = 0; offset < 16; offset += 8) {
         dataAttachments.put(typeDataKey(34, offset + 0), PlayerDirection.UP);
         dataAttachments.put(typeDataKey(34, offset + 1), PlayerDirection.DOWN);
         addCardinals(34, offset + 2, offset + 5, offset + 3, offset + 4);
      }

      nonDataAttachments.put(37, PlayerDirection.DOWN);
      nonDataAttachments.put(38, PlayerDirection.DOWN);
      nonDataAttachments.put(39, PlayerDirection.DOWN);
      nonDataAttachments.put(40, PlayerDirection.DOWN);

      for (int blockId : new int[]{50, 76, 75}) {
         dataAttachments.put(typeDataKey(blockId, 0), PlayerDirection.DOWN);
         dataAttachments.put(typeDataKey(blockId, 5), PlayerDirection.DOWN);
         addCardinals(blockId, 4, 1, 3, 2);
      }

      nonDataAttachments.put(55, PlayerDirection.DOWN);
      nonDataAttachments.put(59, PlayerDirection.DOWN);
      nonDataAttachments.put(63, PlayerDirection.DOWN);
      nonDataAttachments.put(64, PlayerDirection.DOWN);
      addCardinals(65, 2, 5, 3, 4);
      addCardinals(68, 2, 5, 3, 4);

      for (int offset = 0; offset < 16; offset += 8) {
         addCardinals(69, offset + 4, offset + 1, offset + 3, offset + 2);
         dataAttachments.put(typeDataKey(69, offset + 5), PlayerDirection.DOWN);
         dataAttachments.put(typeDataKey(69, offset + 6), PlayerDirection.DOWN);
         dataAttachments.put(typeDataKey(69, offset + 7), PlayerDirection.UP);
         dataAttachments.put(typeDataKey(69, offset + 0), PlayerDirection.UP);
      }

      nonDataAttachments.put(70, PlayerDirection.DOWN);
      nonDataAttachments.put(71, PlayerDirection.DOWN);
      nonDataAttachments.put(72, PlayerDirection.DOWN);

      for (int offset = 0; offset < 16; offset += 8) {
         addCardinals(77, offset + 4, offset + 1, offset + 3, offset + 2);
         addCardinals(143, offset + 4, offset + 1, offset + 3, offset + 2);
      }

      dataAttachments.put(typeDataKey(77, 0), PlayerDirection.UP);
      dataAttachments.put(typeDataKey(77, 5), PlayerDirection.DOWN);
      dataAttachments.put(typeDataKey(143, 0), PlayerDirection.UP);
      dataAttachments.put(typeDataKey(143, 5), PlayerDirection.DOWN);
      nonDataAttachments.put(81, PlayerDirection.DOWN);
      nonDataAttachments.put(83, PlayerDirection.DOWN);
      nonDataAttachments.put(92, PlayerDirection.DOWN);
      nonDataAttachments.put(93, PlayerDirection.DOWN);
      nonDataAttachments.put(94, PlayerDirection.DOWN);

      for (int offset = 0; offset < 16; offset += 4) {
         addCardinals(96, offset + 0, offset + 3, offset + 1, offset + 2);
         addCardinals(167, offset + 0, offset + 3, offset + 1, offset + 2);
      }

      nonDataAttachments.put(104, PlayerDirection.DOWN);
      nonDataAttachments.put(105, PlayerDirection.DOWN);
      dataAttachments.put(typeDataKey(106, 0), PlayerDirection.UP);
      addCardinals(106, 1, 2, 4, 8);
      nonDataAttachments.put(115, PlayerDirection.DOWN);

      for (int offset = 0; offset < 16; offset += 4) {
         addCardinals(127, offset + 0, offset + 1, offset + 2, offset + 3);
      }

      for (int offset = 0; offset < 16; offset += 4) {
         addCardinals(131, offset + 2, offset + 3, offset + 0, offset + 1);
      }

      nonDataAttachments.put(132, PlayerDirection.DOWN);
      nonDataAttachments.put(140, PlayerDirection.DOWN);
      nonDataAttachments.put(141, PlayerDirection.DOWN);
      nonDataAttachments.put(142, PlayerDirection.DOWN);
      nonDataAttachments.put(145, PlayerDirection.DOWN);
      nonDataAttachments.put(147, PlayerDirection.DOWN);
      nonDataAttachments.put(148, PlayerDirection.DOWN);
      nonDataAttachments.put(149, PlayerDirection.DOWN);
      nonDataAttachments.put(150, PlayerDirection.DOWN);
      nonDataAttachments.put(171, PlayerDirection.DOWN);
      nonDataAttachments.put(175, PlayerDirection.DOWN);
      nonDataAttachments.put(176, PlayerDirection.DOWN);
      addCardinals(177, 4, 2, 5, 3);
      nonDataAttachments.put(193, PlayerDirection.DOWN);
      nonDataAttachments.put(194, PlayerDirection.DOWN);
      nonDataAttachments.put(195, PlayerDirection.DOWN);
      nonDataAttachments.put(196, PlayerDirection.DOWN);
      nonDataAttachments.put(197, PlayerDirection.DOWN);

      for (int offset = 0; offset < 16; offset += 8) {
         addCardinals(27, offset + 3, offset + 4, offset + 2, offset + 5);
         addCardinals(28, offset + 3, offset + 4, offset + 2, offset + 5);
         addCardinals(66, offset + 3, offset + 4, offset + 2, offset + 5);
         addCardinals(157, offset + 3, offset + 4, offset + 2, offset + 5);
      }
   }
}
