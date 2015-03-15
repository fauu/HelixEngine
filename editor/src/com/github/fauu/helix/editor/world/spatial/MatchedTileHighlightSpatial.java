/*
 * Copyright (C) 2014, 2015 Helix Engine Developers 
 * (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.editor.world.spatial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.shader.ShaderAttribute;
import com.github.fauu.helix.spatial.Spatial;

public class MatchedTileHighlightSpatial extends Spatial 
                                         implements RenderableProvider {
  
  private static final Color COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);

  private static final float Z_OFFSET = 0.03f;

  private Renderable renderable;
  
  public MatchedTileHighlightSpatial() {
    renderable = new Renderable();
    
    renderable.material
        = new Material(new ColorAttribute(ColorAttribute.Diffuse, COLOR),
                       new BlendingAttribute(GL20.GL_SRC_ALPHA, 
                                             GL20.GL_ONE_MINUS_SRC_ALPHA),
                       new ShaderAttribute(3));
    renderable.meshPartOffset = 0;
    renderable.primitiveType = GL20.GL_TRIANGLES;
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
      default: throw new UnsupportedOperationException();
    }
    
    ready = true;
  }
  
  @Override
  public void getRenderables(Array<Renderable> renderables,
                             Pool<Renderable> pool) {
    renderables.add(renderable);
  }

}
