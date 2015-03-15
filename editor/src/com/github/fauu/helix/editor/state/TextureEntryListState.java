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

package com.github.fauu.helix.editor.state;

import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.TextureEntry;
import com.github.fauu.helix.editor.event.TextureEntryListStateChangedEvent;

import java.util.LinkedList;
import java.util.List;

public class TextureEntryListState {
  
  private List<TextureEntry> data;
  
  public TextureEntryListState() {
    data = new LinkedList<>();
  }
  
  public void initialize(List<TextureEntry> entries) {
    data = entries;

    postChangeEventToUI();
  }
  
  public List<TextureEntry> getData() {
    return data;
  }
  
  public void setSelected(String name, boolean value) {
    for (TextureEntry te : data) {
      if (te.getName().equalsIgnoreCase(name)) {
        te.setSelected(value);
      } else {
        te.setSelected(!value);
      }
    }

    postChangeEventToWorld();
  }
  
  private void postChangeEventToUI() {
    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new TextureEntryListStateChangedEvent(data));
  }

  private void postChangeEventToWorld() {
    HelixEditor.getInstance()
               .getWorldEventBus()
               .post(new TextureEntryListStateChangedEvent(data));
  }

}