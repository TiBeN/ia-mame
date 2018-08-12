CHANGELOG
=========

1.1
---

-   Change project name to `ia-arcade`

1.0
---

-   Fix ia-mame does not find Window mame64.exe binary by itself
-   Improve readability of logs 
-   Fix Download progress counter
-   Now support all romsets collections available at archive.org since 0.149
-   Fix JDK 9+ "java.lang.NoClassDefFoundError" errors (issue #10)

0.9
---

-   Upgraded the Windows executable generator "Launch4J Maven Plugin" 
    dependency to latest version (1.7.12) as an attempt to fix reported 
    Avast antivirus flagging ia-mame.exe as a threat. If the issue persist
    let me now
-   Added support for MESS 0.149 CHD: Vsmile CD collection (to be used with
    "vsmilpro" driver)
-   Added support for MESS 0.149 CHD: Mac HDD collection (to be used with
    "macclasc" driver)
-   Added support for MESS 0.149 CHD: 3DO M2 collection (to be used with
    "3do_m2" driver)

0.8
---

-   Added a "-noexecmame" option to prevent ia-mame to launch mame after its 
    own execution.
-   Now discover the Mame runtime command line options scheme dynamically 
    instead of storing a static list internally. This greatly improves the
    accuracy of options regardless of the Mame version.
-   Now support software list softwares clones 
    (see [issue #4](https://github.com/TiBeN/ia-mame/issues/4))
-   Fixed: Use Uppercase letters on command line throws exception (allowed by 
    Mame) (see [issue #5](https://github.com/TiBeN/ia-mame/issues/5))
-   Fixed: ia-mame does not find the Machine data on the XML using 
    Mame < 0.16[2|1] because "machine" tag was previously named "game", 
    which now makes ia-mame support olders versions of Mame (tested from 0.149)    
-   Handle software having many cd-rom/chd parts
-   7z support implemented

0.7
---

-   Now distributed as a zipped package as well
-   Added support for negative boolean options (-[no]option)
-   Fixed bug some mame games seems to need others files ex Punisher qsound

0.6
---

-   Fixed bug `ia-mame <system> <software>` with wrong software should not 
    break the launch of Mame to display software name suggestions
-   Now support download of Mame system CHD files
-   The download total size and progress in percent is now displayed.
-   Handle the configuration parameters (ie rompath and so on) set from
    the command line too

0.5
---

-   Improved INFO traces format

0.4
---

-   Improved packaging: Now build ia-mame as a native exe binary file for 
    window and self-contained executable script+jar for \*nix 
-   Added a downloading counter progression
-   Removed the use of a configuration file and defined MAME\_EXEC env var
    instead

0.3
---

-   Now support paths relatives to mame binary on the mame configuration
    (which is the case on unmodified MAME installations) and set the cwd to
    the folder containing the MAME binary when executing it. 
-   Added batch launcher for Windows

0.2
---

-   Added support for MESS 0.149 CHD: CDTV collection
-   Added support for MESS 0.149 CHD: Cd32 collection
-   Added support for MESS 0.149 CHD: MegaCD (jpn) collection
-   Added support for MESS 0.149 CHD: Pippin collection
-   Added support for MESS 0.149 CHD: MegaCD collection
-   Fixed wrong config file path
-   Added this CHANGELOG

0.1
---

-   Initial Release
-   Launcher for Linux
-   Support MESS 0.149 CHD: Saturn and MESS 0.149 CHD: PSX but doesn't work 
    on recents versions of mame (0.170 for example) due to softwarelist data 
    and bios requirement diff.
-   Support Collections: MAME 0.151 ROMs, MESS 0.151 BIOS ROM Collection, 
    MESS 0.151 Software List ROMs, MESS 0.149 CHD: CDi, 
    MESS 0.149 CHD: PCE CD, MESS 0.149 CHD: Sega CD, and MESS 0.149 CHD: NeoCD
