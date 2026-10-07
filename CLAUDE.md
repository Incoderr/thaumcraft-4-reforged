# Thaumcraft 4 Reforged

Порт Thaumcraft 4.2.3.5 (MC 1.7.10, Azanor) на Minecraft 26.3 (`main`), 1.21.1 и 1.19.2 (`mc/*`). Только для личного использования: логика, числа, тексты и ассеты оригинала переносятся 1:1. План и фазы: `PLAN.md`, инвентарь контента: `INVENTORY.md`.

## Статус

- Фаза 0 (инструменты и декомпиляция): готово, кроме GitHub-репозитория (отложен, `gh` не установлен, remote не настроен)
- Следующая: фаза 1 (каркас NeoForge 26.3 и швы `platform/`)

## Окружение

- Java 25 / 21 / 17: `/opt/homebrew/opt/openjdk@{25,21,17}`, системной `java` нет
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
- `tools/remap_srg.py <dirs> [--mappings] [--report]` SRG → MCP (`func_`, `field_`, `p_`)
- `tools/inventory.py` → `INVENTORY.md` и черновик `tools/id_map.json` (ручные правки в id_map сохраняются)
- `tools/id_map.json` старые регистрации TC4 с meta → новые id (`old`, `meta`, `lang`, `id`, `state`, `skip`)
- `tools/vanilla_ids.py` → `tools/vanilla_ids.json` ванильные id 1.7.10 (числовые, `id:meta`, поля `Blocks.*`/`Items.*`) → современные по профилям версий
- `tools/mcformat.py` профили версий 26.3/1.21.1/1.19.2 (из TC3)
- `tools/convert_assets.py` текстуры, звуки, `.obj`, lang (22 языка) → `src/main/resources/assets/thaumcraft`; шейдеры варпа откладываются в `tools/assets_pending/` до фазы 10

## Код-стайл

- Java: 4 пробела, без комментариев, как в TC3
- Python: 4 пробела, двойные кавычки, без комментариев, `pathlib`
- Коммиты на английском `feat:`/`fix:`, без строк соавторства

## Отступления от плана

- Ванильные id лежат отдельно в `tools/vanilla_ids.json`, а не в `id_map.json`: генерируются из DataFixer-ов 26.3 и не правятся руками
- Forge 1.7.10 в справочнике без наложенных патчей: исходники Forge и патчи-диффы раздельно (бинарные патчи требуют pack200/Java 8)
- Baubles: Curios на всех трёх версиях (под 26.3 есть beta), свои слоты не нужны

## Заметки

- Числа оригинала (подробно в `INVENTORY.md`): 45 блоков, 108 предметов (`itemPhotoPlate`, `itemFlyingCarpet`, `itemBootsFortress` объявлены, но не созданы), 73 BE, 50 сущностей, 15 контейнеров, 39 пакетов, 201 исследование, 66 звуковых событий, 22 языка
- `id_map.json`: meta, меняющие только состояние, свёрнуты в blockstate (`state`), поэтому один id может встречаться в нескольких записях; уникальна пара (id, state). Проверка: `python3 tools/inventory.py --check`
- Первичные насыщенные камни и осколки названы по TC4 (`order_infused_stone`, `entropy_shard`), прочие общие с TC3 id взяты из TC3
- Lang собирается кодом в нескольких местах: имя жезла (`item.thaumcraft.wand` = `%%CAP %%ROD %%OBJ`), ядра големов (в ключе только режим), знамёна по цвету (ключ блока один, сейчас в нём последний цвет). Плейсхолдеры TC (`%CAP`, `%n`) в lang экранированы как `%%`, `%d` заменены на `%s`
- Битые строки в оригинальных lang (es_AR, es_MX, ca_ES, ru_RU строка 1) пропускаются конвертером
- `.mtl` у `.obj` без путей к текстурам (в оригинале абсолютные пути Windows), текстуры задаются в JSON-моделях
- Ванильные id: `vanilla_ids.json` (`flatten.get("id:meta") or names.get("id")`, затем `profiles[<версия>]`); блок `skull` по умолчанию `skeleton_skull`, предметы `skull:N` точные
- Медь, олово, серебро, свинец (самородки, кластеры, колпачки) в оригинале условны, записи в id_map есть
