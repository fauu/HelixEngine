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

package com.github.fauu.helix.spatial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class AreaSpatial extends Spatial {

  private ModelInstance instance;

  public AreaSpatial(Model model) {
    instance = new ModelInstance(model);

    instance.transform.rotate(new Vector3(0, 0, 1), -180);
    instance.transform.rotate(new Vector3(1, 0, 0), 90);

    for (Material material : instance.materials) {
      TextureAttribute ta
          = (TextureAttribute) material.get(TextureAttribute.Diffuse);

      ta.textureDescription.magFilter = Texture.TextureFilter.Nearest;
      ta.textureDescription.minFilter = Texture.TextureFilter.Nearest;

      material.set(ta);
      material.set(ColorAttribute.createDiffuse(Color.WHITE));
    }

    ready = true;
  }

  @Override
  public void update(UpdateType type, Object value) { }

  @Override
  public void getRenderables(Array<Renderable> renderables,
                             Pool<Renderable> pool) {
    instance.getRenderables(renderables, pool);
  }
}
