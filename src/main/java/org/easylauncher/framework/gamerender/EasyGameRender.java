package org.easylauncher.framework.gamerender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.easylauncher.framework.gamerender.engine.*;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderGLException;
import org.easylauncher.framework.gamerender.engine.exception.shader.ShaderLoadException;
import org.easylauncher.framework.gamerender.engine.object.Material;
import org.easylauncher.framework.gamerender.engine.object.Texture;
import org.easylauncher.framework.gamerender.engine.scene.Scene;
import org.easylauncher.framework.gamerender.game.skin.entity.ArmEntity;
import org.easylauncher.framework.gamerender.game.skin.entity.BodyEntity;
import org.easylauncher.framework.gamerender.game.skin.entity.HeadEntity;
import org.easylauncher.framework.gamerender.game.skin.entity.LegEntity;
import org.easylauncher.framework.gamerender.game.skin.model.ArmModel;
import org.easylauncher.framework.gamerender.game.skin.model.BodyModel;
import org.easylauncher.framework.gamerender.game.skin.model.HeadModel;
import org.easylauncher.framework.gamerender.game.skin.model.LegModel;
import org.joml.Vector2f;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;

@Getter
@AllArgsConstructor
public class EasyGameRender implements AppLogic {

    private static final float MOUSE_SENSITIVITY = 0.05F;
    private static final float MOVEMENT_SPEED = 0.05F;

    public static void main(String[] args) throws ShaderGLException, ShaderLoadException {
        EasyGameRender instance = new EasyGameRender(new RenderOptions());
        Engine engine = new Engine("EasyGameRender Sample", new Window.Options().width(400).height(400), instance);
        engine.start();
    }

    private final RenderOptions renderOptions;

    @SneakyThrows
    @Override
    public void init(Window window, Scene scene, Render render) {
        Path skinTexturePath = Paths.get("examples").resolve("skins").resolve("mrcotek.png");
        try (InputStream resource = Files.newInputStream(skinTexturePath)) {
            Material material = new Material();
            material.setTexture(new Texture(resource));
            scene.setSkinMaterial(material);
        }

        boolean slim = false;

        HeadModel headModel = new HeadModel();
        BodyModel bodyModel = new BodyModel();
        ArmModel leftArmModel = new ArmModel(true, slim);
        ArmModel rightArmModel = new ArmModel(false, slim);
        LegModel leftLegModel = new LegModel(true);
        LegModel rightLegModel = new LegModel(false);

        scene.addModel(headModel);
        scene.addModel(bodyModel);
        scene.addModel(leftArmModel);
        scene.addModel(rightArmModel);
        scene.addModel(leftLegModel);
        scene.addModel(rightLegModel);

        HeadEntity headEntity = headModel.createEntity();
        BodyEntity bodyEntity = bodyModel.createEntity();
        ArmEntity leftArmEntity = leftArmModel.createEntity();
        ArmEntity rightArmEntity = rightArmModel.createEntity();
        LegEntity leftLegEntity = leftLegModel.createEntity();
        LegEntity rightLegEntity = rightLegModel.createEntity();

        scene.addEntity(headEntity);
        scene.addEntity(bodyEntity);
        scene.addEntity(leftArmEntity);
        scene.addEntity(rightArmEntity);
        scene.addEntity(leftLegEntity);
        scene.addEntity(rightLegEntity);

        scene.getCamera().moveBackwards(70F);
    }

    @Override
    public void input(Window window, Scene scene, long diffTime) {
//        float move = diffTime * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
//        if (window.isKeyPressed(GLFW_KEY_W)) {
//            camera.moveForward(move);
//        } else if (window.isKeyPressed(GLFW_KEY_S)) {
//            camera.moveBackwards(move);
//        }
//        if (window.isKeyPressed(GLFW_KEY_A)) {
//            camera.moveLeft(move);
//        } else if (window.isKeyPressed(GLFW_KEY_D)) {
//            camera.moveRight(move);
//        }
//        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
//            camera.moveUp(move);
//        } else if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT) || window.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
//            camera.moveDown(move);
//        }

        if (window.isKeyPressed(GLFW_KEY_TAB)) {
            MouseInput mouseInput = window.getMouseInput();
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation(
                    (float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY)
            );
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTime) {

    }

    @Override
    public void cleanup() {

    }

}
