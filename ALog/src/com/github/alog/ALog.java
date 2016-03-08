package com.github.alog;

import android.util.Log;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class ALog {
    
    // Log Level
    public enum ALogLevel {
        TRACE(Level.TRACE, Log.VERBOSE),
        DEBUG(Level.DEBUG, Log.DEBUG),
        INFO(Level.INFO, Log.INFO),
        WARN(Level.WARN, Log.WARN),
        ERROR(Level.ERROR, Log.ERROR),
        ;
        
        private final Level level;
        private final int androidLevel;
        
        ALogLevel(Level level, int level2) {
            this.level = level;
            this.androidLevel = level2;
        }
        
        public Level getLevel() {
            return this.level;
        }
        
        public int getAndroidLevel() {
            return this.androidLevel;
        }
    } // enum
    
    private static final String TAG = ALog.class.getSimpleName();
    private static final boolean D = true;

    // <app>/asset/logback.xml
    private static final String ASSETS_DIR = "assets";
    private static final String AUTOCONFIG_FILE = "logback.xml";
    private static final String DEFAULT_PATH_LOGBACK_CONFIG =  ASSETS_DIR + "/" + AUTOCONFIG_FILE;

    private static final LoggerContext mLoggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private static final JoranConfigurator mConfig = new JoranConfigurator();
    private static final ALogger mALogger = ALogger.ALoggerFactory.getLogger(TAG);
    
    private static int mLogLevel = Log.DEBUG;
    private static ALogLevel mALogLevel = ALogLevel.DEBUG;
    private static boolean isUpdated = false;

    static {
        mLoggerContext.reset();
        mConfig.setContext(mLoggerContext);

        try {
            InputStream assetConfigXml = findConfigAsStream();

            if (assetConfigXml != null)
                mConfig.doConfigure(assetConfigXml);
            else {
                Log.w(TAG, "1. Fail load log back config!!!, Set default config");
                BasicLogcatConfigurator.configureDefaultContext();
            }
        } catch (JoranException e) {
            Log.w(TAG, "2. Fail load log back config!!!, Set default config");
            BasicLogcatConfigurator.configureDefaultContext();
        }

        // PID
        MDC.put("PID", String.format("%5d", AUtil.myPid()));
        MDC.put("discriminator", AUtil.getMyAppName());
    }
    
    private ALog() { }

    private static InputStream findConfigAsStream() {
        return ALog.class.getClassLoader().getResourceAsStream(DEFAULT_PATH_LOGBACK_CONFIG);
    }

    /**
     *
     * @param inputStream : Inputstream of logback.xml of configuration file.
     */
    public static void init(InputStream inputStream) {
        config(inputStream, AUtil.myPid(), AUtil.getMyAppName());
    }

    public static void init(String path) {
        config(path, AUtil.myPid(), AUtil.getMyAppName());
    }

    public static void config(InputStream inputStream, int pid, String appName) {

        mLoggerContext.reset();
        mConfig.setContext(mLoggerContext);

        try {
            mConfig.doConfigure(inputStream);
        } catch (JoranException e) {
            Log.e(TAG, "Fail load log back config!!!, Set default config");
            BasicLogcatConfigurator.configureDefaultContext();
        }

        MDC.put("PID", String.format("%5d", pid));
        MDC.put("discriminator", appName);
    }

    public static void config(String configPath, int pid, String appName) {

        mLoggerContext.reset();
        mConfig.setContext(mLoggerContext);

        try {
            mConfig.doConfigure(configPath);
        } catch (JoranException e) {
            Log.e(TAG, "Fail load log back config!!!, Set default config");
            BasicLogcatConfigurator.configureDefaultContext();
        }

        MDC.put("PID", String.format("%5d", pid));
        MDC.put("discriminator", appName);
    }
    
    public static void updateLogLevel(int newLevel) {
        
        if (mLogLevel == newLevel)
            return;
        
        isUpdated = true;
        mLogLevel = newLevel;
        
        switch(mLogLevel) {
        case Log.VERBOSE:
            mALogLevel = ALogLevel.TRACE;
            break;
        case Log.DEBUG:
            mALogLevel = ALogLevel.DEBUG;
            break;
        case Log.INFO:
            mALogLevel = ALogLevel.INFO;
            break;
        case Log.WARN:
            mALogLevel = ALogLevel.WARN;
            break;
        case Log.ERROR:
            mALogLevel = ALogLevel.ERROR;
            break;
        default:
            break;
        }
    }
    
    public static int v(String msg) {
        return v(mALogger, TAG, msg);
    }
    
    public static int v(String msg, Throwable tr) {
        return v(mALogger, TAG, msg + '\n' + getStackTraceString(tr));
    }

    public static int d(String msg) {
        return d(mALogger, TAG, msg);
    }
    
    public static int d(String msg, Throwable tr) {
        return d(mALogger, TAG, msg + '\n' + getStackTraceString(tr));
    }

    public static int i(String msg) {
        return i(mALogger, TAG, msg);
    }
    
    public static int i(String msg, Throwable tr) {
        return i(mALogger, TAG, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(String msg) {
        return w(mALogger, TAG, msg);
    }
    
    public static int w(String msg, Throwable tr) {
        return w(mALogger, TAG, msg + '\n' + getStackTraceString(tr));
    }

    public static int e(String msg) {
        return e(mALogger, TAG, msg);
    }
    
    public static int e(String msg, Throwable tr) {
        return e(mALogger, TAG, msg + '\n' + getStackTraceString(tr));
    }
    
    public static int v(String tag, String msg) {
        println(ALogLevel.TRACE, null, tag, msg);
        return 0;
    }
    
    public static int v(String tag, String msg, Throwable tr) {
        println(ALogLevel.TRACE, null, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int d(String tag, String msg) {
        println(ALogLevel.DEBUG, null, tag, msg);
        return 0;
    }
    
    public static int d(String tag, String msg, Throwable tr) {
        println(ALogLevel.DEBUG, null, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int i(String tag, String msg) {
        println(ALogLevel.INFO, null, tag, msg);
        return 0;
    }
    
    public static int i(String tag, String msg, Throwable tr) {
        println(ALogLevel.INFO, null, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int w(String tag, String msg) {
        println(ALogLevel.WARN, null, tag, msg);
        return 0;
    }
    
    public static int w(String tag, String msg, Throwable tr) {
        println(ALogLevel.WARN, null, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int e(String tag, String msg) {
        println(ALogLevel.ERROR, null, tag, msg);
        return 0;
    }
    
    public static int e(String tag, String msg, Throwable tr) {
        println(ALogLevel.ERROR, null, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int v(ALogger logger, String tag, String msg) {
        println(ALogLevel.TRACE, logger, tag, msg);
        return 0;
    }
    
    public static int v(ALogger logger, String tag, String msg, Throwable tr) {
        println(ALogLevel.TRACE, logger, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int d(ALogger logger, String tag, String msg) {
        println(ALogLevel.DEBUG, logger, tag, msg);
        return 0;
    }
    
    public static int d(ALogger logger, String tag, String msg, Throwable tr) {
        println(ALogLevel.DEBUG, logger, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int i(ALogger logger, String tag, String msg) {
        println(ALogLevel.INFO, logger, tag, msg);
        return 0;
    }
    
    public static int i(ALogger logger, String tag, String msg, Throwable tr) {
        println(ALogLevel.INFO, logger, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int w(ALogger logger, String tag, String msg) {
        println(ALogLevel.WARN, logger, tag, msg);
        return 0;
    }
    
    public static int w(ALogger logger, String tag, String msg, Throwable tr) {
        println(ALogLevel.WARN, logger, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    public static int e(ALogger logger, String tag, String msg) {
        println(ALogLevel.ERROR, logger, tag, msg);
        return 0;
    }
    
    public static int e(ALogger logger, String tag, String msg, Throwable tr) {
        println(ALogLevel.ERROR, logger, tag, msg + '\n' + getStackTraceString(tr));
        return 0;
    }
    
    /**
     * Codes from AOSP
     * android/util/Log.java
     * 
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
    
    private static ALogger getLogger(ALogger logger, String tag) {
        if (logger == null)
            return ALogger.ALoggerFactory.getLogger(tag);
        else
            return logger;
    }
    
    private static void println(ALogLevel level, ALogger alogger, String tag, String msg) {
        
        Logger logger = ALog.getLogger(alogger, tag).getLogger();
        
        MDC.put("TID", String.format("%5d", android.os.Process.myTid()));
        
        if (isUpdated == true) {
            isUpdated = false;
            logger.setLevel(mALogLevel.getLevel());
        }
        
        switch(level) {
        case DEBUG:
            if (D)
                logger.debug(msg);
            break;
        case ERROR:
            if (D)
                logger.error(msg);
            break;
        case INFO:
            if (D)
                logger.info(msg);
            break;
        case TRACE:
            if (D)
                logger.trace(msg);
            break;
        case WARN:
            if (D)
                logger.warn(msg);
            break;
        default:
            break;
        }
    }
}