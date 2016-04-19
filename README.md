IaMame
======

[![Build
Status](https://travis-ci.org/TiBeN/ia-mame.svg?branch=master)](https://travis-ci.org/TiBeN/ia-mame)

IaMame is a thin Mame emulator command line wrapper which downloads 
automatically needed system roms and softwares from [The Internet 
Archive Mess and Mame collections](https://archive.org/details/messmame) 
if they are missing on the rompath directory before launching Mame.

No needs to have Gigas of roms collections on your drive anymore!

Mame roms, softwarelist roms and most of cd-rom CHD collections are
supported.

Prerequisites
-------------

- Mame 0.161+

- Java 1.7+

Installation
------------

- Make sure you have Java 7+ and Mame 0.161+ somewhere on your drive.

- Make sure you have working copy of the Mame emulator which suits your 
  operating system.

- Make sure your Mame rompath is writable. If not, make it writable or 
  add another writable one on the 'rompath' directive of the `mame.ini`.

- Download the 
  [release tarball](https://github.com/TiBeN/ia-mame/releases/latest) and 
  unzip it somewhere.

- Tell IaMame where is your Mame executable by making it available on your 
  $PATH environment variable — its name must match mame[64][.exe] — or by 
  setting it explicitly using the $MAME_EXEC environment variable.
 
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

Usage
-----

IaMame works exactly like the original Mame command line. Simply launch a
game, ia-mame will take care to see what files are missing on your rompath
and will download them before launching Mame.

### Linux, Os X

Let's try Street Fighter 2 arcade board:

```bash
$ cd /path/to/ia-mame/bin
$ export MAME_EXEC=/path/to/mame64
$ ia-mame sf2

INFO: Download from archive.org missing rom files: [sf2] for machine "Stree...
Downloading 668kB / ??kB, progress: ??"
```

### Windows 

Let's try Columns on the Sega Master System:

```batch
C:\> cd \path\to\ia-mame\bin
C:\> SET MAME_EXEC=C:\path\to\your\Mame
C:\> ia-mame.bat sms columns

INFO: Download from archive.org missing rom files: [sms] for machine "Master...
Downloading 25kB / ??kB, progress: ??
INFO: Download from archive.org missing software file: Software: [device: sms_cart...
Downloading 4kB / ??kB, progress: ??
```

Don't like command line ?
-------------------------

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

I have not tested now but IaMame should be compatible with frontends, the
usage command line beeing the same as the original Mame.

Known issues
------------

I'm facing issues on some CHD collections (psx, saturn) due to version 
differences between the collection and the Mame executable. 
It is especially true if the delta of the version is high.
(PSX CHDs will work great using Mame v0.161 but not with 0.170 for example)

