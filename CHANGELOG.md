CHANGELOG
=========

Current
-------

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
