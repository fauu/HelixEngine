package com.github.fauu.helix.graphics.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class BloomShaderLoader {

  public static ShaderProgram createShader(String vertexName,
      String fragmentName) {

    String vertexShader = Gdx.files.internal(
        "shader/bloom/" + vertexName
            + ".vertex.glsl").readString();
    String fragmentShader = Gdx.files.internal(
        "shader/bloom/" + fragmentName
            + ".fragment.glsl").readString();
    ShaderProgram.pedantic = false;
    ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
    if (!shader.isCompiled()) {
      System.out.println(shader.getLog());
      Gdx.app.exit();
    } else
      Gdx.app.debug("shader compiled", shader.getLog());
    return shader;
  }
}
