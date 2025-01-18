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
import net.rakkys.mirror.RakkysMagicMirror;

public class MirrorUseCriterion extends AbstractCriterion<MirrorUseCriterion.Conditions> {
    static final Identifier ID = new Identifier(RakkysMagicMirror.MOD_ID, "mirror_use");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(obj.get("useCount").getAsInt());
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, int useCount) {
        this.trigger(player, (conditions) -> conditions.matches(useCount));
    }

    public static class Conditions extends AbstractCriterionConditions {
        int useCount;

        public Conditions(int useCount) {
            super(ID, LootContextPredicate.EMPTY);
            this.useCount = useCount;
        }

        public static MirrorUseCriterion.Conditions create(int useCount) {
            return new Conditions(useCount);
        }

        public boolean matches(int useCount) {
            return useCount >= this.useCount;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject json = super.toJson(predicateSerializer);
            json.add("useCount", new JsonPrimitive(this.useCount));
            return json;
        }
    }
}
