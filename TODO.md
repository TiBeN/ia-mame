TODO
====

Features/Improvements
---------------------

-   Improve the compatibility of CHD software Collections when used with 
    a different version of Mame than the collection 
    (embed software list hashs matching the version?)
-   Do some pretty basic GUI to ease the use on Windows or by users not 
    confortable with command line
-   ~~Support for auto-completion~~ This should be a separate project
-   Do support of news command line switchs since Groovymame merged with
    mame
-   Support for torrents sources as up-to-date provider
-   [STOPPED] Add support for 7z compressed files when scanning for rom files 
    on the rompath. 
    -   *Implemented for Mame roms but need to be implemented for 
        softwarelist. But need to check 3rd bullet bug first because 
        of possible refactoring*.
-   Check if possible to support olds collection source from archive.org:
    https://archive.org/details/MAME0.78-MAME2003-ROMs-CHDs-Samples
-   Add some kind of communication channel for frontends to read the
    download progress and activity data.
-   [WIP] Handle software having many cd-rom/chd parts

Bugs/Checks
-----------

-   ia-mame does not find the Machine data on the XML using Mame < 0.16[2|1] 
    because "machine" tag was previously named "game".
-   File length for Mame roms is not fetched at all
-   Do more tests with differents version of Mame to be more precise on the
    README about Mame version support.
-   A wrong machine name given on command line must not break the
    underlying Mame execution to display suggestions.
-   Check the 32/64bits compatibility on Windows (see reddit post:
    https://www.reddit.com/r/emulation/comments/4tkrqb/iamame_07_released/)
