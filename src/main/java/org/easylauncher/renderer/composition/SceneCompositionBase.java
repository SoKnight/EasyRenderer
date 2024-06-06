package org.easylauncher.renderer.composition;

import lombok.Getter;
import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.engine.graph.Entity;
import org.easylauncher.renderer.engine.graph.Model;
import org.easylauncher.renderer.engine.scene.Scene;
import org.easylauncher.renderer.game.cape.CapeModel;
import org.easylauncher.renderer.game.skin.entity.ArmEntity;
import org.easylauncher.renderer.game.skin.model.ArmModel;
import org.easylauncher.renderer.game.skin.model.BodyModel;
import org.easylauncher.renderer.game.skin.model.HeadModel;
import org.easylauncher.renderer.game.skin.model.LegModel;

final class SceneCompositionBase implements SceneComposition {

    @Getter
    private final Scene scene;
    private final ArmEntity[] armEntities;

    public SceneCompositionBase(Scene scene, RenderOptions options, boolean capeSupported) {
        this.scene = scene;
        this.armEntities = new ArmEntity[2];

        Model[] models = constructModels(capeSupported);
        scene.addModels(models);

        int armEntityIndex = 0;
        for (Model model : models) {
            Entity entity = model.createEntity(options);
            scene.addEntity(entity);

            if (entity instanceof ArmEntity cast) {
                armEntities[armEntityIndex++] = cast;
            }
        }
    }

    @Override
    public void updateArmsThickness(boolean thinArms) {
        for (ArmEntity armEntity : armEntities) {
            armEntity.updateThickness(thinArms);
        }
    }

    private static Model[] constructModels(boolean capeSupported) {
        Model[] models = new Model[capeSupported ? 7 : 6];
        models[0] = new HeadModel();
        models[1] = new BodyModel();
        models[2] = new ArmModel(true);
        models[3] = new ArmModel(false);
        models[4] = new LegModel(true);
        models[5] = new LegModel(false);

        if (capeSupported)
            models[6] = new CapeModel();

        return models;
    }

}
