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

package com.github.fauu.helix.editor.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.github.fauu.helix.component.DisplayableComponent;
import com.github.fauu.helix.datum.DisplayableUpdateRequest;
import com.github.fauu.helix.displayable.Displayable;

public class DisplayableIntermediary extends Manager {

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  public void setAreaDisplayableOpacity(float value) {
    Entity area = world.getManager(TagManager.class).getEntity("area");

    displayableMapper.get(area)
                     .requestUpdate(
                         new DisplayableUpdateRequest(Displayable.UpdateType.OPACITY,
                                                  value));
  }

}
