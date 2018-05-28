TODO
====

Features/Improvements
---------------------

-   Integrate new updated archive.org collections
-   Reformat and update the content of this todo file
-   Remove ?? in download display
-   Cache somewhere the zipview HTML file to speed up getting file length
-   Improve the compatibility of CHD software Collections when used with 
    a different version of Mame than the collection 
    (embed software list hashs matching the version?)
-   Do some pretty basic GUI to ease the use on Windows or by users not 
    confortable with command line
-   ~~Support for auto-completion~~ This should be a separate project
-   Support for torrents sources as up-to-date provider
-   Check if possible to support olds collection source from archive.org:
    https://archive.org/details/MAME0.78-MAME2003-ROMs-CHDs-Samples
-   Add some kind of communication channel for frontends to read the
    download progress and activity data.
-   Add "dry-run" option to not trigger the download but simulate just to 
    know if rom is available or not

Bugs/Checks
-----------

-   File length for Mame roms is not fetched at all
-   Check the 32/64bits compatibility on Windows (see reddit post:
    https://www.reddit.com/r/emulation/comments/4tkrqb/iamame_07_released/)
    Reported another time. IaMame doesn't find automatically mame64.exe
-   Download chd from a 404 doesn't throw any exceptions
-   Malformed XML "JAXB error". Mame or IaMame fault? can be reproduced
    with softwarelist of system "pippin" with Mame 0.162 . In any case, 
    better handle the error (avoid stack trace)
