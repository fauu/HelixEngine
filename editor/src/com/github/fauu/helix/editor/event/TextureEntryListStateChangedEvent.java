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

package com.github.fauu.helix.editor.event;

import com.github.fauu.helix.editor.datum.TextureEntry;

import java.util.List;

public class TextureEntryListStateChangedEvent {

  private List<TextureEntry> message;
  
  public TextureEntryListStateChangedEvent(List<TextureEntry> message) {
    this.message = message;
  }
  
  public List<TextureEntry> getMessage() {
    return message;
  }

}
