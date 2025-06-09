#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : ".*-> \(.*\)"`
    if expr "$link" : ".*/.*" > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
APP_HOME=`dirname "$PRG"`
APP_HOME=`cd "$APP_HOME" && pwd`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use a simple way to check if we are running on Cygwin
case "`uname`" in
    CYGWIN*) cygwin=true ;;
    *) cygwin=false ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "$APP_HOME" ] &&
        APP_HOME=`cygpath --unix "$APP_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    [ -n "$CLASSPATH" ] &&
        CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# Locate a JDK installation directory which will be used to run the IDE. 
# Try (in order):
#
#  1.  JAVA_HOME
#  2.  JDK_HOME
#  3.  JRE_HOME
#  4.  java found in PATH
#
JDK_LOCATION_FOUND=false
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM
        JRE_HOME="$JAVA_HOME/jre"
        JDK_LOCATION_FOUND=true
    elif [ -x "$JAVA_HOME/bin/java" ] ; then
        JRE_HOME="$JAVA_HOME"
        JDK_LOCATION_FOUND=true
    fi
fi
if [ "$JDK_LOCATION_FOUND" = "false" ] && [ -n "$JDK_HOME" ] ; then
    if [ -x "$JDK_HOME/jre/sh/java" ] ; then
        # IBM
        JRE_HOME="$JDK_HOME/jre"
        JDK_LOCATION_FOUND=true
    elif [ -x "$JDK_HOME/bin/java" ] ; then
        JRE_HOME="$JDK_HOME"
        JDK_LOCATION_FOUND=true
    fi
fi
if [ "$JDK_LOCATION_FOUND" = "false" ] && [ -n "$JRE_HOME" ] ; then
    if [ -x "$JRE_HOME/sh/java" ] ; then
        # IBM
        JRE_HOME="$JRE_HOME"
        JDK_LOCATION_FOUND=true
    elif [ -x "$JRE_HOME/bin/java" ] ; then
        JRE_HOME="$JRE_HOME"
        JDK_LOCATION_FOUND=true
    fi
fi
if [ "$JDK_LOCATION_FOUND" = "false" ] ; then
    JAVA_EXE=`which java 2>/dev/null`
    if [ -n "$JAVA_EXE" ] && [ ! "`expr "$JAVA_EXE" : ".*java.*"`" = "0" ] ; then
        JDK_LOCATION_FOUND=true
    fi
fi

if [ "$JDK_LOCATION_FOUND" = "false" ] ; then
    echo
    echo "ERROR: Java is not found or not executable."
    echo
    exit 1
fi

# Set standard commands for invoking Java, if not already set.
if [ -z "$JAVA_EXE" ] ; then
    JAVA_EXE="$JRE_HOME/bin/java"
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin ; then
    APP_HOME=`cygpath --path --windows "$APP_HOME"`
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    JRE_HOME=`cygpath --path --windows "$JRE_HOME"`
    CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
    CYGWIN_HOME=`cygpath --path --windows "$HOME"`
fi

# Escape application args
escaped_args=""
for arg do
    if ! echo "$arg" | grep -q " "; then
        escaped_args="$escaped_args \"$arg\""
    else
        escaped_args="$escaped_args $arg"
    fi
done

# Split up the JVM options only if spaces are available
if echo "$DEFAULT_JVM_OPTS" | grep -q " "; then
    JVM_OPTS=($DEFAULT_JVM_OPTS)
else
    JVM_OPTS=$DEFAULT_JVM_OPTS
fi
JVM_OPTS="${JVM_OPTS[@]}" "$JAVA_OPTS" "$GRADLE_OPTS"
JVM_OPTS="$JVM_OPTS -Dorg.gradle.appname=$APP_BASE_NAME"

# Execute Gradle
exec "$JAVA_EXE" "${JVM_OPTS[@]}" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain $escaped_args


