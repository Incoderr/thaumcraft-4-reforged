import gen_phase4
from gen_phase5 import mc_box, copy_texture

NS = "thaumcraft"
FACINGS = gen_phase4.FACINGS
RUNIC_MATRIX_ITEM = {"type": f"{NS}:runic_matrix"}
VIS_CHARGE_RELAY_ITEM = {"type": f"{NS}:vis_charge_relay"}
CENTRIFUGE_ITEM = {"type": f"{NS}:centrifuge"}
CRYSTALLIZER_ITEM = {"type": f"{NS}:essentia_crystallizer"}
RESERVOIR_ITEM = {"type": f"{NS}:essentia_reservoir"}
FILLED_JAR_ITEM = {"type": f"{NS}:filled_jar"}
ARM_DIRECTIONS = {"north": {}, "south": {"y": 180}, "east": {"y": 90}, "west": {"y": 270}, "down": {"x": 90}, "up": {"x": 270}}


def table_model(ctx, name, source):
    copy_texture(ctx, source, f"{name}_model")
    texture = ctx.ref(f"{name}_model")
    ctx.block_model(name, {
        "parent": "minecraft:block/block",
        "textures": {"particle": texture, "t": texture},
        "elements": [
            mc_box((-8, 0, -8), (16, 8, 16), (0, 0), (128, 64)),
            mc_box((-8, 12, -8), (16, 4, 16), (0, 32), (128, 64)),
            mc_box((3, 8, -7), (4, 4, 4), (72, 0), (128, 64)),
            mc_box((-7, 8, 3), (4, 4, 4), (72, 0), (128, 64)),
            mc_box((3, 8, 3), (4, 4, 4), (72, 0), (128, 64)),
            mc_box((-7, 8, -7), (4, 4, 4), (72, 0), (128, 64)),
        ],
    })
    ctx.simple_state(name)
    ctx.block_item(name)
    ctx.self_drop(name)
    ctx.mineable("axe", name)


def pedestal(ctx):
    def element(start, end, cull):
        faces = {"down": {"texture": "#top"}, "up": {"texture": "#top"}}
        for side in ["north", "south", "west", "east"]:
            faces[side] = {"texture": "#side"}
        for face in cull:
            faces[face]["cullface"] = face
        return {"from": start, "to": end, "faces": faces}

    ctx.block_model("arcane_pedestal", {
        "parent": "minecraft:block/block",
        "textures": {"particle": ctx.ref("pedestal_side"), "side": ctx.ref("pedestal_side"), "top": ctx.ref("pedestal_top")},
        "elements": [
            element([0, 0, 0], [16, 4, 16], ["down", "north", "south", "west", "east"]),
            element([4, 4, 4], [12, 12, 12], []),
            element([2, 12, 2], [14, 16, 14], ["up"]),
        ],
    })
    ctx.simple_state("arcane_pedestal")
    ctx.block_item("arcane_pedestal")
    ctx.self_drop("arcane_pedestal")
    ctx.mineable("pickaxe", "arcane_pedestal")


def arcane(ctx):
    table_model(ctx, "arcane_worktable", "worktable")
    table_model(ctx, "deconstruction_table", "decontable")

    gen_phase4.particle_model(ctx, "vis_charge_relay", "thaumiumblock")
    gen_phase4.item_base_particle(ctx, "vis_charge_relay", "thaumiumblock")
    gen_phase4.special_item(ctx, "vis_charge_relay", VIS_CHARGE_RELAY_ITEM)
    ctx.blockstate("vis_charge_relay", {"variants": {
        f"connected={connected},facing={facing}": {"model": ctx.ref("vis_charge_relay")}
        for facing in FACINGS
        for connected in ["false", "true"]
    }})
    ctx.self_drop("vis_charge_relay")
    ctx.mineable("pickaxe", "vis_charge_relay")


def infusion(ctx):
    for name in ["runic_matrix", "infusion_pillar", "infusion_pillar_top"]:
        gen_phase4.particle_model(ctx, name, "arcane_stone")
        ctx.simple_state(name)
        ctx.mineable("pickaxe", name)
    gen_phase4.item_base_particle(ctx, "runic_matrix", "arcane_stone")
    gen_phase4.special_item(ctx, "runic_matrix", RUNIC_MATRIX_ITEM)
    ctx.self_drop("runic_matrix")
    ctx.write_loot("infusion_pillar", [])
    ctx.write_loot("infusion_pillar_top", [])
    pedestal(ctx)


def cube_element(start, end, texture="#t", faces=None):
    names = faces or ["down", "up", "north", "south", "west", "east"]
    return {"from": [start] * 3, "to": [end] * 3, "faces": {face: {"texture": texture} for face in names}}


def arm_element(texture, extended, width=(7, 9)):
    low, high = width
    start = -6 if extended else 0
    return {
        "from": [low, low, start],
        "to": [high, high, 7 if low == 7 else low],
        "faces": {face: {"texture": texture} for face in ["down", "up", "north", "south", "west", "east"]},
    }


def tube_models(ctx):
    for name, texture in [("pipe_1", "pipe_1"), ("pipe_buffer", "pipe_buffer")]:
        for extended in [False, True]:
            suffix = "_long" if extended else ""
            ctx.block_model(f"essentia_arm_{name}{suffix}", {
                "parent": "minecraft:block/block",
                "textures": {"particle": ctx.ref(texture), "t": ctx.ref(texture)},
                "elements": [arm_element("#t", extended)],
            })
    cores = {
        "essentia_core_small": ("pipe_3", 6.5, 9.5),
        "essentia_core_big": ("pipe_2", 6, 10),
        "essentia_core_filter": ("pipe_filter", 5.5, 10.5),
        "essentia_core_restrict": ("pipe_restrict", 6.5, 9.5),
        "essentia_core_buffer": ("pipe_buffer", 4, 12),
    }
    for name, (texture, low, high) in cores.items():
        elements = [cube_element(low, high)]
        if name == "essentia_core_filter":
            elements.append(cube_element(6, 10, "#core"))
        ctx.block_model(name, {
            "parent": "minecraft:block/block",
            "textures": {"particle": ctx.ref(texture), "t": ctx.ref(texture), "core": ctx.ref("pipe_filter_core")},
            "elements": elements,
        })


def tube_state(ctx, name, arm, core_all=None, core_idle=None, core_arms=None):
    parts = []
    for direction, rotation in ARM_DIRECTIONS.items():
        for kind, suffix in [("normal", ""), ("extended", "_long")]:
            parts.append({"when": {direction: kind}, "apply": {"model": ctx.ref(f"essentia_arm_{arm}{suffix}"), **rotation}})
    if core_all:
        parts.append({"apply": {"model": ctx.ref(core_all)}})
    if core_idle:
        parts.append({"when": {direction: "none" for direction in ARM_DIRECTIONS}, "apply": {"model": ctx.ref(core_idle)}})
    if core_arms:
        parts.append({
            "when": {"OR": [{direction: "normal|extended"} for direction in ARM_DIRECTIONS]},
            "apply": {"model": ctx.ref(core_arms)},
        })
    ctx.blockstate(name, {"multipart": parts})


def tube_item(ctx, name, core, arm_texture="pipe_1", core_texture=None):
    particle = ctx.ref(core_texture or arm_texture)
    ctx.item_model(name, {
        "parent": "minecraft:block/block",
        "textures": {"particle": particle, "t": particle, "a": ctx.ref(arm_texture)},
        "elements": [
            {"from": [7, 0, 7], "to": [9, 16, 9], "faces": {face: {"texture": "#a"} for face in ["down", "up", "north", "south", "west", "east"]}},
            cube_element(*core),
        ],
    })
    ctx.item_definition(name, ctx.ref(name, "item"))


def essentia(ctx):
    tube_models(ctx)
    tube_state(ctx, "essentia_tube", "pipe_1", core_idle="essentia_core_big", core_arms="essentia_core_small")
    tube_state(ctx, "directional_essentia_tube", "pipe_1", core_idle="essentia_core_big", core_arms="essentia_core_small")
    tube_state(ctx, "essentia_valve", "pipe_1", core_all="essentia_core_big")
    tube_state(ctx, "filtered_essentia_tube", "pipe_1", core_all="essentia_core_filter")
    tube_state(ctx, "restricted_essentia_tube", "pipe_1", core_all="essentia_core_restrict")
    tube_state(ctx, "essentia_buffer", "pipe_buffer", core_all="essentia_core_buffer")
    tube_item(ctx, "essentia_tube", (6.5, 9.5), core_texture="pipe_3")
    tube_item(ctx, "directional_essentia_tube", (6.5, 9.5), core_texture="pipe_3")
    tube_item(ctx, "essentia_valve", (6, 10), core_texture="pipe_2")
    tube_item(ctx, "filtered_essentia_tube", (5.5, 10.5), core_texture="pipe_filter")
    tube_item(ctx, "restricted_essentia_tube", (6.5, 9.5), core_texture="pipe_restrict")
    tube_item(ctx, "essentia_buffer", (4, 12), arm_texture="pipe_buffer")
    for name in ["essentia_tube", "directional_essentia_tube", "essentia_valve", "filtered_essentia_tube", "restricted_essentia_tube", "essentia_buffer"]:
        ctx.self_drop(name)
        ctx.mineable("pickaxe", name)

    gen_phase4.particle_model(ctx, "alchemical_centrifuge", "thaumiumblock")
    ctx.simple_state("alchemical_centrifuge")
    gen_phase4.item_base_particle(ctx, "alchemical_centrifuge", "thaumiumblock")
    gen_phase4.special_item(ctx, "alchemical_centrifuge", CENTRIFUGE_ITEM)
    for name, texture, item in [
        ("essentia_crystallizer", "thaumiumblock", CRYSTALLIZER_ITEM),
        ("essentia_reservoir", "thaumiumblock", RESERVOIR_ITEM),
    ]:
        gen_phase4.particle_model(ctx, name, texture)
        ctx.blockstate(name, {"variants": {f"facing={facing}": {"model": ctx.ref(name)} for facing in FACINGS}})
        gen_phase4.item_base_particle(ctx, name, texture)
        gen_phase4.special_item(ctx, name, item)
    for name in ["alchemical_centrifuge", "essentia_crystallizer", "essentia_reservoir"]:
        ctx.self_drop(name)
        ctx.mineable("pickaxe", name)

    for name, top, side in [("warded_jar", "jar_top", "jar_side"), ("void_jar", "jar_top_void", "jar_side_void")]:
        def element(start, end):
            return {"from": start, "to": end, "faces": {
                "down": {"texture": "#bottom"}, "up": {"texture": "#top"},
                "north": {"texture": "#side"}, "south": {"texture": "#side"},
                "west": {"texture": "#side"}, "east": {"texture": "#side"},
            }}

        ctx.block_model(name, {
            "parent": "minecraft:block/block",
            "textures": {"particle": ctx.ref(side), "side": ctx.ref(side), "top": ctx.ref(top), "bottom": ctx.ref("jar_bottom")},
            "elements": [element([3, 0, 3], [13, 12, 13]), element([5, 12, 5], [11, 14, 11])],
        })
        ctx.simple_state(name)
        ctx.block_item(name)
    for name in ["filled_jar", "filled_void_jar"]:
        ctx.item_model(name, {"parent": ctx.ref("void_jar" if "void" in name else "warded_jar")})
        gen_phase4.special_item(ctx, name, FILLED_JAR_ITEM)


def generate(ctx):
    arcane(ctx)
    infusion(ctx)
    essentia(ctx)
