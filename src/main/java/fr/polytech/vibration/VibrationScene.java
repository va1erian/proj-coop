/*
 * Copyright (C) 2015 hadrien
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.polytech.vibration;

import com.hackoeur.jglm.Vec3;
import fr.polytech.utils.model.BasicOBJLoader;
import fr.polytech.utils.model.Model;
import fr.polytech.utils.model.ModelLoader;
import fr.polytech.utils.scene.AbstractScene;
import fr.polytech.utils.scene.StaticProp;

/**
 *
 * @author hadrien
 */
public class VibrationScene extends AbstractScene {

    private StaticProp staticPart;
    private StaticProp movingPart;
    
    public VibrationScene() {
        light.setPos(new Vec3(2.0f, 9.0f, 0));
    }
    
    @Override
    public void initResources() throws Exception {
        ModelLoader loader = new BasicOBJLoader();
        
        Model m = loader.loadModel("/models/plaque_vibrante1.obj");
        staticPart = new StaticProp(m);
        props.add(staticPart);
        
        m = loader.loadModel("/models/plaque_vibrante2.obj");
        movingPart = new StaticProp(m);
        props.add(movingPart);
        
    }

    @Override
    public void update(float dt) {
    }
    
}
