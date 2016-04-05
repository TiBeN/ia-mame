::-------------------------------------------------------
::::-- IaMame
::::--
::::-- ia-mame Windows startup batch script
::::-----------------------------------------------------

@ECHO OFF

SET root_path=%~dp0

:: The following is only needed when executing from 
:: the sources tree, the jar beeing made available on the lib
:: folder on the release tarball
SET ia_mame_jar=%root_path%..\target\ia-mame.jar

SET lib_path=%root_path%..\lib
SET config_file=%root_path%..\etc\config
SET dry_run=0
SET debug=0

IF "%1" == "--dry-run" SET dry_run=1 & SHIFT
IF "%1" == "--debug" SET debug=1 & SHIFT

:: Assign all command line args to a variable
SET args=
:loop 
    IF "%1" == "" GOTO endloop
    SET args=%args% %1
    SHIFT
    GOTO loop
:endloop

echo %args%

java -classpath "%lib_path%\*;%ia_mame_jar%" -Diamame.configfile="%config_file%" -Diamame.dryrun="%dry_run%" -Diamame.debug=%debug% org.tibennetwork.iamame.IaMame %args%

