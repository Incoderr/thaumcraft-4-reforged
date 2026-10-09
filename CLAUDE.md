# Thaumcraft 4 Reforged

Порт Thaumcraft 4.2.3.5 (MC 1.7.10, Azanor) на Minecraft 26.3 (`main`), 1.21.1 и 1.19.2 (`mc/*`). Только для личного использования: логика, числа, тексты и ассеты оригинала переносятся 1:1. План и фазы: `PLAN.md`, инвентарь контента: `INVENTORY.md`.

## Статус

- Фаза 0 (инструменты и декомпиляция): готово, кроме GitHub-репозитория (отложен, `gh` не установлен, remote не настроен)
- Фаза 1 (каркас NeoForge 26.3.0.57-beta, швы `platform/`, луч через `client/geom`, свои слоты Baubles, `check_seams.py`): готово, кроме визуальной проверки в `runClient`
- Фаза 2 (48 аспектов, теги предметов и сущностей с генерацией из рецептов, мировые блоки, базовые предметы, оверлей аспектов по Shift, ванильные рецепты): готово, кроме визуальной проверки в `runClient`
- Фаза 3 (генерация мира, биомы через TerraBlender, деревня с волшебником и банкиром, урны, ящики, мешочки, лут в сундуках): готово, кроме визуальной проверки в `runClient`
- Фаза 4 (ноды и их генерация, жезлы, вис-сеть, стабилизаторы, трансдьюсер, пьедестал, банка для ноды, HUD виса, GameTest-ы): готово, кроме визуальной проверки в `runClient`
- Фаза 5 (знания игрока, сканирование, варп, 201 исследование, рецепты TC данными, исследовательский стол с гексами, Таумономикон, команды, GameTest-ы): готово, кроме визуальной проверки в `runClient`
- Следующая: ветка `mc/1.21.1` (M-core), затем фаза 6 (алхимия и эссенция)

## Окружение

- Java 25 / 21 / 17: `/opt/homebrew/opt/openjdk@{25,21,17}`, системной `java` нет
- Gradle: `export JAVA_HOME=/opt/homebrew/opt/openjdk@25`, сервер `tail -f cmds | ./gradlew runServer` (`cmds` в .gitignore, в `run/server.properties` `pause-when-empty-seconds=-1`)
- `reference/mc`, `reference/neo` пересобраны под NeoForge 26.3.0.57-beta (из `build/moddev/artifacts/minecraft-patched-*-sources.jar` и `neoforge-*-sources.jar`)
- Python 3 (`python3`), скрипты в `tools/` без внешних зависимостей
- Зависимости по версиям: таблица «Решения» в `PLAN.md`
- CI: `.github/workflows/build.yml` из TC3 (nightly-релиз `nightly-<mc>`, версия Java и MC из `gradle.properties`), заработает после фазы 1 и появления remote

## Справочники (в .gitignore)

- `Thaumcraft-1.7.10-4.2.3.5.jar` оригинал в корне
- `reference/decompiled/` декомпилят TC4 (Vineflower 1.12, MCP `stable_12-1.7.10`), ассеты оригинала в `reference/decompiled/assets/`; непереименованные SRG-имена в `reference/decompiled-unmapped.txt` (поля TC4, скопированные из кода 1.8, и безымянные в MCP параметры)
- `reference/mc1710/minecraft` MC 1.7.10 клиент (SpecialSource по `joined.srg` → Vineflower → MCP); `reference/mc1710/forge` исходники Forge/FML 10.13.4.1614 из userdev; `reference/mc1710/forge-patches/{forge,fml}` патчи Forge к ванильным классам в виде диффов (к декомпиляту не применены, читать рядом с ванильным классом)
- `reference/mc`, `reference/neo` исходники MC/NeoForge 26.3, `reference/1.21.1`, `reference/1.19.2` справочники бэкпортов (клонированы из TC3)
- `tools/cache/` загрузки: `1.7.10/` (client.jar, библиотеки, Forge universal/userdev, Baubles 1.0.1.10, SpecialSource, `mcp1710/*.csv`, `joined.srg`), `vineflower.jar`

## Инструменты

- `tools/fetch_1710.py` скачивает всё для 1.7.10 в `tools/cache/1.7.10` (идемпотентно)
- `tools/decompile.py mc1710|tc4` пересобирает `reference/mc1710` и `reference/decompiled`
- `tools/convert_recipes.py` разбирает `ConfigRecipes.java` TC4 мини-интерпретатором (циклы разворачиваются) в `crafting/ConfigRecipes.java` (генерируется, руками не править) и `tools/tc_recipes.json` (обычный крафт для `gen_phase5.py`, алиасы на рецепты фазы 2 в `ALIASES`)
- `tools/remap_srg.py <dirs> [--mappings] [--report]` SRG → MCP (`func_`, `field_`, `p_`)
- `tools/inventory.py` → `INVENTORY.md` и черновик `tools/id_map.json` (ручные правки в id_map сохраняются)
- `tools/id_map.json` старые регистрации TC4 с meta → новые id (`old`, `meta`, `lang`, `id`, `state`, `skip`)
- `tools/vanilla_ids.py` → `tools/vanilla_ids.json` ванильные id 1.7.10 (числовые, `id:meta`, поля `Blocks.*`/`Items.*`) → современные по профилям версий
- `tools/mcformat.py` профили версий 26.3/1.21.1/1.19.2 (из TC3)
- `tools/convert_assets.py` текстуры, звуки, `.obj`, lang (22 языка) → `src/main/resources/assets/thaumcraft`; шейдеры варпа откладываются в `tools/assets_pending/` до фазы 10
- `tools/convert_research.py` разбирает `ConfigResearch` TC4 и пишет `research/ConfigResearch.java` (201 исследование, варп, страницы, рецепты скипетра и рунного усиления с ключами `ConfigResearch.*`), руками не править

## Код-стайл

- Java: 4 пробела, без комментариев, как в TC3
- Python: 4 пробела, двойные кавычки, без комментариев, `pathlib`
- Коммиты на английском `feat:`/`fix:`, без строк соавторства

## Отступления от плана

- Ванильные id лежат отдельно в `tools/vanilla_ids.json`, а не в `id_map.json`: генерируются из DataFixer-ов 26.3 и не правятся руками
- Forge 1.7.10 в справочнике без наложенных патчей: исходники Forge и патчи-диффы раздельно (бинарные патчи требуют pack200/Java 8)
- Олово, серебро, свинец: есть только рецепт слиток → 9 самородков (условие на непустой `c:ingots/<металл>`); обратные рецепты и переплавка кластеров требуют результата-тега, в JSON невозможно, нужен свой тип рецепта
- Тотем из обсидиана: состояния `part`/`carving` как в TC3 вместо выбора текстуры по соседям в рендере; древний камень и скала выбирают текстуру случайным вариантом blockstate
- Блендинг аспектов (GL_ONE у большинства) в GUI не воспроизводится, все иконки рисуются обычной альфой
- Генерация мира: крупные постройки сдвинуты на −8 по X/Z (фича пишет только в радиусе 1 чанка); высоты берутся по `MOTION_BLOCKING` (аналог `getHeightValue` 1.7, `*_WG` на этапе фич не видит деревья). Курган требует ровной площадки 19×19, как в TC4, на рельефе 26.3 встречается реже
- Серебродрево вне магических биомов: `#c:is_magical` (кроме магического леса и порчи) + `windswept_forest` + `old_growth_birch_forest` вместо Forest Hills
- Сундуки кургана и хилтопа получают `minecraft:chests/simple_dungeon` один раз (в TC4 хилтоп бросал дважды, курган с шансом 1/5 добавлял бросок); TC-лут попадает туда через `loot_modifiers`
- Лут в ванильных сундуках: `rolls` как у ChestGenHooks 1.7.10, `minecraft:empty` с весом ванильной суммы 1.7.10, поэтому вероятность на бросок совпадает. Записи только для предметов, у которых `gen_resources` генерирует определение; отсутствующие подхватятся при перегенерации
- `TcLoot` (урны, ящики, мешочки): записи по id лениво, отсутствующие предметы пропускаются, качество снаряжения без предмета падает на ближайшее нижнее. Камень виса с случайным висом пропущен до фазы 8
- Деревня: в TC4 не больше одной башни и одного дома банкира на деревню (веса 15/25), в jigsaw-пулах лимитов нет, поэтому веса 1 в `houses`. Волшебник и банкир без рабочего места (`Xp: 1` в шаблоне держит профессию), волшебнику в фазе 5/7 добавить POI. Сделки: каждый уровень 1–5 берёт 2 из общего пула профессии (в TC4 один случайный трейд и новый после использования последнего)
- Биомы: веса регионов TerraBlender 3 (магический лес вместо леса и берёзового леса) и 1 (порча вместо равнин), синий вариант магического леса (`blueMagicalForest`) не поддержан, плотность травы и цветов по-современному
- Baubles: на 26.3 свои слоты по образцу Baubles 1.0.1.10 за фасадом `platform/Baubles`. Curios `17.0.0-beta.2+26.3` собран под NeoForge 26.3.0.7 и падает на 26.3.0.37+ (`ModConfig.Type.COMMON` удалён в FML 12.0.8)
- Знания игрока в attachment `research/Knowledge` (копируется при смерти целиком, как TC4 с данными по имени игрока), вместо файлов `.thaum`; ключи сканирования строками (id предмета, id сущности + суффиксы TC4, `NODE<id>`) вместо хешей, наборы с префиксом `#` не переносятся
- Сервер проверяет то, чему TC4 верил с клиента: цель и дистанцию скана, пул и `secondary` при покупке, наличие аспекта и гекса при размещении, оба аспекта при комбинировании
- Константы конфига исследований (`research_difficulty = 0`, `aspect_total_cap = 100`, `allowCheatSheet = false`, wuss mode) в `research/ResearchConfig` до `ModConfigSpec` (14)
- События варпа и выдача `ELDRITCHMINOR`/`ELDRITCHMAJOR`/`@BATHSALTS` в фазе 10 (пока `/thaumcraft research`), сферы аспектов с мобов и аспект-орб у первичного амулета в фазе 11, руны скана таумометра и искры у полки в фазе 15
- Рецепты TC переведены в данные в фазе 5 (план ставил их в 7): `crafting/TcRecipes` по ключам TC4, машины фаз 6/7 используют те же записи. Счётчики после разворота циклов: arcane 109, тигель 54, инфузия 63, инфузионные зачарования 24, compound 9. Динамические `ArcaneWandRecipe`, `ArcaneSceptreRecipe`, `InfusionRunicAugmentRecipe`, покраска роб в фазе 7; результаты инфузии с NBT (апгрейды големов, очки и маски) хранятся как центральный предмет с `data`; ярлыки банок (`JarLabel*`) без JSON до своего типа рецепта
- Бонусы окружения стола сохраняются с количеством (TC4 при загрузке сводил каждый к 1); кристаллы и банки в скане окружения пропускаются до фаз 9 и 6
- Тост «Research Completed!» ванильный (вверху справа, стопкой), заголовок захардкожен как в TC4 (ключ в фазе 14); линии карты и гексов вместо `glLineWidth(3)` + `GL_LINE_SMOOTH` рисуются полосой квадов `client/platform/GuiLines` (3 физических пикселя, сглаженные края, у гексов аддитивный `GUI_ADDITIVE`); BER стола рисует письменные принадлежности и заметку плоскими предметами
- Имя жезла: шаблон `item.thaumcraft.wand` разбирается на клиенте по плейсхолдерам `%CAP`/`%ROD`/`%OBJ` (порядок в языках разный), части через `item.Wand.*`
- Нода у храма: по стартам структур `desert_pyramid`/`jungle_temple`/`swamp_hut` в текущем чанке; `netherTotem` пуст (в TC4 тотем генерировался только в измерениях 0 и 1); чёрные списки измерений и биомов не перенесены, биомные эффекты нод выключены в Нижнем мире и Энде
- Видимость нод с таумометром: баг TC4 в `isVisibleTo` сохранён (конус всегда истинен, видно в пределах 48 блоков)
- Пьедестал перезарядки принимает только жезлы (амулет виса в фазе 8), без `Container` и воронок; энергезированная нода не реализует `Node`, как в TC4
- Рендер: анимации WAVE/CHARGE и фокус жезла, левая рука как правая (без зеркального сдвига 26.3), тексты на экране таумометра обычной альфой вместо аддитивной, вспышка на конце луча реле не перенесены (фазы 8 и 15), пульс луча реле приближённый

## Заметки

- Швы (`platform/`): `ModNetwork` (+ `client/platform/ClientNetwork`), `StackData`/`StackKey` (data components), `DataAttachments`/`DataKey` (attachments, сохраняются как `{value: ...}`), `Transfer`, `Teleport`, `BiomeEdit`, `Registration` (+ `TcToolMaterial`, `TcArmorMaterial`, `ArmorSlot`; топливо через компонент `COOKING_FUEL`), `Baubles`. Шов `SaveIO` на `main` пустой: BE используют ванильные `ValueInput`/`ValueOutput`
- Рендер FX: `WorldFx` (`client/platform`) отдаёт текстуру и blend, геометрия в `client/geom` пишет в `QuadSink`; `TcRenderTypes` ставят REPEAT-сэмплер (как GL_REPEAT в TC4)
- Новые пакеты регистрируются в `network/ModPayloads`, клиентские хендлеры в `client/ClientPayloadHandlers`; `/thaumcraft fx beam <цель> [цвет]` отладочная команда
- `onEquipped`/`onUnequipped` баублов вызываются только на сервере
- Аспекты: `aspect/Aspect` (enum 48, порядок TC4), `AspectList` (семантика TC4), компонент `AspectKeys.ASPECTS` на фиалах, эссенциях, манабобах, ярлыках. `ConfigAspects.java` генерируется `tools/convert_tags.py` (руками не править), неразрешённые цели (контент будущих фаз) молча пропускаются. Реестр собирается на сервере (`AspectSync`, `ServerStartedEvent` и `/reload`) и уходит клиенту `AspectTagsPayload`. Источники рецептов TC (тигель, arcane, инфузия) подключаются через `RecipeAspectSource.register` в порядке TC4
- Имена сущностей 1.7.10 → id: `tools/entity_ids.json` (в `id_map.json` нельзя: `inventory.py` перезаписывает всё, кроме `blocks`/`items`)
- Ресурсы: `python3 tools/gen_resources.py` полностью перезаписывает `src/generated/resources`; текстуры остаются под именами TC4. Самодельные ассеты лежат в `tools/assets_extra/` (копируются `convert_assets.py`)
- Еда только через `Registration.food` (Consumable/consume_effects запрещены вне `platform/`)
- Хуки на будущие фазы: `world/gen/NodeHooks` (ноды структур, храмов, дикие, тотем: 4; эльдрич-кольцо: 13), `world/biome/TaintDecorationHook` (10), `WorldGenConfig` → `ModConfigSpec` (14), спавнер виспа в хилтопе пуст до появления `thaumcraft:wisp` (11), `ManaBeanItem.awardAspectPool` (5), `ZombieBrainItem.applyWarp` (5/10), `InfusionStabiliser` у свечей (7), `SilverwoodTreeGenerator.setKnotPlacer` (4), генераторы деревьев в `world/tree/` для фазы 3
- `/thaumcraft aspects hand|item <предмет>|entity <цель>` печатает теги; `/thaumcraft worldgen mound|hilltop|greatwood|silverwood` ставит постройку в позиции источника (работает через `execute positioned` с консоли)
- Генераторы ресурсов фазы 3: `tools/gen_world.py`, `gen_biomes.py`, `gen_loot.py`, `gen_villages.py` (+ `gen_structures.py` для NBT деревни) вызываются из `gen_resources.main()` с `ctx` = модуль `gen_resources`; курган из `tools/convert_structures.py` → `src/main/resources/thaumcraft/structures/mound.txt`
- Тест генерации на сервере: `gamerule max_block_modifications`, `forceload add` (до 256 чанков), подсчёт блоков через `fill ... air replace <блок>`

- Числа оригинала (подробно в `INVENTORY.md`): 45 блоков, 108 предметов (`itemPhotoPlate`, `itemFlyingCarpet`, `itemBootsFortress` объявлены, но не созданы), 73 BE, 50 сущностей, 15 контейнеров, 39 пакетов, 201 исследование, 66 звуковых событий, 22 языка
- `id_map.json`: meta, меняющие только состояние, свёрнуты в blockstate (`state`), поэтому один id может встречаться в нескольких записях; уникальна пара (id, state). Проверка: `python3 tools/inventory.py --check`
- Первичные насыщенные камни и осколки названы по TC4 (`order_infused_stone`, `entropy_shard`), прочие общие с TC3 id взяты из TC3
- Lang собирается кодом в нескольких местах: имя жезла (`item.thaumcraft.wand` = `%%CAP %%ROD %%OBJ`), ядра големов (в ключе только режим), знамёна по цвету (ключ блока один, сейчас в нём последний цвет). Плейсхолдеры TC (`%CAP`, `%n`) в lang экранированы как `%%`, `%d` заменены на `%s`
- Битые строки в оригинальных lang (es_AR, es_MX, ca_ES, ru_RU строка 1) пропускаются конвертером
- `.mtl` у `.obj` без путей к текстурам (в оригинале абсолютные пути Windows), текстуры задаются в JSON-моделях
- Ванильные id: `vanilla_ids.json` (`flatten.get("id:meta") or names.get("id")`, затем `profiles[<версия>]`); блок `skull` по умолчанию `skeleton_skull`, предметы `skull:N` точные
- Медь, олово, серебро, свинец (самородки, кластеры, колпачки) в оригинале условны, записи в id_map есть
- Жезл: один предмет `thaumcraft:wand`, компоненты `wand_parts` (cap, rod, sceptre), `wand_vis` (centivis, ключи как NBT TC4), `wand_target` (позиция объекта при тапе); таблицы колпачков и стержней в `wand/WandRegistry`, хранение через `wand/Wands`, траты через `WandItem.consumeAllVis`. Тап: `WandItem.use` → `WandInteractable.onWandUse` ставит `wand_target` → `onUseTick` → `onUsingWandTick`
- Ноды: `node/NodeBlockEntity` (тип BE `node` на `aura_node`, `charged_obsidian_totem`, `silverwood_knot`), генерация `node/NodeGenerator`, хуки будущих фаз `node/NodeHooks`; вис-сеть `vis/VisNet` + `vis/VisNode`; триггеры жезла `wand/WandTriggers` (банка: `blockentity/NodeJarTrigger`)
- Special-модели предметов: `thaumcraft:wand`, `aura_node`, `node_jar` (`client/render/ItemRenderers`), `node_stabilizer` (`advanced`), `node_transducer`, `vis_relay` (`client/render/WorldRenderers`); OBJ через `ObjModel`, пайплайны без depth-теста для открытых нод в `ModRenderPipelines`
- Жезл и таумометр как `ItemRenderType` 1.7.10: `items/*.json` это `minecraft:select` по `display_context` (`legacy_special_item` в `gen_phase4.py`), поле `view` (`inventory`, `equipped`, `first_person`, `entity`), базовая модель без родителя; `client/render/LegacyItemView.apply` переводит позу 26.3 в пространство 1.7.10 (инвентарь с хелпером Forge), дальше числа рендерера TC4. Первое лицо таумометра с руками и показаниями скана: `ThaumometerHandRenderer` (`RenderHandEvent`)
- В 26.3 (SDL3) левая кнопка мыши `1`: проверять `InputConstants.MOUSE_BUTTON_LEFT`, а не `0`
- `/thaumcraft node place <pos> [тип] [модификатор]|info <pos>`, `/thaumcraft wand fill [игроки]|give <игроки> <колпачок> <стержень> [скипетр]`
- Исследования: модель `research/{ResearchItem,ResearchPage,ResearchCategories}`, генерируемый `ConfigResearch`; логика `ResearchManager` (`complete`, `completeWithSiblings`, `handlePlayerComplete`, подсказки `@KEY`), `Warp`, `ScanManager` (+ клиент `client/research/ThaumometerClient` через `ScanClientHooks`), заметки `ResearchNotes` + компонент `research_note` (заметка без компонента = неизвестная), гексы `HexGrid`. `ResearchGate.isComplete` теперь настоящая проверка
- Книга: `client/research/ResearchBrowserScreen` (карта), `ResearchPageScreen` (страницы), уведомления `ResearchNotifications` + `ResearchHud`, тост `ResearchToast`; общий код открывает книгу через `ResearchClientHooks`. Стол: `blockentity/ResearchTableBlockEntity`, `menu/ResearchTableMenu` (меню с позицией через шов `platform/Menus`), `client/screen/ResearchTableScreen`, tint `thaumcraft:research_note`
- `/thaumcraft research [list] | <игроки> list|all|reset|<ключ>`, `aspect <игроки> <аспект|all> <n>`, `warp <игроки> set|add <n> [perm|temp|sticky]` (по умолчанию липкий, как в TC4)
- GameTest-ы: `test/ThaumcraftGameTests` (функции в реестре `test_function`, площадка `thaumcraft:test/platform` из `tools/gen_structures.py`), запуск `./gradlew runGameTestServer`
