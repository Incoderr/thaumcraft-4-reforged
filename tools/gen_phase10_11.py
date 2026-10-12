import json

NS = "thaumcraft"


def write_damage_types(ctx):
    definitions = {
        "taint": ["minecraft:bypasses_armor", "neoforge:is_magic"],
        "dissolve": ["minecraft:bypasses_armor"],
        "tentacle": [],
        "swarm": [],
    }
    tag_values = {}
    for name, tags in definitions.items():
        ctx.write(
            ctx.DATA / NS / "damage_type" / f"{name}.json",
            {"exhaustion": 0.3, "message_id": f"{NS}.{name}", "scaling": "when_caused_by_living_non_player"},
        )
        for tag in tags:
            namespace, path = tag.split(":", 1)
            tag_path = ctx.DATA / namespace / "tags" / "damage_type" / f"{path}.json"
            current = json.loads(tag_path.read_text(encoding="utf-8")) if tag_path.exists() else {"replace": False, "values": []}
            values = current["values"]
            if f"{NS}:{name}" not in values:
                values.append(f"{NS}:{name}")
            current["values"] = sorted(values, key=lambda entry: entry if isinstance(entry, str) else entry["id"])
            ctx.write(tag_path, current)

    ctx.write(
        ctx.DATA / "minecraft" / "tags" / "entity_type" / "arthropod.json",
        {"replace": False, "values": [f"{NS}:eldritch_crab"]},
    )


def block(ctx, name, texture):
    ctx.cube_all(name, texture)
    ctx.simple_state(name)
    ctx.block_item(name)
    ctx.self_drop(name)


def cross(ctx, name, texture):
    ctx.block_model(name, {"parent": "minecraft:block/cross", "textures": {"cross": ctx.ref(texture, "block")}})
    ctx.simple_state(name)
    ctx.generated_item(name, texture, texture_kind="block")
    ctx.self_drop(name)


def fluid_item(ctx, item, texture):
    ctx.generated_item(item, texture)


def generate(ctx):
    write_damage_types(ctx)

    for name, texture in [
        ("crusted_taint", "taint_crust"),
        ("tainted_soil", "taint_soil"),
        ("flesh_block", "fleshblock"),
    ]:
        block(ctx, name, texture)

    for name, texture in [
        ("fibrous_taint", "taint_fibres"),
        ("tainted_grass", "taintgrass1"),
        ("tainted_plant", "taintgrass2"),
        ("spore_stalk", "taint_spore_stalk_1"),
        ("mature_spore_stalk", "taint_spore_stalk_2"),
    ]:
        cross(ctx, name, texture)

    for name, texture in [
        ("purifying_fluid_bucket", "bucket_pure"),
        ("liquid_death_bucket", "bucket_death"),
        ("bath_salts", "bath_salts"),
        ("sanitizing_soap", "soap"),
        ("bottled_taint", "bottle_taint"),
    ]:
        fluid_item(ctx, name, texture)

    for name in ["purifying_fluid", "liquid_death"]:
        ctx.block_model(name, {"parent": "minecraft:block/block", "textures": {"particle": "minecraft:block/water_still"}})
        ctx.simple_state(name)
