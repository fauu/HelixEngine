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

	private Vector3 tmpV1 = new Vector3();

	private Vector3 tmpV2 = new Vector3();

	@Override
	public void sort (Camera camera, Array<Renderable> renderables) {
		this.camera = camera;

		renderables.sort(this);
	}

	@Override
	public int compare (Renderable o1, Renderable o2) {
		o1.worldTransform.getScale(tmpV1);
    o2.worldTransform.getScale(tmpV2);

    if (tmpV1.len() > 20f) {
      return 1;
    } else if (tmpV2.len() > 20f) {
      return -1;
    } else {
      // This makes no sense but works for now
      float dst = (int) (1000f * camera.position.dst2(tmpV1)) -
                  (int)  (1000f * camera.position.dst2(tmpV2));

      return dst < 0 ? -1 : (dst > 0 ? 1 : 0);
    }
	}

}