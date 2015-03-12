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

package com.github.fauu.helix;

import com.badlogic.gdx.Game;
import com.github.fauu.helix.screen.MainScreen;

public class HelixGame extends Game {

  private static HelixGame instance;

  @Override
  public void create () {
    instance = this;

    setScreen(new MainScreen());
  }

  public static HelixGame getInstance() {
    return instance;
  }

}
