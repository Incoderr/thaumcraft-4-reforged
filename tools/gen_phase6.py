import gen_phase4
from gen_phase5 import mc_box, copy_texture

NS = "thaumcraft"
FACINGS = gen_phase4.FACINGS
RUNIC_MATRIX_ITEM = {"type": f"{NS}:runic_matrix"}
VIS_CHARGE_RELAY_ITEM = {"type": f"{NS}:vis_charge_relay"}


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


def generate(ctx):
    arcane(ctx)
    infusion(ctx)
