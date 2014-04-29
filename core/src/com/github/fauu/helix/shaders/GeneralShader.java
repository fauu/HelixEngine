package com.github.fauu.helix.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GeneralShader extends BaseShader {

  protected final int u_projTrans = register(new Uniform("u_projTrans"));
  protected final int u_worldTrans = register(new Uniform("u_worldTrans"));

  public GeneralShader() {
    super();
  }

  @Override
  public void init() {
    super.init(program, null);
  }

  @Override
  public int compareTo(final Shader other) {
    return 0;
  }

  @Override
  public boolean canRender(final Renderable instance) {
    return true;
  }

  @Override
  public void begin(final Camera camera, final RenderContext context) {
    this.camera = camera;
    this.context = context;

    program.begin();
    set(u_projTrans, camera.combined);
  }

  @Override
  public void render(final Renderable renderable) {
    set(u_worldTrans, renderable.worldTransform);

    renderable.mesh.render(program, renderable.primitiveType, renderable.meshPartOffset,
        renderable.meshPartSize, true);
  }

  @Override
  public void end() {
    program.end();
  }

  @Override
  public void dispose() {
    super.dispose();
    program.dispose();
  }

  public void createProgram(final String vertex, final String fragment) {
    program = new ShaderProgram(vertex, fragment);
    if (!program.isCompiled()) {
      throw new GdxRuntimeException("Couldn't compile shader " + program.getLog());
    }
  }

}
