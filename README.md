Ia Mame
=======

IaMame is a thin command line wrapper for the Mame Emulator which downloads
automatically needed system roms and software from [The Internet Archive 
Mess and Mame collections](https://archive.org/details/messmame) if they are 
not found on the rompath directory before execution.
No needs to have Gigas of roms collections on your drive anymore.

*It is still under development* but actualy works for Mame roms, softwarelist roms
and most of cd-rom CHD collections. I'm facing issues on some CHD collections 
(pxs, saturn) due to version differences between the collection and the 
Mame executable. It is especially true if the delta of the version is high.
(PSX CHDs will work great using Mame v0.161 but not with 0.170 for example)

The goal of `ia-mame` is to be totally transparent. So simply tell it where is
your mame executable and use it like the real mame. 

It works on Linux, Windows, and should work on Mac but it has not been tested.

Prerequisites
-------------

- Mame 0.161+

- Java 1.7+

Be sure your Mame copy works as expected before trying IaMame, especially 
be sure that one of the paths on the rompath parameter is writable. IaMame 
will store downloaded files on the first writable found.

The `hash` path parameter is important too because IaMame relies on its
content.

Installation
------------

Simply download the 
[release tarball](https://github.com/TiBeN/ia-mame/releases/latest), then 
unzip it.

### Compilation from the sources

The compilation requires Maven.

- git clone this repository:

```bash
$ git clone https://github.com/TiBeN/ia-mame
```

- Build and package using maven:

```bash
$ cd /path/to/ia-mame
$ mvn package
```

Configuration
-------------

If Mame is available on your $PATH environment var, there is nothing to do, 
IaMame will find it itself if its name matches `mame[64][.exe]` 

Otherwise, you can edit the file `etc/config` and set the `mame.binary` 
parameter to the absolute path of your mame executable.

There is no need to configure anything else.

Usage
-----

Use it exactly the way you use the original Mame command-line. 
Launchers are available on the `bin`path. 
Linux users must use `ia-mame` launcher whereas Windows users must use 
`ia-mame.bat`. 

- Let's try the original arcade version of Street Fighter 2:

```bash
$ ./bin/ia-mame sf2
INFO: Download from archive.org missing rom files: [sf2] for machine "Street Fighter II: The World Warrior (World 910522)"
```

- Let's try Columns on the Sega Master System:

```bash
$ ./bin/ia-mame sms columns
INFO: Download from archive.org missing rom files: [sms] for machine "Master System II"
INFO: Download from archive.org missing software file: Software: [device: sms_cart, name: Columns (Euro, USA, Bra, Kor) (columns), publisher: Sega, machine: Master System II])
```

If you feels not confortable with the Mame command line, i suggest you to
become familiar with it by reading the official 
[documentation](http://docs.mamedev.org/). The original [MESS
documentation](http://www.mess.org/mess/howto) can be pretty useful too to 
better understand the softwarelist mechanism and knowing which `<system>` 
and `<software>` names to type. Mess is now merged with Mame but was 
previously the console/computer part of Mame. 

What's more, i'm planning to do a very rudimentary GUI to simplify things a
little for users which do not like command line, especially on Windows
environment.
