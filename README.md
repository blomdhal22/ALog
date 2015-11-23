ALog
====

Android Log using logback android.

Overview
--------

As fat jar, ALog wraped [`logback-android`][9] and [`slf4j-api`][10] and provide facade.

Please refer to https://github.com/tony19/logback-android for detailed information.

Dependency
-----------

* [`logback-android`][9]
* [`slf4j-api`][10]


Getting Started
----------------

1. Add ALog.jar to your project class path.
2. Configure ALog using ${project-root}/assets/logback.xml. By default, basic configuration.

*Example:*

```java
import com.github.alog.ALog;
import com.github.alog.ALogger;
import com.github.alog.ALogger.ALoggerFactory;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    
    private static String TAG = MainActivity.class.getSimpleName();
    private static ALogger LOG = ALoggerFactory.getLogger(TAG);
    private static boolean D = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (D) ALog.i(LOG, TAG, "onCreate()");
    }
}
```


Configuration Sample
---------------------

This is sample config for.

1. Basic Android Log
2. Store logfile in /data/data/(your package)/logs
3. 8MB per file. if over 8MB, compact as zip.
4. Maximum file zip file count 3.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">

  <property name="ROOT_DIR" value="/data/data/"/>

  <!-- Create a logcat appender -->
  <appender name="LOGCAT" class="ch.qos.logback.classic.android.LogcatAppender">
    <encoder>
      <pattern>%msg</pattern>
    </encoder>
  </appender>

  <appender name="FILE-PACKAGE" class="ch.qos.logback.classic.sift.SiftingAppender">
    <discriminator>
      <key>discriminator</key>
      <defaultValue>unknown</defaultValue>
    </discriminator>

    <sift>
      <appender name="FILE-TIME-${discriminator}" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${ROOT_DIR}/${discriminator}/logs/${discriminator}.log</file>

        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
          <fileNamePattern>${ROOT_DIR}/${discriminator}/logs/${discriminator}.log.%i.zip</fileNamePattern>
          <minIndex>1</minIndex>
          <maxIndex>3</maxIndex>
        </rollingPolicy>

        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
          <maxFileSize>8MB</maxFileSize>
        </triggeringPolicy>

        <encoder>
          <pattern>%date{MM-dd HH:mm:ss.SSS} %X{PID} %X{TID} %-5level %logger{36}: %msg%n</pattern>
        </encoder>
      </appender>
    </sift>
  </appender>

  <!-- Write INFO (and higher-level) messages to the log file -->
  <root level="DEBUG">
    <appender-ref ref="LOGCAT" />
    <appender-ref ref="FILE-PACKAGE" />
  </root>

</configuration>
```

 [1]: http://logback.qos.ch
 [3]: http://tony19.github.com/logback-android
 [4]: https://github.com/tony19/logback-android/wiki/Changelog
 [5]: http://logback.qos.ch/manual/index.html
 [6]: http://tony19.github.com/logback-android/doc/1.1.1-4/
 [9]: https://bitbucket.org/tony19/logback-android-jar/downloads/logback-android-1.1.1-4.jar
 [10]: http://search.maven.org/remotecontent?filepath=org/slf4j/slf4j-api/1.7.6/slf4j-api-1.7.6.jar
 [11]: https://github.com/tony19/logback-android/wiki/Appender-Notes
 [12]: https://github.com/tony19/logback-android/wiki/FAQ
 [13]: https://github.com/tony19/logback-android/wiki/Appender-Notes#fileappender
 [14]: https://github.com/tony19/logback-android/wiki/Appender-Notes#smtpappender
 [15]: https://github.com/tony19/logback-android/wiki/Appender-Notes#socketappender-syslogappender
 [16]: https://github.com/tony19/logback-android/wiki/Appender-Notes#socketappender-syslogappender
 [17]: https://github.com/tony19/logback-android/wiki/Appender-Notes#dbappender