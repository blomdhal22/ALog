package com.github.alog;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.Logger;

public class ALogger {
    
    private Logger mLogger;
    
    private ALogger(Logger logger) {
        mLogger = logger;
    }
    
    public Logger getLogger() {
        return mLogger;
    }
    
    // =======================================================================
    // Factory
    public static class ALoggerFactory {
        
        private static final Map<String, ALogger> mALoggerCache = new HashMap<String, ALogger>();
        
        public static ALogger getLogger(String name) {
            
            ALogger alogger = mALoggerCache.get(name);
            
            if (alogger != null)
                return alogger;
            
            alogger = new ALogger((Logger) LoggerFactory.getLogger(name));
            mALoggerCache.put(name, alogger);
            
            return alogger;
        }
        
        public static ALogger getLogger(Class<?> clazz) {
            return ALoggerFactory.getLogger(clazz.getSimpleName());
        }
    }
}