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

package com.domsplace.VillagesGUI.Utilities;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author      Dominic
 * @since       04/10/2013
 */
public class NotificationUtilities {
    public static void msgBox(String title, String message) {
        msgBox(null, title, message);
    }
    
    public static void msgBox(JFrame frame, String title, String message) {
        msgBox(frame, title, message, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void msgBox(String title, String message, int warning) {
        msgBox(null, title, message, warning);
    }
    
    public static void msgBox(JFrame frame, String title, String message, int warning) {
        JOptionPane.showMessageDialog(frame,message, title, JOptionPane.WARNING_MESSAGE);
    }
}
