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

package com.github.fauu.helix.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.github.fauu.helix.component.DisplayableComponent;
import com.github.fauu.helix.displayable.Displayable;
import com.github.fauu.helix.displayable.ModelDisplayable;

// TODO: Cache animation controllers
public class AnimationProcessingSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  public AnimationProcessingSystem() {
    super(Aspect.getAspectForAll(DisplayableComponent.class));
  }

  @Override
  protected void process(Entity e) {
    Displayable displayable = displayableMapper.get(e).get();

    if (displayable instanceof ModelDisplayable) {
      ((ModelDisplayable) displayable).getAnimationController()
                                      .update(Gdx.graphics.getDeltaTime());
    }
  }

}
