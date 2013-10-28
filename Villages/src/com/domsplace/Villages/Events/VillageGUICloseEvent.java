/*
 * Copyright 2013 Dominic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.EventBase;
import com.domsplace.Villages.GUI.VillagesFrame;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class VillageGUICloseEvent extends EventBase {
    private VillagesFrame frame;
    
    public VillageGUICloseEvent(VillagesFrame frame) {
        this.frame = frame;
    }
    
    public VillagesFrame getFrame() {return this.frame;}
}
