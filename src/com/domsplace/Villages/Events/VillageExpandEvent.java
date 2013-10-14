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

import com.domsplace.Villages.Bases.CancellableEvent;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      Dominic
 * @since       09/10/2013
 */
public class VillageExpandEvent extends CancellableEvent {
    private Village village;
    private List<Region> expandingRegions;
    private Resident expander;
    
    public VillageExpandEvent(Village village, List<Region> expandingRegions, Resident expander) {
        this.village = village;
        this.expandingRegions = new ArrayList<Region>(expandingRegions);
        this.expander = expander;
    }
    
    public Village getVillage() {return this.village;}
    public List<Region> getRegions() {return new ArrayList<Region>(expandingRegions);}
    public Resident getExpander() {return this.expander;}
}
