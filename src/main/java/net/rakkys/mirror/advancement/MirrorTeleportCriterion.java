package net.rakkys.mirror.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.rakkys.mirror.RakkysMagicMirror;

import java.util.function.Predicate;

public class MirrorTeleportCriterion extends AbstractCriterion<MirrorTeleportCriterion.Conditions> {
    static final Identifier ID = new Identifier(RakkysMagicMirror.MOD_ID, "mirror_teleport");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(obj.get("distance").getAsDouble());
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, BlockPos startPos, BlockPos endPos) {
        this.trigger(player, (conditions -> conditions.matches(startPos, endPos)));
    }

    public static class Conditions extends AbstractCriterionConditions {
        double distance;

        public Conditions(double distance) {
            super(ID, LootContextPredicate.EMPTY);
            this.distance = distance;
        }

        public static MirrorTeleportCriterion.Conditions create(double distance) {
            return new Conditions(distance);
        }

        public boolean matches(BlockPos startPos, BlockPos endPos) {
            return !startPos.isWithinDistance(endPos, this.distance);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject json = super.toJson(predicateSerializer);
            json.add("distance", new JsonPrimitive(this.distance));
            return json;
        }
    }
}
