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

package com.github.fauu.helix.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class HelixRenderableSorter implements RenderableSorter,
                                              Comparator<Renderable> {
	private Camera camera;

	private Vector3 scale1 = new Vector3();
  private Vector3 position1 = new Vector3();

	private Vector3 scale2 = new Vector3();
  private Vector3 position2 = new Vector3();

	@Override
	public void sort (Camera camera, Array<Renderable> renderables) {
		this.camera = camera;

		renderables.sort(this);
	}

	@Override
	public int compare (Renderable o1, Renderable o2) {
		o1.worldTransform.getScale(scale1);
    o2.worldTransform.getScale(scale2);

    if (scale1.len() > 20f) {
      return 1;
    } else if (scale2.len() > 20f) {
      return -1;
    } else {
      // This makes no sense but works for now
      float dst = (int) (1000f * camera.position.dst2(scale1)) -
                  (int)  (1000f * camera.position.dst2(scale2));

      return dst < 0 ? -1 : (dst > 0 ? 1 : 0);
    }
	}

}