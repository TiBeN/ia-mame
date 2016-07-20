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
-   Support for others providers (torrent sources ?)
-   Add support for 7z compressed files when scanning for rom files on the 
    rompath

Bugs/Checks
-----------

-   ia-mame does not find the Machine data on the XML using Mame < 0.16[2|1] 
    because "machine" tag was previously named "game".
-   File length for Mame roms is not fetched at all
-   Check if softwares needing many files case is handled correctly (multi
    CD for example)
-   Do more tests with differents version of Mame to be more precise on the
    README about Mame version support.
