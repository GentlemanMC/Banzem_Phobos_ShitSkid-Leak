// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.manager;

import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.modules.client.HUD;
import me.mohalk.banzem.features.notifications.Notifications;
import java.util.ArrayList;

public class NotificationManager
{
    private final ArrayList<Notifications> notifications;
    
    public NotificationManager() {
        this.notifications = new ArrayList<Notifications>();
    }
    
    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            this.getNotifications().get(i).onDraw(posY);
            posY -= Banzem.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 5;
        }
    }
    
    public void addNotification(final String text, final long duration) {
        this.getNotifications().add(new Notifications(text, duration));
    }
    
    public ArrayList<Notifications> getNotifications() {
        return this.notifications;
    }
}
