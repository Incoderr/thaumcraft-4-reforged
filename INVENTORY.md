# Инвентарь контента Thaumcraft 4.2.3.5

Файл генерируется скриптом `tools/inventory.py` по декомпилату `reference/decompiled/` и `tools/id_map.json`. Руками правится только `tools/id_map.json` (новые id и skip), остальное пересобирается командой `python3 tools/inventory.py`.

## Сводка

| Показатель | Значение |
|---|---|
| Блоки (`registerBlock`) | 45 |
| Поля `public static Block` в ConfigBlocks | 45 |
| Предметы (`registerItem`) | 108 |
| Поля `public static Item` в ConfigItems | 111 |
| Block entities (`registerTileEntity`) | 73 |
| Сущности (`registerModEntity`) | 50 |
| Яйца призыва (`addMapping`) | 29 |
| Меню/контейнеры (серверные GUI id с контейнером) | 15 |
| Пакеты (`registerMessage`) | 39 |
| Исследования (`new ResearchItem`) | 201 |
| Вкладки исследований | 6 |
| События звука (`sounds.json`) | 66 |
| Файлы `.ogg` | 111 |
| Зелья | 9 |
| Биомы | 4 |
| Ключи lang `tile.*`/`item.*` | 461 |
| Ключи lang `tile.*`/`item.*` `.name` | 411 |
| Записи id_map: блоки | 206 |
| Записи id_map: предметы | 261 |
| Классов всего | 942 |

## Классы по пакетам

Источник: классы из Thaumcraft-1.7.10-4.2.3.5.jar.

| Пакет | Классов |
|---|---|
| thaumcraft/client/renderers | 170 |
| thaumcraft/common/lib | 137 |
| thaumcraft/common/entities | 136 |
| thaumcraft/common/items | 84 |
| thaumcraft/common/tiles | 80 |
| thaumcraft/codechicken | 73 |
| thaumcraft/common/blocks | 71 |
| thaumcraft/api | 48 |
| thaumcraft/client/fx | 45 |
| thaumcraft/common/container | 26 |
| thaumcraft/client/gui | 24 |
| thaumcraft/api (корень) | 19 |
| thaumcraft/client/lib | 10 |
| thaumcraft/common/config | 9 |
| truetyper | 6 |
| thaumcraft/client (корень) | 2 |
| thaumcraft/common (корень) | 2 |

## Блоки

Регистрации в `ConfigBlocks`. Колонка «meta-подтипов» это число записей `id_map.json` для блока.

| Регистрация | Класс блока | Класс ItemBlock | Unlocalized | Записей id_map |
|---|---|---|---|---|
| blockFluxGoo | BlockFluxGoo | BlockFluxGooItem | blockFluxGoo | 1 |
| blockFluxGas | BlockFluxGas | BlockFluxGasItem | blockFluxGas | 1 |
| blockFluidPure | BlockFluidPure |  | blockFluidPure | 1 |
| blockFluidDeath | BlockFluidDeath |  | blockFluidDeath | 1 |
| blockCustomOre | BlockCustomOre | BlockCustomOreItem | blockCustomOre | 8 |
| blockMagicalLog | BlockMagicalLog | BlockMagicalLogItem | blockMagicalLog | 2 |
| blockMagicalLeaves | BlockMagicalLeaves | BlockMagicalLeavesItem | blockMagicalLeaves | 2 |
| blockCustomPlant | BlockCustomPlant | BlockCustomPlantItem | blockCustomPlant | 6 |
| blockTaint | BlockTaint | BlockTaintItem | blockTaint | 3 |
| blockTaintFibres | BlockTaintFibres | BlockTaintFibresItem | blockTaintFibres | 5 |
| blockCosmeticOpaque | BlockCosmeticOpaque | BlockCosmeticOpaqueItem | blockCosmeticOpaque | 3 |
| blockCosmeticSolid | BlockCosmeticSolid | BlockCosmeticSolidItem | blockCosmeticSolid | 16 |
| blockCrystal | BlockCrystal | BlockCrystalItem | blockCrystal | 8 |
| blockTube | BlockTube | BlockTubeItem | blockTube | 8 |
| blockMetalDevice | BlockMetalDevice | BlockMetalDeviceItem | blockMetalDevice | 14 |
| blockWoodenDevice | BlockWoodenDevice | BlockWoodenDeviceItem | blockWoodenDevice | 25 |
| blockStoneDevice | BlockStoneDevice | BlockStoneDeviceItem | blockStoneDevice | 15 |
| blockMirror | BlockMirror | BlockMirrorItem | blockMirror | 3 |
| blockTable | BlockTable | BlockTableItem | blockTable | 12 |
| blockChestHungry | BlockChestHungry |  | blockChestHungry | 1 |
| blockArcaneDoor | BlockArcaneDoor |  | blockArcaneDoor | 1 |
| blockLifter | BlockLifter |  | blockLifter | 1 |
| blockMagicBox | BlockMagicBox |  | blockMagicBox | 1 |
| blockAlchemyFurnace | BlockAlchemyFurnace |  | blockAlchemyFurnace | 1 |
| blockJar | BlockJar | BlockJarItem | blockJar | 4 |
| blockCandle | BlockCandle | BlockCandleItem | blockCandle | 16 |
| blockEldritch | BlockEldritch | BlockEldritchItem | blockEldritch | 11 |
| blockAiry | BlockAiry | BlockAiryItem | blockAiry | 9 |
| blockManaPod | BlockManaPod |  | blockManaPod | 1 |
| blockArcaneFurnace | BlockArcaneFurnace |  | blockArcaneFurnace | 1 |
| blockWarded | BlockWarded |  | blockWarded | 1 |
| blockHole | BlockHole |  | blockHole | 1 |
| blockPortalEldritch | BlockEldritchPortal |  | blockPortalEldritch | 1 |
| blockEssentiaReservoir | BlockEssentiaReservoir | BlockEssentiaReservoirItem | blockEssentiaReservoir | 1 |
| blockEldritchNothing | BlockEldritchNothing |  | blockPortalNothing | 1 |
| blockStairsArcaneStone | BlockCosmeticStairs |  | blockStairsArcaneStone | 1 |
| blockStairsGreatwood | BlockCosmeticStairs |  | blockStairsGreatwood | 1 |
| blockStairsSilverwood | BlockCosmeticStairs |  | blockStairsSilverwood | 1 |
| blockStairsEldritch | BlockCosmeticStairs |  | blockStairsEldritch | 1 |
| blockCosmeticSlabWood | BlockCosmeticWoodSlab | BlockCosmeticWoodSlabItem | blockCosmeticSlabWood | 2 |
| blockCosmeticSlabStone | BlockCosmeticStoneSlab | BlockCosmeticStoneSlabItem | blockCosmeticSlabStone | 2 |
| blockCosmeticDoubleSlabWood | BlockCosmeticWoodSlab |  | blockCosmeticDoubleSlabWood | 2 |
| blockCosmeticDoubleSlabStone | BlockCosmeticStoneSlab |  | blockCosmeticDoubleSlabStone | 2 |
| blockLootUrn | BlockLoot | BlockLootItem | blockLootUrn | 3 |
| blockLootCrate | BlockLoot | BlockLootItem | blockLootCrate | 3 |

### Блоки: meta в новые id

| Старое имя | meta | Название (en) | Новый id | Состояние | Skip |
|---|---|---|---|---|---|
| blockFluxGoo |  | Flux Goo | flux_goo |  |  |
| blockFluxGas |  | Flux Gas | flux_gas |  |  |
| blockFluidPure |  | Purifying Fluid | purifying_fluid |  |  |
| blockFluidDeath |  | Liquid Death | liquid_death |  |  |
| blockCustomOre | 0 | Cinnabar Ore | cinnabar_ore |  |  |
| blockCustomOre | 1 | Air Infused Stone | air_infused_stone |  |  |
| blockCustomOre | 2 | Fire Infused Stone | fire_infused_stone |  |  |
| blockCustomOre | 3 | Water Infused Stone | water_infused_stone |  |  |
| blockCustomOre | 4 | Earth Infused Stone | earth_infused_stone |  |  |
| blockCustomOre | 5 | Order Infused Stone | order_infused_stone |  |  |
| blockCustomOre | 6 | Entropy Infused Stone | entropy_infused_stone |  |  |
| blockCustomOre | 7 | Amber Bearing Stone | amber_ore |  |  |
| blockCustomPlant | 0 | Greatwood Sapling | greatwood_sapling |  |  |
| blockCustomPlant | 1 | Silverwood Sapling | silverwood_sapling |  |  |
| blockCustomPlant | 2 | Shimmerleaf | shimmerleaf |  |  |
| blockCustomPlant | 3 | Cinderpearl | cinderpearl |  |  |
| blockCustomPlant | 4 | Ethereal Bloom | ethereal_bloom |  |  |
| blockCustomPlant | 5 | Vishroom | vishroom |  |  |
| blockTaint | 0 | Crusted Taint | crusted_taint |  |  |
| blockTaint | 1 | Tainted Soil | tainted_soil |  |  |
| blockTaint | 2 | Block of Flesh | flesh_block |  |  |
| blockTaintFibres | 0 | Fibrous Taint | fibrous_taint |  |  |
| blockTaintFibres | 1 | Tainted Grass | tainted_grass |  |  |
| blockTaintFibres | 2 | Tainted Plant | tainted_plant |  |  |
| blockTaintFibres | 3 | Spore Stalk | spore_stalk |  |  |
| blockTaintFibres | 4 | Mature Spore Stalk | mature_spore_stalk |  |  |
| blockCosmeticOpaque | 0 | Amber Block | amber_block |  |  |
| blockCosmeticOpaque | 1 | Amber Bricks | amber_bricks |  |  |
| blockCosmeticOpaque | 2 | Warded Glass | warded_glass |  |  |
| blockCosmeticSolid | 0 | Obsidian Totem | obsidian_totem |  |  |
| blockCosmeticSolid | 1 | Obsidian Tile | obsidian_tile |  |  |
| blockCosmeticSolid | 2 | Paving Stone of Travel | travel_paving_stone |  |  |
| blockCosmeticSolid | 3 | Paving Stone of Warding | warding_paving_stone |  |  |
| blockCosmeticSolid | 4 | Thaumium Block | thaumium_block |  |  |
| blockCosmeticSolid | 5 | Tallow Block | tallow_block |  |  |
| blockCosmeticSolid | 6 | Arcane Stone Block | arcane_stone |  |  |
| blockCosmeticSolid | 7 | Arcane Stone Bricks | arcane_stone_bricks |  |  |
| blockCosmeticSolid | 8 | Charged Obsidian Totem | charged_obsidian_totem |  |  |
| blockCosmeticSolid | 9 | Golem Fetter | golem_fetter |  |  |
| blockCosmeticSolid | 10 | Active Golem Fetter | golem_fetter | {"active": "true"} |  |
| blockCosmeticSolid | 11 | Ancient Stone | ancient_stone |  |  |
| blockCosmeticSolid | 12 | Ancient Rock | ancient_rock |  |  |
| blockCosmeticSolid | 13 | Ancient Stone | ancient_stone_no_spawn |  |  |
| blockCosmeticSolid | 14 | Crusted Stone | crusted_stone |  |  |
| blockCosmeticSolid | 15 | Ancient Stone Pedestal | ancient_stone_pedestal |  |  |
| blockCrystal | 0 | Air Crystal Cluster | air_crystal_cluster |  |  |
| blockCrystal | 1 | Fire Crystal Cluster | fire_crystal_cluster |  |  |
| blockCrystal | 2 | Water Crystal Cluster | water_crystal_cluster |  |  |
| blockCrystal | 3 | Earth Crystal Cluster | earth_crystal_cluster |  |  |
| blockCrystal | 4 | Order Crystal Cluster | order_crystal_cluster |  |  |
| blockCrystal | 5 | Entropy Crystal Cluster | entropy_crystal_cluster |  |  |
| blockCrystal | 6 | Mixed Crystal Cluster | mixed_crystal_cluster |  |  |
| blockCrystal | 7 | Strange Crystals | strange_crystals |  |  |
| blockTube | 0 | Essentia Tube | essentia_tube |  |  |
| blockTube | 1 | Essentia Valve | essentia_valve |  |  |
| blockTube | 2 | Alchemical Centrifuge | alchemical_centrifuge |  |  |
| blockTube | 3 | Filtered Essentia Tube | filtered_essentia_tube |  |  |
| blockTube | 4 | Essentia Buffer | essentia_buffer |  |  |
| blockTube | 5 | Restricted Essentia Tube | restricted_essentia_tube |  |  |
| blockTube | 6 | Directional Essentia Tube | directional_essentia_tube |  |  |
| blockTube | 7 | Essentia Crystallizer | essentia_crystallizer |  |  |
| blockMetalDevice | 0 | Crucible | crucible |  |  |
| blockMetalDevice | 1 | Arcane Alembic | alembic |  |  |
| blockMetalDevice | 2 | Vis Charge Relay | vis_charge_relay |  |  |
| blockMetalDevice | 3 | Advanced Alchemical Construct | advanced_alchemical_construct |  |  |
| blockMetalDevice | 5 | Item Grate | item_grate |  |  |
| blockMetalDevice | 6 | Item Grate (Closed) | item_grate | {"closed": "true"} |  |
| blockMetalDevice | 7 | Arcane Lamp | arcane_lamp |  |  |
| blockMetalDevice | 8 | Lamp of Growth | lamp_of_growth |  |  |
| blockMetalDevice | 9 | Alchemical Construct | alchemical_construct |  |  |
| blockMetalDevice | 10 | Thaumatorium | thaumatorium |  |  |
| blockMetalDevice | 11 | Thaumatorium | thaumatorium_top |  |  |
| blockMetalDevice | 12 | Mnemonic Matrix | mnemonic_matrix |  |  |
| blockMetalDevice | 13 | Lamp of Fertility | lamp_of_fertility |  |  |
| blockMetalDevice | 14 | Vis Relay | vis_relay |  |  |
| blockWoodenDevice | 0 | Arcane Bellows | arcane_bellows |  |  |
| blockWoodenDevice | 1 | Arcane Ear | arcane_ear |  |  |
| blockWoodenDevice | 2 | Arcane Pressure Plate | arcane_pressure_plate |  |  |
| blockWoodenDevice | 4 | Arcane Bore Base | arcane_bore_base |  |  |
| blockWoodenDevice | 5 | Arcane Bore | arcane_bore |  |  |
| blockWoodenDevice | 6 | Greatwood Planks | greatwood_planks |  |  |
| blockWoodenDevice | 7 | Silverwood Planks | silverwood_planks |  |  |
| blockWoodenDevice | 8 | Crimson Cult Banner | banner |  |  |
| blockStoneDevice | 0 | Alchemical Furnace | alchemical_furnace |  |  |
| blockStoneDevice | 1 | Arcane Pedestal | arcane_pedestal |  |  |
| blockStoneDevice | 2 | Runic Matrix | runic_matrix |  |  |
| blockStoneDevice | 3 | Infusion Pillar | infusion_pillar |  |  |
| blockStoneDevice | 4 | Infusion Pillar | infusion_pillar_base |  |  |
| blockStoneDevice | 5 | Wand Recharge Pedestal | wand_recharge_pedestal |  |  |
| blockStoneDevice | 6 | Infusion Pillar |  |  | meta never placed: lang leftover, infusion pillar parts are metas 3 and 4 (damageDropped redirects them to blockCosmeticSolid 7 and 6) |
| blockStoneDevice | 7 | Infusion Pillar |  |  | meta never placed: lang leftover, infusion pillar parts are metas 3 and 4 (damageDropped redirects them to blockCosmeticSolid 7 and 6) |
| blockStoneDevice | 8 | Compound Recharge Focus | compound_recharge_focus |  |  |
| blockStoneDevice | 9 | Node Stabilizer | node_stabilizer |  |  |
| blockStoneDevice | 10 | Advanced Node Stabilizer | advanced_node_stabilizer |  |  |
| blockStoneDevice | 11 | Node Transducer | node_transducer |  |  |
| blockStoneDevice | 12 | Arcane Spa | arcane_spa |  |  |
| blockStoneDevice | 13 | Focal Manipulator | focal_manipulator |  |  |
| blockStoneDevice | 14 | Flux Scrubber | flux_scrubber |  |  |
| blockMirror | 0 | Magic Mirror | magic_mirror |  |  |
| blockMirror | 6 | Essentia Mirror | essentia_mirror |  |  |
| blockMirror |  | Magic Mirror |  |  | fallback name key with the same text as tile.blockMirror.0.name, covered by magic_mirror |
| blockTable | 0 | Table | table | {"axis": "x"} |  |
| blockTable | 1 | Table | table | {"axis": "z"} |  |
| blockTable | 14 | Deconstruction Table | deconstruction_table |  |  |
| blockTable | 15 | Arcane Worktable | arcane_worktable |  |  |
| blockChestHungry |  | Hungry Chest | hungry_chest |  |  |
| blockArcaneDoor |  | Arcane Door | arcane_door |  |  |
| blockLifter |  | Arcane Levitator | arcane_levitator |  |  |
| blockMagicBox |  |  |  |  | unreachable: no recipe, worldgen, creative entry or placement site anywhere in the code (getSubBlocks is empty) |
| blockAlchemyFurnace |  | Advanced Alchemical Furnace | advanced_alchemical_furnace |  |  |
| blockJar | 0 | Warded Jar | warded_jar |  |  |
| blockJar | 1 | Brain in a Jar | brain_jar |  |  |
| blockJar | 2 | Node in a Jar | node_jar |  |  |
| blockJar | 3 | Void Jar | void_jar |  |  |
| blockCandle | 0 | White Tallow Candle | white_tallow_candle |  |  |
| blockCandle | 1 | Orange Tallow Candle | orange_tallow_candle |  |  |
| blockCandle | 2 | Magenta Tallow Candle | magenta_tallow_candle |  |  |
| blockCandle | 3 | Light Blue Tallow Candle | light_blue_tallow_candle |  |  |
| blockCandle | 4 | Yellow Tallow Candle | yellow_tallow_candle |  |  |
| blockCandle | 5 | Lime Tallow Candle | lime_tallow_candle |  |  |
| blockCandle | 6 | Pink Tallow Candle | pink_tallow_candle |  |  |
| blockCandle | 7 | Gray Tallow Candle | gray_tallow_candle |  |  |
| blockCandle | 8 | Light Gray Tallow Candle | light_gray_tallow_candle |  |  |
| blockCandle | 9 | Cyan Tallow Candle | cyan_tallow_candle |  |  |
| blockCandle | 10 | Purple Tallow Candle | purple_tallow_candle |  |  |
| blockCandle | 11 | Blue Tallow Candle | blue_tallow_candle |  |  |
| blockCandle | 12 | Brown Tallow Candle | brown_tallow_candle |  |  |
| blockCandle | 13 | Green Tallow Candle | green_tallow_candle |  |  |
| blockCandle | 14 | Red Tallow Candle | red_tallow_candle |  |  |
| blockCandle | 15 | Black Tallow Candle | black_tallow_candle |  |  |
| blockEldritch | 0 | Eldritch Altar | eldritch_altar |  |  |
| blockEldritch | 1 | Eldritch Obelisk | eldritch_obelisk |  |  |
| blockEldritch | 2 | Eldritch Obelisk | eldritch_obelisk | {"part": "top"} |  |
| blockEldritch | 3 | Eldritch Capstone | eldritch_capstone |  |  |
| blockEldritch | 4 | Glowing Crusted Stone | glowing_crusted_stone |  |  |
| blockEldritch | 5 | Glyphed Stone | glyphed_stone |  |  |
| blockEldritch | 7 | Ancient Doorway | ancient_doorway |  |  |
| blockEldritch | 8 | Ancient Locking Mechanism | ancient_locking_mechanism |  |  |
| blockEldritch | 9 | Crusted Opening | crusted_opening |  |  |
| blockEldritch | 10 | Runed Stone | runed_stone |  |  |
| blockAiry | 0 | Aura Node | aura_node |  |  |
| blockAiry | 1 | Nitor | nitor_light |  |  |
| blockAiry | 2 | Glimmer of Light | glimmer_of_light |  |  |
| blockAiry | 3 | Glimmer of Light | arcane_lamp_light |  |  |
| blockAiry | 4 | Warding Aura | warding_aura |  |  |
| blockAiry | 5 | Energized Aura Node | energized_aura_node |  |  |
| blockAiry | 10 | Static Field | static_field |  |  |
| blockAiry | 11 | Sapping Field | sapping_field |  |  |
| blockAiry | 12 | Impassable | impassable_field |  |  |
| blockManaPod |  |  | mana_pod |  |  |
| blockArcaneFurnace |  | Infernal Furnace | infernal_furnace |  |  |
| blockWarded |  |  | warded_block |  |  |
| blockHole |  | Empty Space | hole |  |  |
| blockPortalEldritch |  | Eldritch Portal | eldritch_portal |  |  |
| blockEssentiaReservoir |  | Essentia Reservoir | essentia_reservoir |  |  |
| blockEldritchNothing |  |  | eldritch_nothing |  |  |
| blockStairsArcaneStone |  | Arcane Stone Stairs | arcane_stone_stairs |  |  |
| blockStairsGreatwood |  | Greatwood Stairs | greatwood_stairs |  |  |
| blockStairsSilverwood |  | Silverwood Stairs | silverwood_stairs |  |  |
| blockStairsEldritch |  | Ancient Stone Stairs | ancient_stone_stairs |  |  |
| blockLootUrn | 0 | Old Urn | loot_urn | {"tier": "common"} |  |
| blockLootCrate | 0 | Abandoned Crate | loot_crate | {"tier": "common"} |  |
| blockSecure |  | Warded Stone Block |  |  | orphan lang key: no block has this unlocalized name (blockWarded has none), TC3 leftover |
| blockMagicalLog | 0 | Greatwood Log | greatwood_log |  |  |
| blockMagicalLog | 1 | Silverwood Log | silverwood_log |  |  |
| blockMagicalLeaves | 0 | Greatwood Leaves | greatwood_leaves |  |  |
| blockMagicalLeaves | 1 | Silverwood Leaves | silverwood_leaves |  |  |
| blockWooden |  | Arcane Wood Block |  |  | orphan lang key: no block has this unlocalized name, TC3 leftover |
| blockWoodenDevice | 8 | White Banner | banner | {"color": "white"} |  |
| blockWoodenDevice | 8 | Orange Banner | banner | {"color": "orange"} |  |
| blockWoodenDevice | 8 | Magenta Banner | banner | {"color": "magenta"} |  |
| blockWoodenDevice | 8 | Light Blue Banner | banner | {"color": "light_blue"} |  |
| blockWoodenDevice | 8 | Yellow Banner | banner | {"color": "yellow"} |  |
| blockWoodenDevice | 8 | Lime Banner | banner | {"color": "lime"} |  |
| blockWoodenDevice | 8 | Pink Banner | banner | {"color": "pink"} |  |
| blockWoodenDevice | 8 | Gray Banner | banner | {"color": "gray"} |  |
| blockWoodenDevice | 8 | Light Gray Banner | banner | {"color": "light_gray"} |  |
| blockWoodenDevice | 8 | Cyan Banner | banner | {"color": "cyan"} |  |
| blockWoodenDevice | 8 | Purple Banner | banner | {"color": "purple"} |  |
| blockWoodenDevice | 8 | Blue Banner | banner | {"color": "blue"} |  |
| blockWoodenDevice | 8 | Brown Banner | banner | {"color": "brown"} |  |
| blockWoodenDevice | 8 | Green Banner | banner | {"color": "green"} |  |
| blockWoodenDevice | 8 | Red Banner | banner | {"color": "red"} |  |
| blockWoodenDevice | 8 | Black Banner | banner | {"color": "black"} |  |
| blockTable | 2 | Research Table | research_table | {"facing": "north"} |  |
| blockCosmeticSlabWood | 0 | Greatwood Slab | greatwood_slab |  |  |
| blockCosmeticSlabWood | 1 | Silverwood Slab | silverwood_slab |  |  |
| blockCosmeticSlabStone | 0 | Arcane Stone Slab | arcane_stone_slab |  |  |
| blockCosmeticSlabStone | 1 | Ancient Stone Slab | ancient_stone_slab |  |  |
| blockWoodenDevice | 3 |  | arcane_pressure_plate | {"powered": "true"} |  |
| blockTable | 3 |  | research_table | {"facing": "south"} |  |
| blockTable | 4 |  | research_table | {"facing": "west"} |  |
| blockTable | 5 |  | research_table | {"facing": "east"} |  |
| blockTable | 6 |  | research_table_part | {"facing": "north"} |  |
| blockTable | 7 |  | research_table_part | {"facing": "south"} |  |
| blockTable | 8 |  | research_table_part | {"facing": "west"} |  |
| blockTable | 9 |  | research_table_part | {"facing": "east"} |  |
| blockEldritch | 6 |  |  |  | meta unused: no lang key and no creation site (only a dim light and icon are defined) |
| blockCosmeticDoubleSlabWood | 0 |  | greatwood_slab | {"type": "double"} |  |
| blockCosmeticDoubleSlabWood | 1 |  | silverwood_slab | {"type": "double"} |  |
| blockCosmeticDoubleSlabStone | 0 |  | arcane_stone_slab | {"type": "double"} |  |
| blockCosmeticDoubleSlabStone | 1 |  | ancient_stone_slab | {"type": "double"} |  |
| blockLootUrn | 1 |  | loot_urn | {"tier": "uncommon"} |  |
| blockLootUrn | 2 |  | loot_urn | {"tier": "rare"} |  |
| blockLootCrate | 1 |  | loot_crate | {"tier": "uncommon"} |  |
| blockLootCrate | 2 |  | loot_crate | {"tier": "rare"} |  |

## Предметы

| Регистрация | Класс | Unlocalized | Записей id_map |
|---|---|---|---|
| WandCasting | ItemWandCasting | WandCasting | 1 |
| WandCap | ItemWandCap | WandCap | 9 |
| WandRod | ItemWandRod | WandRod | 17 |
| FocusPouch | ItemFocusPouchBauble | FocusPouch | 1 |
| FocusFire | ItemFocusFire | FocusFire | 1 |
| FocusShock | ItemFocusShock | FocusShock | 1 |
| FocusHellbat | ItemFocusHellbat | FocusHellbat | 1 |
| FocusFrost | ItemFocusFrost | FocusFrost | 1 |
| FocusTrade | ItemFocusTrade | FocusTrade | 1 |
| FocusExcavation | ItemFocusExcavation | FocusExcavation | 1 |
| FocusPortableHole | ItemFocusPortableHole | FocusPortableHole | 1 |
| FocusPech | ItemFocusPech | FocusPech | 1 |
| FocusWarding | ItemFocusWarding | FocusWarding | 1 |
| FocusPrimal | ItemFocusPrimal | FocusPrimal | 1 |
| ItemEssence | ItemEssence | ItemEssence | 2 |
| ItemManaBean | ItemManaBean | ItemManaBean | 1 |
| ItemWispEssence | ItemWispEssence | ItemWispEssence | 1 |
| ItemResource | ItemResource | ItemResource | 19 |
| ItemShard | ItemShard | ItemShard | 7 |
| ItemResearchNotes | ItemResearchNotes | ItemResearchNotes | 2 |
| ItemInkwell | ItemInkwell | ItemInkwell | 1 |
| ItemThaumonomicon | ItemThaumonomicon | ItemThaumonomicon | 2 |
| ItemThaumometer | ItemThaumometer | ItemThaumometer | 1 |
| ItemGoggles | ItemGoggles | ItemGoggles | 1 |
| ItemHelmetThaumium | ItemThaumiumArmor | ItemHelmetThaumium | 1 |
| ItemChestplateThaumium | ItemThaumiumArmor | ItemChestplateThaumium | 1 |
| ItemLeggingsThaumium | ItemThaumiumArmor | ItemLeggingsThaumium | 1 |
| ItemBootsThaumium | ItemThaumiumArmor | ItemBootsThaumium | 1 |
| ItemShovelThaumium | ItemThaumiumShovel | ItemShovelThaumium | 1 |
| ItemPickThaumium | ItemThaumiumPickaxe | ItemPickThaumium | 1 |
| ItemAxeThaumium | ItemThaumiumAxe | ItemAxeThaumium | 1 |
| ItemSwordThaumium | ItemThaumiumSword | ItemSwordThaumium | 1 |
| ItemHoeThaumium | ItemThaumiumHoe | ItemHoeThaumium | 1 |
| ItemArcaneDoor | ItemArcaneDoor | ItemArcaneDoor | 1 |
| ItemNugget | ItemNugget | ItemNugget | 15 |
| BootsTraveller | ItemBootsTraveller | BootsTraveller | 1 |
| ItemNuggetChicken | ItemNuggetEdible | ItemNuggetChicken | 1 |
| ItemNuggetBeef | ItemNuggetEdible | ItemNuggetBeef | 1 |
| ItemNuggetPork | ItemNuggetEdible | ItemNuggetPork | 1 |
| ItemNuggetFish | ItemNuggetEdible | ItemNuggetFish | 1 |
| TripleMeatTreat | ItemTripleMeatTreat | TripleMeatTreat | 1 |
| ItemSwordElemental | ItemElementalSword | ItemSwordElemental | 1 |
| ItemShovelElemental | ItemElementalShovel | ItemShovelElemental | 1 |
| ItemPickaxeElemental | ItemElementalPickaxe | ItemPickaxeElemental | 1 |
| ItemAxeElemental | ItemElementalAxe | ItemAxeElemental | 1 |
| ItemHoeElemental | ItemElementalHoe | ItemHoeElemental | 1 |
| ItemChestplateRobe | ItemRobeArmor | ItemChestplateRobe | 1 |
| ItemLeggingsRobe | ItemRobeArmor | ItemLeggingsRobe | 1 |
| ItemBootsRobe | ItemRobeArmor | ItemBootsRobe | 1 |
| ArcaneDoorKey | ItemKey | ArcaneDoorKey | 2 |
| HandMirror | ItemHandMirror | HandMirror | 1 |
| HoverHarness | ItemHoverHarness | HoverHarness | 1 |
| BlockJarFilledItem | ItemJarFilled | BlockJarFilledItem | 2 |
| BlockJarNodeItem | ItemJarNode | BlockJarNodeItem | 1 |
| TrunkSpawner | ItemTrunkSpawner | TrunkSpawner | 1 |
| ItemGolemPlacer | ItemGolemPlacer | ItemGolemPlacer | 8 |
| ItemGolemCore | ItemGolemCore | ItemGolemCore | 14 |
| ItemGolemUpgrade | ItemGolemUpgrade | ItemGolemUpgrade | 6 |
| GolemBell | ItemGolemBell | GolemBell | 1 |
| ItemGolemDecoration | ItemGolemDecoration | ItemGolemDecoration | 9 |
| ItemBowBone | ItemBowBone | ItemBowBone | 1 |
| PrimalArrow | ItemPrimalArrow | ItemPrimalArrow | 6 |
| ItemResonator | ItemResonator | ItemResonator | 1 |
| ItemBaubleBlanks | ItemBaubleBlanks | ItemBaubleBlanks | 9 |
| ItemAmuletRunic | ItemAmuletRunic | ItemAmuletRunic | 2 |
| ItemRingRunic | ItemRingRunic | ItemRingRunic | 4 |
| ItemGirdleRunic | ItemGirdleRunic | ItemGirdleRunic | 2 |
| ItemAmuletVis | ItemAmuletVis | ItemAmuletVis | 2 |
| ItemGirdleHover | ItemGirdleHover | ItemGirdleHover | 1 |
| ItemSpawnerEgg | ItemSpawnerEgg | ItemSpawnerEgg | 29 |
| ItemZombieBrain | ItemZombieBrain | ItemZombieBrain | 1 |
| ItemBathSalts | ItemBathSalts | ItemBathSalts | 1 |
| ItemCrystalEssence | ItemCrystalEssence | ItemCrystalEssence | 1 |
| ItemBucketDeath | ItemBucketDeath | ItemBucketDeath | 1 |
| ItemBucketPure | ItemBucketPure | ItemBucketPure | 1 |
| ItemHelmetFortress | ItemFortressArmor | ItemHelmetFortress | 1 |
| ItemChestplateFortress | ItemFortressArmor | ItemChestplateFortress | 1 |
| ItemLeggingsFortress | ItemFortressArmor | ItemLeggingsFortress | 1 |
| ItemEldritchObject | ItemEldritchObject | ItemEldritchObject | 5 |
| ItemHelmetVoid | ItemVoidArmor | ItemHelmetVoid | 1 |
| ItemChestplateVoid | ItemVoidArmor | ItemChestplateVoid | 1 |
| ItemLeggingsVoid | ItemVoidArmor | ItemLeggingsVoid | 1 |
| ItemBootsVoid | ItemVoidArmor | ItemBootsVoid | 1 |
| ItemShovelVoid | ItemVoidShovel | ItemShovelVoid | 1 |
| ItemPickVoid | ItemVoidPickaxe | ItemPickVoid | 1 |
| ItemAxeVoid | ItemVoidAxe | ItemAxeVoid | 1 |
| ItemSwordVoid | ItemVoidSword | ItemSwordVoid | 1 |
| ItemHoeVoid | ItemVoidHoe | ItemHoeVoid | 1 |
| ItemHelmetVoidFortress | ItemVoidRobeArmor | ItemHelmetVoidRobe | 1 |
| ItemChestplateVoidFortress | ItemVoidRobeArmor | ItemChestplateVoidRobe | 1 |
| ItemLeggingsVoidFortress | ItemVoidRobeArmor | ItemLeggingsVoidRobe | 1 |
| ItemSanitySoap | ItemSanitySoap | ItemSanitySoap | 1 |
| ItemSanityChecker | ItemSanityChecker | ItemSanityChecker | 1 |
| ItemBottleTaint | ItemBottleTaint | ItemBottleTaint | 1 |
| ItemHelmetCultistRobe | ItemCultistRobeArmor | ItemHelmetCultistRobe | 1 |
| ItemChestplateCultistRobe | ItemCultistRobeArmor | ItemChestplateCultistRobe | 1 |
| ItemLeggingsCultistRobe | ItemCultistRobeArmor | ItemLeggingsCultistRobe | 1 |
| ItemBootsCultist | ItemCultistBoots | ItemBootsCultist | 1 |
| ItemHelmetCultistPlate | ItemCultistPlateArmor | ItemHelmetCultistPlate | 1 |
| ItemChestplateCultistPlate | ItemCultistPlateArmor | ItemChestplateCultistPlate | 1 |
| ItemLeggingsCultistPlate | ItemCultistPlateArmor | ItemLeggingsCultistPlate | 1 |
| ItemHelmetCultistLeaderPlate | ItemCultistLeaderArmor | ItemHelmetCultistLeaderPlate | 1 |
| ItemChestplateCultistLeaderPlate | ItemCultistLeaderArmor | ItemChestplateCultistLeaderPlate | 1 |
| ItemLeggingsCultistLeaderPlate | ItemCultistLeaderArmor | ItemLeggingsCultistLeaderPlate | 1 |
| ItemSwordCrimson | ItemCrimsonSword | ItemSwordCrimson | 1 |
| ItemLootBag | ItemLootBag | ItemLootBag | 3 |
| ItemCompassStone | ItemCompassStone | ItemCompassStone | 1 |
| ItemPrimalCrusher | ItemPrimalCrusher | ItemPrimalCrusher | 1 |

### Предметы: meta в новые id

| Старое имя | meta | Название (en) | Новый id | Skip |
|---|---|---|---|---|
| WandCap | 0 | Iron Cap | iron_wand_cap |  |
| WandCap | 1 | Gold Cap | gold_wand_cap |  |
| WandCap | 2 | Charged Thaumium Cap | thaumium_wand_cap |  |
| WandCap | 3 | Copper Cap | copper_wand_cap |  |
| WandCap | 4 | Charged Silver Cap | silver_wand_cap |  |
| WandCap | 5 | Inert Silver Cap | inert_silver_wand_cap |  |
| WandCap | 6 | Inert Thaumium Cap | inert_thaumium_wand_cap |  |
| WandCap | 7 | Charged Void metal Cap | void_wand_cap |  |
| WandCap | 8 | Inert Void metal Cap | inert_void_wand_cap |  |
| WandRod | 0 | Greatwood Rod | greatwood_rod |  |
| WandRod | 1 | Obsidian Rod | obsidian_rod |  |
| WandRod | 2 | Silverwood Rod | silverwood_rod |  |
| WandRod | 3 | Icy Rod | icy_rod |  |
| WandRod | 4 | Quartz Rod | quartz_rod |  |
| WandRod | 5 | Reed Rod | reed_rod |  |
| WandRod | 6 | Blazing Rod | blazing_rod |  |
| WandRod | 7 | Bone Rod | bone_rod |  |
| WandRod | 50 | Greatwood Staff Core | greatwood_staff_core |  |
| WandRod | 51 | Obsidian Staff Core | obsidian_staff_core |  |
| WandRod | 52 | Silverwood Staff Core | silverwood_staff_core |  |
| WandRod | 53 | Icy Staff Core | icy_staff_core |  |
| WandRod | 54 | Quartz Staff Core | quartz_staff_core |  |
| WandRod | 55 | Reed Staff Core | reed_staff_core |  |
| WandRod | 56 | Blazing Staff Core | blazing_staff_core |  |
| WandRod | 57 | Bone Staff Core | bone_staff_core |  |
| WandRod | 100 | Staff Core of the Primal | primal_staff_core |  |
| FocusPouch |  | Focus Pouch | focus_pouch |  |
| FocusFire |  | Wand Focus: Fire | focus_fire |  |
| FocusShock |  | Wand Focus: Shock | focus_shock |  |
| FocusHellbat |  | Wand Focus: Nine Hells | focus_nine_hells |  |
| FocusFrost |  | Wand Focus: Frost | focus_frost |  |
| FocusTrade |  | Wand Focus: Equal Trade | focus_equal_trade |  |
| FocusExcavation |  | Wand Focus: Excavation | focus_excavation |  |
| FocusPortableHole |  | Wand Focus: Portable Hole | focus_portable_hole |  |
| FocusPech |  | Wand Focus: Pech's Curse | focus_pech |  |
| FocusWarding |  | Wand Focus: Warding | focus_warding |  |
| FocusPrimal |  | Wand Focus: Primal | focus_primal |  |
| ItemEssence | 0 | Glass Phial | glass_phial |  |
| ItemEssence | 1 | Phial of Essentia | essentia_phial |  |
| ItemManaBean |  | Mana Bean | mana_bean |  |
| ItemWispEssence |  | Ethereal Essence | wisp_essence |  |
| ItemResource | 0 | Alumentum | alumentum |  |
| ItemResource | 1 | Nitor | nitor |  |
| ItemResource | 2 | Thaumium Ingot | thaumium_ingot |  |
| ItemResource | 3 | Quicksilver | quicksilver |  |
| ItemResource | 4 | Magic Tallow | magic_tallow |  |
| ItemResource | 5 | Zombie Brain (defunct) |  | defunct: not in getSubItems and no recipe uses it, replaced by the separate ItemZombieBrain item |
| ItemResource | 6 | Amber | amber |  |
| ItemResource | 7 | Enchanted Fabric | enchanted_fabric |  |
| ItemResource | 8 | Vis Filter | vis_filter |  |
| ItemResource | 9 | Knowledge Fragment | knowledge_fragment |  |
| ItemResource | 10 | Mirrored Glass | mirrored_glass |  |
| ItemResource | 11 | Tainted Goo | tainted_goo |  |
| ItemResource | 12 | Taint Tendril | taint_tendril |  |
| ItemResource | 13 | Jar Label | jar_label |  |
| ItemResource | 14 | Salis Mundus | salis_mundus |  |
| ItemResource | 15 | Primal Charm | primal_charm |  |
| ItemResource | 16 | Void metal Ingot | void_metal_ingot |  |
| ItemResource | 17 | Void Seed | void_seed |  |
| ItemResource | 18 | Gold Coin | gold_coin |  |
| ItemShard | 0 | Air Shard | air_shard |  |
| ItemShard | 1 | Fire Shard | fire_shard |  |
| ItemShard | 2 | Water Shard | water_shard |  |
| ItemShard | 3 | Earth Shard | earth_shard |  |
| ItemShard | 4 | Order Shard | order_shard |  |
| ItemShard | 5 | Entropy Shard | entropy_shard |  |
| ItemShard | 6 | Balanced Shard | balanced_shard |  |
| ItemInkwell |  | Scribing Tools | scribing_tools |  |
| ItemThaumonomicon | 42 |  | thaumonomicon_cheat |  |
| ItemThaumonomicon | 0 | Thaumonomicon | thaumonomicon |  |
| ItemThaumometer |  | Thaumometer | thaumometer |  |
| ItemGoggles |  | Goggles of Revealing | goggles_of_revealing |  |
| ItemHelmetThaumium |  | Thaumium Helm | thaumium_helmet |  |
| ItemChestplateThaumium |  | Thaumium Chestplate | thaumium_chestplate |  |
| ItemLeggingsThaumium |  | Thaumium Leggings | thaumium_leggings |  |
| ItemBootsThaumium |  | Thaumium Boots | thaumium_boots |  |
| ItemShovelThaumium |  | Thaumium Shovel | thaumium_shovel |  |
| ItemPickThaumium |  | Thaumium Pickaxe | thaumium_pickaxe |  |
| ItemAxeThaumium |  | Thaumium Axe | thaumium_axe |  |
| ItemSwordThaumium |  | Thaumium Sword | thaumium_sword |  |
| ItemHoeThaumium |  | Thaumium Hoe | thaumium_hoe |  |
| ItemArcaneDoor |  | Arcane Door | arcane_door |  |
| ItemNugget | 0 | Iron Nugget |  | replaced by vanilla minecraft:iron_nugget (tag c:nuggets/iron), as in TC3 Reforged |
| ItemNugget | 1 | Copper Nugget | copper_nugget |  |
| ItemNugget | 2 | Tin Nugget | tin_nugget |  |
| ItemNugget | 3 | Silver Nugget | silver_nugget |  |
| ItemNugget | 4 | Lead Nugget | lead_nugget |  |
| ItemNugget | 5 | Quicksilver Drop | quicksilver_drop |  |
| ItemNugget | 6 | Thaumium Nugget | thaumium_nugget |  |
| ItemNugget | 7 | Void metal Nugget | void_metal_nugget |  |
| ItemNugget | 16 | Native Iron Cluster | native_iron_cluster |  |
| ItemNugget | 17 | Native Copper Cluster | native_copper_cluster |  |
| ItemNugget | 18 | Native Tin Cluster | native_tin_cluster |  |
| ItemNugget | 19 | Native Silver Cluster | native_silver_cluster |  |
| ItemNugget | 20 | Native Lead Cluster | native_lead_cluster |  |
| ItemNugget | 21 | Native Cinnabar Cluster | native_cinnabar_cluster |  |
| ItemNugget | 31 | Native Gold Cluster | native_gold_cluster |  |
| BootsTraveller |  | Boots of the Traveler | boots_traveller |  |
| ItemNuggetChicken |  | Chicken Nugget | chicken_nugget |  |
| ItemNuggetBeef |  | Beef Nugget | beef_nugget |  |
| ItemNuggetPork |  | Pork Nugget | pork_nugget |  |
| ItemNuggetFish |  | Fish Nugget | fish_nugget |  |
| TripleMeatTreat |  | Triple Meat Treat | triple_meat_treat |  |
| ItemSwordElemental |  | Sword of the Zephyr | elemental_sword |  |
| ItemShovelElemental |  | Shovel of the Earthmover | elemental_shovel |  |
| ItemPickaxeElemental |  | Pickaxe of the Core | elemental_pickaxe |  |
| ItemAxeElemental |  | Axe of the Stream | elemental_axe |  |
| ItemHoeElemental |  | Hoe of Growth | elemental_hoe |  |
| ItemChestplateRobe |  | Thaumaturge's Robe | robe_chestplate |  |
| ItemLeggingsRobe |  | Thaumaturge's Leggings | robe_leggings |  |
| ItemBootsRobe |  | Thaumaturge's Boots | robe_boots |  |
| ArcaneDoorKey | 0 | Iron Arcane Key | iron_arcane_key |  |
| ArcaneDoorKey | 1 | Gold Arcane Key | gold_arcane_key |  |
| HandMirror |  | Magic Hand Mirror | hand_mirror |  |
| HoverHarness |  | Thaumostatic Harness | hover_harness |  |
| BlockJarFilledItem |  | Jar of Essentia | filled_jar |  |
| BlockJarNodeItem |  | Node in a Jar | node_jar |  |
| TrunkSpawner |  | Traveling Trunk | traveling_trunk |  |
| ItemGolemPlacer | 0 | Straw Golem | straw_golem |  |
| ItemGolemPlacer | 1 | Wood Golem | wood_golem |  |
| ItemGolemPlacer | 2 | Tallow Golem | tallow_golem |  |
| ItemGolemPlacer | 3 | Clay Golem | clay_golem |  |
| ItemGolemPlacer | 4 | Flesh Golem | flesh_golem |  |
| ItemGolemPlacer | 5 | Stone Golem | stone_golem |  |
| ItemGolemPlacer | 6 | Iron Golem | iron_golem |  |
| ItemGolemPlacer | 7 | Thaumium Golem | thaumium_golem |  |
| ItemGolemCore | 0 | Fill | golem_core_fill |  |
| ItemGolemCore | 1 | Empty | golem_core_empty |  |
| ItemGolemCore | 2 | Gather | golem_core_gather |  |
| ItemGolemCore | 3 | Harvest | golem_core_harvest |  |
| ItemGolemCore | 4 | Guard | golem_core_guard |  |
| ItemGolemCore | 5 | Decanting | golem_core_decanting |  |
| ItemGolemCore | 6 | Alchemy | golem_core_alchemy |  |
| ItemGolemCore | 7 | Chop | golem_core_chop |  |
| ItemGolemCore | 8 | Use | golem_core_use |  |
| ItemGolemCore | 9 | Butcher | golem_core_butcher |  |
| ItemGolemCore | 10 | Sorting | golem_core_sorting |  |
| ItemGolemCore | 11 | Fishing | golem_core_fishing |  |
| ItemGolemCore | 100 | Blank | golem_core_blank |  |
| ItemGolemCore |  | Golem Animation Core |  | generic display name of all cores, per-core lang values only hold the mode word, covered by golem_core_* ids |
| ItemGolemUpgrade | 0 | Golem Upgrade: Air | golem_upgrade_air |  |
| ItemGolemUpgrade | 1 | Golem Upgrade: Earth | golem_upgrade_earth |  |
| ItemGolemUpgrade | 2 | Golem Upgrade: Fire | golem_upgrade_fire |  |
| ItemGolemUpgrade | 3 | Golem Upgrade: Water | golem_upgrade_water |  |
| ItemGolemUpgrade | 4 | Golem Upgrade: Order | golem_upgrade_order |  |
| ItemGolemUpgrade | 5 | Golem Upgrade: Entropy | golem_upgrade_entropy |  |
| GolemBell |  | Golemancer's Bell | golem_bell |  |
| ItemGolemDecoration | 0 | Top Hat | top_hat |  |
| ItemGolemDecoration | 1 | Spectacles | spectacles |  |
| ItemGolemDecoration | 2 | Bowtie | bowtie |  |
| ItemGolemDecoration | 3 | Fez | fez |  |
| ItemGolemDecoration | 4 | Dart Launcher | dart_launcher |  |
| ItemGolemDecoration | 5 | Visor | visor |  |
| ItemGolemDecoration | 6 | Iron Plating | iron_plating |  |
| ItemGolemDecoration | 7 | Mace Arm | mace_arm |  |
| ItemGolemDecoration |  | Accessory |  | generic tooltip title Accessory, covered by per-decoration ids |
| ItemBowBone |  | Bow of Bone | bone_bow |  |
| PrimalArrow | 0 | Air Arrow | air_arrow |  |
| PrimalArrow | 1 | Fire Arrow | fire_arrow |  |
| PrimalArrow | 2 | Water Arrow | water_arrow |  |
| PrimalArrow | 3 | Earth Arrow | earth_arrow |  |
| PrimalArrow | 4 | Order Arrow | order_arrow |  |
| PrimalArrow | 5 | Entropy Arrow | entropy_arrow |  |
| ItemResonator |  | Essentia Resonator | essentia_resonator |  |
| ItemBaubleBlanks | 0 | Mundane Amulet | mundane_amulet |  |
| ItemBaubleBlanks | 1 | Mundane Ring | mundane_ring |  |
| ItemBaubleBlanks | 2 | Mundane Belt | mundane_belt |  |
| ItemBaubleBlanks | 3 | Apprentices Ring of %TYPE | apprentice_ring_air |  |
| ItemAmuletRunic | 0 | Amulet of Runic Shielding | runic_shielding_amulet |  |
| ItemAmuletRunic | 1 | Amulet of Emergency Shielding | emergency_shielding_amulet |  |
| ItemRingRunic | 0 | Ring of Protection | protection_ring |  |
| ItemRingRunic | 1 | Ring of Runic Shielding | runic_shielding_ring |  |
| ItemRingRunic | 2 | Charged Ring of Shielding | charged_shielding_ring |  |
| ItemRingRunic | 3 | Revitalizing Ring of Shielding | revitalizing_shielding_ring |  |
| ItemGirdleRunic | 0 | Girdle of Runic Shielding | runic_shielding_girdle |  |
| ItemGirdleRunic | 1 | Kinetic Girdle of Shielding | kinetic_shielding_girdle |  |
| ItemAmuletVis | 0 | Vis Stone | vis_stone |  |
| ItemAmuletVis | 1 | Amulet of Vis Storage | vis_amulet |  |
| ItemGirdleHover |  | Thaumostatic Girdle | thaumostatic_girdle |  |
| ItemZombieBrain |  | Zombie Brain | zombie_brain |  |
| ItemBathSalts |  | Purifying Bath Salts | bath_salts |  |
| ItemCrystalEssence |  | Crystallized Essence | crystallized_essence |  |
| ItemBucketDeath |  | Bucket of Liquid Death | liquid_death_bucket |  |
| ItemBucketPure |  | Bucket of Purifying Fluid | purifying_fluid_bucket |  |
| ItemHelmetFortress |  | Thaumium Fortress Helm | fortress_helmet |  |
| ItemChestplateFortress |  | Thaumium Fortress Cuirass | fortress_chestplate |  |
| ItemLeggingsFortress |  | Thaumium Fortress Thigh Guards | fortress_leggings |  |
| ItemEldritchObject | 0 | Eldritch Eye | eldritch_eye |  |
| ItemEldritchObject | 1 | Crimson Rites | crimson_rites |  |
| ItemEldritchObject | 2 | Runed Tablet | runed_tablet |  |
| ItemEldritchObject | 3 | Primordial Pearl | primordial_pearl |  |
| ItemEldritchObject | 4 | Eldritch Obelisk Placer | eldritch_obelisk_placer |  |
| ItemHelmetVoid |  | Void Helm | void_helmet |  |
| ItemChestplateVoid |  | Void Chestplate | void_chestplate |  |
| ItemLeggingsVoid |  | Void Leggings | void_leggings |  |
| ItemBootsVoid |  | Void Boots | void_boots |  |
| ItemShovelVoid |  | Void Shovel | void_shovel |  |
| ItemPickVoid |  | Void Pickaxe | void_pickaxe |  |
| ItemAxeVoid |  | Void Axe | void_axe |  |
| ItemSwordVoid |  | Void Sword | void_sword |  |
| ItemHoeVoid |  | Void Hoe | void_hoe |  |
| ItemHelmetVoidFortress |  | Void Thaumaturge Hood | void_robe_helmet |  |
| ItemChestplateVoidFortress |  | Void Thaumaturge Robe | void_robe_chestplate |  |
| ItemLeggingsVoidFortress |  | Void Thaumaturge Leggings | void_robe_leggings |  |
| ItemSanitySoap |  | Sanitizing Soap | sanitizing_soap |  |
| ItemSanityChecker |  | Sanity Checker | sanity_checker |  |
| ItemBottleTaint |  | Bottled Taint | bottled_taint |  |
| ItemHelmetCultistRobe |  | Crimson Cult Hood | crimson_cult_hood |  |
| ItemChestplateCultistRobe |  | Crimson Cult Robe | crimson_cult_robe |  |
| ItemLeggingsCultistRobe |  | Crimson Cult Leggings | crimson_cult_leggings |  |
| ItemBootsCultist |  | Crimson Cult Boots | crimson_cult_boots |  |
| ItemHelmetCultistPlate |  | Crimson Cult Helm | crimson_cult_helm |  |
| ItemChestplateCultistPlate |  | Crimson Cult Chestplate | crimson_cult_chestplate |  |
| ItemLeggingsCultistPlate |  | Crimson Cult Greaves | crimson_cult_greaves |  |
| ItemHelmetCultistLeaderPlate |  | Crimson Praetor Helm | crimson_praetor_helm |  |
| ItemChestplateCultistLeaderPlate |  | Crimson Praetor Chestplate | crimson_praetor_chestplate |  |
| ItemLeggingsCultistLeaderPlate |  | Crimson Praetor Greaves | crimson_praetor_greaves |  |
| ItemSwordCrimson |  | Crimson Blade | crimson_blade |  |
| ItemLootBag | 0 | Common Treasure | common_loot_bag |  |
| ItemLootBag | 1 | Uncommon Treasure | uncommon_loot_bag |  |
| ItemLootBag | 2 | Rare Treasure | rare_loot_bag |  |
| ItemCompassStone |  | Sinister Lodestone | sinister_lodestone |  |
| ItemPrimalCrusher |  | Primal Crusher | primal_crusher |  |
| WandCasting |  | %CAP %ROD %OBJ | wand |  |
| BlockJarFilledItem | 3 | Void Jar of Essentia | filled_void_jar |  |
| ItemResearchNotes | 0 | Research Notes | research_notes |  |
| ItemResearchNotes | 64 | Discovery | discovery |  |
| ItemBaubleBlanks | 4 |  | apprentice_ring_earth |  |
| ItemBaubleBlanks | 5 |  | apprentice_ring_fire |  |
| ItemBaubleBlanks | 6 |  | apprentice_ring_water |  |
| ItemBaubleBlanks | 7 |  | apprentice_ring_order |  |
| ItemBaubleBlanks | 8 |  | apprentice_ring_entropy |  |
| ItemSpawnerEgg | 0 |  | brainy_zombie_spawn_egg |  |
| ItemSpawnerEgg | 1 |  | giant_brainy_zombie_spawn_egg |  |
| ItemSpawnerEgg | 2 |  | wisp_spawn_egg |  |
| ItemSpawnerEgg | 3 |  | fire_bat_spawn_egg |  |
| ItemSpawnerEgg | 4 |  | pech_spawn_egg |  |
| ItemSpawnerEgg | 5 |  | mind_spider_spawn_egg |  |
| ItemSpawnerEgg | 6 |  | eldritch_guardian_spawn_egg |  |
| ItemSpawnerEgg | 7 |  | eldritch_warden_spawn_egg |  |
| ItemSpawnerEgg | 8 |  | cultist_knight_spawn_egg |  |
| ItemSpawnerEgg | 9 |  | cultist_cleric_spawn_egg |  |
| ItemSpawnerEgg | 10 |  | cultist_leader_spawn_egg |  |
| ItemSpawnerEgg | 11 |  | cultist_portal_spawn_egg |  |
| ItemSpawnerEgg | 12 |  | eldritch_golem_spawn_egg |  |
| ItemSpawnerEgg | 13 |  | eldritch_crab_spawn_egg |  |
| ItemSpawnerEgg | 14 |  | inhabited_zombie_spawn_egg |  |
| ItemSpawnerEgg | 15 |  | thaumic_slime_spawn_egg |  |
| ItemSpawnerEgg | 16 |  | taint_spider_spawn_egg |  |
| ItemSpawnerEgg | 17 |  | taintacle_spawn_egg |  |
| ItemSpawnerEgg | 18 |  | taintacle_small_spawn_egg |  |
| ItemSpawnerEgg | 19 |  | taint_spore_spawn_egg |  |
| ItemSpawnerEgg | 20 |  | taint_spore_swarmer_spawn_egg |  |
| ItemSpawnerEgg | 21 |  | taint_swarm_spawn_egg |  |
| ItemSpawnerEgg | 22 |  | taint_chicken_spawn_egg |  |
| ItemSpawnerEgg | 23 |  | taint_cow_spawn_egg |  |
| ItemSpawnerEgg | 24 |  | taint_creeper_spawn_egg |  |
| ItemSpawnerEgg | 25 |  | taint_pig_spawn_egg |  |
| ItemSpawnerEgg | 26 |  | taint_sheep_spawn_egg |  |
| ItemSpawnerEgg | 27 |  | taint_villager_spawn_egg |  |
| ItemSpawnerEgg | 28 |  | taintacle_giant_spawn_egg |  |

### Части жезлов (`ConfigItems`)

- Колпачки (`WandCap`): iron, gold, thaumium, void, copper, silver. Медный и серебряный создаются после обнаружения слитков других модов (`postInit`).
- Стержни жезлов (`WandRod`): wood, greatwood, obsidian, blaze, ice, quartz, bone, reed, silverwood.
- Стержни посохов (`StaffRod`): greatwood, obsidian, blaze, ice, quartz, bone, reed, silverwood, primal, primal.

## Block entities

| Id в 1.7.10 | Класс |
|---|---|
| TileHole | TileHole |
| TileArcaneFurnace | TileArcaneFurnace |
| TileArcaneFurnaceNozzle | TileArcaneFurnaceNozzle |
| TileCrucible | TileCrucible |
| TileSiphon | TileAlembic |
| TileJar | TileJarFillable |
| TileJarVoid | TileJarFillableVoid |
| TileJarNode | TileJarNode |
| TileJarBrain | TileJarBrain |
| TileResearchTable | TileResearchTable |
| TileTable | TileTable |
| TileArcaneWorkbench | TileArcaneWorkbench |
| TileChestHungry | TileChestHungry |
| TileBellows | TileBellows |
| TileSensor | TileSensor |
| TileArcanePressurePlate | TileArcanePressurePlate |
| TileArcaneBore | TileArcaneBore |
| TileArcaneBoreBase | TileArcaneBoreBase |
| TileArcaneLamp | TileArcaneLamp |
| TileArcaneLampGrowth | TileArcaneLampGrowth |
| TileArcaneLampFertility | TileArcaneLampFertility |
| TileOwned | TileOwned |
| TileLifter | TileLifter |
| TileNitor | TileNitor |
| TileNode | TileNode |
| TileCrystal | TileCrystal |
| TileEldritchCrystal | TileEldritchCrystal |
| TileMirror | TileMirror |
| TileMirrorEssentia | TileMirrorEssentia |
| TilePedestal | TilePedestal |
| TileWandPedestal | TileWandPedestal |
| TileInfusionStone | TileInfusionMatrix |
| TileInfusionPillar | TileInfusionPillar |
| TileAlchemyFurnace | TileAlchemyFurnace |
| TileAlchemyFurnaceAdvanced | TileAlchemyFurnaceAdvanced |
| TileAlchemyFurnaceAdvancedNozzle | TileAlchemyFurnaceAdvancedNozzle |
| TileDeconstructionTable | TileDeconstructionTable |
| TilePurifyTotem | TileEtherealBloom |
| TileTube | TileTube |
| TileTubeValve | TileTubeValve |
| TileCentrifuge | TileCentrifuge |
| TileTubeFilter | TileTubeFilter |
| TileTubeBuffer | TileTubeBuffer |
| TileTubeRestrict | TileTubeRestrict |
| TileTubeOneway | TileTubeOneway |
| TileThaumatorium | TileThaumatorium |
| TileThaumatoriumTop | TileThaumatoriumTop |
| TileBrainbox | TileBrainbox |
| TileWarded | TileWarded |
| TileGrate | TileGrate |
| TileWardingStone | TileWardingStone |
| TileWardingStoneFence | TileWardingStoneFence |
| TileMagicBox | TileMagicBox |
| TileNodeStabilizer | TileNodeStabilizer |
| TileVisRelay | TileVisRelay |
| TileNodeEnergized | TileNodeEnergized |
| TileNodeConverter | TileNodeConverter |
| TileMagicWorkbenchCharger | TileMagicWorkbenchCharger |
| TileManaPod | TileManaPod |
| TileSpa | TileSpa |
| TileEldritchAltar | TileEldritchAltar |
| TileEldritchObelisk | TileEldritchObelisk |
| TileEldritchCap | TileEldritchCap |
| TileBanner | TileBanner |
| TileEldritchPortal | TileEldritchPortal |
| TileEssentiaReservoir | TileEssentiaReservoir |
| TileEssentiaCrystalizer | TileEssentiaCrystalizer |
| TileEldritchNothing | TileEldritchNothing |
| TileEldritchLock | TileEldritchLock |
| TileEldritchCrabSpawner | TileEldritchCrabSpawner |
| TileFocalManipulator | TileFocalManipulator |
| TileFluxScrubber | TileFluxScrubber |
| TileEldritchTrap | TileEldritchTrap |

## Сущности

| Мод-id | Имя | Класс | Дальность | Частота | Скорость | Яйцо (цвета) |
|---|---|---|---|---|---|---|
| 0 | SpecialItem | EntitySpecialItem | 64 | 20 | true |  |
| 1 | PermanentItem | EntityPermanentItem | 64 | 20 | true |  |
| 2 | FollowItem | EntityFollowingItem | 64 | 20 | false |  |
| 3 | AspectOrb | EntityAspectOrb | 120 | 20 | true |  |
| 4 | FallingTaint | EntityFallingTaint | 64 | 3 | true |  |
| 5 | Alumentum | EntityAlumentum | 64 | 20 | true |  |
| 6 | PrimalOrb | EntityPrimalOrb | 64 | 20 | true |  |
| 7 | FrostShard | EntityFrostShard | 64 | 20 | true |  |
| 8 | Dart | EntityDart | 64 | 20 | false |  |
| 9 | PrimalArrow | EntityPrimalArrow | 64 | 20 | false |  |
| 10 | PechBlast | EntityPechBlast | 64 | 20 | true |  |
| 11 | EldritchOrb | EntityEldritchOrb | 64 | 20 | true |  |
| 12 | BottleTaint | EntityBottleTaint | 64 | 20 | true |  |
| 13 | GolemOrb | EntityGolemOrb | 64 | 20 | true |  |
| 14 | ShockOrb | EntityShockOrb | 64 | 20 | true |  |
| 15 | ExplosiveOrb | EntityExplosiveOrb | 64 | 20 | true |  |
| 16 | Ember | EntityEmber | 64 | 20 | true |  |
| 17 | Golem | EntityGolemBase | 64 | 3 | true |  |
| 18 | TravelingTrunk | EntityTravelingTrunk | 64 | 3 | true |  |
| 19 | BrainyZombie | EntityBrainyZombie | 64 | 3 | true | FFC0FF / 008000 |
| 20 | GiantBrainyZombie | EntityGiantBrainyZombie | 64 | 3 | true | FFC0FF / 004000 |
| 21 | Wisp | EntityWisp | 64 | 3 | true | FFC0FF / FFFFFF |
| 22 | Firebat | EntityFireBat | 64 | 3 | true | FFC0FF / C00000 |
| 23 | Pech | EntityPech | 64 | 3 | true | FFC0FF / 400040 |
| 24 | MindSpider | EntityMindSpider | 64 | 3 | true | AAAAAA / 404040 |
| 25 | EldritchGuardian | EntityEldritchGuardian | 64 | 3 | true | 222222 / 404040 |
| 26 | EldritchWarden | EntityEldritchWarden | 64 | 3 | true | 552222 / 404040 |
| 27 | CultistKnight | EntityCultistKnight | 64 | 3 | true | FF5055 / 000080 |
| 28 | CultistCleric | EntityCultistCleric | 64 | 3 | true | FF5055 / 800000 |
| 29 | CultistLeader | EntityCultistLeader | 64 | 3 | true | FF5055 / 505050 |
| 30 | CultistPortal | EntityCultistPortal | 64 | 20 | false | FF5055 / FF50FF |
| 31 | EldritchGolem | EntityEldritchGolem | 64 | 3 | true | 555555 / 404040 |
| 32 | EldritchCrab | EntityEldritchCrab | 64 | 3 | true | 555555 / 550000 |
| 33 | InhabitedZombie | EntityInhabitedZombie | 64 | 3 | true | 557755 / 550000 |
| 34 | ThaumSlime | EntityThaumicSlime | 64 | 3 | true | FFC0FF / FF80FF |
| 35 | TaintSpider | EntityTaintSpider | 64 | 3 | true | FFC0FF / 404040 |
| 36 | Taintacle | EntityTaintacle | 64 | 3 | false | FFC0FF / 800080 |
| 37 | TaintacleTiny | EntityTaintacleSmall | 64 | 3 | false | FFC0FF / 800090 |
| 38 | TaintSpore | EntityTaintSpore | 64 | 20 | false | FFC0FF / 800070 |
| 39 | TaintSwarmer | EntityTaintSporeSwarmer | 64 | 20 | false | FFC0FF / 800060 |
| 40 | TaintSwarm | EntityTaintSwarm | 64 | 3 | true | FFC0FF / 800050 |
| 41 | TaintedChicken | EntityTaintChicken | 64 | 3 | true | FFC0FF / C0C0C0 |
| 42 | TaintedCow | EntityTaintCow | 64 | 3 | true | FFC0FF / 7E3A3B |
| 43 | TaintedCreeper | EntityTaintCreeper | 64 | 3 | true | FFC0FF / 00FF00 |
| 44 | TaintedPig | EntityTaintPig | 64 | 3 | true | FFC0FF / EF99EF |
| 45 | TaintedSheep | EntityTaintSheep | 64 | 3 | true | FFC0FF / 808080 |
| 46 | TaintedVillager | EntityTaintVillager | 64 | 3 | true | FFC0FF / 00FFFF |
| 47 | TaintacleGiant | EntityTaintacleGiant | 64 | 3 | false | FFC0FF / 808080 |
| 48 | SpecialItemGrate | EntityItemGrate | 64 | 20 | true |  |
| 49 | GolemBobber | EntityGolemBobber | 64 | 64 | false |  |

Жители деревень: `entWizardId = 190`, `entBankerId = 191` (`VillagerRegistry`, компоненты `ComponentWizardTower`, `ComponentBankerHome`).

## Меню и контейнеры

| GUI id | Container (сервер) | Gui (клиент) |
|---|---|---|
| 0 | ContainerGolem | GuiGolem |
| 1 | ContainerPech | GuiPech |
| 2 | ContainerTravelingTrunk | GuiTravelingTrunk |
| 3 | ContainerThaumatorium | GuiThaumatorium |
| 4 | нет | нет |
| 5 | ContainerFocusPouch | GuiFocusPouch |
| 6 | нет | нет |
| 7 | нет | нет |
| 8 | ContainerDeconstructionTable | GuiDeconstructionTable |
| 9 | ContainerAlchemyFurnace | GuiAlchemyFurnace |
| 10 | ContainerResearchTable | GuiResearchTable |
| 11 | нет | нет |
| 12 | нет | GuiResearchBrowser |
| 13 | ContainerArcaneWorkbench | GuiArcaneWorkbench |
| 14 | нет | нет |
| 15 | ContainerArcaneBore | GuiArcaneBore |
| 16 | ContainerHandMirror | GuiHandMirror |
| 17 | ContainerHoverHarness | GuiHoverHarness |
| 18 | ContainerMagicBox | GuiMagicBox |
| 19 | ContainerSpa | GuiSpa |
| 20 | ContainerFocalManipulator | GuiFocalManipulator |

## Рецепты

| Тип | Вызовов в коде | С разворачиванием числовых циклов | Вызовов в циклах по коллекции |
|---|---|---|---|
| Arcane shaped (`addArcaneCraftingRecipe`) | 84 | 104 | 0 |
| Arcane shapeless | 5 | 5 | 0 |
| Тигель (`addCrucibleRecipe`) | 49 | 54 | 0 |
| Инфузия (`addInfusionCraftingRecipe`) | 63 | 63 | 0 |
| Инфузионные зачарования | 24 | 24 | 0 |
| Бонусы переплавки (`addSmeltingBonus`) | 18 | 18 | 0 |
| Ванильные shaped (включая oredict) | 59 | 59 | 0 |
| Ванильные shapeless (включая oredict и NBT) | 14 | 32 | 1 |
| Ванильные плавки | 8 | 8 | 0 |

Часть вызовов лежит внутри циклов (краски, первичные аспекты, `Aspect.aspects.values()`), поэтому число рецептов в игре больше числа вызовов. Для портирования источник истины это сам код `ConfigRecipes.java`.

## Исследования

Вкладок: 6, исследований: 201.

| Вкладка | Исследований | Ключи |
|---|---|---|
| BASICS | 19 | ASPECTS, PECH, NODES, WARP, RESEARCH, KNOWFRAG, THAUMONOMICON, ORE, PLANTS, ENCHANT, NODETAPPER1, NODEPRESERVE, NODEJAR, NODETAPPER2, RESEARCHER1, DECONSTRUCTOR, RESEARCHER2, RESEARCHDUPE, CRIMSON |
| THAUMATURGY | 42 | BASICTHAUMATURGY, FOCUSFIRE, FOCUSFROST, FOCUSHELLBAT, FOCUSEXCAVATION, FOCUSWARDING, FOCUSSHOCK, FOCUSTRADE, FOCUSPORTABLEHOLE, FOCUSPOUCH, CAP_iron, CAP_gold, CAP_thaumium, CAP_copper, CAP_silver, ROD_wood, ROD_greatwood, ROD_reed, ROD_blaze, ROD_obsidian, ROD_ice, ROD_quartz, ROD_bone, ROD_silverwood, SCEPTRE, ROD_greatwood_staff, ROD_reed_staff, ROD_blaze_staff, ROD_obsidian_staff, ROD_ice_staff, ROD_quartz_staff, ROD_bone_staff, ROD_silverwood_staff, WANDPED, VISAMULET, WANDPEDFOC, NODESTABILIZER, NODESTABILIZERADV, VISPOWER, VISCHARGERELAY, FOCALMANIPULATION, VAMPBAT |
| ALCHEMY | 35 | PHIAL, CRUCIBLE, NITOR, ALUMENTUM, ALCHEMICALDUPLICATION, ALCHEMICALMANUFACTURE, ENTROPICPROCESSING, LIQUIDDEATH, BOTTLETAINT, THAUMIUM, PUREIRON, PUREGOLD, PURECOPPER, PURETIN, PURESILVER, PURELEAD, TRANSIRON, TRANSGOLD, TRANSCOPPER, TRANSTIN, TRANSSILVER, TRANSLEAD, TALLOW, ETHEREALBLOOM, BATHSALTS, SANESOAP, ARCANESPA, DISTILESSENTIA, JARLABEL, JARVOID, TUBES, TUBEFILTER, ESSENTIACRYSTAL, CENTRIFUGE, THAUMATORIUM |
| ARTIFICE | 50 | BASICARTIFACE, ARCANESTONE, GRATE, TABLE, ARCTABLE, RESTABLE, THAUMOMETER, PAVETRAVEL, PAVEWARD, GOGGLES, ARCANEEAR, SINSTONE, LEVITATOR, INFERNALFURNACE, BELLOWS, ENCHFABRIC, INFUSION, FLUXSCRUB, RUNICARMOR, RUNICCHARGED, RUNICHEALING, RUNICKINETIC, RUNICEMERGENCY, BANNERS, RUNICAUGMENTATION, BOOTSTRAVELLER, HOVERHARNESS, HOVERGIRDLE, MIRROR, MIRRORHAND, MIRRORESSENTIA, ARCANEBORE, ARCANELAMP, LAMPGROWTH, LAMPFERTILITY, BONEBOW, PRIMALARROW, ELEMENTALAXE, ELEMENTALPICK, ELEMENTALSWORD, ELEMENTALSHOVEL, ELEMENTALHOE, WARDEDARCANA, JARBRAIN, INFUSIONENCHANTMENT, ARMORFORTRESS, HELMGOGGLES, MASKGRINNINGDEVIL, MASKANGRYGHOST, MASKSIPPINGFIEND |
| GOLEMANCY | 39 | HUNGRYCHEST, GOLEMFETTER, TRAVELTRUNK, GOLEMSTRAW, GOLEMWOOD, GOLEMTALLOW, GOLEMCLAY, GOLEMFLESH, GOLEMSTONE, GOLEMIRON, GOLEMTHAUMIUM, GOLEMBELL, COREGATHER, COREFILL, COREEMPTY, CORESORTING, COREUSE, COREHARVEST, COREFISHING, CORELUMBER, COREGUARD, COREBUTCHER, CORELIQUID, COREALCHEMY, UPGRADEAIR, UPGRADEEARTH, UPGRADEFIRE, UPGRADEWATER, UPGRADEORDER, UPGRADEENTROPY, ADVANCEDGOLEM, TINYHAT, TINYGLASSES, TINYBOWTIE, TINYFEZ, TINYDART, TINYVISOR, TINYARMOR, TINYHAMMER |
| ELDRITCH | 16 | ELDRITCHMINOR, ELDRITCHMAJOR, OCULUS, ENTEROUTER, OUTERREV, PRIMPEARL, PRIMNODE, ADVALCHEMYFURNACE, PRIMALCRUSHER, SANITYCHECK, VOIDMETAL, ESSENTIARESERVOIR, CAP_void, ARMORVOIDFORTRESS, FOCUSPRIMAL, ROD_primal_staff |

## Звуки

`sounds.json`: событий 66, ссылок на файлы 111, `.ogg` на диске 111.

События: `heartbeat`, `spill`, `bubble`, `alembicknock`, `creak`, `squeek`, `golemironshoot`, `jar`, `swarm`, `swarmattack`, `fly`, `key`, `doorfail`, `cameraticks`, `cameraclack`, `pump`, `page`, `learn`, `write`, `erase`, `brain`, `crystal`, `wispdead`, `wisplive`, `wand`, `wandfail`, `rumble`, `ice`, `jacobs`, `hhoff`, `hhon`, `pech_idle`, `pech_trade`, `pech_dice`, `pech_hit`, `pech_death`, `pech_charge`, `shock`, `fireloop`, `zap`, `craftfail`, `craftstart`, `runicShieldEffect`, `runicShieldCharge`, `swing`, `wind`, `tool`, `gore`, `roots`, `tentacle`, `upgrade`, `whispers`, `monolith`, `infuser`, `infuserstart`, `egidle`, `egattack`, `egdeath`, `egscreech`, `crabclaw`, `crabdeath`, `crabtalk`, `chant`, `coins`, `urnbreak`, `evilportal`.

## Пакеты

| № | Пакет | Сторона получателя |
|---|---|---|
| 0 | PacketBiomeChange | CLIENT |
| 1 | PacketConfig | CLIENT |
| 2 | PacketMiscEvent | CLIENT |
| 3 | PacketSyncWipe | CLIENT |
| 4 | PacketSyncAspects | CLIENT |
| 5 | PacketSyncResearch | CLIENT |
| 6 | PacketSyncScannedItems | CLIENT |
| 7 | PacketSyncScannedEntities | CLIENT |
| 8 | PacketSyncScannedPhenomena | CLIENT |
| 9 | PacketResearchComplete | CLIENT |
| 10 | PacketAspectPool | CLIENT |
| 11 | PacketAspectDiscovery | CLIENT |
| 12 | PacketScannedToServer | SERVER |
| 13 | PacketAspectCombinationToServer | SERVER |
| 14 | PacketPlayerCompleteToServer | SERVER |
| 15 | PacketAspectPlaceToServer | SERVER |
| 16 | PacketRunicCharge | CLIENT |
| 17 | PacketBoreDig | CLIENT |
| 18 | PacketNote | CLIENT |
| 19 | PacketSyncWarp | CLIENT |
| 20 | PacketWarpMessage | CLIENT |
| 21 | PacketNote | SERVER |
| 22 | PacketItemKeyToServer | SERVER |
| 23 | PacketFocusChangeToServer | SERVER |
| 24 | PacketFlyToServer | SERVER |
| 25 | PacketFXBlockBubble | CLIENT |
| 26 | PacketFXBlockDig | CLIENT |
| 27 | PacketFXBlockSparkle | CLIENT |
| 28 | PacketFXBlockArc | CLIENT |
| 29 | PacketFXBlockZap | CLIENT |
| 30 | PacketFXEssentiaSource | CLIENT |
| 31 | PacketFXInfusionSource | CLIENT |
| 32 | PacketFXShield | CLIENT |
| 33 | PacketFXSonic | CLIENT |
| 34 | PacketFXWispZap | CLIENT |
| 35 | PacketFXZap | CLIENT |
| 36 | PacketFXVisDrain | CLIENT |
| 37 | PacketFXBeamPulse | CLIENT |
| 38 | PacketFXBeamPulseGolemBoss | CLIENT |

## Зелья, зачарования, биомы, измерения

### Зелья

| Класс | Цвет | Вредное |
|---|---|---|
| PotionFluxTaint | 0x663377 | да |
| PotionVisExhaust | 0x664477 | да |
| PotionInfectiousVisExhaust | 0x665577 | да |
| PotionUnnaturalHunger | 0x446633 | да |
| PotionWarpWard | 0xE0F2F7 | нет |
| PotionDeathGaze | 0x664433 | да |
| PotionBlurredVision | 0x808080 | да |
| PotionSunScorned | 0xF8D86A | да |
| PotionThaumarhia | 0x664477 | да |

### Зачарования

Регистрируются два: `EnchantmentHaste` (вес 3) и `EnchantmentRepair` (вес 2); id 150 и 151 в конфиге. Классы `EnchantmentPotency`, `EnchantmentFrugal`, `EnchantmentWandFortune` нигде не используются (см. «Не переносим»). Инфузионные зачарования жезлов и инструментов (`addInfusionEnchantmentRecipe`) это отдельная система рецептов.

### Биомы

| Класс | Название |
|---|---|
| BiomeGenEerie | Eerie |
| BiomeGenEldritch | Eldritch |
| BiomeGenMagicalForest | Magical Forest |
| BiomeGenTaint | Tainted Land |

Id по умолчанию в 1.7.10: biomeTaintID = 192, biomeMagicalForestID = 193, biomeEerieID = 194, biomeEldritchID = 195. Они динамические и в порте не нужны.

### Измерение

Outer Lands: `WorldProviderOuter`, `ChunkProviderOuter`, лабиринт (`MazeGenerator`, `Gen*`), id измерения `-42` (`Config.dimensionOuterId`).

## Решения по id для проверки

- Первичные аспекты названы по TC4 (`order`, `entropy`), TC3 использует `vis` и `dull`: `order_infused_stone`, `entropy_infused_stone`, `order_shard`, `entropy_shard`, `order_crystal_cluster`. Остальные общие id взяты из TC3 (`crucible`, `alembic`, `warded_jar`, `brain_jar`, `arcane_worktable`, `golem_*`, `top_hat` и т.д.).
- Meta, которые только меняют состояние, схлопнуты в blockstate: `golem_fetter` (meta 10, `active=true`), `item_grate` (meta 6, `closed=true`), `arcane_pressure_plate` (meta 3, `powered=true`), `table` (оси x и z), `research_table` и `research_table_part` (направление), `eldritch_obelisk` (meta 2, `part=top`), `loot_urn` и `loot_crate` (`tier`), двойные плиты (`type=double`).
- Знамя культа: один блок `banner` с состоянием `color`, ключи `tile.blockWoodenDevice.8.N.name` привязаны к нему. Цвет в оригинале лежит в `TileBanner`, поэтому вместо 16 отдельных блоков достаточно свойства или данных блока.
- Блок узла аур `aura_node` (blockAiry 0) и невидимые служебные блоки blockAiry названы по английским названиям. Блок для нитора назван `nitor_light`, чтобы не пересекаться с предметом `nitor` (ItemResource 1).
- Жезлы: один предмет `wand` (колпачок, стержень, жезл/скипетр/посох хранятся в NBT). Колпачки `*_wand_cap` и стержни `*_rod`, `*_staff_core` отдельные предметы по meta. Фокусы отдельные предметы `focus_*`, их апгрейды в данных.
- `thaumonomicon_cheat` (meta 42 книги) доступен только при `Config.allowCheatSheet` (по умолчанию выключено), это отладочный предмет. В id_map он сохранён, оставлять ли его решает человек.
- Самородки, кластеры и колпачки меди, олова, серебра и свинца в оригинале появляются в творческой вкладке только при наличии слитков других модов. `iron_nugget` пропущен (есть в ваниле), `copper_nugget` оставлен, хотя в 26.3 есть ванильный медный самородок.
- Яйца призыва: один предмет `ItemSpawnerEgg` с 29 meta разложен на `<entity>_spawn_egg`. Названия берутся из `entity.Thaumcraft.*.name`, ключей `item.*` для них нет.
- Ядра големов: в lang значение `item.ItemGolemCore.N.name` содержит только слово режима (`Fill`, `Gather`) без полного названия. Конвертеру ассетов нужно собрать название из общего `item.ItemGolemCore.name` и режима.
- Пустые мета-блоки `blockStoneDevice` 6 и 7, `blockEldritch` 6 и `blockMagicBox` помечены skip как недостижимые. Если при портировании генерации найдётся использование, поменять skip на id.

## Не переносим

| Что | Причина |
|---|---|
| `itemPhotoPlate` | Поле объявлено в `ConfigItems`, присваиваний 0, регистрации и класса нет. Камеры (`ThaumicCamera`) в этой версии тоже нет. |
| `itemFlyingCarpet` | Поле объявлено в `ConfigItems`, присваиваний 0, регистрации и класса нет. |
| `WorldGenSilverwoodTreesOld` | Старый генератор дерева, ссылок из остального кода: 0. Версия 4.2.3.2 вернула его только ради Thaumic Tinkerer (см. `changelog.txt`). |
| `EnchantmentPotency`, `EnchantmentFrugal`, `EnchantmentWandFortune` | Классы-призраки, ссылок снаружи: 0, 0, 0. Не регистрируются, из той же правки для Thaumic Tinkerer. |
| `thaumcraft/codechicken/core/launch/DepLoader` | Coremod-загрузчик зависимостей CodeChicken. Не нужен: зависимости в современных загрузчиках описываются в метаданных мода. |
| `truetyper` | Рендер TTF-шрифта для книги. Заменяется ванильным шрифтом `minecraft:uniform` (решение в PLAN.md). |
| Встроенный `thaumcraft/codechicken/lib` | Копия CodeChicken Lib (CCModel, OBJ, освещение, `Cuboid6`). Заменяется загрузчиком OBJ и ванильным рендером. |
| Код для Thaumic Tinkerer | Отдельных классов нет: единственный след это возвращённый мёртвый код выше (`changelog.txt`, 4.2.3.2) и API-вызовы в `thaumcraft/api`. |
| `blockMagicBox` / `TileMagicBox` / `ContainerMagicBox` | Блок зарегистрирован, но ни рецептов, ни генерации, ни творческой вкладки (`getSubBlocks` пуст), ни места, где он ставится. |
| Конфиг `Config.java` (id блоков, зелий, биомов, зачарований) | Числовые id не нужны в современных реестрах. Остаются только настройки, которые решаются в фазе конфигурации. |

### Записи id_map со skip

| Реестр | Старое имя | meta | Lang | Причина |
|---|---|---|---|---|
| blocks | blockStoneDevice | 6 | tile.blockStoneDevice.6.name | meta never placed: lang leftover, infusion pillar parts are metas 3 and 4 (damageDropped redirects them to blockCosmeticSolid 7 and 6) |
| blocks | blockStoneDevice | 7 | tile.blockStoneDevice.7.name | meta never placed: lang leftover, infusion pillar parts are metas 3 and 4 (damageDropped redirects them to blockCosmeticSolid 7 and 6) |
| blocks | blockMirror |  | tile.blockMirror.name | fallback name key with the same text as tile.blockMirror.0.name, covered by magic_mirror |
| blocks | blockMagicBox |  |  | unreachable: no recipe, worldgen, creative entry or placement site anywhere in the code (getSubBlocks is empty) |
| blocks | blockSecure |  | tile.blockSecure.name | orphan lang key: no block has this unlocalized name (blockWarded has none), TC3 leftover |
| blocks | blockWooden |  | tile.blockWooden.name | orphan lang key: no block has this unlocalized name, TC3 leftover |
| blocks | blockEldritch | 6 |  | meta unused: no lang key and no creation site (only a dim light and icon are defined) |
| items | ItemResource | 5 | item.ItemResource.5.name | defunct: not in getSubItems and no recipe uses it, replaced by the separate ItemZombieBrain item |
| items | ItemNugget | 0 | item.ItemNugget.0.name | replaced by vanilla minecraft:iron_nugget (tag c:nuggets/iron), as in TC3 Reforged |
| items | ItemGolemCore |  | item.ItemGolemCore.name | generic display name of all cores, per-core lang values only hold the mode word, covered by golem_core_* ids |
| items | ItemGolemDecoration |  | item.ItemGolemDecoration.name | generic tooltip title Accessory, covered by per-decoration ids |

## Сверка с PLAN.md

| Показатель | PLAN.md | Фактически | Итог |
|---|---|---|---|
| Блоки | 45 | 45 | совпадает |
| Предметы | 111 | 108 | расхождение -3 |
| Исследования | 201 | 201 | совпадает |
| Arcane shaped | 84 | 84 | совпадает |
| Arcane shapeless | 5 | 5 | совпадает |
| Тигель | 49 | 49 | совпадает |
| Инфузия | 63 | 63 | совпадает |
| Инфузионные зачарования | 24 | 24 | совпадает |
| Бонусы переплавки | 18 | 18 | совпадает |
| Классы | 942 | 942 | совпадает |

Пояснения к расхождениям:

- Предметов 111 в PLAN.md это число полей `public static Item` в `ConfigItems` (111). Зарегистрировано 108. Не зарегистрированы и нигде не создаются: `itemPhotoPlate`, `itemFlyingCarpet`, `itemBootsFortress`.
- Блоков 45 совпадает с числом полей `public static Block` (45). Число строк, содержащих `registerBlock` в файле, больше на 2 из-за вызова и объявления метода `registerBlocks`.
- Числа рецептов совпадают с числом вызовов `ThaumcraftApi.add*` в `ConfigRecipes`. Часть вызовов выполняется в циклах, реальное число рецептов в игре больше (см. таблицу «Рецепты»).
- 942 класса это все `.class` в jar, включая вложенные и анонимные (`$`). Java-файлов в декомпилате 859, из них 855 в `thaumcraft/` и 4 в `truetyper/`.
- PLAN.md упоминает «12 классов на `baubles/api`». В jar таких классов нет (`baubles.*` только в `dependancies.info` как внешняя зависимость), есть 7 классов предметов в `thaumcraft/common/items/baubles`, которые реализуют внешний `IBauble`.
- Звуков в PLAN.md 112, в `sounds.json` событий 66 и ссылок на файлы 111, `.ogg` на диске 111.
- Условные предметы: медные, оловянные, серебряные и свинцовые самородки/кластеры и колпачки (медь, серебро) показываются в творческой вкладке только при обнаружении слитков через oredict (`Config.foundCopperIngot` и т.п.). В `id_map.json` они сохранены, решение по ним нужно принять отдельно.

