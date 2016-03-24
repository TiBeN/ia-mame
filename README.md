Ia Mame
=======

IaMame is a thin command line wrapper for the Mame Emulator which downloads
automatically needed system roms and software from [The Internet Archive 
Mess and Mame collections](https://archive.org/details/messmame) if they are 
not found on the rompath directory before execution.
No needs to have Gigas of roms collections on your drive anymore.

*It is still under development* but actualy works for Mame roms, softwarelist roms
and some cd-rom CHD collections (pcecd, neocd, segacd). I'm facing software 
list version differences issues between the collection and the Mame executable 
on some other (psx, saturn..) which i need to address.

The goal of `ia-mame` is to be totally transparent. So simply tell it where is
your mame executable on a configuration file and use it like the real mame. 

It works for Linux and should on Windows and Mac but i have not tested for
now.

Compile, config, and use
------------------------

As it is still under development they is no releases for now. So the only
way to test is to use Maven

- git clone this repository:

```
$ git clone https://github.com/TiBeN/ia-mame
```

- Build and package using maven:
```
$ cd /path/to/ia-mame
$ mvn package
```

- Tell it where is your mame executable

This is done through a small config file in your home folder:

```
$ cd /path/to/ia-mame
$ mkdir $HOME/.config/ia-mame
$ cp ./doc/ia-mame-config-example $HOME/.config/ia-mame/config
```

Edit this file and set the `mame.binary` parameter to the absolute path
of your mame executable. If your mame executable can be found on your $PATH
environment there is no need to create this config file.

There is no need to configure anything else but the underlying Mame itself.
IaMame deduces your rompath and other needed configuration by using the
mame executable.

So be sure mame works as expected before trying iamame, especially be sure
one of the paths on the rompath directive is writable. The downloaded roms
will be stored into this path. If there are many, it will store on the
first found.

Finally, uses it like a normal mame execution:

Usage: ia-mame \<normal-mame-command-line-arguments\>

Examples:
```
# Let's try Street Fighter 2
$ ./bin/ia-mame sf2
INFO: Download from archive.org missing rom files: [sf2] for machine "Street Fighter II: The World Warrior (World 910522)"

# Let's try Columns on the genesis
$ ./bin/ia-mame genesis columns
INFO: Download from archive.org missing rom files: [sms] for machine "Master System II"
INFO: Download from archive.org missing software file: Software: [device: sms_cart, name: Columns (Euro, USA, Bra, Kor) (columns), publisher: Sega, machine: Master System II])
```


