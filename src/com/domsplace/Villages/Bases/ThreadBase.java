package com.domsplace.Villages.Bases;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class ThreadBase extends BaseBase implements Runnable {
    private static final List<ThreadBase> THREADS = new ArrayList<ThreadBase>();
    
    //Static
    public static void stopAllThreads() {
        for(ThreadBase t : THREADS) {
            t.stopThread();
        }
    }
    
    public static void registerThread(ThreadBase thread) {
        debug("Thread: " + thread.getClass().getSimpleName() + " :Registered.");
        ThreadBase.getThreads().add(thread);
    }
    
    public static List<ThreadBase> getThreads() {
        return ThreadBase.THREADS;
    }
    
    //Instance
    private BukkitTask thread;
    
    public ThreadBase(long delay, long repeat) {
        this(delay, repeat, false);
    }
    
    public ThreadBase(long delay, long repeat, boolean async) {
        delay = delay * 20L;
        repeat = repeat * 20L;
        
        if(async) {
            this.thread = Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), this, delay, repeat);
        } else {
            this.thread = Bukkit.getScheduler().runTaskTimer(getPlugin(), this, delay, repeat);
        }
        
        ThreadBase.registerThread(this);
    }
    
    public BukkitTask getThread() {
        return this.thread;
    }
    
    public void stopThread() {
        if(this.thread == null) return;
        this.getThread().cancel();
    }

    @Override
    public void run() {
    }
}
