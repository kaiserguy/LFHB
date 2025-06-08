#!/usr/bin/env sh

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME\n\n       Please set the JAVA_HOME variable in your environment to match the\n       location of your Java installation." >&2
        exit 1
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || { echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.\n\n       Please set the JAVA_HOME variable in your environment to match the\n       location of your Java installation." >&2; exit 1; }
fi

# Determine the script directory.
SCRIPT_DIR=$(dirname "$0")

# Execute the Gradle command.
exec "$JAVACMD" -jar "$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
