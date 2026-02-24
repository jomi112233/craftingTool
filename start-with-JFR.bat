@echo off
cd craftingtool

set JFR_OPTS=-XX:StartFlightRecording=name=app,settings=profile,dumponexit=true,filename=recording.jfr

mvn javafx:run -Dexec.args="%JFR_OPTS%"

pause
