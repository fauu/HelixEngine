package com.github.fauu.helix.editor.world.spatial;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.manager.TextureManager;
import com.github.fauu.helix.shader.ShaderAttribute;
import com.github.fauu.helix.spatial.Spatial;
import com.github.fauu.helix.spatial.dto.TextureDTO;

public class TileToPaintSpatial extends Spatial implements RenderableProvider {

  private static final float Z_OFFSET = 0.02f;
  
  private Renderable renderable;
  
  public TileToPaintSpatial() {
    renderable = new Renderable();
    
    renderable.material
        = new Material(new TextureAttribute(TextureAttribute.Diffuse),
                       new ShaderAttribute(1));
    renderable.meshPartOffset = 0;
    renderable.primitiveType = GL20.GL_TRIANGLES;
  }

  @Override
  public void getRenderables(Array<Renderable> renderables,
      Pool<Renderable> pool) {
    renderables.add(renderable);
  }

  @Override
  public void update(UpdateType type, Object value) {
    ready = false;
    
    switch (type) {
      case POSITION:
        renderable.worldTransform
                  .setToTranslation(((Vector3) value).cpy()
                                                     .add(0.0f, 0.0f, Z_OFFSET));
        break;
      case ORIENTATION:
        // TODO: Implement me
        break;
      case GEOMETRY:
        Mesh newMesh = (Mesh) value;

        if (renderable.mesh == null || !renderable.mesh.equals(newMesh)) {
          renderable.mesh = newMesh.copy(true);
          renderable.meshPartSize = newMesh.getNumIndices();
        }
        break;
      case TEXTURE:
        TextureDTO textureDTO = (TextureDTO) value;
        
        Texture setTexture = textureDTO.getSetTexture();
        TextureRegion region = textureDTO.getRegion();

        Vector2 textureCoords 
            = new Vector2(region.getU(), region.getV());
        Vector2 textureScaling 
            = new Vector2((float) region.getRegionWidth() /
                                  setTexture.getWidth(),
                          (float) region.getRegionHeight() /
                                  setTexture.getHeight());
        
        Matrix3 uvTransformation
            = new Matrix3().translate(textureCoords)
                           .scale(textureScaling.x, -1 * textureScaling.y)
                           .translate(0, -1);
        
        renderable.mesh.transformUV(uvTransformation);
        renderable.material.set(new TextureAttribute(TextureAttribute.Diffuse,
                                region));
        break;
      default: throw new UnsupportedOperationException();
    }
    
    ready = true;
  }

}
