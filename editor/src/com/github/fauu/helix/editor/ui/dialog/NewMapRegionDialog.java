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

package com.github.fauu.helix.editor.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.fauu.helix.editor.HelixEditor;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class NewMapRegionDialog extends VisWindow {
  
  public NewMapRegionDialog() {
    super("New Map Region");

    setModal(true);
    
    closeOnEscape();
    
    row();
    
    // TODO: Add input validation
    VisTable fieldsetContainer = new VisTable(true);
    
    fieldsetContainer.add(new VisLabel("Dimensions:"));
    row();
    
    final VisTextField widthField = new VisTextField("64");
    widthField.setMaxLength(3);
    fieldsetContainer.add(widthField);
    
    fieldsetContainer.add(new VisLabel("x"));
    
    final VisTextField lengthField = new VisTextField("48");
    lengthField.setMaxLength(3);
    fieldsetContainer.add(lengthField);
    
    // TODO: Get rid of magic numbers. Here, there and everywhere.
    add(fieldsetContainer).padTop(3).spaceBottom(4);
    row();
    
    VisTable actionButtonsContainer = new VisTable(true);

    VisTextButton cancelButton = new VisTextButton("Cancel");
    cancelButton.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            fadeOut();
          }
        });
    actionButtonsContainer.add(cancelButton);

    VisTextButton createButton = new VisTextButton("Create");
    createButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
             HelixEditor.getInstance().closeCurrentMapRegion();
 
             HelixEditor.getInstance().createAndOpenMapRegion(
               Integer.parseInt(widthField.getText()), 
               Integer.parseInt(lengthField.getText()));
 
             fadeOut();
           }
        });
    actionButtonsContainer.add(createButton);
    
    add(actionButtonsContainer).right().padBottom(4);
    
    pack();
    centerWindow();
  }

}
