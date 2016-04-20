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

- Java 1.7+

- A version of Mame 0.161+ that suits your operating system. Make sure it
  works properly before trying ia-mame


Installation
------------

### Linux / OS X

- Make your rompath writable or add another writable one on the 'rompath' 
  directive of `mame.ini`

  ```bash 
  $ chmod +w /path/to/mame/roms
  ```

- Install ia-mame and make it executable: 

  ```bash 
  $ sudo curl -fsSLo /usr/local/bin/ia-mame https://github.com/TiBeN/ia-mame/releases/download/0.4/ia-mame
  $ sudo chmod +x /usr/local/bin/ia-mame
  ```

- Tell IaMame where is your Mame executable by making it available on your 
  $PATH environment variable — its name must match mame[64][.exe] — or by 
  setting on your ~./bashrc, or somewhere else, the $MAME_EXEC environment 
  variable:

  ```bash
  $ vim ~/.bashrc
  export MAME_EXEC=/path/to/mame64
  ```

### Windows

- Download
  [ia-mame.exe](https://github.com/TiBeN/ia-mame/releases/download/0.4/ia-mame.exe) and place it on the folder where is your mame.exe

You can alternatively place it somewhere else and use the $MAME\_EXEC env
variable:

```batch
C:\> SET MAME_EXEC=C:\Users\tiben\mame\mame.exe
```

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

The binary files ia-mame (Linux/OS X) and ia-mame.exe (Windows) and the
executable jar are available on the `target` directory.

Usage
-----

IaMame works exactly like the original Mame command line. Simply launch a
game, ia-mame will take care to see what files are missing on your rompath
and will download them before launching Mame.

### Linux, Os X

Let's try Street Fighter 2 arcade board:

```bash
$ ia-mame sf2

INFO: Download from archive.org missing rom files: [sf2] for machine "Stree...
Downloading 668kB / ??kB, progress: ??"
```

### Windows 

Let's try Columns on the Sega Master System.

Open a console `cmd`

```batch
C:\> cd \path\to\mame
C:\> ia-mame.exe sms columns

INFO: Download from archive.org missing rom files: [sms] for machine "Master...
Downloading 25kB / ??kB, progress: ??
INFO: Download from archive.org missing software file: Software: [device: sms_cart...
Downloading 4kB / ??kB, progress: ??
```

### Executable JAR

Alternatively, the provided 
[executable
jar](https://github.com/TiBeN/ia-mame/releases/download/0.4/ia-mame.jar) 
can be used directly:

```
$ java -jar ia-mame.jar sf2
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
