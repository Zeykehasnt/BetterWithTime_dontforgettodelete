package com.bwt.entities;

import com.bwt.utils.rectangular_entity.EntityRectDimensions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BwtEntities implements ModInitializer {
    public static final EntityType<WindmillEntity> windmillEntity = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("bwt", "windmill"),
            EntityType.Builder.create(
                    (EntityType.EntityFactory<WindmillEntity>) WindmillEntity::new,
                    SpawnGroup.MISC
            ).build()
    );
    public static final EntityType<WaterWheelEntity> waterWheelEntity = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("bwt", "water_wheel"),
            EntityType.Builder.create(
                (EntityType.EntityFactory<WaterWheelEntity>) WaterWheelEntity::new,
                SpawnGroup.MISC
            ).build()
    );
    public static final EntityType<MovingRopeEntity> movingRopeEntity = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("bwt", "moving_rope"),
            EntityType.Builder.create(
                    (EntityType.EntityFactory<MovingRopeEntity>) MovingRopeEntity::new,
                    SpawnGroup.MISC
            )
            .setDimensions(0.98f, 0.98f)
            .build()
    );
    public static final EntityType<BroadheadArrowEntity> broadheadArrowEntity = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("bwt", "broadhead_arrow"),
            EntityType.Builder.create(
                    (EntityType.EntityFactory<BroadheadArrowEntity>) BroadheadArrowEntity::new,
                    SpawnGroup.MISC
            )
            .setDimensions(0.5f, 0.5f)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
            .build()
    );
    public static final EntityType<DynamiteEntity> dynamiteEntity = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("bwt", "dynamite"),
            EntityType.Builder.create(
                    (EntityType.EntityFactory<DynamiteEntity>) DynamiteEntity::new,
                    SpawnGroup.MISC
            )
            .setDimensions(0.25f, 0.40f)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
            .build()
    );
            )
            .build()
    );

    @Override
    public void onInitialize() {
    }
}
