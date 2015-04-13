package com.github.fauu.helix.graphics.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Bloomlib allow easy but efficient way to add bloom effect as post process
 * effect
 * 
 * @author kalle_h
 * 
 */
public class Bloom {

  /**
   * To use implement bloom more like a glow. Texture alpha channel can be
   * used as mask which part are glowing and whic are not. see more info at:
   * http://www.gamasutra.com/view/feature/2107/realtime_glow.php
   * 
   * NOTE: need to be set before bloom instance is created. After that this
   * does nothing.
   */
  public static boolean useAlphaChannelAsMask = false;

  /** how many blur pass */
  public int blurPasses = 1;

  private ShaderProgram tresholdShader;
  private ShaderProgram bloomShader;

  private Mesh fullScreenQuad;

  private Texture pingPongTex1;
  private Texture pingPongTex2;
  private Texture original;

  private FrameBuffer frameBuffer;
  private FrameBuffer pingPongBuffer1;
  private FrameBuffer pingPongBuffer2;

  private ShaderProgram blurShader;

  private float bloomIntensity;
  private float originalIntensity;
  private float treshold;
  private int w;
  private int h;
  private boolean blending = true;
  private boolean capturing = false;
  private float r = 0;
  private float g = 0;
  private float b = 0;
  private float a = 0;
  private boolean disposeFBO = true;

  /**
   * IMPORTANT NOTE CALL THIS WHEN RESUMING
   */
  public void resume() {
    bloomShader.begin();
    {
      bloomShader.setUniformi("u_texture0", 0);
      bloomShader.setUniformi("u_texture1", 1);
    }
    bloomShader.end();

    setSize(w, h);
    setTreshold(treshold);
    setBloomIntesity(bloomIntensity);
    setOriginalIntesity(originalIntensity);

    original = frameBuffer.getColorBufferTexture();
    pingPongTex1 = pingPongBuffer1.getColorBufferTexture();
    pingPongTex2 = pingPongBuffer2.getColorBufferTexture();
  }

  /**
   * Initialize bloom class that capsulate original scene capturate,
   * tresholding, gaussian blurring and blending. Default values: depth = true
   * blending = false 32bits = true
   */
  public Bloom() {
    initialize(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4,
        null, true, false, true);
  }

  /**
   * Initialize bloom class that capsulate original scene capturate,
   * tresholding, gaussian blurring and blending.
   * 
   * @param FBO_W
   * @param FBO_H
   *            how big fbo is used for bloom texture, smaller = more blur and
   *            lot faster but aliasing can be problem
   * @param hasDepth
   *            do rendering need depth buffer
   * @param useBlending
   *            does fbo need alpha channel and is blending enabled when final
   *            image is rendered. This allow to combine background graphics
   *            and only do blooming on certain objects param use32bitFBO does
   *            fbo use higher precision than 16bits.
   */
  public Bloom(int FBO_W, int FBO_H, boolean hasDepth, boolean useBlending,
      boolean use32bitFBO) {
    initialize(FBO_W, FBO_H, null, hasDepth, useBlending, use32bitFBO);

  }

  /**
   * EXPERT FUNCTIONALITY. no error checking. Use this only if you know what
   * you are doing. Remember that bloom.capture() clear the screen so use
   * continue instead if that is a problem.
   * 
   * Initialize bloom class that capsulate original scene capturate,
   * tresholding, gaussian blurring and blending.
   * 
   * * @param sceneIsCapturedHere diposing is user responsibility.
   * 
   * @param FBO_W
   * @param FBO_H
   *            how big fbo is used for bloom texture, smaller = more blur and
   *            lot faster but aliasing can be problem
   * @param hasDepth
   *            do rendering need depth buffer
   * @param useBlending
   *            does fbo need alpha channel and is blending enabled when final
   *            image is rendered. This allow to combine background graphics
   *            and only do blooming on certain objects param use32bitFBO does
   *            fbo use higher precision than 16bits.
   */
  public Bloom(int FBO_W, int FBO_H, FrameBuffer sceneIsCapturedHere,
      boolean useBlending, boolean use32bitFBO) {

    initialize(FBO_W, FBO_H, sceneIsCapturedHere, true, useBlending,
        use32bitFBO);
    disposeFBO = false;
  }

  private void initialize(int FBO_W, int FBO_H, FrameBuffer fbo,
      boolean hasDepth, boolean useBlending, boolean use32bitFBO) {
    blending = useBlending;
    Format format = null;

    if (use32bitFBO) {
      if (useBlending) {
        format = Format.RGBA8888;
      } else {
        format = Format.RGB888;
      }

    } else {
      if (useBlending) {
        format = Format.RGBA4444;
      } else {
        format = Format.RGB565;
      }
    }
    if (fbo == null) {
      frameBuffer = new FrameBuffer(format, Gdx.graphics.getWidth(),
          Gdx.graphics.getHeight(), hasDepth);
    } else {
      frameBuffer = fbo;
    }

    pingPongBuffer1 = new FrameBuffer(format, FBO_W, FBO_H, false);

    pingPongBuffer2 = new FrameBuffer(format, FBO_W, FBO_H, false);

    original = frameBuffer.getColorBufferTexture();
    pingPongTex1 = pingPongBuffer1.getColorBufferTexture();
    pingPongTex2 = pingPongBuffer2.getColorBufferTexture();

    fullScreenQuad = createFullScreenQuad();
    final String alpha = useBlending ? "alpha_" : "";

    bloomShader = BloomShaderLoader.createShader("screenspace",
        alpha + "bloom");

    if (useAlphaChannelAsMask) {
      tresholdShader = BloomShaderLoader.createShader("screenspace",
          "maskedtreshold");
    } else {
      tresholdShader = BloomShaderLoader.createShader("screenspace",
          alpha + "treshold");
    }

    blurShader = BloomShaderLoader.createShader("blurspace",
        alpha + "gaussian");

    setSize(FBO_W, FBO_H);
    setBloomIntesity(2.5f);
    setOriginalIntesity(0.8f);
    setTreshold(0.5f);

    bloomShader.begin();
    {
      bloomShader.setUniformi("u_texture0", 0);
      bloomShader.setUniformi("u_texture1", 1);
    }
    bloomShader.end();
  }

  /**
   * Set clearing color for capturing buffer
   * 
   * @param r
   * @param g
   * @param b
   * @param a
   */
  public void setClearColor(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  /**
   * Call this before rendering scene.
   */
  public void capture() {
    if (!capturing) {
      capturing = true;
      frameBuffer.begin();
      Gdx.gl.glClearColor(r, g, b, a);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }
  }

  /**
   * Pause capturing to fbo.
   */
  public void capturePause() {
    if (capturing) {
      capturing = false;
      frameBuffer.end();
    }
  }

  /** Start capturing again after pause, no clearing is done to framebuffer */
  public void captureContinue() {
    if (!capturing) {
      capturing = true;
      frameBuffer.begin();
    }
  }

  /**
   * Call this after scene. Renders the bloomed scene.
   */
  public void render() {
    if (capturing) {
      capturing = false;
      frameBuffer.end();
    }

    Gdx.gl.glDisable(GL20.GL_BLEND);
    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
//    Gdx.gl.glDepthMask(false);

    gaussianBlur();

    if (blending) {
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    pingPongTex1.bind(1);
    original.bind(0);
    bloomShader.begin();
    {
      fullScreenQuad.render(bloomShader, GL20.GL_TRIANGLE_FAN);
    }
    bloomShader.end();

  }

  private void gaussianBlur() {

    // cut bright areas of the picture and blit to smaller fbo

    original.bind(0);
    pingPongBuffer1.begin();
    {
      tresholdShader.begin();
      {
        // tresholdShader.setUniformi("u_texture0", 0);
        fullScreenQuad.render(tresholdShader, GL20.GL_TRIANGLE_FAN, 0,
            4);
      }
      tresholdShader.end();
    }
    pingPongBuffer1.end();

    for (int i = 0; i < blurPasses; i++) {

      pingPongTex1.bind(0);

      // horizontal
      pingPongBuffer2.begin();
      {
        blurShader.begin();
        {
          blurShader.setUniformf("dir", 1f, 0f);
          fullScreenQuad.render(blurShader, GL20.GL_TRIANGLE_FAN, 0,
              4);
        }
        blurShader.end();
      }
      pingPongBuffer2.end();

      pingPongTex2.bind(0);
      // vertical
      pingPongBuffer1.begin();
      {
        blurShader.begin();
        {
          blurShader.setUniformf("dir", 0f, 1f);

          fullScreenQuad.render(blurShader, GL20.GL_TRIANGLE_FAN, 0,
              4);
        }
        blurShader.end();
      }
      pingPongBuffer1.end();
    }
  }

  /**
   * set intensity for bloom. higher mean more brightening for spots that are
   * over treshold
   * 
   * @param intensity
   *            multiplier for blurred texture in combining phase. must be
   *            positive.
   */
  public void setBloomIntesity(float intensity) {
    bloomIntensity = intensity;
    bloomShader.begin();
    {
      bloomShader.setUniformf("BloomIntensity", intensity);
    }
    bloomShader.end();
  }

  /**
   * set intensity for original scene. under 1 mean darkening and over 1 means
   * lightening
   * 
   * @param intensity
   *            multiplier for captured texture in combining phase. must be
   *            positive.
   */
  public void setOriginalIntesity(float intensity) {
    originalIntensity = intensity;
    bloomShader.begin();
    {
      bloomShader.setUniformf("OriginalIntensity", intensity);
    }
    bloomShader.end();
  }

  /**
   * Treshold for bright parts. everything under treshold is clamped to 0
   * 
   * @param treshold
   *            must be in range 0..1
   */
  public void setTreshold(float treshold) {
    this.treshold = treshold;
    tresholdShader.begin();
    {
      tresholdShader.setUniformf("treshold", treshold,
          1f / (1 - treshold));
    }
    tresholdShader.end();
  }

  private void setSize(int FBO_W, int FBO_H) {
    w = FBO_W;
    h = FBO_H;
    blurShader.begin();
    blurShader.setUniformf("size", FBO_W, FBO_H);
    blurShader.end();
  }

  /**
   * Call this when application is exiting.
   * 
   */
  public void dispose() {
    if (disposeFBO)
      frameBuffer.dispose();

    fullScreenQuad.dispose();

    pingPongBuffer1.dispose();
    pingPongBuffer2.dispose();

    blurShader.dispose();
    bloomShader.dispose();
    tresholdShader.dispose();

  }

  private Mesh createFullScreenQuad() {
    float[] verts = new float[16];// VERT_SIZE
    int i = 0;
    verts[i++] = -1; // x1
    verts[i++] = -1; // y1

    verts[i++] = 0f; // u1
    verts[i++] = 0f; // v1

    verts[i++] = 1f; // x2
    verts[i++] = -1; // y2

    verts[i++] = 1f; // u2
    verts[i++] = 0f; // v2

    verts[i++] = 1f; // x3
    verts[i++] = 1f; // y2

    verts[i++] = 1f; // u3
    verts[i++] = 1f; // v3

    verts[i++] = -1; // x4
    verts[i++] = 1f; // y4

    verts[i++] = 0f; // u4
    verts[i++] = 1f; // v4

    Mesh tmpMesh = new Mesh(true, 4, 0, new VertexAttribute(
        Usage.Position, 2, "a_position"), new VertexAttribute(
        Usage.TextureCoordinates, 2, "a_texCoord0"));

    tmpMesh.setVertices(verts);
    return tmpMesh;

  }

}
