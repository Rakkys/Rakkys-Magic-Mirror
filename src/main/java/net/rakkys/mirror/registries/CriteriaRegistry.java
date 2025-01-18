package net.rakkys.mirror.registries;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.Criteria;
import net.rakkys.mirror.advancement.MirrorTeleportCriterion;
import net.rakkys.mirror.advancement.MirrorUseCriterion;

public class CriteriaRegistry extends Criteria {
    public static MirrorUseCriterion MIRROR_USE = register(new MirrorUseCriterion());
    public static MirrorTeleportCriterion MIRROR_TELEPORT_DISTANCE = register(new MirrorTeleportCriterion());

    public static void register() {

    }
}
