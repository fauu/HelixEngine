/*
 * Copyright (C) 2014-2016 Helix Engine Developers
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

  private Vector3 translation1 = new Vector3();
  private Vector3 translation2 = new Vector3();

	@Override
	public void sort(Camera camera, Array<Renderable> renderables) {
		this.camera = camera;

		renderables.sort(this);
	}

	@Override
	public int compare(Renderable r1, Renderable r2) {
		r1.worldTransform.getTranslation(translation1);
    r2.worldTransform.getTranslation(translation2);

		float z1 = translation1.z;
		float z2 = translation2.z;

		if (z1 < 0 && z2 > 0) {
			return -1;
		} else if (z1 > 0 && z2 < 0) {
			return 1;
		} else {
			if (z1 < z2) {
				return 1;
			} else if (z1 > z2) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}