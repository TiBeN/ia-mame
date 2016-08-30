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
-   Check if possible to support olds collection source from archive.org:
    https://archive.org/details/MAME0.78-MAME2003-ROMs-CHDs-Samples
-   Add some kind of communication channel for frontends to read the
    download progress and activity data.
-   Implements Collections 3DO, MacHDD,VsmileCD

Bugs/Checks
-----------

-   File length for Mame roms is not fetched at all
-   Do more tests with differents version of Mame to be more precise on the
    README about Mame version support.
-   Check the 32/64bits compatibility on Windows (see reddit post:
    https://www.reddit.com/r/emulation/comments/4tkrqb/iamame_07_released/)
-   Download chd from a 404 doesn't throw any exceptions
-   Malformed XML "JAXB error". Mame or IaMame fault? can be reproduced
    with softwarelist of system "pippin" with Mame 0.162 . In any case, 
    better handle the error (avoid stack trace)

